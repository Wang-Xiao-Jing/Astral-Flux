package xiaojin.astralflux.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;
import xiaojin.astralflux.util.SourceSoulUtil;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.mixinimod.IModPlayer;

@Mixin(Player.class)
public abstract class MixinPlayer implements IModPlayer, ISourceSoul {
  @Unique
  private static final String SOURCE_SOUL_VALUE_NBT = "source_soul_value";

   /**
   * 源魂值
   */
  @Unique
  private double astralFlux$sourceSoulValue;

  /**
   * 暂停源魂恢复计时tick
   */
  @Unique
  private int astralFlux$pauseSourceSoulRecoveryTick;

  @Unique
  @Override
  public double astralFlux$getSourceSoulValue() {
    return astralFlux$sourceSoulValue;
  }

  @Unique
  @Override
  public void astralFlux$setSourceSoulValue(double sourceSoulValue) {
    // 源魂值改变时显示源魂条
    if (astralFlux$getPlayer() instanceof LocalPlayer && astralFlux$sourceSoulValue != sourceSoulValue) {
      SourceSoulBarLayerDraw.INSTANCE.addOperation(sourceSoulValue - astralFlux$sourceSoulValue);
    }
    astralFlux$sourceSoulValue = Math.clamp(sourceSoulValue, 0, astralFlux$getPlayer().getAttributeValue(ModAttributes.MAX_SOURCE_SOUL));
  }

  @Unique
  @Override
  public void astralFlux$modifySourceSoulValue(double sourceSoulValue) {
    astralFlux$setSourceSoulValue(astralFlux$getSourceSoulValue() + sourceSoulValue);
  }

  @Unique
  private @NotNull Player astralFlux$getPlayer() {
    return (Player) (Object) this;
  }

  @Unique
  @Override
  public void astralFlux$saveSourceSoulValue() {
    astralFlux$getPlayer().getPersistentData().putDouble(SOURCE_SOUL_VALUE_NBT, astralFlux$sourceSoulValue);
  }

  @Unique
  @Override
  public void astralFlux$loadSourceSoulValue() {
    var nbt = astralFlux$getPlayer().getPersistentData();
    if (!nbt.contains(SOURCE_SOUL_VALUE_NBT)) {
      nbt.putDouble(SOURCE_SOUL_VALUE_NBT, 0);
    }
    astralFlux$sourceSoulValue = nbt.getDouble(SOURCE_SOUL_VALUE_NBT);
  }

  @Unique
  @Override
  public boolean astralFlux$allowSourceSoulRecover() {
    return astralFlux$pauseSourceSoulRecoveryTick <= 0 &&
      SourceSoulUtil.getMaxValue(astralFlux$getPlayer()) > astralFlux$sourceSoulValue;
  }

  @Unique
  public int astralFlux$getPauseSourceSoulRecoveryTick() {
    return astralFlux$pauseSourceSoulRecoveryTick;
  }

  @Unique
  public void astralFlux$setPauseSourceSoulRecoveryTick(int tick) {
    this.astralFlux$pauseSourceSoulRecoveryTick = tick;
  }
}

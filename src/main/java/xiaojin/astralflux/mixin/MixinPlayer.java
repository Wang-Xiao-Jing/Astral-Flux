package xiaojin.astralflux.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xiaojin.astralflux.api.abs.AbstractAegusBarrierShieldManager;
import xiaojin.astralflux.mixin.api.IModPlayer;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

@Mixin(Player.class)
public abstract class MixinPlayer implements IModPlayer {
  @CheckForNull
  @Unique
  private AbstractAegusBarrierShieldManager astralFlux$shieldManager = null;

  @CheckForNull
  @Override
  public AbstractAegusBarrierShieldManager astralFlux$getShieldManager() {
    return this.astralFlux$shieldManager;
  }

  @Override
  public void astralFlux$setShieldManager(
    @Nullable final AbstractAegusBarrierShieldManager shieldManager) {
    this.astralFlux$shieldManager = shieldManager;
  }
}

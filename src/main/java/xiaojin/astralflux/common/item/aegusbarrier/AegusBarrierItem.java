package xiaojin.astralflux.common.item.aegusbarrier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import xiaojin.astralflux.api.ItemLeftClickEmpty;
import xiaojin.astralflux.api.sourcesoul.IModifySourceSouItem;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManager;
import xiaojin.astralflux.init.ModDataComponentTypes;
import xiaojin.astralflux.mixin.api.IModPlayer;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.Objects;

public class AegusBarrierItem extends Item implements IModifySourceSouItem, ItemLeftClickEmpty {

  public AegusBarrierItem(final Properties properties) {
    super(properties.component(ModDataComponentTypes.BOOLEAN, false));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BOW;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand usedHand) {
    var itemInHand = player.getItemInHand(usedHand);
    var modifyValue = getModifyValue(itemInHand, player);

    if (SourceSoulUtil.getValue(player) < modifyValue
      || !SourceSoulUtil.isModifyAllowed(player, modifyValue)) {
      return InteractionResultHolder.fail(itemInHand);
    }

    final var modPlayer = IModPlayer.of(player);
    if (Objects.nonNull(modPlayer.astralFlux$getShieldManager())) {
      final var manager = modPlayer.astralFlux$getShieldManager();
      boolean isDirty = false;
      for (int i = 0; i < manager.getShields().length; i++) {
        if (manager.getShields()[i] != null) {
          continue;
        }

        isDirty = true;
        manager.addShield(i);
      }

      if (isDirty) {
        SourceSoulUtil.modify(player, modifyValue);
      }

      return InteractionResultHolder.success(itemInHand);
    }

    final var manager = new AegusBarrierShieldManager(level);
    manager.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
    level.addFreshEntity(manager);
    modPlayer.astralFlux$setShieldManager(manager);

    SourceSoulUtil.modify(player, modifyValue);
    return InteractionResultHolder.consume(itemInHand);
  }

  @Override
  public void onUseTick(final Level level, final LivingEntity livingEntity, final ItemStack stack, final int remainingUseDuration) {
    if (!(livingEntity instanceof Player player) || level.isClientSide()) {
      return;
    }

    var value = SourceSoulUtil.getValue(player);
    if (value > 0) {
      return;
    }

    enterCD(stack, player);
  }

  @Override
  public void releaseUsing(final ItemStack stack, final Level level, final LivingEntity livingEntity, final int remainingUseDuration) {
    if (!(livingEntity instanceof Player player) || level.isClientSide()) {
      return;
    }

    enterCD(stack, player);
  }

  @Override
  public int getUseDuration(final ItemStack stack, final LivingEntity entity) {
    return (int) (20 * 0.2);
  }

  @Override
  public double getModifyValue(final ItemStack stack, final LivingEntity operatedLivingEntity) {
    return -32;
  }

  /**
   * 进入冷却
   */
  public void enterCD(final ItemStack stack, final Player player) {
//    player.getCooldowns().addCooldown(this, 20 * 10);// TODO 如果要禁用请注释该行
  }

  @Override
  public void leftClick(final ItemStack stack, final Player player) {
    final var modPlayer = IModPlayer.of(player);
    modPlayer.astralFlux$setShieldManager(null);
  }
}

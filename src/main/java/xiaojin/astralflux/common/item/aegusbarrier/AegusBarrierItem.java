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
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManagerEntity;
import xiaojin.astralflux.init.ModDataComponentTypes;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.SourceSoulUtil;

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
    var canBeModified = SourceSoulUtil.getValue(player) >= modifyValue;

    if (!canBeModified) {
      return InteractionResultHolder.fail(itemInHand);
    }

    // 添加护盾
    AegusBarrierShieldHandler handler = player.getExistingDataOrNull(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    AegusBarrierShieldManagerEntity manager;
    if (handler == null) {
      handler = AegusBarrierShieldHandler.create(player);
      manager = handler.getManager();
    } else if ((manager = handler.getManager()).getExpandsCount() >= 7) {
      // 添加失败 因为已经达到最大数量
      return InteractionResultHolder.fail(itemInHand);
    }

    if (!SourceSoulUtil.isModifyAllowed(player, modifyValue)) {
      return InteractionResultHolder.fail(itemInHand);
    }

    // 添加未完全成型的护盾
    manager.addShield();
    player.startUsingItem(usedHand);
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
    var barrierShields = player.getExistingDataOrNull(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }
    barrierShields.remove(player);
  }
}

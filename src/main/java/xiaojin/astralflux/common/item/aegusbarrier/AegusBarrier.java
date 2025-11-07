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
import xiaojin.astralflux.init.ModDataComponent;
import xiaojin.astralflux.init.ModDateAttachments;
import xiaojin.astralflux.util.SourceSoulUtil;

public class AegusBarrier extends Item implements IModifySourceSouItem, ItemLeftClickEmpty {

  public AegusBarrier(final Properties properties) {
    super(properties.component(ModDataComponent.BOOLEAN, false));
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

    // 检测源魂值是否充足
    if (level.isClientSide()) {
      if (canBeModified) {
        return InteractionResultHolder.success(itemInHand);
      }
      return InteractionResultHolder.fail(itemInHand);
    }
    if (!canBeModified) {
      return InteractionResultHolder.fail(itemInHand);
    }

    // 添加护盾
    var shields = player.getExistingDataOrNull(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    if (shields == null) {
      shields = player.getData(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    } else if (shields.getExpandsCount() >= 7) {
      // 添加失败 因为已经达到最大数量
      return InteractionResultHolder.fail(itemInHand);
    }
    // 源魂值消耗
    if (!SourceSoulUtil.modify(player, modifyValue)) {
      return InteractionResultHolder.fail(itemInHand);
    }
    // 添加未完全成型的护盾
    shields.addShield();
    player.startUsingItem(usedHand);
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
    if (!(livingEntity instanceof Player player) || level.isClientSide() || remainingUseDuration > 0) {
      return;
    }
    if (livingEntity.hasData(ModDateAttachments.AEGUS_BARRIER_SHIELD)) {
      livingEntity.getData(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    }
    enterCD(stack, player);
  }

  @Override
  public int getUseDuration(final ItemStack stack, final LivingEntity entity) {
    if (!(entity instanceof Player player)) {
      return 0;
    }
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
    player.getCooldowns().addCooldown(this, 20 * 10);
  }

  @Override
  public void leftClick(final ItemStack stack, final Player player) {
    var barrierShields = player.getExistingDataOrNull(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }
    barrierShields.remove(player);
  }
}

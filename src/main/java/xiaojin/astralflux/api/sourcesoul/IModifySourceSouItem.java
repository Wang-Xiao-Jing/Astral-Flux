package xiaojin.astralflux.api.sourcesoul;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IModifySourceSouItem {
  /**
   * 获取物品的要修改的源魂值
   *
   * @param stack        物品
   * @param livingEntity
   * @return 增加或减少的源魂值
   */
  double getModifyValue(ItemStack stack, final LivingEntity livingEntity);
}

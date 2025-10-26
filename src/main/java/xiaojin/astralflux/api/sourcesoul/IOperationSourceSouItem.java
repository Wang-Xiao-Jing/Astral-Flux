package xiaojin.astralflux.api.sourcesoul;

import net.minecraft.world.item.ItemStack;

public interface IOperationSourceSouItem {
  /**
   * 获取物品的要操作的源魂值
   * @param stack 物品
   * @return 增加或减少的源魂值
   */
  double getOperationValue(ItemStack stack);
}

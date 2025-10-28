package xiaojin.astralflux.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xiaojin.astralflux.api.sourcesoul.IOperationSourceSouItem;

public class AegusBarrier extends Item implements IOperationSourceSouItem {


  public AegusBarrier(final Properties properties) {
    super(properties);
  }

  @Override
  public double getOperationValue(final ItemStack stack) {
    return -32;
  }

  public static boolean canBarrierWork(ItemStack stack) {
    // TODO 获取当前护盾是否激活
    return true;
  }

  public static boolean canBarrierSideWork(ItemStack stack, int sideIndex) {
    // TODO 获取护盾当前面是否有效
    return true;
  }


}

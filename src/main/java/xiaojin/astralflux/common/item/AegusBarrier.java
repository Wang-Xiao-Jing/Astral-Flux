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
}

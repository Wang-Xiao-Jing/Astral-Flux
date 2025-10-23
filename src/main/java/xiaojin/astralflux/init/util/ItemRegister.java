package xiaojin.astralflux.init.util;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static xiaojin.astralflux.init.ModItems.REGISTRY;

public class ItemRegister {
  @NotNull
  protected static DeferredItem<Item> register(String name, Item.Properties properties) {
    return register(name, Item::new, properties);
  }

  @NotNull
  protected static <I extends Item> DeferredItem<I> register(String name,
                                                             Function<Item.Properties, ? extends I> item,
                                                             Item.Properties properties) {
    return REGISTRY.registerItem(name, item, properties);
  }
}

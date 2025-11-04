package xiaojin.astralflux.init.util;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;

import java.util.function.Function;

public abstract class ItemRegisterUtil {
  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AstralFlux.ID);

  @NotNull
  protected static DeferredItem<Item> register
    (String name, Item.Properties properties) {
    return register(name, Item::new, properties);
  }

  @NotNull
  protected static <I extends Item> DeferredItem<I> register
    (String name, Function<Item.Properties, ? extends I> item, Item.Properties properties) {
    return REGISTRY.registerItem(name, item, properties);
  }
}

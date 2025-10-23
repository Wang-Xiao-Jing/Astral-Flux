package xiaojin.astralflux.init;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.init.util.ItemRegister;

import java.util.function.Supplier;

public final class ModItems extends ItemRegister {
  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AstralFlux.ID);

  public static final Holder<Item> AEGUS_BARRIER = register("aegus_barrier", new Item.Properties());
}

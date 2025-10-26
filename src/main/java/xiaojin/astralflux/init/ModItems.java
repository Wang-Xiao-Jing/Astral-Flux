package xiaojin.astralflux.init;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.common.item.AegusBarrier;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.ItemRegister;

public final class ModItems extends ItemRegister {
  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AstralFlux.ID);

  public static final Holder<Item> AEGUS_BARRIER = register("aegus_barrier",
    AegusBarrier::new, new Item.Properties().stacksTo(1));
}

package xiaojin.astralflux.init;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import xiaojin.astralflux.common.item.aegusbarrier.AegusBarrier;
import xiaojin.astralflux.init.util.ItemRegisterUtil;

public final class ModItems extends ItemRegisterUtil {

  public static final DeferredItem<AegusBarrier> AEGUS_BARRIER = register(
    "aegus_barrier",
    AegusBarrier::new, new Item.Properties().stacksTo(1));
}

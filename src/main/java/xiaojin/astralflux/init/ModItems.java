package xiaojin.astralflux.init;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.common.item.aegusbarrier.AegusBarrierItem;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.ItemRegisterUtil;

public final class ModItems extends ItemRegisterUtil {
  public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(AstralFlux.ID);

  public static final DeferredItem<AegusBarrierItem> AEGUS_BARRIER = register(
    "aegus_barrier",
    AegusBarrierItem::new, new Item.Properties().stacksTo(1));
}

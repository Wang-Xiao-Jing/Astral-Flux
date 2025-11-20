package xiaojin.astralflux.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.CreativeModeTabRegisterUtil;

import java.util.function.Supplier;

public final class ModCreativeModeTabs extends CreativeModeTabRegisterUtil {
  public static final DeferredRegister<CreativeModeTab> REGISTRY = AstralFlux.modRegister(BuiltInRegistries.CREATIVE_MODE_TAB);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASTRAL_FLUX_TAB =
    REGISTRY.register(AstralFlux.ID + "_tab", () -> CreativeModeTab.builder()
      .icon(() -> ModItems.AEGUS_BARRIER.value().getDefaultInstance())
      .title(Component.translatable("itemGroup." + AstralFlux.ID + "_tab"))
      .displayItems((pr, out) -> ModItems.REGISTRY.getEntries().stream()
        .map(Supplier::get)
        // 导入物品注册表的所有物品
        .forEach(out::accept))
      .build()
    );
}

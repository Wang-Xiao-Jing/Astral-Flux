package xiaojin.astralflux;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.init.ModCreativeModeTabs;
import xiaojin.astralflux.init.ModItems;

@Mod(AstralFlux.ID)
public class AstralFlux {
  public static final String ID = "astralflux";
  public static final Logger LOGGER = LogManager.getLogger(ID);

  public AstralFlux(IEventBus bus, ModContainer container) {
    LOGGER.info("HELLO from server starting");
    ModAttributes.REGISTRY.register(bus);
    ModItems.REGISTRY.register(bus);
    ModCreativeModeTabs.REGISTRY.register(bus);
  }

  public static ResourceLocation modRL(final String name) {
    return ResourceLocation.fromNamespaceAndPath(AstralFlux.ID, name);
  }

  public static <T> @NotNull DeferredRegister<T> modRegister(Registry<T> registry) {
    return DeferredRegister.create(registry, AstralFlux.ID);
  }
}

package xiaojin.astralflux.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.init.*;

@Mod(AstralFlux.ID)
public final class AstralFlux {
  public static final String ID = "astralflux";
  public static final Logger LOGGER = LogManager.getLogger(ID);

  public AstralFlux(IEventBus bus, ModContainer container) {
    LOGGER.info("HELLO from server starting");
    container.registerConfig(ModConfig.Type.CLIENT, AstralFluxConfig.CLIENT_CONFIG_SPEC);
    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    ModDataComponentTypes.REGISTRY.register(bus);
    ModDateAttachmentTypes.REGISTRY.register(bus);
    ModEntityTypes.REGISTRY.register(bus);
    ModAttributes.REGISTRY.register(bus);
    ModItems.REGISTRY.register(bus);
    ModCreativeModeTabs.REGISTRY.register(bus);
  }

  public static ResourceLocation modRL(final String name) {
    return ResourceLocation.fromNamespaceAndPath(AstralFlux.ID, name);
  }

  public static String modRLText(final String name) {
    return AstralFlux.ID + ":" + name;
  }

  public static <T> @NotNull DeferredRegister<T> modRegister(Registry<T> registry) {
    return DeferredRegister.create(registry, AstralFlux.ID);
  }
}

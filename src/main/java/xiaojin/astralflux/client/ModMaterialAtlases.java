package xiaojin.astralflux.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import xiaojin.astralflux.core.AstralFlux;

import java.util.Map;


@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModMaterialAtlases {

  private static final Map<ResourceLocation, ResourceLocation> MANAGER_TEXTURES = Map.of();

  @SubscribeEvent
  public static void registerMaterialAtlases(RegisterMaterialAtlasesEvent event) {
    AstralFlux.LOGGER.info("Registering Material Atlases");
    for (var manager : MANAGER_TEXTURES.entrySet()) {
      event.register(manager.getKey(), manager.getValue());
    }
    AstralFlux.LOGGER.info("Registering Material Atlases Completed");
  }
}

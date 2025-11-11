package xiaojin.astralflux.client.event;

import com.google.common.collect.Sets;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xiaojin.astralflux.client.renderer.entiey.special.AegusBarrierShieldEntityRenderer;
import xiaojin.astralflux.core.AstralFlux;

import java.util.Set;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModModelLayer {
  private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
  public static final ModelLayerLocation AEGUS_BARRIER_SHIELD = register("aegus_barrier_shield");

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    AstralFlux.LOGGER.info("Registering Layer Definitions");

    event.registerLayerDefinition(ModModelLayer.AEGUS_BARRIER_SHIELD, AegusBarrierShieldEntityRenderer::createBodyLayer);

    AstralFlux.LOGGER.info("Registering Layer Definitions Completed");
  }

  private static ModelLayerLocation register(String path) {
    return register(path, "main");
  }

  private static ModelLayerLocation register(String path, String model) {
    ModelLayerLocation modellayerlocation = createLocation(path, model);
    if (ALL_MODELS.add(modellayerlocation)) {
      return modellayerlocation;
    }
    throw new IllegalStateException("Duplicate registration for " + modellayerlocation);
  }

  private static ModelLayerLocation createLocation(String path, String model) {
    return new ModelLayerLocation(ResourceLocation.withDefaultNamespace(path), model);
  }

  public static Stream<ModelLayerLocation> getKnownLocations() {
    return ALL_MODELS.stream();
  }
}

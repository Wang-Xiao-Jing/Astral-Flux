package xiaojin.astralflux.client;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin.Model;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import xiaojin.astralflux.client.renderer.entiey.special.AegusBarrierShieldEntityRenderer;
import xiaojin.astralflux.client.renderer.layer.AegusBarriesShieldRenderer;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxKey;

import java.util.Objects;

import static xiaojin.astralflux.core.AstralFlux.ID;

@Mod(value = AstralFlux.ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ID, value = Dist.CLIENT)
public final class AstralFluxClient {
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    AstralFlux.LOGGER.info("HELLO FROM CLIENT SETUP");
  }

//  @SubscribeEvent
//  public static void registerGeometryLoaders(ModelEvent.BakingCompleted event) {
//  }

  @SubscribeEvent
  public static void RegisterAdditional(ModelEvent.RegisterAdditional event) {
    event.register(AegusBarrierShieldEntityRenderer.MODEL_RESOURCE_LOCATION);
  }

  @SubscribeEvent
  public static void registerBindings(RegisterKeyMappingsEvent event) {
    AstralFluxKey.init(event);
  }

  @SubscribeEvent
  public static void registerEntityRender(EntityRenderersEvent.RegisterRenderers event) {
  }

  @SubscribeEvent
  public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
    final PlayerRenderer rendererWide = event.getSkin(Model.WIDE);
    final PlayerRenderer rendererSlim = event.getSkin(Model.SLIM);

    if (Objects.nonNull(rendererWide)) {
      rendererWide.addLayer(new AegusBarriesShieldRenderer(rendererWide));
    }

    if (Objects.nonNull(rendererSlim)) {
      rendererSlim.addLayer(new AegusBarriesShieldRenderer(rendererSlim));
    }
  }

}

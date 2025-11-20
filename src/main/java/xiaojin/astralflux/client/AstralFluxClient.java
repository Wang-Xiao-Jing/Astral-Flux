package xiaojin.astralflux.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import xiaojin.astralflux.client.renderer.armpose.AegusBarrierArmPose;
import xiaojin.astralflux.client.renderer.AegusBarrierShieldRenderer;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxKey;
import xiaojin.astralflux.init.ModItems;

import static xiaojin.astralflux.core.AstralFlux.ID;

@Mod(value = AstralFlux.ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ID, value = Dist.CLIENT)
public final class AstralFluxClient {
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    AstralFlux.LOGGER.info("HELLO FROM CLIENT SETUP");
  }

  @SubscribeEvent
  public static void registerClientExtensionsEvent(RegisterClientExtensionsEvent event) {
    event.registerItem(new AegusBarrierArmPose(), ModItems.AEGUS_BARRIER);
  }

   @SubscribeEvent
   public static void registerGeometryLoaders(ModelEvent.BakingCompleted event) {
     AegusBarrierShieldRenderer.INSTANCE.init();
   }

  @SubscribeEvent
  public static void RegisterAdditional(ModelEvent.RegisterAdditional event) {
    event.register(AegusBarrierShieldRenderer.MODEL_RESOURCE_LOCATION);
  }

  @SubscribeEvent
  public static void registerBindings(RegisterKeyMappingsEvent event) {
    AstralFluxKey.init(event);
  }

  @SubscribeEvent
  public static void registerEntityRender(EntityRenderersEvent.RegisterRenderers event) {
  }
}

package xiaojin.astralflux.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xiaojin.astralflux.client.renderer.entiey.special.AegusBarrierShieldEntityRenderer;
import xiaojin.astralflux.client.renderer.entiey.special.AegusBarrierShieldManagerRenderer;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModEntityTypes;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModRegisterRender {
  @SubscribeEvent
  public static void registerRenders(final EntityRenderersEvent.RegisterRenderers event) {
    AstralFlux.LOGGER.info("Registering Entity Renderers");

    event.registerEntityRenderer(ModEntityTypes.AEGUS_BARRIER_SHIELD_MANAGER_ENTITY.get(), AegusBarrierShieldManagerRenderer::new);
    event.registerEntityRenderer(ModEntityTypes.AEGUS_BARRIER_SHIELD_ENTITY.get(), AegusBarrierShieldEntityRenderer::new);

    AstralFlux.LOGGER.info("Registering Entity Renderers Completed");
  }
}

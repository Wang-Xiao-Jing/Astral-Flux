package xiaojin.astralflux.client.event;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import xiaojin.astralflux.client.renderer.item.AegusBarrierShieldShieldRendererBak;
import xiaojin.astralflux.core.AstralFlux;


@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModGameRender {

  @SubscribeEvent
  public static void levelRender(final RenderLevelStageEvent event) {
    final var stage = event.getStage();
    final var minecraft = Minecraft.getInstance();
    final var level = minecraft.level;
    final var frustum = event.getFrustum();
    final var pose = event.getPoseStack();
    final var camera = event.getCamera();
    final var partialTick = event.getPartialTick();
    if (stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
      AegusBarrierShieldShieldRendererBak.INSTANCE
        .levelRender(minecraft, level, frustum, pose, camera, partialTick);
    }
  }
}

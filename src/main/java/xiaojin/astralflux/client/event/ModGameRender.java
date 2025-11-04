package xiaojin.astralflux.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import xiaojin.astralflux.client.renderer.item.AegusBarrierShieldShieldRenderer;
import xiaojin.astralflux.core.AstralFlux;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModGameRender {

  @SubscribeEvent
  public static void levelRender(final RenderLevelStageEvent event) {
    final RenderLevelStageEvent.Stage stage = event.getStage();
    final Minecraft minecraft = Minecraft.getInstance();
    final ClientLevel level = minecraft.level;
    final Frustum frustum = event.getFrustum();
    final PoseStack pose = event.getPoseStack();
    final Camera camera = event.getCamera();

    if (stage == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
      AegusBarrierShieldShieldRenderer.get().levelRender(minecraft, level, frustum, pose, camera);
    }
  }
}

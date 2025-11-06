package xiaojin.astralflux.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import xiaojin.astralflux.client.ModRenderType;
import xiaojin.astralflux.client.renderer.ModRender;
import xiaojin.astralflux.client.util.RenderUtil;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachments;
import xiaojin.astralflux.util.ModUtil;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldShieldRenderer implements ModRender {
  private static final AegusBarrierShieldShieldRenderer INSTANCE = new AegusBarrierShieldShieldRenderer();
  private static final ResourceLocation TEXTURES = AstralFlux.modRL("textures/entity/aegus_barrier/shield.png");

  public static AegusBarrierShieldShieldRenderer get() {
    return INSTANCE;
  }

  @Override
  public void levelRender(final Minecraft minecraft,
                          final ClientLevel level,
                          final Frustum frustum,
                          final PoseStack pose,
                          final Camera camera,
                          final DeltaTracker partialTick) {
    var player = minecraft.player;
    if (player == null) {
      return;
    }
    var barrierShields = player.getExistingDataOrNull(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }
    var eyePosition = player.getEyePosition();
    var partialTicks = partialTick.getRealtimeDeltaTicks();
    float yaw = barrierShields.getViewYRot(partialTicks) * Mth.DEG_TO_RAD;   // 偏航角（绕Y轴）
    float pitch = barrierShields.getViewXRot(partialTicks) * Mth.DEG_TO_RAD; // 俯仰角（绕X轴）

    var scale = new Vec3(
      Math.cos(pitch) * Math.sin(yaw),
      Math.sin(pitch),
      Math.cos(pitch) * Math.cos(yaw))
      .normalize()
      .scale(1.5);
    var vec3 = eyePosition.add(scale);
    var bufferSource = minecraft.renderBuffers().bufferSource();
    var consumer = bufferSource.getBuffer(ModRenderType.ICON.apply(TEXTURES));

    pose.pushPose();
    pose.mulPose(new Quaternionf().rotationYXZ(0, pitch, 0));
    var indexVertexs = ModUtil.getIndexVertexs(vec3.x, vec3.y, vec3.z, 1.5, yaw);
    for (var q : indexVertexs) {
      var x = (float) q.x;
      var y = (float) q.y;
      var z = (float) q.z;
      RenderUtil.renderTextures(pose, consumer, 24, x, y, z, 0, 0, 1, 1, 256, 256, 256, 256);
    }

    pose.popPose();
  }
}

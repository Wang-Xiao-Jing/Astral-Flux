package xiaojin.astralflux.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3f;
import xiaojin.astralflux.client.renderer.ModRender;
import xiaojin.astralflux.init.ModDateAttachments;
import xiaojin.astralflux.util.ModUtil;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldShieldRenderer implements ModRender {
  private static final AegusBarrierShieldShieldRenderer INSTANCE = new AegusBarrierShieldShieldRenderer();

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
    var indexVertexs = ModUtil.getIndexVertexs(vec3.x, vec3.y, vec3.z, 1.5);
    for (var indexVertex : indexVertexs) {
      for (var vertex : indexVertex) {
//        vertex
      }
    }
  }
}

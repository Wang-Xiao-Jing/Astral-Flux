package xiaojin.astralflux.client.renderer;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3d;
import xiaojin.astralflux.client.ModRenderType;
import xiaojin.astralflux.common.item.aegusbarrier.AegusBarrierShieldHandler;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.ModUtil;

import java.util.List;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldRenderer implements ModRender {
  public static final ItemStack ITEM_STACK = ItemStack.EMPTY;
  public static final ResourceLocation MODDED_RL = AstralFlux.modRL("entity/aegus_barrier_shield");
  public static final ModelResourceLocation MODEL_RESOURCE_LOCATION =
    ModelResourceLocation.standalone(MODDED_RL);
  private static final ItemColors ITEM_COLORS_COLOR = ItemColors.createDefault(BlockColors.createDefault());

  private BakedModel bakedModel;

  public static final AegusBarrierShieldRenderer INSTANCE = new AegusBarrierShieldRenderer();

  public void init() {
    this.bakedModel = Minecraft.getInstance().getModelManager().getModel(MODEL_RESOURCE_LOCATION);
  }

  @Override
  public void levelRender(final Minecraft minecraft,
                          final ClientLevel level,
                          final Frustum frustum,
                          final PoseStack poseStack,
                          final Camera camera,
                          final DeltaTracker partialTick) {
    level.players().forEach(player -> {
      if (!player.isAlive()){
        return;
      }
      var handler = player.getExistingDataOrNull(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
      if (handler == null) {
        return;
      }
      poseStack.pushPose();
      var cameraPos = camera.getPosition();
      poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
      render(handler, player, minecraft, poseStack, partialTick);
      poseStack.popPose();
    });
  }

  private void render(final AegusBarrierShieldHandler handler, final Player player, final Minecraft minecraft, final PoseStack poseStack, final DeltaTracker partialTick) {
    var partialTicks = partialTick.getGameTimeDeltaPartialTick(false);
    var realtimeTicks = partialTick.getRealtimeDeltaTicks();

    var renderBuffers = minecraft.renderBuffers();
    var bufferSource = renderBuffers.bufferSource();
    var combinedLight = LightTexture.FULL_BRIGHT;
    var combinedOverlay = OverlayTexture.NO_OVERLAY;

    // 将偏航角和俯仰角转换为方向向量
    var yaw = handler.getViewYRot(partialTicks) * Mth.DEG_TO_RAD;

    // 计算方向向量
    var dirX = -Math.sin(yaw) * Math.cos(0);
    var dirY = -Math.sin(0);
    var dirZ = Math.cos(yaw) * Math.cos(0);
    var direction = new Vec3(dirX, 0, dirZ).normalize();

    var eyePosition = player.getEyePosition(partialTicks);
    var pos = eyePosition.add(direction.scale(2f));

    poseStack.translate(pos.x, pos.y, pos.z);
    poseStack.mulPose(Axis.YP.rotation(-yaw));

    var indexVetexs = ModUtil.getIndexVetexs60(1.5f);
    for (var entry : handler.getShields().entrySet()) {
      var number = entry.getKey();
      float start = Math.min(1, entry.getValue() - 1 / (20 * 0.2f));
      float end = Math.min(1, entry.getValue() / (20 * 0.2f));
      var size = Mth.lerp(realtimeTicks, start, end);
      poseStack.pushPose();

      var rot = this.getResult(number);
      poseStack.mulPose(Axis.YP.rotationDegrees(rot.y));
      poseStack.mulPose(Axis.XP.rotationDegrees(rot.x));

      var offset = this.offset(number);
      var indexVetex = indexVetexs[number];
      poseStack.translate(
        indexVetex.x() + offset.x,
        indexVetex.y() + offset.y,
        indexVetex.z() + offset.z
      );

      poseStack.pushPose();
      poseStack.translate(-0.7f * size, -0.7f * size, -0.5f * size);
      poseStack.scale(1.5f * size, 1.5f * size, 1 * size);
      renderModel(number, poseStack, bufferSource, combinedLight, combinedOverlay);
      poseStack.popPose();
      poseStack.popPose();
    }
  }

  private Vector3d offset(final int index) {
    double x = 0.0;
    double y = 0.0;
    double z = 0.0;

    switch (index) {
      case 1 -> {
        x = 0.1;
        y = -0.25;
        z = 0.75;
      }
      case 2 -> {
        x = -0.1;
        y = -0.25;
        z = 0.75;
      }
      case 3 -> {
        z = 0.75;
      }
      case 4 -> {
        x = -0.1;
        y = 0.25;
        z = 0.75;
      }
      case 5 -> {
        x = 0.1;
        y = 0.25;
        z = 0.75;
      }
      case 6 -> {
        z = 0.75;
      }
    }

    return new Vector3d(x, y, z);
  }

  private Vector2f getResult(final int index) {
    float entityXRot = 0;
    float entityYRot = 0;

    switch (index) {
      case 1 -> {
        entityXRot = -30;
        entityYRot = 30;
      }
      case 2 -> {
        entityXRot = -30;
        entityYRot = -30;
      }
      case 3 -> {
        entityYRot = -45;
      }
      case 4 -> {
        entityXRot = 30;
        entityYRot = -30;
      }
      case 5 -> {
        entityXRot = 30;
        entityYRot = 30;
      }
      case 6 -> {
        entityYRot = 45;
      }
    }

    return new Vector2f(entityXRot, entityYRot);
  }

  private void renderModel(int i, final PoseStack poseStack, final MultiBufferSource.BufferSource bufferSource, final int combinedLight, final int combinedOverlay) {
    for (BakedModel model : this.bakedModel.getRenderPasses(ITEM_STACK, false)) {
      VertexConsumer vertexconsumer = bufferSource.getBuffer(ModRenderType.AEGUS_BARRIER_SHIELD);
      RandomSource randomsource = RandomSource.create();
      randomsource.setSeed(42L);
      List<BakedQuad> quads = model.getQuads(null, null, randomsource);
      this.renderQuadList(i, poseStack, vertexconsumer, quads, combinedLight, combinedOverlay);
    }
  }

  private void renderQuadList(int i, PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, int combinedLight, int combinedOverlay) {
    boolean flag = !ITEM_STACK.isEmpty();
    PoseStack.Pose posestackPose = poseStack.last();

    for (BakedQuad bakedquad : quads) {
      int l = -1;
      if (flag && bakedquad.isTinted()) {
        l = ITEM_COLORS_COLOR.getColor(ITEM_STACK, bakedquad.getTintIndex());
      }

      float a = (float) FastColor.ARGB32.alpha(l) / 255.0F;
      float r = (float) FastColor.ARGB32.red(l) / 255.0F;
      float g = (float) FastColor.ARGB32.green(l) / 255.0F;
      float b = (float) FastColor.ARGB32.blue(l) / 255.0F;
      double timeVariable = (double) (System.currentTimeMillis() % 100000) / 250;
      float v = (float) (0.1 * Math.sin(timeVariable + i * 50) + 0.2);
      buffer.putBulkData(posestackPose, bakedquad, r, g, b, a * v, combinedLight, combinedOverlay, true);
    }
  }
}

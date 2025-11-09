package xiaojin.astralflux.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xiaojin.astralflux.client.renderer.ModRender;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachments;

import java.util.List;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldShieldRenderer implements ModRender {
  private static final ItemStack EMPTY_ITEM_STACK = ItemStack.EMPTY;
  public static final ResourceLocation MODDED_RL = AstralFlux.modRL("entity/aegus_barrier_shield");
  public static final ModelResourceLocation MODEL_RESOURCE_LOCATION =
    ModelResourceLocation.standalone(MODDED_RL);
  private static final ItemColors ITEM_COLORS_COLOR = ItemColors.createDefault(BlockColors.createDefault());
  private BakedModel bakedModel;

  public static final AegusBarrierShieldShieldRenderer INSTANCE = new AegusBarrierShieldShieldRenderer();

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
    var player = minecraft.player;
    if (player == null) {
      return;
    }

    var barrierShields = player.getExistingDataOrNull(ModDateAttachments.AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }
    var partialTicks = partialTick.getGameTimeDeltaPartialTick(true);

    var renderBuffers = minecraft.renderBuffers();
    var bufferSource = renderBuffers.bufferSource();
    var cameraPos = camera.getPosition();
    var combinedLight = LightTexture.FULL_BRIGHT;
    var combinedOverlay = OverlayTexture.NO_OVERLAY;

    poseStack.pushPose();

    poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    // 将偏航角和俯仰角转换为方向向量
    var yaw = barrierShields.getViewYRot(partialTicks) * Mth.DEG_TO_RAD;
    var pitch = barrierShields.getViewXRot(partialTicks) * Mth.DEG_TO_RAD;

    // 计算方向向量
    var dirX = -Math.sin(yaw) * Math.cos(pitch);
    var dirY = -Math.sin(pitch);
    var dirZ = Math.cos(yaw) * Math.cos(pitch);
    var direction = new Vec3(dirX, dirY, dirZ).normalize();

    var eyePosition = player.getEyePosition(partialTicks);
    var pos = eyePosition.add(direction.scale(1.5f));
    poseStack.translate(pos.x, pos.y, pos.z);
    poseStack.mulPose(camera.rotation());

    renderModel(poseStack, bufferSource, combinedLight, combinedOverlay);

    poseStack.popPose();
  }

  private void renderModel(final PoseStack poseStack, final MultiBufferSource.BufferSource bufferSource, final int combinedLight, final int combinedOverlay) {
    boolean flag1 = true;
    poseStack.pushPose();
    poseStack.translate(-0.7f, -0.7f, -0.5f);
    poseStack.scale(1.5f, 1.5f, 1);
    for (var model : this.bakedModel.getRenderPasses(EMPTY_ITEM_STACK, flag1)) {
      for (var rendertype : model.getRenderTypes(EMPTY_ITEM_STACK, flag1)) {
        var vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource,
          rendertype,
          false,
          EMPTY_ITEM_STACK.hasFoil());
        var randomsource = RandomSource.create();
        long i = 42L;

        for (Direction direction : Direction.values()) {
          randomsource.setSeed(i);
          this.renderQuadList(poseStack,
            vertexconsumer,
            model.getQuads(null, direction, randomsource),
            EMPTY_ITEM_STACK,
            combinedLight,
            combinedOverlay);
        }

        randomsource.setSeed(i);
        this.renderQuadList(poseStack,
          vertexconsumer,
          model.getQuads(null, null, randomsource),
          EMPTY_ITEM_STACK,
          combinedLight,
          combinedOverlay);
      }
    }
    poseStack.popPose();
  }

  private void renderQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack itemStack, int combinedLight, int combinedOverlay) {
    boolean flag = !itemStack.isEmpty();
    var posestack$pose = poseStack.last();

    for (var bakedquad : quads) {
      int i = -1;
      if (flag && bakedquad.isTinted()) {
        i = ITEM_COLORS_COLOR.getColor(itemStack, bakedquad.getTintIndex());
      }

      float f = (float) FastColor.ARGB32.alpha(i) / 255.0F;
      float f1 = (float) FastColor.ARGB32.red(i) / 255.0F;
      float f2 = (float) FastColor.ARGB32.green(i) / 255.0F;
      float f3 = (float) FastColor.ARGB32.blue(i) / 255.0F;
      buffer.putBulkData(posestack$pose, bakedquad, f1, f2, f3, f, combinedLight, combinedOverlay, true);
    }
  }
}

package xiaojin.astralflux.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import xiaojin.astralflux.client.renderer.ModRender;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.ABSHelper;

import java.util.List;
import java.util.Objects;

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

  private AegusBarrierShieldShieldRenderer() {}

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
    for (AbstractClientPlayer clientPlayer : level.players()) {
      if (minecraft.player != clientPlayer && !minecraft.player
        .shouldRender(clientPlayer.getX(), clientPlayer.getY(), clientPlayer.getZ())) {
        continue;
      }

      this.startRender(poseStack, clientPlayer);
    }
  }

  private void startRender(final PoseStack poseStack, final AbstractClientPlayer player) {
    final var pos = player.getEyePosition();
    final Vec3 lookingVec = player.getLookAngle();
    final double angleY = Math.atan2(lookingVec.x, lookingVec.z);
    final double angleX = Math.atan(-lookingVec.y);

    final var data = player.getExistingData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    if (data.isEmpty() || data.get().isEmpty()) {
      return;
    }

    final double[] arr = ABSHelper.decodeArray(data.get());

    poseStack.pushPose();
    poseStack.mulPose(Axis.YP.rotation((float) angleY));
    poseStack.mulPose(Axis.XP.rotation((float) angleX));
    poseStack.scale(1.5f, 1.5f, 1);

    for (int i = 0; i < 7; i++) {
      poseStack.pushPose();

      final var p = arr[i];
      final var offsetPos = ABSHelper.getOffsetPos(i, angleX, p, pos);
      poseStack.translate(-offsetPos.x, -offsetPos.y, -offsetPos.z);
      this.renderModel(poseStack);
      poseStack.popPose();
    }

    poseStack.popPose();
  }

  private void renderModel(final PoseStack poseStack) {
    boolean flag1 = true;
    poseStack.pushPose();
    poseStack.translate(-0.7f, -0.7f, -0.5f);
    poseStack.scale(1.5f, 1.5f, 1);
    for (var model : this.bakedModel.getRenderPasses(EMPTY_ITEM_STACK, flag1)) {
      for (var rendertype : model.getRenderTypes(EMPTY_ITEM_STACK, flag1)) {
        var vertexconsumer = ItemRenderer.getFoilBufferDirect(
          Minecraft.getInstance().renderBuffers().bufferSource(),
          rendertype,
          false,
          EMPTY_ITEM_STACK.hasFoil());

        var randomsource = RandomSource.create();
        long i = 42L;

        for (Direction direction : Direction.values()) {
          randomsource.setSeed(i);
          this.renderQuadList(poseStack, vertexconsumer,
            model.getQuads(null, direction, randomsource));
        }

        randomsource.setSeed(i);
        this.renderQuadList(poseStack, vertexconsumer, model.getQuads(null, null, randomsource));
      }
    }
    poseStack.popPose();
  }

  private void renderQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads) {
    boolean flag = !AegusBarrierShieldShieldRenderer.EMPTY_ITEM_STACK.isEmpty();
    var posestack$pose = poseStack.last();

    for (var bakedquad : quads) {
      int i = -1;
      if (flag && bakedquad.isTinted()) {
        i = ITEM_COLORS_COLOR.getColor(AegusBarrierShieldShieldRenderer.EMPTY_ITEM_STACK, bakedquad.getTintIndex());
      }

      float f = (float) FastColor.ARGB32.alpha(i) / 255.0F;
      float f1 = (float) FastColor.ARGB32.red(i) / 255.0F;
      float f2 = (float) FastColor.ARGB32.green(i) / 255.0F;
      float f3 = (float) FastColor.ARGB32.blue(i) / 255.0F;
      buffer.putBulkData(posestack$pose, bakedquad, f1, f2, f3, f,
        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, true);
    }
  }
}

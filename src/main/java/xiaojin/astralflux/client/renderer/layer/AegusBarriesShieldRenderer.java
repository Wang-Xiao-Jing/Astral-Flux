package xiaojin.astralflux.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
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
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManager;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.ModMath;

import java.util.List;

public class AegusBarriesShieldRenderer extends RenderLayer<AbstractClientPlayer,
  PlayerModel<AbstractClientPlayer>> {
  public static final ResourceLocation MODDED_RL =
    AstralFlux.modRL("entity/aegus_barrier_shield");
  public static final ModelResourceLocation MODEL_RESOURCE_LOCATION =
    ModelResourceLocation.standalone(MODDED_RL);
  private static final ItemColors ITEM_COLORS_COLOR =
    ItemColors.createDefault(BlockColors.createDefault());
  private static final BakedModel BAKED_MODEL = Minecraft.getInstance()
    .getModelManager().getModel(MODEL_RESOURCE_LOCATION);
  private static final ItemStack EMPTY_ITEM_STACK = ItemStack.EMPTY;

  public AegusBarriesShieldRenderer(
    final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
    super(renderer);
  }

  @Override
  protected ResourceLocation getTextureLocation(AbstractClientPlayer entity) {
    return MODDED_RL;
  }

  @Override
  public void render(final PoseStack poseStack,
                     final MultiBufferSource buffer,
                     final int packedLight,
                     final AbstractClientPlayer player,
                     final float limbSwing,
                     final float limbSwingAmount,
                     final float partialTicks,
                     final float ageInTicks,
                     final float netHeadYaw,
                     final float headPitch) {
    final var handler =
      player.getExistingDataOrNull(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    if (handler == null) {
      return;
    }


    if (barrierShields.isEmpty()) {
      return;
    }

    final var renderBuffers = Minecraft.getInstance().renderBuffers();
    final var bufferSource = renderBuffers.bufferSource();
    final var eyePos = player.getEyePosition();

    poseStack.pushPose();
    poseStack.translate(eyePos.x, eyePos.y, eyePos.z);

    // 将偏航角和俯仰角转换为方向向量
    var yaw = player.getYRot() * Mth.DEG_TO_RAD;
    var pitch = player.getXRot() * Mth.DEG_TO_RAD;

    // 计算方向向量
    var dirX = -Math.sin(yaw) * Math.cos(pitch);
    var dirY = -Math.sin(pitch);
    var dirZ = Math.cos(yaw) * Math.cos(pitch);
    var direction = new Vec3(dirX, dirY, dirZ).normalize();

    var eyePosition = player.getEyePosition(partialTicks);
    var pos = eyePosition.add(direction.scale(1.5f));
    poseStack.translate(pos.x, pos.y, pos.z);

    poseStack.mulPose(Axis.YP.rotation(-yaw));
    poseStack.mulPose(Axis.XP.rotation(pitch));

    final var indexVetexs = ModMath.getIndexVetexs60(1.5f);
    for (var number : barrierShields.keySet()) {
      var indexVetex = indexVetexs[number];
      poseStack.translate(indexVetex.x(), indexVetex.y(), indexVetex.z());
      renderModel(poseStack, bufferSource
      );
    }

    poseStack.popPose();
  }

  private void renderModel(final PoseStack poseStack, final BufferSource bufferSource) {
    boolean flag1 = true;
    poseStack.pushPose();
    poseStack.translate(-0.7f, -0.7f, -0.5f);
    poseStack.scale(1.5f, 1.5f, 1);
    for (var model : BAKED_MODEL.getRenderPasses(EMPTY_ITEM_STACK, flag1)) {
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
            model.getQuads(null, direction, randomsource)
          );
        }

        randomsource.setSeed(i);
        this.renderQuadList(poseStack,
          vertexconsumer,
          model.getQuads(null, null, randomsource)
        );
      }
    }
    poseStack.popPose();
  }

  private void renderQuadList(final PoseStack poseStack,
                              final VertexConsumer buffer,
                              final List<BakedQuad> quads) {
    boolean flag = !AegusBarriesShieldRenderer.EMPTY_ITEM_STACK.isEmpty();
    var posestack$pose = poseStack.last();

    for (var bakedquad : quads) {
      int i = -1;
      if (flag && bakedquad.isTinted()) {
        i = ITEM_COLORS_COLOR.getColor(AegusBarriesShieldRenderer.EMPTY_ITEM_STACK, bakedquad.getTintIndex());
      }

      float f = (float) FastColor.ARGB32.alpha(i) / 255.0F;
      float f1 = (float) FastColor.ARGB32.red(i) / 255.0F;
      float f2 = (float) FastColor.ARGB32.green(i) / 255.0F;
      float f3 = (float) FastColor.ARGB32.blue(i) / 255.0F;
      buffer.putBulkData(posestack$pose, bakedquad, f1, f2, f3, f,
        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY
        , true);
    }
  }

}

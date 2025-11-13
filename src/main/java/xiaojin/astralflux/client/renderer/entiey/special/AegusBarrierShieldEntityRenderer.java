package xiaojin.astralflux.client.renderer.entiey.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.util.ModUtil;

import java.util.List;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldEntityRenderer extends EntityRenderer<AegusBarrierShieldEntity> {
  private static final ItemStack EMPTY_ITEM_STACK = ItemStack.EMPTY;
  public static final ResourceLocation MODDED_RL = AstralFlux.modRL("entity/aegus_barrier_shield");
  public static final ModelResourceLocation MODEL_RESOURCE_LOCATION =
    ModelResourceLocation.standalone(MODDED_RL);
  private static final ItemColors ITEM_COLORS_COLOR = ItemColors.createDefault(BlockColors.createDefault());
  public static final RenderType TYPE = RenderType.eyes(TextureAtlas.LOCATION_BLOCKS);
  private static BakedModel bakedModel;

  public AegusBarrierShieldEntityRenderer(final EntityRendererProvider.Context context) {
    super(context);
  }

  public static LayerDefinition createBodyLayer() {
    bakedModel = Minecraft.getInstance().getModelManager().getModel(MODEL_RESOURCE_LOCATION);
    var meshDefinition = new MeshDefinition();
    return LayerDefinition.create(meshDefinition, 24, 24);
  }

  @Override
  public void render(final AegusBarrierShieldEntity pEntity,
                     final float entityYaw,
                     final float partialTick,
                     final PoseStack poseStack,
                     final MultiBufferSource bufferSource, final int packedLight) {
    super.render(pEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);


    poseStack.pushPose();

    final Vec3 lookingVec = pEntity.getLookAngle();
    final double angleX = Math.atan2(lookingVec.x, lookingVec.z);
    final double angleY = Math.atan(-lookingVec.y);

    poseStack.mulPose(Axis.YP.rotation((float) angleX));
    poseStack.mulPose(Axis.XP.rotation((float) angleY));

    // poseStack.mulPose(Axis.YP.rotation(-pEntity.getViewYRot(partialTick)));
    // poseStack.mulPose(Axis.XP.rotation(pEntity.getViewXRot(partialTick)));

    poseStack.translate(-0.7f, -0.7f, -0.5f);
    poseStack.scale(1.5f, 1.5f, 1);

    var combinedOverlay = OverlayTexture.NO_OVERLAY;
    renderModel(poseStack, bufferSource, packedLight, combinedOverlay);

    poseStack.popPose();
  }

  private void renderModel(final PoseStack poseStack, final MultiBufferSource bufferSource, final int combinedLight, final int combinedOverlay) {
    boolean flag1 = true;
    for (BakedModel model : bakedModel.getRenderPasses(EMPTY_ITEM_STACK, flag1)) {
      for (RenderType rendertype : model.getRenderTypes(EMPTY_ITEM_STACK, flag1)) {
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource,
          rendertype,
          false,
          EMPTY_ITEM_STACK.hasFoil());
        RandomSource randomsource = RandomSource.create();
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

  @Override
  public ResourceLocation getTextureLocation(final AegusBarrierShieldEntity entity) {
    return MODDED_RL;
  }
}

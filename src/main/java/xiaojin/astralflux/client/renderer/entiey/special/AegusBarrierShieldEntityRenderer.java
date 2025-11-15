package xiaojin.astralflux.client.renderer.entiey.special;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;

import javax.annotation.Nonnull;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldEntityRenderer extends EntityRenderer<AegusBarrierShieldEntity> {
  public AegusBarrierShieldEntityRenderer(Context context) {
    super(context);
  }

  @Override
  public void render(final AegusBarrierShieldEntity pEntity,
                     final float entityYaw,
                     final float partialTick,
                     final PoseStack poseStack,
                     final MultiBufferSource bufferSource,
                     final int packedLight) {
  }


  @Override
  @Nonnull
  public ResourceLocation getTextureLocation(final AegusBarrierShieldEntity entity) {
    return ResourceLocation.fromNamespaceAndPath("", "");
  }
}

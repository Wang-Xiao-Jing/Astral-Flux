package xiaojin.astralflux.client.renderer.entiey.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.LightTexture;
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

  public AegusBarrierShieldEntityRenderer(final EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(final AegusBarrierShieldEntity pEntity,
                     final float entityYaw,
                     final float partialTick,
                     final PoseStack poseStack,
                     final MultiBufferSource bufferSource, final int packedLight) {
    super.render(pEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(final AegusBarrierShieldEntity entity) {
    return null;
  }
}

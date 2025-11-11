package xiaojin.astralflux.client.renderer.entiey.special;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManagerEntity;

public class AegusBarrierShieldManagerRenderer extends EntityRenderer<AegusBarrierShieldManagerEntity> {
  public AegusBarrierShieldManagerRenderer(final EntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public ResourceLocation getTextureLocation(final AegusBarrierShieldManagerEntity entity) {
    return null;
  }
}

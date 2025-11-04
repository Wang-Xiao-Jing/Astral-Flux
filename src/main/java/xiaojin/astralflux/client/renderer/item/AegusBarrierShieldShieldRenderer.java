package xiaojin.astralflux.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import xiaojin.astralflux.client.renderer.ModRender;
import xiaojin.astralflux.init.ModItems;

/**
 * 埃癸斯壁垒盾牌渲染
 */
public class AegusBarrierShieldShieldRenderer implements ModRender {
  private static final AegusBarrierShieldShieldRenderer INSTANCE = new AegusBarrierShieldShieldRenderer();

  public static AegusBarrierShieldShieldRenderer get() {
    return INSTANCE;
  }

  @Override
  public void levelRender(final Minecraft minecraft, final ClientLevel level, final Frustum frustum, final PoseStack pose, final Camera camera) {
    var item = minecraft.player.getMainHandItem();
    if (!item.is(ModItems.AEGUS_BARRIER)) {
      return;
    }
  }
}

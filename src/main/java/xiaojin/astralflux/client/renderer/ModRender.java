package xiaojin.astralflux.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;

public interface ModRender {
  void levelRender(Minecraft minecraft, ClientLevel level, Frustum frustum, PoseStack pose, Camera camera);
}

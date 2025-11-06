package xiaojin.astralflux.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;

public class RenderUtil {
  public static void renderTextures(PoseStack pose, VertexConsumer buffer,
                                    float size,
                                    float x, float y, float z,
                                    float u, float v, float u1, float v1,
                                    int r, int g, int b, int a) {
    int fullBright = LightTexture.FULL_BRIGHT;
    PoseStack.Pose last = pose.last();
    float s = size / 2;
    renderVertex(last, buffer, x + s, y - s, z, r, g, b, a, u1, v1, fullBright);
    renderVertex(last, buffer, x + s, y + s, z, r, g, b, a, u1, v, fullBright);
    renderVertex(last, buffer, x - s, y + s, z, r, g, b, a, u, v, fullBright);
    renderVertex(last, buffer, x - s, y - s, z, r, g, b, a, u, v1, fullBright);
  }

  private static void renderVertex(PoseStack.Pose pose, VertexConsumer buffer,
                                   float x, float y, float z,
                                   int r, int g, int b, int a,
                                   float u, float v, int packedLight) {
    buffer.addVertex(pose, x, y, z)
      .setUv(u, v)
      .setColor(r, g, b, a)
      .setLight(packedLight);
  }
}

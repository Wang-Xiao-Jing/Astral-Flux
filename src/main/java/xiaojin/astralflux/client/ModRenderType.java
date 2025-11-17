package xiaojin.astralflux.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.CompositeState.builder;
import static net.minecraft.client.renderer.RenderType.create;


public final class ModRenderType {
  public static final RenderType AEGUS_BARRIER_SHIELD = RenderType.create(
    "aegus_barrier_shield",
    DefaultVertexFormat.NEW_ENTITY,
    VertexFormat.Mode.QUADS,
    786432,
    true,
    true,
    RenderType.CompositeState.builder()
      .setShaderState(RenderStateShard.RENDERTYPE_EYES_SHADER)
      .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
      .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
      .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
      .setWriteMaskState(RenderStateShard.COLOR_WRITE)
      .setLayeringState(RenderStateShard.NO_LAYERING)
      .createCompositeState(false)
  );
}

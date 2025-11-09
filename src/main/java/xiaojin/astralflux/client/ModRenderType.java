package xiaojin.astralflux.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.CompositeState.builder;
import static net.minecraft.client.renderer.RenderType.create;


public final class ModRenderType {
  public static final Function<ResourceLocation, RenderType> ICON =
    Util.memoize(texture -> create("icon",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, QUADS,
      1536, builder()
        .setShaderState(/*RENDERTYPE_TEXT_SHADER,*/RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER)
        .setTextureState(new TextureStateShard(texture, false, false))
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setLightmapState(LIGHTMAP)
        .createCompositeState(false)));
}

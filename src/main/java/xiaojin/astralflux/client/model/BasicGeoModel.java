package xiaojin.astralflux.client.model;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import xiaojin.astralflux.AstralFlux;

@OnlyIn(Dist.CLIENT)
public class BasicGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
  public final ResourceLocation modelPath;
  public final ResourceLocation texturePath;
  public final ResourceLocation animationsPath;

  public BasicGeoModel(String pathName) {
    this(pathName, pathName, pathName);
  }

  public BasicGeoModel(String pathName, String texturePath) {
    this(pathName, texturePath, pathName);
  }

  public BasicGeoModel(String modelPath, String texturePath, String animationsPath) {
    this.modelPath = getGeoModelPath(modelPath);
    this.texturePath = getGeoTexturePath(texturePath);
    this.animationsPath = getGeoAnimationsPath(animationsPath);
  }

  public static @NotNull ResourceLocation getGeoAnimationsPath(String animationsPath) {
    return AstralFlux.modRL("animations/" + animationsPath + ".png");
  }

  public static @NotNull ResourceLocation getGeoTexturePath(String texturePath) {
    return AstralFlux.modRL("textures/" + texturePath + ".png");
  }

  public static @NotNull ResourceLocation getGeoModelPath(String modelPath) {
    return AstralFlux.modRL("geo/" + modelPath + ".geo.json");
  }

  @Override
  public ResourceLocation getModelResource(T animatable) {
    return modelPath;
  }

  @Override
  public ResourceLocation getTextureResource(T animatable) {
    return texturePath;
  }

  @Override
  public ResourceLocation getAnimationResource(T animatable) {
    return animationsPath;
  }

  public static class BlockModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
    public BlockModel(String pathName) {
      super("block/" + pathName);
    }

    public BlockModel(String modelPath, String texturePath) {
      super("block/" + modelPath, "block/" + texturePath);
    }

    public BlockModel(String modelPath, String texturePath, String animationsPath) {
      super("block/" + modelPath, "block/" + texturePath, "block/" + animationsPath);
    }
  }

  public static class ItemModel<T extends GeoAnimatable> extends BasicGeoModel<T> {
    public ItemModel(String pathName) {
      super("item/" + pathName);
    }

    public ItemModel(String modelPath, String texturePath) {
      super("item/" + modelPath, "item/" + texturePath);
    }

    public ItemModel(String modelPath, String texturePath, String animationsPath) {
      super("item/" + modelPath, "item/" + texturePath, "item/" + animationsPath);
    }
  }
}

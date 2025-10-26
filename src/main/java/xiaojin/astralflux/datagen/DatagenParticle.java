package xiaojin.astralflux.datagen;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import xiaojin.astralflux.core.AstralFlux;

import java.util.function.Supplier;

/// 粒子
public class DatagenParticle extends ParticleDescriptionProvider {
  public DatagenParticle(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  @Override
  protected void addDescriptions() {
  }

  private <p extends ParticleOptions> void createSprite(Supplier<ParticleType<p>> type,
                                                        String name) {
    sprite(type.get(), ResourceLocation.fromNamespaceAndPath(AstralFlux.ID, name));
  }
}

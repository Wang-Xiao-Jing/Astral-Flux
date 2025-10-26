package xiaojin.astralflux.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import xiaojin.astralflux.core.AstralFlux;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatagenDatapackBuiltinEntries extends DatapackBuiltinEntriesProvider {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.DAMAGE_TYPE, context -> {
    });

  public DatagenDatapackBuiltinEntries(PackOutput output,
                                       CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, BUILDER, Set.of(AstralFlux.ID));
  }

  public static Holder.Reference<DamageType> createDamageType(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageType, float exhaustion) {
    return createDamageType(context, damageType, DamageScaling.ALWAYS, exhaustion,
      DamageEffects.HURT, DeathMessageType.DEFAULT);
  }

  public static Holder.Reference<DamageType> createDamageType(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageType, DamageScaling damageScaling, float exhaustion, DamageEffects damageEffects, DeathMessageType deathMessageType) {
    return context.register(damageType, new DamageType(damageType.location().getPath(),
      damageScaling, exhaustion, damageEffects, deathMessageType));
  }
}

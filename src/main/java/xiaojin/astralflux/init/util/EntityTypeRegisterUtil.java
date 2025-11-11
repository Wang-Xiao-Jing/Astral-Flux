package xiaojin.astralflux.init.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModEntityTypes;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class EntityTypeRegisterUtil {

  protected static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, EntityType.Builder<T> builder) {
    return register(name, () -> builder.build(AstralFlux.modRLText(name)));
  }

  protected static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, EntityType.EntityFactory<T> factory, MobCategory category, Function<EntityType.Builder<T>, EntityType.Builder<T>> builder) {
    return register(name, builder.apply(EntityType.Builder.of(factory, category)));
  }

  private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, Supplier<EntityType<T>> entityType) {
    return ModEntityTypes.REGISTRY.register(name, entityType);
  }
}

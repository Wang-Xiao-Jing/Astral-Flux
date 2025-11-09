package xiaojin.astralflux.init.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModEntityTypes;

import java.util.function.Supplier;

public abstract class EntityTypeRegisterUtil {

  protected static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, EntityType.Builder<T> builder) {
    return register(name, () -> builder.build(AstralFlux.modRLText(name)));
  }

  private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, Supplier<EntityType<T>> entityType) {
    return ModEntityTypes.REGISTRY.register(name, entityType);
  }
}

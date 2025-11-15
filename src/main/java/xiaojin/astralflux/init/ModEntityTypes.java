package xiaojin.astralflux.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManager;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.EntityTypeRegisterUtil;

public final class ModEntityTypes extends EntityTypeRegisterUtil {
  public static final DeferredRegister<EntityType<?>> REGISTRY = AstralFlux.modRegister(BuiltInRegistries.ENTITY_TYPE);

  public static final DeferredHolder<EntityType<?>, EntityType<AegusBarrierShieldManager>> AEGUS_BARRIER_SHIELD_MANAGER_ENTITY = register("aegus_barrier_shield_manager",
    AegusBarrierShieldManager::new, MobCategory.MISC, builder -> builder
      .sized(0, 0)
      .fireImmune()
      .clientTrackingRange(4)
      .updateInterval(20)
      .noSave()
      .noSummon());

  public static final DeferredHolder<EntityType<?>, EntityType<AegusBarrierShieldEntity>> AEGUS_BARRIER_SHIELD_ENTITY = register("aegus_barrier_shield",
    AegusBarrierShieldEntity::new, MobCategory.MISC, builder -> builder
      .sized(1.5f, 1.5f)
      .eyeHeight(0.5f)
      .clientTrackingRange(4)
      .updateInterval(20)
      .fireImmune()
      .noSave()
      .noSummon());
}

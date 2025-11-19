package xiaojin.astralflux.common.entity.special;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import xiaojin.astralflux.init.ModEntityTypes;

import java.util.Optional;
import java.util.UUID;

// TODO 添加拦截能力
public class AegusBarrierShieldEntity extends Entity implements GeoEntity, TraceableEntity {
  public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldEntity.class, EntityDataSerializers.OPTIONAL_UUID);
  public static final EntityDataAccessor<Boolean> IS_INTACT_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldEntity.class, EntityDataSerializers.BOOLEAN);

  private int tick;
  /**
   * 是否移除
   */
  private boolean shouldRemove;
  @Nullable
  private UUID ownerUUID;
  @Nullable
  private Entity cachedOwner;

  private AegusBarrierShieldManagerEntity manager;

  public AegusBarrierShieldEntity(final EntityType<?> entityType, final Level level) {
    super(entityType, level);
  }

  public AegusBarrierShieldEntity(final Level level, AegusBarrierShieldManagerEntity manager) {
    super(ModEntityTypes.AEGUS_BARRIER_SHIELD_ENTITY.get(), level);
    this.manager = manager;
    setOwner(manager.getOwner());
    this.setIntact(false);
  }

  @Override
  public void tick() {
    if (this.tickCount >= 20 * 0.2 && !isIntact()) {
      this.setIntact(true);
    }
    super.tick();
  }

  @Nullable
  @Override
  public Entity getOwner() {
    if (this.level().isClientSide) {
      this.ownerUUID = this.entityData.get(OWNER_UUID_ACCESSOR).orElse(null);
    }

    if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
      return this.cachedOwner;
    }

    if (this.ownerUUID != null && this.level() instanceof ServerLevel serverlevel) {
      this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
      return this.cachedOwner;
    }

    return null;
  }

  public Entity getEffectSource() {
    return MoreObjects.firstNonNull(this.getOwner(), this);
  }

  protected boolean ownedBy(Entity entity) {
    return entity.getUUID().equals(this.ownerUUID);
  }

  @Override
  public void remove(final RemovalReason reason) {
    super.remove(reason);
    this.shouldRemove = true;
  }

  public void setOwner(@Nullable final Entity owner) {
    this.cachedOwner = owner;
    this.ownerUUID = owner != null ? owner.getUUID() : null;
  }

  @Override
  public boolean canBeHitByProjectile() {
    return true;
  }

  @Override
  public void defineSynchedData(final SynchedEntityData.Builder builder) {
    builder.define(OWNER_UUID_ACCESSOR, Optional.empty());
    builder.define(IS_INTACT_ACCESSOR, false);
  }

  @Override
  protected void readAdditionalSaveData(final CompoundTag compound) {}

  @Override
  protected void addAdditionalSaveData(final CompoundTag compound) {}

  @Override
  public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {}

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return null;
  }

  @Override
  public boolean shouldBeSaved() {
    return false;
  }

  // TODO isIntact不为false时 被攻击到移除
  public boolean shouldRemove() {
    return shouldRemove;
  }

  public void setRemove() {
    this.shouldRemove = true;
  }

  public boolean isIntact() {
    return this.entityData.get(IS_INTACT_ACCESSOR);
  }

  public AegusBarrierShieldManagerEntity getManager() {
    return manager;
  }

  public void setManager(final AegusBarrierShieldManagerEntity manager) {
    this.manager = manager;
  }

  public void setIntact(boolean intact) {
    this.entityData.set(IS_INTACT_ACCESSOR, intact);
  }
}

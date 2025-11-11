package xiaojin.astralflux.client.renderer.entiey.special;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

// TODO 添加entityType
// 添加拦截能力
public class AegusBarrierShieldEntiey extends Entity implements GeoEntity, TraceableEntity {
  public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(AegusBarrierShieldEntiey.class, EntityDataSerializers.OPTIONAL_UUID);

  private int tick;
  /**
   * 是否完整
   */
  private boolean isIntact; // TODO 需要同步到客户端
  /**
   * 是否移除
   */
  private boolean isRemove;
  @Nullable
  private UUID ownerUUID;
  @Nullable
  private Entity cachedOwner;

  private AegusBarrierShieldManagerEntiey manager;

  public AegusBarrierShieldEntiey(final EntityType<?> entityType, final Level level) {
    super(entityType, level);
  }

  public AegusBarrierShieldEntiey(final Level level, AegusBarrierShieldManagerEntiey manager) {
    super(entityType, level);
    this.manager = manager;
    setOwner(manager.getOwner());
  }

  @Override
  public void tick() {
    super.tick();
    if (this.tickCount >= 20 * 0.2 && !isIntact) {
      this.isIntact = true;
    }
    this.tickCount++;
  }

  @Override
  public void remove(final RemovalReason reason) {
    super.remove(reason);
  }

  @Nullable
  @Override
  public Entity getOwner() {
    if (this.level().isClientSide) {
      this.ownerUUID = this.entityData.get(OWNER_UUID).orElse(null);
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
  public void restoreFrom(Entity entity) {
    super.restoreFrom(entity);
    if (entity instanceof AegusBarrierShieldEntiey entiey) {
      this.cachedOwner = entiey.cachedOwner;
    }
  }

  public void setOwner(@Nullable final Entity owner) {
    this.cachedOwner = owner;
    this.ownerUUID = owner != null ? owner.getUUID() : null;
  }

  @Override
  protected void defineSynchedData(final SynchedEntityData.Builder builder) {

  }

  @Override
  protected void readAdditionalSaveData(final CompoundTag compound) {

  }

  @Override
  protected void addAdditionalSaveData(final CompoundTag compound) {

  }

  @Override
  public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {

  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return null;
  }

  @Override
  public boolean shouldBeSaved() {
    return false;
  }

  public boolean isRemove() {
    return isRemove;
  }

  public boolean isIntact() {
    return isIntact;
  }

  public AegusBarrierShieldManagerEntiey getManager() {
    return manager;
  }

  public void setManager(final AegusBarrierShieldManagerEntiey manager) {
    this.manager = manager;
  }
}

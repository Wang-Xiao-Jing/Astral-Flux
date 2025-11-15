package xiaojin.astralflux.common.entity.special;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus.Internal;
import xiaojin.astralflux.api.abs.AbstractAegusBarrierShieldManager;
import xiaojin.astralflux.common.ABSBoundingBox;
import xiaojin.astralflux.init.ModEntityTypes;

public class AegusBarrierShieldEntity extends Entity {
  public final AbstractAegusBarrierShieldManager manager;
  boolean shouldRemove;

  @Internal
  public AegusBarrierShieldEntity(EntityType<AegusBarrierShieldEntity> entityType, Level world) {
    super(entityType, world);
    this.manager = null;
  }

  public AegusBarrierShieldEntity(final Level level, AbstractAegusBarrierShieldManager manager) {
    super(ModEntityTypes.AEGUS_BARRIER_SHIELD_ENTITY.get(), level);
    this.manager = manager;
    this.setBoundingBox(new ABSBoundingBox(this));
  }

  @Override
  public void restoreFrom(Entity entity) {
    super.restoreFrom(entity);
    if (!(entity instanceof AegusBarrierShieldEntity entity1)) {
      return;
    }
  }

  @Override
  public boolean canBeHitByProjectile() {
    return true;
  }

  @Override
  public void defineSynchedData(final SynchedEntityData.Builder builder) {
  }

  @Override
  protected void readAdditionalSaveData(final CompoundTag compound) {}

  @Override
  protected void addAdditionalSaveData(final CompoundTag compound) {}

  @Override
  public boolean shouldBeSaved() {
    return false;
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    this.shouldRemove = true;
    return super.hurt(source, amount);
  }

  boolean shouldRemove() {
    return shouldRemove;
  }
}

package xiaojin.astralflux.api.abs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;
import xiaojin.astralflux.init.ModEntityTypes;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractAegusBarrierShieldManager extends Entity {
  public AbstractAegusBarrierShieldManager(Level level) {
    super(ModEntityTypes.AEGUS_BARRIER_SHIELD_MANAGER_ENTITY.get(), level);
    Arrays.fill(this.getShields(), new AegusBarrierShieldEntity(level, this));
    Arrays.fill(this.getExpandingProgress(), 0.0);
  }

  /**
   * Manager 每 tick 执行的任务。
   */
  abstract protected void managerTick();

  /**
   * 获取护盾展开进度。 0.0 为未展开，1.0 为完全展开，-1.0 为失效。
   */
  abstract public double[] getExpandingProgress();

  /**
   * 获取全部护盾。
   */
  abstract public AegusBarrierShieldEntity[] getShields();

  /**
   * 获取是否依然处于展开状态。
   */
  public boolean isStillInExpanding() {
    return Arrays.stream(getExpandingProgress()).allMatch(it -> it == 1.0);
  }

  /**
   * 获取是否全部盾牌已失效。
   */
  public boolean allInExpired() {
    return !isStillInExpanding() && Arrays.stream(getShields()).allMatch(Objects::isNull);
  }

  public boolean isEmpty() {
    return this.allInExpired();
  }

  public void removeShield(final int index) {
    this.getShields()[index] = null;
    this.getExpandingProgress()[index] = -1.0;
  }

  public void addShield(final int index) {
    this.getShields()[index] = new AegusBarrierShieldEntity(this.level(), this);
    this.getExpandingProgress()[index] = 0.0;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.level().isClientSide) {
      this.managerTick();
    }
  }

  @Override
  protected void defineSynchedData(Builder builder) {
    // Do nothing.
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compoundTag) {
    // Do nothing.
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag compoundTag) {
    // Do nothing.
  }
}

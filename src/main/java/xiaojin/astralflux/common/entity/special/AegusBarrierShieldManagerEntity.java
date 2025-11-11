package xiaojin.astralflux.common.entity.special;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.init.ModEntityTypes;
import xiaojin.astralflux.util.ModUtil;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.*;

public class AegusBarrierShieldManagerEntity extends Entity implements TraceableEntity {
  public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
  public static final EntityDataAccessor<OptionalInt> OWNER_ID_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
  public static final EntityDataAccessor<Integer> CONSUME_TICS_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, EntityDataSerializers.INT);
  public static final EntityDataAccessor<Boolean> IS_CONSUME_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, EntityDataSerializers.BOOLEAN);
  public static final EntityDataAccessor<Integer> EXPANDS_COUNT_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, EntityDataSerializers.INT);

  public static final int CONSUME_VALUE = -15;

  /**
   * 子盾的旋转和位置映射
   */
  private static final Quaterniond[] INDEX_VETEXS = ModUtil.getIndexVetexs(1.5f);

  private final Map<Integer, AegusBarrierShieldEntity> shieldList = HashMap.newHashMap(7); // TODO 需要同步到客户端

  @Nullable
  private UUID ownerUUID;
  @Nullable
  private Entity cachedOwner;
  private float targetXRot;
  private float targetYRot;

  public AegusBarrierShieldManagerEntity(final EntityType<?> entityType, final Level level) {
    this(entityType, level, null);
  }

  public AegusBarrierShieldManagerEntity(final Level level, @Nullable final Entity owner) {
    this(ModEntityTypes.AEGUS_BARRIER_SHIELD_MANAGER_ENTITY.get(), level, owner);
  }

  public AegusBarrierShieldManagerEntity(final EntityType<?> entityType, final Level level, @Nullable final Entity owner) {
    super(entityType, level);
    setOwner(owner);
  }

  public void setOwner(@Nullable final Entity owner) {
    this.cachedOwner = owner;
    this.ownerUUID = owner != null ? owner.getUUID() : null;
    if (this.ownerUUID != null) {
      this.entityData.set(OWNER_UUID_ACCESSOR, Optional.of(this.ownerUUID));
      this.entityData.set(OWNER_ID_ACCESSOR, OptionalInt.of(owner.getId()));
    }
  }

  @Nullable
  public AegusBarrierShieldEntity getShield(int index) {
    return shieldList.get(index);
  }

  @Override
  public void tick() {
    super.tick();
    var ownerEntity = getOwner();
    var isClientSide = level().isClientSide();

    // 检测是否需要增加或移除护盾
    if (increaseOrRemoval(isClientSide, ownerEntity)) {
      return;
    }

    turn(this.targetYRot, this.targetXRot);

    if (ownerEntity != null) {
      setPos(ownerEntity.getEyePosition());
    }

    // TODO重写逻辑目前异常
    var iterator = this.shieldList.entrySet().iterator();
    while (iterator.hasNext()) {
      var entry = iterator.next();
      var entiey = entry.getValue();
      var index = entry.getKey();
      var vetex = INDEX_VETEXS[index];
      var vec3 = new Vec3(vetex.x(), vetex.y(), vetex.z());

      var yRot = getYRot();
      var xRot = getXRot();

      var pos = position()
        .add(Vec3.directionFromRotation(yRot, xRot).scale(2))
        .add(vec3.yRot(yRot).xRot(xRot));

      entiey.setPos(pos);
      // 调整该处以适配旋转
      entiey.turn(yRot, xRot);

      if (!isClientSide && entiey.isRemove()) {
        entiey.remove(RemovalReason.DISCARDED);
        iterator.remove();
      }
    }

    if (ownerEntity != null) {
      setTargetRot(ownerEntity.getYRot(), ownerEntity.getXRot());
    }

    if (this.isConsume()) {
      this.setConsumeTics(this.getConsumeTics() + 1);
    }
  }

  private boolean increaseOrRemoval(final boolean isClientSide, final Entity entity) {
    if (isClientSide) {
      return false;
    }

    if (!(entity instanceof LivingEntity livingEntity)) {
      remove(RemovalReason.DISCARDED);
      return true;
    }

    // 消耗源魂
    if (this.getConsumeTics() % (20 * 3) == 0) {
      if (!SourceSoulUtil.isModifyAllowed(livingEntity, CONSUME_VALUE)) {
        remove(RemovalReason.DISCARDED);
        return true;
      } else {
        SourceSoulUtil.modify(livingEntity, CONSUME_VALUE);
      }
    }
    // 添加盾牌
    if (this.tickCount % (4.0) == 0) {
      addShield();
      if (!this.isConsume()) {
        setConsume(true);
      }
    }

    return false;
  }

  public boolean addShield() {
    var count = getExpandsCount();
    if (count >= 7) {
      return false;
    }

    var shieldEntity = this.shieldList.get(Math.min(count - 1, 0));
    if (shieldEntity != null && !shieldEntity.isIntact()) {
      return false;
    }

    var newEntiey = new AegusBarrierShieldEntity(level(), this);
    level().addFreshEntity(newEntiey);
    this.shieldList.put(count, newEntiey);
    this.setExpandsCount(count + 1);
    return true;
  }

  @Override
  public void remove(final RemovalReason reason) {
    super.remove(reason);
    this.shieldList.values().forEach(e -> e.remove(RemovalReason.DISCARDED));

    var entity = getOwner();
    if (!(entity instanceof LivingEntity livingEntity)) {
      return;
    }

    livingEntity.removeData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
  }

  @Override
  protected void defineSynchedData(final SynchedEntityData.Builder builder) {
    builder.define(OWNER_UUID_ACCESSOR, Optional.empty());
    builder.define(OWNER_ID_ACCESSOR, OptionalInt.empty());
    builder.define(CONSUME_TICS_ACCESSOR, 0);
    builder.define(IS_CONSUME_ACCESSOR, false);
    builder.define(EXPANDS_COUNT_ACCESSOR, 0);
  }

  public void setTargetYRot(float targetYRot) {
    this.targetYRot = targetYRot;
  }

  public void setTargetXRot(float targetXRot) {
    this.targetXRot = targetXRot;
  }

  public void setTargetRot(float xRot, float yRot) {
    this.setTargetXRot(xRot);
    this.setTargetYRot(yRot);
  }

  @Nullable
  @Override
  public Entity getOwner() {
    var level = this.level();
    if (level.isClientSide) {
      this.ownerUUID = this.entityData.get(OWNER_UUID_ACCESSOR).orElse(null);
      var ownerId = this.entityData.get(OWNER_ID_ACCESSOR);
      if (ownerId.isPresent()){
        this.cachedOwner = level.getEntity(ownerId.getAsInt());
      }
    }

    if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
      return this.cachedOwner;
    }

    if (this.ownerUUID != null && level instanceof ServerLevel serverlevel) {
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
  protected void addAdditionalSaveData(CompoundTag compound) {}

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {}

  @Override
  public boolean shouldBeSaved() {
    return false;
  }

  public int getConsumeTics() {
    return this.entityData.get(CONSUME_TICS_ACCESSOR);
  }

  public void setConsumeTics(int consumeTics) {
    this.entityData.set(CONSUME_TICS_ACCESSOR, consumeTics);
  }

  public boolean isConsume() {
    return this.entityData.get(IS_CONSUME_ACCESSOR);
  }

  public void setConsume(boolean consume) {
    this.entityData.set(IS_CONSUME_ACCESSOR, consume);
  }

  public int getExpandsCount() {
    return this.entityData.get(EXPANDS_COUNT_ACCESSOR);
  }

  public void setExpandsCount(int expandsCount) {
    this.entityData.set(EXPANDS_COUNT_ACCESSOR, expandsCount);
  }
}

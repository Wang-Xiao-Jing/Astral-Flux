package xiaojin.astralflux.common.entity.special;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.init.ModEntityDataSerializers;
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
  public static final EntityDataAccessor<Map<Integer, Map.Entry<Integer, UUID>>> SHIELD_LIST_ACCESSOR =
    SynchedEntityData.defineId(AegusBarrierShieldManagerEntity.class, ModEntityDataSerializers.AEGUS_BARRIER_SHIELD_MANAGER_SHIELDS.get());

  public static final int CONSUME_VALUE = -15;

  /**
   * 子盾的旋转和位置映射
   */
  private static final Vector3d[] INDEX_VETEXS = ModUtil.getIndexVetexs60(1.5f);

  private final Map<Integer, AegusBarrierShieldEntity> shieldList = new Int2ObjectOpenHashMap<>(7);

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
      getEntityData().set(OWNER_UUID_ACCESSOR, Optional.of(this.ownerUUID));
      getEntityData().set(OWNER_ID_ACCESSOR, OptionalInt.of(owner.getId()));
    }
  }

  @Nullable
  public AegusBarrierShieldEntity getShield(int index) {
    return this.shieldList.get(index);
  }

  @Override
  public void tick() {
    super.tick();
    var owner = this.getOwner();
    var isClientSide = level().isClientSide();

    // 检测是否需要增加或移除护盾
    if (increaseOrRemoval(isClientSide, owner)) {
      return;
    }


    turn(this.targetYRot, this.targetXRot);

    if (owner != null) {
      this.moveTo(owner.getEyePosition());
      this.setXRot(owner.xRotO);
      this.setYRot(owner.yRotO);
    }

    // TODO重写逻辑目前异常
    var iterator = this.shieldList.entrySet().iterator();
    while (iterator.hasNext()) {
      final var entry = iterator.next();
      final var entity = entry.getValue();
      final int index = entry.getKey();

      if (Objects.isNull(entity)) {
        continue;
      }

      final var vec3 = new Vector3d(INDEX_VETEXS[index]);
      final Vec3 lookingVec = this.getLookAngle();
      final double angle = Math.atan2(lookingVec.x, lookingVec.z);

      // 位移到玩家位置，同时保持中心对齐
      vec3.add(0, 0, 2 + (index == 0 ? 0.5 : 0));
      // vec3.rotateY(angle);
      vec3.rotateY(angle);

      var offsetPos = new Vec3(
        vec3.x + this.getX(),
        vec3.y + this.getY(),
        vec3.z + this.getZ()
      );

      entity.moveTo(offsetPos);
      // 调整该处以适配旋转
      // entiey.turn(yRot, xRot);
      entity.setXRot(0);
      entity.setYRot(this.getYRot());

      var vecLookAt = new Vector3d(lookingVec.x, lookingVec.y, lookingVec.z);
      switch(index) {
        case 1 -> {
          entity.setXRot(-30);
          entity.setYRot(entity.getYRot() - 30);
        }
        case 2 -> {
          entity.setXRot(-30);
          entity.setYRot(entity.getYRot() + 30);
        }
        case 3 -> {
          entity.setYRot(entity.getYRot() + 30);
        }
        case 4 -> {
          entity.setXRot(30);
          entity.setYRot(entity.getYRot() + 30);
        }
        case 5 -> {
          entity.setXRot(30);
          entity.setYRot(entity.getYRot() - 30);
        }
        case 6 -> {
          entity.setYRot(entity.getYRot() - 30);
        }
        default -> {}
      }

      if (!isClientSide && entity.isRemove()) {
        entity.remove(RemovalReason.DISCARDED);
        iterator.remove();
      }
    }

    if (owner != null) {
      setTargetRot(owner.getYRot(), owner.getXRot());
    }

    if (this.isConsume()) {
      this.setConsumeTics(this.getConsumeTics() + 1);
    }
  }

  private void removeShieldEntity(int index, final AegusBarrierShieldEntity shieldEntity) {
    var data = getEntityData();
    data.get(SHIELD_LIST_ACCESSOR).remove(index);
    data.set(SHIELD_LIST_ACCESSOR, data.get(SHIELD_LIST_ACCESSOR),true);
    onSyncedDataUpdated(SHIELD_LIST_ACCESSOR);
    shieldEntity.remove(RemovalReason.DISCARDED);
  }

  private void addShieldEntity(int index, final AegusBarrierShieldEntity shieldEntity) {
    var data = getEntityData();
    data.get(SHIELD_LIST_ACCESSOR).put(index, Map.entry(shieldEntity.getId(), shieldEntity.getUUID()));
    data.set(SHIELD_LIST_ACCESSOR, data.get(SHIELD_LIST_ACCESSOR),true);
    this.shieldList.put(index, shieldEntity);
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

    // Fixme 为什么使用 map 而不是 list？
    this.shieldList.put(count, newEntiey);
    addShieldEntity(count, newEntiey);
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
    builder.define(SHIELD_LIST_ACCESSOR, new Int2ObjectOpenHashMap<>(7));
  }

  @Override
  public void onSyncedDataUpdated(final List<SynchedEntityData.DataValue<?>> dataValues) {
    super.onSyncedDataUpdated(dataValues);
    var dataValue = dataValues.stream()
      .filter(d -> d.id() == 13).findFirst();
    if (dataValue.isEmpty()){
      return;
    }
    var uuidIntegerMap = (Map<Integer, Map.Entry<Integer, UUID>>) dataValue.orElseThrow().value();
    for (var entry : uuidIntegerMap.entrySet()) {
      var number = entry.getKey();
      var integerUUIDEntry = entry.getValue();
      var shieldEntity = this.level().getEntity(integerUUIDEntry.getKey());
      assert shieldEntity instanceof AegusBarrierShieldEntity || !shieldEntity.getUUID().equals(integerUUIDEntry.getValue()) : "Shield entity not found";
      this.shieldList.put(number, (AegusBarrierShieldEntity) shieldEntity);
    }
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
      this.ownerUUID = getEntityData().get(OWNER_UUID_ACCESSOR).orElse(null);
      var ownerId = getEntityData().get(OWNER_ID_ACCESSOR);
      if (ownerId.isPresent()) {
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
  protected void addAdditionalSaveData(CompoundTag compound) {
  }

  @Override
  protected void readAdditionalSaveData(CompoundTag compound) {
  }

  @Override
  public boolean shouldBeSaved() {
    return false;
  }

  public int getConsumeTics() {
    return getEntityData().get(CONSUME_TICS_ACCESSOR);
  }

  public void setConsumeTics(int consumeTics) {
    getEntityData().set(CONSUME_TICS_ACCESSOR, consumeTics);
  }

  public boolean isConsume() {
    return getEntityData().get(IS_CONSUME_ACCESSOR);
  }

  public void setConsume(boolean consume) {
    getEntityData().set(IS_CONSUME_ACCESSOR, consume);
  }

  public int getExpandsCount() {
    return getEntityData().get(EXPANDS_COUNT_ACCESSOR);
  }

  public void setExpandsCount(int expandsCount) {
    getEntityData().set(EXPANDS_COUNT_ACCESSOR, expandsCount);
  }
}

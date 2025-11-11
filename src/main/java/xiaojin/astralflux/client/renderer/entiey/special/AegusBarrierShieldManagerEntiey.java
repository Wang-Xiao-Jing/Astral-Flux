package xiaojin.astralflux.client.renderer.entiey.special;

import com.google.common.base.MoreObjects;
import com.google.common.collect.HashBiMap;
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
import xiaojin.astralflux.util.ModUtil;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.*;

// TODO 添加entityType
public class AegusBarrierShieldManagerEntiey extends Entity implements TraceableEntity {
  public static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(AegusBarrierShieldManagerEntiey.class, EntityDataSerializers.OPTIONAL_UUID);
  public static final int CONSUME_VALUE = -15;

  /**
   * 子盾的旋转和位置映射
   */
  private static final Quaterniond[] INDEX_VETEXS = ModUtil.getIndexVetexs(1.5f);

  private final Map<Integer, AegusBarrierShieldEntiey> shieldList = HashMap.newHashMap(7); // TODO 需要同步到客户端

  @Nullable
  private UUID ownerUUID;
  @Nullable
  private Entity cachedOwner;
  private float targetXRot;
  private float targetYRot;
  private int consumeTics; // TODO 需要同步到客户端
  private boolean isConsume; // TODO 需要同步到客户端
  private int expandsCount; // TODO 需要同步到客户端

  public AegusBarrierShieldManagerEntiey(final EntityType<?> entityType, final Level level) {
    this(entityType, level, null);
  }

  public AegusBarrierShieldManagerEntiey(final Level level, @Nullable final Entity owner) {
    this(entityType, level, owner);
  }

  public AegusBarrierShieldManagerEntiey(final EntityType<?> entityType, final Level level, @Nullable final Entity owner) {
    super(entityType, level);
    setOwner(owner);
    addShield();
  }

  public void setOwner(@Nullable final Entity owner) {
    this.cachedOwner = owner;
    this.ownerUUID = owner != null ? owner.getUUID() : null;
    if (this.ownerUUID != null) {
      this.entityData.set(OWNER_UUID, Optional.of(this.ownerUUID));
    }
  }

  @Nullable
  public AegusBarrierShieldEntiey getShield(int index) {
    return shieldList.get(index);
  }

  public Map<Integer, AegusBarrierShieldEntiey> getShieldList() {
    return HashBiMap.create(this.shieldList);
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

    this.tickCount++;
    if (this.isConsume) {
      this.consumeTics++;
    }
  }

  /**
   * 检测是否需要增加或移除护盾
   */
  private boolean increaseOrRemoval(final boolean isClientSide, final Entity entity) {
    if (isClientSide) {
      return false;
    }

    if (!(entity instanceof LivingEntity livingEntity)) {
      remove(RemovalReason.DISCARDED);
      return true;
    }

    // 消耗源魂
    if (this.consumeTics % (20 * 3) == 0) {
      if (!SourceSoulUtil.isModifyAllowed(livingEntity, CONSUME_VALUE)) {
        remove(RemovalReason.DISCARDED);
        return true;
      } else {
        SourceSoulUtil.modify(livingEntity, CONSUME_VALUE);
      }
    }

    // 添加盾牌
    if (this.tickCount % (20 * 0.2) == 0) {
      addShield();
      if (!this.isConsume) {
        isConsume = true;
      }
    }

    return false;
  }

  public boolean addShield() {
    if (this.expandsCount == 7) {
      return false;
    }

    var number = this.shieldList.keySet().stream().max(Integer::compareTo);
    if (number.isEmpty()) {
      return false;
    }

    var integer = number.get();
    var entity = this.shieldList.get(integer);

    if (!entity.isIntact()) {// 添加一个新护盾
      return false;
    }

    var newEntiey = new AegusBarrierShieldEntiey(level(), this);
    if (level().addFreshEntity(newEntiey)) {
      return false;
    }
    this.shieldList.put(integer, newEntiey);
    this.expandsCount++;
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
  }

  @Override
  protected void defineSynchedData(final SynchedEntityData.Builder builder) {
    builder.define(OWNER_UUID, Optional.empty());
  }

  /**
   * 设置目标Y轴旋转角度
   *
   * @param targetYRot 目标Y轴旋转角度
   */
  public void setTargetYRot(float targetYRot) {
    this.targetYRot = targetYRot;
  }

  /**
   * 设置目标X轴旋转角度
   *
   * @param targetXRot 目标X轴旋转角度
   */
  public void setTargetXRot(float targetXRot) {
    this.targetXRot = targetXRot;
  }

  /**
   * 设置目标旋转角度
   *
   * @param xRot X轴旋转角度
   * @param yRot Y轴旋转角度
   */
  public void setTargetRot(float xRot, float yRot) {
    this.setTargetXRot(xRot);
    this.setTargetYRot(yRot);
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
    if (entity instanceof AegusBarrierShieldManagerEntiey entiey) {
      this.cachedOwner = entiey.cachedOwner;
    }
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
}

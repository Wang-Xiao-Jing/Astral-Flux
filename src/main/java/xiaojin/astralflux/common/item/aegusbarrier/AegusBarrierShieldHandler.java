package xiaojin.astralflux.common.item.aegusbarrier;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.loading.math.function.generic.AbsFunction;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManagerEntity;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModDateAttachmentTypes;

import java.util.*;

public final class AegusBarrierShieldHandler {
  private final AegusBarrierShieldManagerEntity manager;
  private final int id;
  private final UUID uuid;
  private final Map<Integer, Integer> shields;
  private int expandsCount;
  private float yRotO;
  private float yRot;
  private boolean isRemoved;

  public AegusBarrierShieldHandler(final AegusBarrierShieldManagerEntity manager) {
    this.manager = manager;
    this.shields = new HashMap<>(Map.ofEntries(manager.getShieldList().entrySet().stream()
      .map(e -> Map.entry(e.getKey(), e.getValue().tickCount))
      .toArray(Map.Entry[]::new)));
    this.expandsCount = manager.getExpandsCount();
    this.yRotO = manager.yRotO;
    this.yRot = manager.getYRot();
    this.id = manager.getId();
    this.uuid = manager.getUUID();
  }

  /**
   * 客户端
   */
  private AegusBarrierShieldHandler(AegusBarrierShieldManagerEntity manager,
                                    Map<Integer, Integer> shields,
                                    int expandsCount,
                                    float yRotO,
                                    float yRot) {

    this.shields = new HashMap<>(shields);
    this.yRotO = yRotO;
    this.yRot = yRot;
    this.expandsCount = expandsCount;
    this.manager = manager;
    this.uuid = this.manager.getUUID();
    this.id = this.manager.getId();
    this.manager.setHandler(this);
  }

  public static AegusBarrierShieldHandler create(IAttachmentHolder holder) {
    assert holder instanceof Player : "AegusBarrierShieldHandler must be attached to a player";
    return create((Player) holder);
  }

  public static AegusBarrierShieldHandler create(Player player) {
    var level = player.level();
    var entity = new AegusBarrierShieldManagerEntity(level, player);
    var handler = new AegusBarrierShieldHandler(entity);
    Vec3 position = player.position();
    entity.absMoveTo(position.x, position.y, position.z);
    entity.absRotateTo(player.getYRot(), player.getXRot());
    level.addFreshEntity(entity);
    entity.setHandler(handler);
    player.setData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD, handler);
    AttributeInstance instance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
    if (instance != null) {
      AttributeModifier aegusBarrierShield = new AttributeModifier(AstralFlux.modRL("aegus_barrier_shield"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
      instance.addOrUpdateTransientModifier(aegusBarrierShield);
    }
    return handler;
  }

  public void remove(Player player) {
    if (isRemoved) {
      return;
    }
    if (manager != null) {
      this.manager.remove(Entity.RemovalReason.DISCARDED);
    }
    player.removeData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    if (player.level() instanceof ServerLevel serverLevel) {
      Vec3 position = player.position();
      serverLevel.playSound(null, position.x, position.y, position.z, SoundEvents.BEACON_DEACTIVATE, SoundSource.AMBIENT);
      AttributeInstance instance = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
      if (instance != null) {
        instance.removeModifier(AstralFlux.modRL("aegus_barrier_shield"));
      }
    }
    this.isRemoved = true;
  }

  public void setYRot(float yRot) {
    this.yRot = yRot;
  }

  public void setRotO(float yRotO) {
    this.yRotO = yRotO;
  }

  public void lerpRotationStep(int steps, double targetYRot) {
    double d0 = 1.0 / (double) steps;
    float f = (float) Mth.rotLerp(d0, yRot, targetYRot);
    this.setYRot(f);
  }

  public void lerpOldRotationStep(int steps, double targetYRot) {
    double d0 = 1.0 / (double) steps;
    float f = (float) Mth.rotLerp(d0, yRotO, targetYRot);
    this.setRotO(f);
  }

  public Map<Integer, Integer> getShields() {
    return shields;
  }

  public void shieldData(int shieldId, int tickCount) {
    this.shields.put(shieldId, tickCount);
  }

  public void removeShield(int shieldId) {
    this.shields.remove(shieldId);
  }

  public void syncData(Player player) {
    player.syncData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
  }

  public float getViewYRot(float partialTick) {
    return partialTick == 1.0F ? this.yRot : Mth.lerp(partialTick, this.yRotO, this.yRot);
  }

  public int getExpandsCount() {
    return expandsCount;
  }

  public void setExpandsCount(final int expandsCount) {
    this.expandsCount = expandsCount;
  }

  public AegusBarrierShieldManagerEntity getManager() {
    return manager;
  }

  public static final class Sync implements AttachmentSyncHandler<AegusBarrierShieldHandler> {
    @Override
    public void write(final RegistryFriendlyByteBuf buf,
                      final AegusBarrierShieldHandler attachment,
                      final boolean initialSync) {
      buf.writeInt(attachment.id);
      buf.writeUUID(attachment.uuid);

      buf.writeFloat(attachment.yRotO);
      buf.writeFloat(attachment.yRot);

      buf.writeInt(attachment.expandsCount);
      buf.writeBoolean(attachment.isRemoved);

      buf.writeArray(attachment.shields.entrySet().toArray(Map.Entry[]::new), (b, entry) -> {
        b.writeInt((int) entry.getKey());
        b.writeInt((int) entry.getValue());
      });
    }

    @Override
    public @NotNull AegusBarrierShieldHandler read(final IAttachmentHolder holder,
                                                   final RegistryFriendlyByteBuf buf,
                                                   @Nullable final AegusBarrierShieldHandler previousValue) {
      assert holder instanceof Player: "AegusBarrierShieldHandler must be attached to an player";
      int id = buf.readInt();
      UUID uuid = buf.readUUID();

      float yRotO = buf.readFloat();
      float yRot = buf.readFloat();
      int expandsCount = buf.readInt();
      boolean isRemoved = buf.readBoolean();

      Map<Integer, Integer> map = Map.ofEntries(buf.readArray(Map.Entry[]::new, (b) ->
        Map.entry(b.readInt(), b.readInt())));

      if (previousValue == null) {
        Entity entity = ((Player) holder).level().getEntity(id);
        assert entity instanceof AegusBarrierShieldManagerEntity : "Entity must be an AegusBarrierShieldManagerEntity";
        assert entity.getUUID().equals(uuid) : "Entity UUID mismatch";
        return new AegusBarrierShieldHandler((AegusBarrierShieldManagerEntity) entity, map, expandsCount, yRotO, yRot);
      }

      previousValue.expandsCount = expandsCount;
      var map1 = previousValue.shields;
      map1.entrySet().removeIf(item -> !map.entrySet().contains(item));;
      if (!map.isEmpty()){
        map1.putAll(map);
      }

      previousValue.yRotO = yRotO;
      previousValue.yRot = yRot;
      previousValue.isRemoved = isRemoved;
      return previousValue;
    }
  }
}

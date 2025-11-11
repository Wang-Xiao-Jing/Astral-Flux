package xiaojin.astralflux.common.item.aegusbarrier;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldManagerEntity;
import xiaojin.astralflux.init.ModDateAttachmentTypes;

import java.util.UUID;

public final class AegusBarrierShieldHandler {
  private final AegusBarrierShieldManagerEntity manager;
  private final UUID managerUUID;
  private final int managerId;

  public AegusBarrierShieldHandler(final AegusBarrierShieldManagerEntity manager) {
    this.manager = manager;
    this.managerUUID = manager.getUUID();
    this.managerId = manager.getId();
  }

  public AegusBarrierShieldHandler(final int managerId, final UUID managerUUID, final Level level) {
    var entity = level.getEntity(managerId);
    assert entity instanceof AegusBarrierShieldManagerEntity : "Invalid managerId";
    assert entity.getUUID().equals(managerUUID) : "Invalid managerId or managerUUID";
    this.manager = (AegusBarrierShieldManagerEntity) entity;
    this.managerUUID = managerUUID;
    this.managerId = managerId;
  }

  public static AegusBarrierShieldHandler create(IAttachmentHolder holder) {
    assert holder instanceof Player : "AegusBarrierShieldHandler must be attached to a player";
    return create((Player) holder);
  }

  public static AegusBarrierShieldHandler create(Player player) {
    var level = player.level();
    var entity = new AegusBarrierShieldManagerEntity(level, player);
    level.addFreshEntity(entity);
    var data = new AegusBarrierShieldHandler(entity);
    player.setData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD, data);
    return data;
  }

  public AegusBarrierShieldManagerEntity getManager() {
    return manager;
  }

  public UUID getManagerUUID() {
    return managerUUID;
  }

  public int getManagerId() {
    return managerId;
  }

  public void remove(Player player) {
    manager.remove(Entity.RemovalReason.DISCARDED);
    player.removeData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
  }


  public static final class Sync implements AttachmentSyncHandler<AegusBarrierShieldHandler> {
    @Override
    public void write(final RegistryFriendlyByteBuf buf,
                      final AegusBarrierShieldHandler attachment,
                      final boolean initialSync) {
      buf.writeInt(attachment.getManagerId());
      buf.writeUUID(attachment.getManagerUUID());
    }

    @Override
    public @NotNull AegusBarrierShieldHandler read(final IAttachmentHolder holder,
                                                   final RegistryFriendlyByteBuf buf,
                                                   @Nullable final AegusBarrierShieldHandler previousValue) {
      assert holder instanceof Entity: "AegusBarrierShieldHandler must be attached to an entity";
      return new AegusBarrierShieldHandler(buf.readInt(), buf.readUUID(), ((Entity) holder).level());
    }
  }
}

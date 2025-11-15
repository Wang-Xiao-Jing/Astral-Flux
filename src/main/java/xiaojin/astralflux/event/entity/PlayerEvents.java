package xiaojin.astralflux.event.entity;

import ctn.ctnapi.util.PayloadUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xiaojin.astralflux.api.ItemLeftClickEmpty;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;
import xiaojin.astralflux.common.payloads.PlayerLeftClickEmptyPayload;
import xiaojin.astralflux.eventadditional.SourceSoulEvents;
import xiaojin.astralflux.events.PlayerLeftClickEmptyEvent;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.api.mixin.IModPlayer;
import xiaojin.astralflux.util.ABSHelper;
import xiaojin.astralflux.util.DirtHelper;

import java.util.EnumSet;
import java.util.Objects;

@EventBusSubscriber
public final class PlayerEvents {
  /**
   * 难度改变
   */
  @SubscribeEvent
  public static void difficultyChangeEvent(DifficultyChangeEvent event) {
    SourceSoulEvents.onDifficultyChange(event);
  }

  /**
   * 玩家重生或维度切换后
   */
  @SubscribeEvent
  public static void playerEventClone(PlayerEvent.Clone event) {
    SourceSoulEvents.onReset(event);
  }

  /**
   * 玩家每tick
   */
  @SubscribeEvent
  public static void playerTickEventPre(PlayerTickEvent.Pre event) {
    SourceSoulEvents.onTick(event);
  }

  /**
   * 受击
   */
  @SubscribeEvent
  public static void livingDamageEventPost(LivingDamageEvent.Post event) {
    if (event.getEntity() instanceof Player) {
      SourceSoulEvents.onHit(event);
    }
  }

  /**
   * 修改源魂事件
   */
  @SubscribeEvent
  public static <T extends LivingEntity> void sourceSoulModifyEvent$Post(SourceSoulModifyEvent.Post<T> event) {
  }

  /**
   * 修改源魂事件
   */
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static <T extends LivingEntity> void sourceSoulModifyEvent$Post$Lowest(SourceSoulModifyEvent.Post<T> event) {
// TODO 测试完成记得解除注释
// SourceSoulEvents.onConsume(event);
    SourceSoulBarLayerDraw.INSTANCE.addModify(event.getModifyValue());
  }

  /**
   * 左键点击空（客户端）
   */
  @SubscribeEvent
  public static void playerInteractEventLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
    playerLeftClickEmpty(event);
  }

  /**
   * 左键点击方块
   */
  @SubscribeEvent
  public static void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    playerLeftClickEmpty(event);
  }

  /**
   * 左键点击空（服务端）
   */
  @SubscribeEvent
  public static void playerLeftClickEmptyEventPre(PlayerLeftClickEmptyEvent.Post event) {
    var itemStack = event.getItemStack();
    if (!(itemStack.getItem() instanceof ItemLeftClickEmpty itemLeftClick)) {
      return;
    }
    itemLeftClick.leftClick(itemStack, event.getEntity());
  }

  @SubscribeEvent
  public static void playerTickEvent(PlayerTickEvent.Post event) {
    final Player player = event.getEntity();
    final var modPlayer = IModPlayer.of(player);
    final var manager = modPlayer.astralFlux$getShieldManager();

    if (player.level().isClientSide || Objects.isNull(manager)) {
      return;
    }

    if (manager.allInExpired()) {
      modPlayer.astralFlux$setShieldManager(null);
      player.setData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD, "");
      return;
    }

    manager.setOldPosAndRot();

    if (manager.level() != player.level()) {
      manager.teleportTo(
        (ServerLevel) player.level(),
        player.getX(),
        player.getY(),
        player.getZ(),
        EnumSet.noneOf(RelativeMovement.class),
        player.getYRot(),
        player.getXRot()
      );
      return;
    }

    manager.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
    manager.update();

    final var data = player.getExistingData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD);
    final String dataNew = ABSHelper.encodeArray(manager.getExpandingProgress());
    if (data.isEmpty() || !data.get().equals(dataNew)) {
      player.setData(ModDateAttachmentTypes.AEGUS_BARRIER_SHIELD, dataNew);
    }
  }

  private static void playerLeftClickEmpty(final PlayerInteractEvent event) {
    DirtHelper.runDirt(event.getSide(),
      () -> {
        final var payload = new PlayerLeftClickEmptyPayload(event.getHand());
        PayloadUtil.sendToServer(payload);
      },
      () -> PlayerLeftClickEmptyPayload.trigger(event.getEntity(), event.getHand())
    );
  }
}

package xiaojin.astralflux.event.entity;

import ctn.ctnapi.util.PayloadUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xiaojin.astralflux.api.ItemLeftClickEmpty;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;
import xiaojin.astralflux.common.payloads.PlayerLeftClickEmptyPayload;
import xiaojin.astralflux.eventadditional.AegusBarrierEvents;
import xiaojin.astralflux.eventadditional.SourceSoulEvents;
import xiaojin.astralflux.events.PlayerLeftClickEmptyEvent;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;

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
    AegusBarrierEvents.onTick(event);
    SourceSoulEvents.onTick(event);
  }

  /**
   * 受击
   */
  @SubscribeEvent
  public static void livingDamageEventPost(LivingDamageEvent.Post event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    SourceSoulEvents.onHit(event);
  }

  /**
   * 受击之前
   */
  @SubscribeEvent
  public static void livingIncomingDamageEvent(LivingIncomingDamageEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    AegusBarrierEvents.onHurt(event);
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
    SourceSoulEvents.onConsume(event);
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

  public static void playerLeftClickEmpty(PlayerInteractEvent event) {
    if (event.getSide().isClient()) {
      PayloadUtil.sendToServer(new PlayerLeftClickEmptyPayload(event.getHand()));
      return;
    }
    PlayerLeftClickEmptyPayload.trigger(event.getEntity(), event.getHand());
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
}

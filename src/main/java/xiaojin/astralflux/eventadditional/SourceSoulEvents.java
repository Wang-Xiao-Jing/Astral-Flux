package xiaojin.astralflux.eventadditional;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.init.ModDateAttachmentTypes;
import xiaojin.astralflux.util.SourceSoulUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static xiaojin.astralflux.util.SourceSoulUtil.setPauseRecoveryTick;

public final class SourceSoulEvents {
  /**
   * 难度改变
   */
  public static void onDifficultyChange(DifficultyChangeEvent event) {
    final var minecraft = ServerLifecycleHooks.getCurrentServer();
    if (minecraft == null) {
      return;
    }
    var difficulty = event.getDifficulty();
    StreamSupport.stream(minecraft.getAllLevels().spliterator(), true).map(ServerLevel::players)
      .filter(list -> !list.isEmpty()).flatMap(List::stream).filter(SourceSoulUtil::isAttribute)
      .collect(Collectors.toMap(serverPlayer -> difficulty, serverPlayer -> serverPlayer))
      .forEach(SourceSoulUtil::difficultyChange);
  }

  /**
   * 玩家重生或维度切换后
   */
  public static void onReset(PlayerEvent.Clone event) {
    final var player = event.getEntity();
    if (!event.isWasDeath()) {
      return;
    }

    // Fixme 那设置 copyOnDeath 干什么（？
    player.setData(ModDateAttachmentTypes.SOURCE_SOUL.value(), 0d);
    player.setData(ModDateAttachmentTypes.SOURCE_SOUL_PAUSE_RECOVERY_TICK.value(), 0);
  }

  /**
   * 玩家每tick
   */
  public static void onTick(PlayerTickEvent.Pre event) {
    final var player = event.getEntity();
    if (!(player instanceof ServerPlayer)) {
      return;
    }
    // TODO 不处于处于特殊环境（比如梦境）时自然恢复
    if (!SourceSoulUtil.allowSourceSoulRecover(player)) {
      SourceSoulUtil.modifyPauseRecoveryTick(player, -1);
      return;
    } else {
      SourceSoulUtil.setPauseRecoveryTick(player, 0);
    }
    var attributeValue = player.getAttributeValue(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
    SourceSoulUtil.modify(player, attributeValue);
  }

  /**
   * 受击
   */
  public static void onHit(LivingDamageEvent.Post event) {
    var entity = event.getEntity();
    if (event.getNewDamage() <= 0) {
      return;
    }
    setPauseRecoveryTick(entity, SourceSoulUtil.HIT_PAUSE_RECOVERY_TICK);
  }

  /**
   * 消耗源魂事件
   */
  public static <T extends LivingEntity> void onConsume(SourceSoulModifyEvent.Post<T> event) {
    if (event.getModifyValue() >= 0) {
      return;
    }
    setPauseRecoveryTick(event.getEntity(), SourceSoulUtil.CONSUME_PAUSE_RECOVERY_TICK);
  }
}

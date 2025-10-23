package xiaojin.astralflux.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;
import xiaojin.astralflux.api.sourcesoul.SourceSoulUtil;
import xiaojin.astralflux.init.ModAttributes;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static xiaojin.astralflux.AstralFlux.ID;

@EventBusSubscriber(modid = ID)
public final class SourceSoulEvent {
  /**
   * 难度改变
   */
  @SubscribeEvent
  public static void onDifficultyChange(DifficultyChangeEvent event) {
    final var minecraft = ServerLifecycleHooks.getCurrentServer();
    if (minecraft != null) {
      var difficulty = event.getDifficulty();
      StreamSupport.stream(minecraft.getAllLevels().spliterator(), true)
        .map(ServerLevel::players)
        .filter(list -> !list.isEmpty()).flatMap(List::stream)
        .filter(SourceSoulUtil::isSourceSoul)
        .collect(Collectors.toMap(serverPlayer -> difficulty,
          serverPlayer -> serverPlayer))
        .forEach((difficulty1, player) -> {
          SourceSoulUtil.difficultyChange(difficulty1, player);
          SourceSoulUtil.synchronize(player);
        });
    }
  }

  /**
   * 保存玩家属性
   */
  @SubscribeEvent
  public static void onSave(PlayerEvent.SaveToFile event) {
    if (event.getEntity() instanceof ServerPlayer serverPlayer) {
      ISourceSoul.of(serverPlayer).astralFlux$saveSourceSoulValue();
      SourceSoulUtil.synchronize(serverPlayer);
    }
  }

  /**
   * 加载玩家-此时客户端玩家未创建
   */
  @SubscribeEvent
  public static void onLoading(PlayerEvent.LoadFromFile event) {
    var player = event.getEntity();
    ISourceSoul.of(player).astralFlux$loadSourceSoulValue();
    SourceSoulUtil.difficultyChange(player.level().getDifficulty(), player);
  }


  /**
   * 登录到世界-此时客户端玩家已创建
   */
  @SubscribeEvent
  public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer serverPlayer) {
      SourceSoulUtil.synchronize(serverPlayer);
    }
  }

  /**
   * 玩家重生或维度切换后
   */
  @SubscribeEvent
  public static void onReset(PlayerEvent.Clone event) {
    final var player = event.getEntity();
    if (event.isWasDeath()) {
      ISourceSoul.of(player).astralFlux$setSourceSoulValue(0);
    }
    if (player instanceof ServerPlayer serverPlayer) {
      ISourceSoul.of(serverPlayer).astralFlux$saveSourceSoulValue();
      SourceSoulUtil.synchronize(serverPlayer);
    }
  }

  // TODO 直在不处于最大上限，没有处于特殊环境（比如梦境）时自然恢复
  // TODO 在被消耗后或恢复被打断后10s内不会再触发自然恢复

  /**
   * 玩家每tick
   */
  @SubscribeEvent
  public static void onTick(PlayerTickEvent.Pre event) {
    final var player = event.getEntity();
    if (!(player instanceof ServerPlayer serverPlayer)) {
      return;
    }
    final var sourceSoul = ISourceSoul.of(serverPlayer);
    if (!sourceSoul.astralFlux$isRecover()) {
      return;
    }
    sourceSoul.astralFlux$increaseSourceSoulValue(player.getAttributeValue(ModAttributes.SOURCE_SOUL_RECOVERY));
    SourceSoulUtil.synchronize(serverPlayer);
  }
}

package xiaojin.astralflux.event;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import xiaojin.astralflux.common.item.AegusBarrier;

@EventBusSubscriber
public class EntityEvent {
  @SubscribeEvent
  public static void entityHurtEvent(LivingIncomingDamageEvent event) {
    final var entity = event.getEntity();
    if (!(entity instanceof Player player) || !(event.getSource().getEntity() instanceof Projectile projectile)) {
      return;
    }

    final var item = player.getItemInHand(player.getUsedItemHand());
    if (!(item.getItem() instanceof AegusBarrier) || !AegusBarrier.canBarrierWork(item)) {
      return;
    }

    final int index = getIndexOfSide(projectile, player);
    if (AegusBarrier.canBarrierSideWork(item, index)) {
      event.setCanceled(true);
    }
  }

  private static int getIndexOfSide(final Projectile projectile, final Player player) {
    if (projectile.getY() > player.getY() + player.getEyeHeight()) {
      return 0;
    }

    final double x = projectile.getX() - player.getX();
    final double y = projectile.getZ() - player.getZ();

    double angle = Math.acos(calCos(x, y)) * 180 / Math.PI;
    angle = projectile.getX() < player.getX() ? -angle : angle;
    angle = (angle + 360) % 360;

    return Mth.floor(angle / 6);
  }

  private static double calCos(final double x0, final double y0) {
    // 角度选取为 [-180, 179]，向量间区则为 [0, -1]
    final double x1 = 0;
    final double y1 = -1;

    final double dotProduct = x0 * y0 + x1 * y1;
    final double fc =
      Math.sqrt(Math.pow(x0, 2) + Math.pow(x1, 2)) * Math.sqrt(Math.pow(y0, 2) + Math.pow(y1, 2));
    return dotProduct / fc;
  }
}

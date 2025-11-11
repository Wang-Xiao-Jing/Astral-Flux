package xiaojin.astralflux.eventadditional;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import xiaojin.astralflux.init.ModAttachmentTypes;

public final class AegusBarrierEvents {
  public static void onTick(final PlayerTickEvent.Pre event) {
    final var entity = event.getEntity();
    var barrierShields = entity.getExistingDataOrNull(ModAttachmentTypes.IS_AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }

    barrierShields.setTargetRot(entity.getXRot(), entity.getYRot());
    barrierShields.tick(entity);
  }

  public static void onHurt(LivingIncomingDamageEvent event) {
    final var entity = event.getEntity();
    if (!(entity instanceof Player player) || !(event.getSource().getEntity() instanceof Projectile projectile)) {
      return;
    }

    var barrierShields = player.getExistingDataOrNull(ModAttachmentTypes.IS_AEGUS_BARRIER_SHIELD);
    if (barrierShields == null) {
      return;
    }

    final int index = getIndexOfSide(projectile, player, barrierShields);
    if (barrierShields.interdict(index)) {
      event.setCanceled(true);
    }
  }

  /**
   * 获取将要命中护盾的定位数。
   *
   * @param projectile 投射物
   * @param player     玩家
   */
  private static int getIndexOfSide(final Projectile projectile,
                                    final Player player,
                                    final AegusBarrierShieldsManager barrierShields) {
    // TODO 改下这个以适配AegusBarrierShields的角度
    final Vec3 diProj = projectile.getLookAngle();
    final Vec3 diPlayer = player.getLookAngle();

    if (diProj.y > -60 && diProj.y < 60) {
      return 0;
    }

    final double x0 = diProj.x;
    final double x1 = diPlayer.x;
    final double y0 = diProj.z;
    final double y1 = diPlayer.z;

    double angle = Math.acos(calCos(x0, y0, x1, y1)) * 180 / Math.PI;
    angle = projectile.getX() < player.getX() ? -angle : angle;
    angle = (angle + 360) % 360;

    return Mth.floor(angle / 6);
  }

  /**
   * 双非零向量求 cos θ。
   */
  private static double calCos(final double x0, final double y0,
                               final double x1, final double y1) {
    final double dp = x0 * x1 + y0 * y1;
    final double fc =
      Math.sqrt(Math.pow(x0, 2) + Math.pow(y0, 2)) * Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
    return dp / fc;
  }
}

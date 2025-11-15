package xiaojin.astralflux.util;

import org.joml.Vector2d;
import org.joml.Vector3d;

public final class ModMath {
  public static Vector2d getIndexVetexs(final double r, final double angle) {
    final double x = r * Math.cos(angle * Math.PI / 180);
    final double y = r * Math.sin(angle * Math.PI / 180);

    return new Vector2d(x, y);
  }

  public static double lerpInAndOut(double delta) {
    return Math.pow(16 * delta, 2) * Math.pow(delta - 1, 2);
  }

  public static double arc(double r, double angle) {
    return (angle * r * Math.PI) / 180;
  }

  public static double arcAngle(double r, double l) {
    return (180 * l) / (r *  Math.PI);
  }

  /**
   * 180 >> 360
   */
  public static double to360Angle(double angle) {
    if (angle > 0) {
      return fixAngle360(angle);
    }

    return (360 + fixAngle180(angle)) % 360;
  }

  /**
   * 360 >> 180
   */
  public static double to180Angle(double angle) {
    if (angle > -180 && angle < 179) {
      return angle;
    }

    return (angle + 180) % 360 - 180;
  }

  public static double fixAngle180(double angle) {
    if (angle < -180) {
      return 179 - (Math.abs(angle) - 180);
    } else if (angle > 179) {
      return (angle - 180) - 180;
    }

    return angle;
  }

  public static double fixAngle360(double angle) {
    return angle % 360;
  }
}

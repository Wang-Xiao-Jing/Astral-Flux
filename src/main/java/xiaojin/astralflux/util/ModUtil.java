package xiaojin.astralflux.util;

import org.joml.Vector2d;
import org.joml.Vector3d;

public final class ModUtil {
  public static Vector3d[] getIndexVetexs60(final double r) {
    final int range = 7;
    final double funAngle = 360.0 / (range - 1);
    Vector3d[] index = new Vector3d[range];

    for (int i = 0; i < range; i++) {
      if (i == 0) {
        index[i] = new Vector3d(0, 0, 0);
        continue;
      }

      final double finalAngle = funAngle * i;

      final Vector2d v2d = getIndexVetexs(r, finalAngle);
      final var rotation = new Vector3d(v2d.x, v2d.y, 0);
      index[i] = rotation;
    }

    return index;
  }

  public static Vector2d getIndexVetexs(final double r, final double angle) {
    final double x = r * Math.cos(angle * Math.PI / 180);
    final double y = r * Math.sin(angle * Math.PI / 180);

    return new Vector2d(x, y);
  }

  public static double to360Angle(double angle) {
    if (angle > 179 || angle < -180) {
      angle = angle % 180;
    }

    return (360 + angle) % 360;
  }
}

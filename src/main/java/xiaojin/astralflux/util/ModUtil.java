package xiaojin.astralflux.util;

import org.joml.Quaterniond;

public final class ModUtil {
  /**
   * 获取环形内全部中心定位。
   *
   * @param x     原点 x。
   * @param y     原点 y。
   * @param z     原点 z。
   * @param r     半径。
   * @param angle 中心角度（360 制）。
   * @return 若干中心点位，Quaternion 四项分别对应坐标 X，坐标 Y，坐标 Z，以及当前中心旋转角度。
   */
  public static Quaterniond[] getIndexVertexs(final double x, final double y, final double z,
                                              final double r, final double angle) {
    final var index = getIndexVetexs(r, angle);
    for (Quaterniond quaterniond : index) {
      quaterniond.x += x;
      quaterniond.y += y;
      quaterniond.z += z;
    }
    return index;
  }

  public static Quaterniond[] getIndexVetexs(final double r, final double angle) {
    final int range = 7;
    final double funAngle = 360.0 / range;
    Quaterniond[] index = new Quaterniond[range];

    int indexOf = 0;

    for (double i = 0; i < 360.0; i += funAngle) {
      final double finalAngle = convertTo180(angle + funAngle);

      final double x = r * Math.cos(finalAngle * Math.PI / 180);
      final double y = r * Math.sin(finalAngle * Math.PI / 180);

      final var op = new Quaterniond(x, 0, y, finalAngle);
      index[indexOf++] = op;
    }

    return index;
  }

  private static double convertTo180(double a) {
    final boolean flag = a < 0;
    a = Math.abs(a) % 360;
    if (flag) {
      a = -a;
    }

    if (a > 179) {
      return -(a - 179 - 180);
    } else if (a < -180) {
      return -(a + 180);
    }

    return a;
  }
}

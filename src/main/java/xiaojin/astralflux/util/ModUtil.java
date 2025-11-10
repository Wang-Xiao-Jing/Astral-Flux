package xiaojin.astralflux.util;

import org.joml.Quaterniond;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.AbstractMap;
import java.util.Map;

public final class ModUtil {

  public static Map.Entry<Vector3fc, Quaterniond>[] getIndexVetexs(final double r, final double angle) {
    final int range = 7;
    final double funAngle = 360.0 / range;
    Map.Entry<Vector3fc, Quaterniond>[] index = new Map.Entry[range];

    int indexOf = 0;

    for (double i = 0; i < 360.0; i += funAngle) {
      final double finalAngle = convertTo180(angle + i);

      final double x = r * Math.cos(finalAngle * Math.PI / 180);
      final double y = r * Math.sin(finalAngle * Math.PI / 180);

      final var position = new Vector3f((float)x, 0, (float)y);
      final var rotation = new Quaterniond(0, 0, 0, finalAngle);
      index[indexOf++] = new AbstractMap.SimpleEntry<>(position, rotation);
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

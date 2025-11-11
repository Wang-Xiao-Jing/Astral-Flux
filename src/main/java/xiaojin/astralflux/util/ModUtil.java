package xiaojin.astralflux.util;

import org.joml.Quaterniond;

public final class ModUtil {


  public static Quaterniond[] getIndexVetexs(final double r) {
    final int range = 7;
    final double funAngle = 360.0 / (range - 1);
    Quaterniond[] index = new Quaterniond[range];

    for (int i = 0; i < range; i++) {
      if (i == 0) {
        index[i] = new Quaterniond(0, 0, 0, 0);
        continue;
      }

      final double finalAngle = funAngle * i;

      final double x = r * Math.cos(finalAngle * Math.PI / 180);
      final double y = r * Math.sin(finalAngle * Math.PI / 180);

      final var rotation = new Quaterniond((float)x, (float)y, 0, finalAngle);
      index[i] = rotation;
    }

    return index;
  }
}

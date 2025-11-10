package xiaojin.astralflux.util;

import org.joml.Quaterniond;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;

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

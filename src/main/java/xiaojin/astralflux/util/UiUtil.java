package xiaojin.astralflux.util;

import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class UiUtil {
  private static Vector3d[][] getIndexVertexs(final int oX, final int oY, final int oZ,
                                              final int r) {
    final Vector3d[][] indexs = new Vector3d[6][];
    final Vector2d[] based = getBsedIndexVertexs(r);

    final Vector3d[] basedTemp = new Vector3d[6];
    for (int i = 0; i < 6; i++) {
      final Vector3d v3d = new Vector3d(based[i].x, oY + r, based[i].y);
      basedTemp[i] = v3d;
    }
    indexs[0] = basedTemp;

    final Quaterniond[] q = new Quaterniond[6];
    for(int i = 0; i < 6; i++){
      q[i] = new Quaterniond(based[i].x, 0, based[i].y, 1).rotateX(120f);
    }

    for(int o = 0; o < 6; o++){
      final Vector3d[] t = new Vector3d[6];
      for (int i = 0; i < 6; i++) {
        int ag = i * 60;
        q[i].rotateY(ag);
        t[i] = new Vector3d(q[i].x, oY, q[i].y);
      }
      indexs[o] = t;
    }


    return indexs;
  }

  private static Vector2d[] getBsedIndexVertexs(final double r) {
    Vector2d[] index = new Vector2d[6];
    for(int i = 0; i < 6; i++) {
      index[i] = new Vector2d(indexVertexX(i, r), indexVertexY(i, r));
    }
    return index;
  }

  private static double indexVertexX(final int index, final double r) {
    return 0 + r * Math.cos(2 * Math.PI / 6 * (index + 120));
  }

  private static double indexVertexY(final int index, final double r) {
    return 0 + r * Math.sin(2 * Math.PI / 6 * (index + 120));
  }
}

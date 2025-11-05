package xiaojin.astralflux.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.joml.Vector3f;

public final class ModUtil {
  /**
   * 计算带平滑延迟的跟随位置
   *
   * @param current      当前位置
   * @param target       目标位置
   * @param speed        跟随速度 (0.0-1.0, 值越小越平滑但延迟越大)
   * @param partialTicks 部分刻度
   * @return 平滑后的位置
   */
  public static Vector3f calculateSmoothFollowPosition(Vec3 current, Vec3 target, float speed, float partialTicks) {
    float interpolatedSpeed = speed * partialTicks;
    return new Vector3f(
      (float) (current.x + (target.x - current.x) * interpolatedSpeed),
      (float) (current.y + (target.y - current.y) * interpolatedSpeed),
      (float) (current.z + (target.z - current.z) * interpolatedSpeed)
    );
  }

  public static Vector3d[][] getIndexVertexs(final double oX, final double oY, final double oZ,
                                              final double r) {
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

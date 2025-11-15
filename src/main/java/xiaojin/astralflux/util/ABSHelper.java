package xiaojin.astralflux.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Vector2f;
import org.joml.Vector3d;

/**
 * "Aegus Barrier Shield Helper".
 */
public class ABSHelper {
  private static final Gson gson = new Gson();
  private static final Vector3d[] shieldPositionOffset;

  /**
   * 获取盾牌的偏移值，已经过旋转处理。
   *
   * @param index 盾牌位置编号
   * @param angle 盾牌实际需要旋转的角度，由 XRot(yaw) 值推算求出。
   * @param origin 原始坐标，通常为 manager 所在坐标。
   */
  @Internal
  public static Vec3 getOffsetPos(final int index, final double angle, final Vec3 origin) {
    // 位移到玩家位置，同时保持中心对齐
    final Vector3d vec3 =
      new Vector3d(shieldPositionOffset[index])
        .add(0, 0, 2 + (index == 0 ? 0.5 : 0))
        .rotateY(angle);

    return new Vec3(
      vec3.x + origin.x(),
      vec3.y + origin.y(),
      vec3.z + origin.z()
    );
  }

  @Internal
  public static Vec3 getOffsetPos(final int index, final double angle, final double progress,
                                  final Vec3 origin) {
    if (progress == 1.0) {
      return getOffsetPos(index, angle, origin);
    }

    final var finalAngle = 60 * index;
    final var r = 0.5 * ModMath.lerpOut(progress);
    final var v2d = ModMath.getIndexVetexs(r, finalAngle);
    final var rotation = new Vector3d(v2d.x, v2d.y, 0);
    final var vec3 = rotation
        .add(0, 0, 2 + (index == 0 ? 0.5 : 0))
        .rotateY(angle);

    return new Vec3(
      vec3.x + origin.x(),
      vec3.y + origin.y(),
      vec3.z + origin.z()
    );
  }


  @Internal
  public static Vector2f getResult(final int index, float pitch) {
    float yaw = 0;

    switch(index) {
      case 1 -> {
        yaw = -30;
        pitch -= 30;
      }
      case 2 -> {
        yaw = -30;
        pitch += 30;
      }
      case 3 -> {
        pitch += 30;
      }
      case 4 -> {
        yaw = 30;
        pitch += 30;
      }
      case 5 -> {
        yaw = 30;
        pitch -= 30;
      }
      case 6 -> {
        pitch -= 30;
      }
    }

    return new Vector2f(yaw, pitch);
  }

  @Internal
  public static double[] decodeArray(final String json) {
    return gson.fromJson(json, TypeToken.getParameterized(double[].class).getType());
  }

  @Internal
  public static String encodeArray(final double[] array) {
    return gson.toJson(array);
  }

  static {
    final var r = 2.0;
    final int range = 7;

    final var tempPos = new Vector3d[range];
    for (int i = 0; i < range; i++) {
      if (i == 0) {
        tempPos[i] = new Vector3d(0, 0, 0);
        continue;
      }

      final var finalAngle = 60 * i;
      final var v2d = ModMath.getIndexVetexs(r, finalAngle);
      final var rotation = new Vector3d(v2d.x, v2d.y, 0);
      tempPos[i] = rotation;
    }

    shieldPositionOffset = tempPos;
  }
}

package xiaojin.astralflux.util;

import net.minecraft.world.phys.Vec3;
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
}

package xiaojin.astralflux.common;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;

import java.util.Optional;

public class AABBAegusBarriesShield extends AABB {
  public final AegusBarrierShieldEntity abs;

  public AABBAegusBarriesShield(AegusBarrierShieldEntity abs) {
    super(abs.blockPosition());
    this.abs = abs;
  }

  /**
   * 获取投射物在当前护盾上的接触点。由于护盾真实 BB 只有中间一块并且可能是斜面，需要使用特殊的算法证明投射物向量在
   * 下一个 tick 的时候将会经过真实 BB，同时需要排除由内而外射出的投射物（通常这是由玩家射出的）。
   * <br>
   * 无需判断 y 轴，因为护盾的 y 视项通常不会翻转（30 度，这在 BB 中近乎可以忽略）。
   */
  @Override
  public Optional<Vec3> clip(Vec3 from, Vec3 to) {
    final var goTo = to.subtract(from);
    final var lookAt = this.abs.getLookAngle();

    final var vecGoTo = new Vector2d(goTo.x(), goTo.z());
    final var vecLookAt = new Vector2d(lookAt.x, lookAt.z());

    final double angle = vecGoTo.angle(vecLookAt) * Mth.DEG_TO_RAD;
    if (Math.abs(angle) > 90) {
      return Optional.empty();
    }

    final var pos = this.abs.position();
    final double half = 12;

    // 从点积转取向量
    final double norm = Math.sqrt(Math.pow(lookAt.x, 2) + Math.pow(lookAt.z, 2));
    final double ux = -lookAt.z / norm;
    final double uy = lookAt.x / norm;

    // 获得视觉垂直平面边缘两点
    final var posA = new Vector2d((pos.x + ux * half), (pos.y + uy * half));
    final var posB = new Vector2d((pos.x - ux * half), (pos.y - uy * half));

    // 证明投射线段将要通过当前平面边缘两点
    // TODO

    return Optional.empty();
  }
}

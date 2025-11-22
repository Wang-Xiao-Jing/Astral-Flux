package xiaojin.astralflux.common;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class AABBAegusBarriesShield extends AABB {
  private final AegusBarrierShieldEntity abs;

  public AABBAegusBarriesShield(AegusBarrierShieldEntity abs) {
    super(abs.getBoundingBox().minX, abs.getBoundingBox().minY, abs.getBoundingBox().minZ,
      abs.getBoundingBox().maxX, abs.getBoundingBox().maxY, abs.getBoundingBox().maxZ);
    this.abs = abs;
  }

  /**
   * 获取投射物在当前护盾上的接触点。由于护盾真实 BB 只有中间一块并且可能是斜面，需要使用特殊的算法证明投射物向量在
   * 下一个 tick 的时候将会经过真实 BB，同时需要排除由内而外射出的投射物（通常这是由玩家射出的）。
   * <br>
   * 无需判断 y 轴，因为护盾的 y 视项通常不会翻转（30 度，这在 BB 中近乎可以忽略）。
   */
  @Override
  public @NotNull Optional<Vec3> clip(Vec3 from, Vec3 to) {
    final var vec3GoTo = to.subtract(from);
    final var vec3LookAt = this.abs.getLookAngle();

    final var vectorGoTo = new Vector2d(vec3GoTo.x(), vec3GoTo.z());
    final var vectorLookAt = new Vector2d(vec3LookAt.x, vec3LookAt.z());

    final boolean vectorWillPassedBackSide = isVectorPassedByBackSide(vectorGoTo, vectorLookAt);
    if (vectorWillPassedBackSide) {
      return Optional.empty();
    }

    final var vecWillPassed = isVectorPassedSegment(from, to, vectorGoTo, vectorLookAt);
    return Objects.nonNull(vecWillPassed)
      ? Optional.of(vecWillPassed)
      : Optional.empty();
  }

  /**
   * 证明向量将要途径背面。
   * @param vectorGoTo 射线向量
   * @param vectorLookAt （护盾的）视觉向量
   */
  private boolean isVectorPassedByBackSide(final Vector2d vectorGoTo, final Vector2d vectorLookAt) {
    // 计算量向量夹角并把圆周制转为角度制。
    final double angle = vectorGoTo.angle(vectorLookAt) * Mth.DEG_TO_RAD;

    // 若夹角角度的绝对值大于等于 90（展开面 180），则意味着是从背面射出。
    return Math.abs(angle) >= 90;
  }

  /**
   * 证明向量将要相交于真实碰撞箱所处的线段。无需证明高度，当碰撞箱被触发计算时，理论向量应该已经处理当前理论 AABB。<br />
   * 当向量穿过真实坐标途径处时，提交数据。
   * @param vecGoTo 射线向量
   * @param vecLookAt （护盾的）视觉向量
   * @return 当返回值为 null 时，表示未经过线段。否则，返回将要途径的目标位置。
   */
  @Nullable
  private Vec3 isVectorPassedSegment(final Vec3 from, final Vec3 to,
                                     final Vector2d vecGoTo, final Vector2d vecLookAt) {
    final var pos = this.abs.position();
    final double half = 12;

    // 从点积转取向量。
    final double norm = Math.sqrt(Math.pow(vecLookAt.x, 2) + Math.pow(vecLookAt.y, 2));
    final double ux = -vecLookAt.y / norm;
    final double uy = vecLookAt.x / norm;

    // 获得视觉垂直平面边缘两点。
    final var posA = new Vector2d((pos.x + ux * half), (pos.y + uy * half));
    final var posB = new Vector2d((pos.x - ux * half), (pos.y - uy * half));

    double rayX = to.x - from.x;
    double rayY = to.y - from.y;
    double segX = posB.x - posA.x;
    double segY = posB.y - posA.y;

    double d = rayX * segY - rayY * segX;
    double dx = posA.x - from.x;
    double dy = posA.y - from.y;
    double t = (dx * vecLookAt.y - dy * vecLookAt.x) / d;
    double u = (dx * vecGoTo.y - dy * vecGoTo.x) / d;

    // 判断叉乘是否相交。
    if (t < 0 || u < 0 || u > 1) {
      return null;
    }

    double oX = from.x + t * rayX;
    double oY = from.z + t * (to.z - from.z);
    double oZ = from.y + t * rayY;

    return new Vec3(oX, oY, oZ);
  }
}

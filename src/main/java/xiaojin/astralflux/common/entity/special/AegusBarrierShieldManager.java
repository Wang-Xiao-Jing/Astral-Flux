package xiaojin.astralflux.common.entity.special;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import xiaojin.astralflux.api.abs.AbstractAegusBarrierShieldManager;
import xiaojin.astralflux.util.ABSHelper;

import java.util.Arrays;

public final class AegusBarrierShieldManager extends AbstractAegusBarrierShieldManager {
  private final AegusBarrierShieldEntity[] shieldList = new AegusBarrierShieldEntity[7];
  private final double[] shieldExpandingProgress = new double[7];
  private boolean expanding = true;

  public  AegusBarrierShieldManager(EntityType<AegusBarrierShieldManager> entityType, Level world) {
    super(entityType, world);
  }

  public AegusBarrierShieldManager(Level level) {
    super(level);
    Arrays.fill(this.getShields(), new AegusBarrierShieldEntity(level, this));
    Arrays.fill(this.getExpandingProgress(), 0.0);
  }

  @Override
  public void update() {
    this.expandShield();
    this.tweakShield();
  }

  @Override
  public double[] getExpandingProgress() {
    return this.shieldExpandingProgress;
  }

  @Override
  public AegusBarrierShieldEntity[] getShields() {
    return this.shieldList;
  }

  @Override
  public boolean isStillInExpanding() {
    return this.expanding || (this.expanding = super.isStillInExpanding());
  }

  @Override
  public void addShield(int index) {
    super.addShield(index);
    this.expanding = false;
  }

  private void expandShield() {
    for (int i = 0; i < this.shieldExpandingProgress.length; i++) {
      if (this.shieldList[i] != null && this.shieldExpandingProgress[i] == -1.0) {
        this.shieldExpandingProgress[i] = 0.0;
      }

      if (this.shieldExpandingProgress[i] >= 1.0) {
        this.shieldExpandingProgress[i] = 1.0;
        continue;
      }

      this.shieldExpandingProgress[i] += 0.1;

      break;
    }
  }

  /**
   * 修改盾牌实体状态。
   */
  private void tweakShield() {
    final Vec3 lookingVec = this.getLookAngle();
    final double angle = Math.atan2(lookingVec.x, lookingVec.z);

    for (int index = 0; index < this.shieldList.length; index++) {
      final var shieldEntity = this.shieldList[index];

      if (shieldEntity.shouldRemove()) {
        this.removeShield(index);
        continue;
      }

      shieldEntity.setOldPosAndRot();
      final Vector2f result = ABSHelper.getRot(index, this.getYRot());
      final Vec3 offsetPos = ABSHelper.getOffsetPos(index, angle, this.position());
      shieldEntity.moveTo(offsetPos.x, offsetPos.y, offsetPos.z, result.y, result.x);
    }
  }
}

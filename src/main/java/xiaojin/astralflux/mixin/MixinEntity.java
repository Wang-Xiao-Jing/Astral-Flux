package xiaojin.astralflux.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xiaojin.astralflux.common.entity.special.AegusBarrierShieldEntity;

@Mixin(Entity.class)
public class MixinEntity {
  @Final
  @Inject(method = "getBoundingBox", at = @At("HEAD"), cancellable = true)
  public void getBB(CallbackInfoReturnable<AABB> cir) {
    if ((Entity) (Object) this instanceof AegusBarrierShieldEntity abs) {
      cir.setReturnValue(abs.makeBoundingBox());
    }
  }
}

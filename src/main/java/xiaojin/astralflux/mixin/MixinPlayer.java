package xiaojin.astralflux.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import xiaojin.astralflux.mixinimod.IModPlayer;

@Mixin(Player.class)
public abstract class MixinPlayer implements IModPlayer {
}

package xiaojin.astralflux.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.common.commands.SourceSoulCommands;

/**
 * 指令事件
 */
@EventBusSubscriber(modid = AstralFlux.ID)
public final class CommandEvents {
  /**
   * 注册指令
   */
	@SubscribeEvent
	public static void register(RegisterCommandsEvent event) {
    SourceSoulCommands.processSourceSoul(event.getDispatcher());
	}
}

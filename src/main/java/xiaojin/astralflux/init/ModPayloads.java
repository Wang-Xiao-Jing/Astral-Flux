package xiaojin.astralflux.init;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import xiaojin.astralflux.common.payloads.PlayerLeftClickEmptyPayload;
import xiaojin.astralflux.core.AstralFlux;

@EventBusSubscriber
public final class ModPayloads {
  @SubscribeEvent
  public static void register(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("1");
    /// 接收来自服务端和客户端的数据
    //..

    /// 接收来自服务端的数据
    //..

    /// 接收来自客户端的数据
    //..
    registrar.commonToServer(PlayerLeftClickEmptyPayload.TYPE, PlayerLeftClickEmptyPayload.STREAM_CODEC, PlayerLeftClickEmptyPayload::toServer);
    AstralFlux.LOGGER.info("Registering payloads finish");
  }
}

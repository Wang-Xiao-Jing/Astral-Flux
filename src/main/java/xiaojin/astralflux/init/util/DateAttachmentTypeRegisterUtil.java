package xiaojin.astralflux.init.util;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

import static xiaojin.astralflux.init.ModAttachmentTypes.REGISTRY;

public abstract class DateAttachmentTypeRegisterUtil {

  protected static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register
    (String name, AttachmentType.Builder<T> factory) {
    return register(name, factory::build);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register
    (String name, Supplier<AttachmentType<T>> factory) {
    return REGISTRY.register(name, factory);
  }
}

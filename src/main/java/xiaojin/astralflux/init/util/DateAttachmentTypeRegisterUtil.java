package xiaojin.astralflux.init.util;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.init.ModDateAttachmentTypes;

import java.util.function.Supplier;

public abstract class DateAttachmentTypeRegisterUtil {

  protected static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register
    (String name, AttachmentType.Builder<T> factory) {
    return register(name, factory::build);
  }

  private static <T> DeferredHolder<AttachmentType<?>, AttachmentType<T>> register
    (String name, Supplier<AttachmentType<T>> factory) {
    return ModDateAttachmentTypes.REGISTRY.register(name, factory);
  }
}

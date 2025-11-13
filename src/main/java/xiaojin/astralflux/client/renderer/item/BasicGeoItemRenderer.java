package xiaojin.astralflux.client.renderer.item;

import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import xiaojin.astralflux.client.model.BasicGeoModel;


public class BasicGeoItemRenderer<T extends Item & GeoAnimatable> extends GeoItemRenderer<T> {
  public BasicGeoItemRenderer(GeoModel<T> model) {
    super(model);
  }

  public BasicGeoItemRenderer(String path) {
    super(new BasicGeoModel.ItemModel<>(path));
  }
}

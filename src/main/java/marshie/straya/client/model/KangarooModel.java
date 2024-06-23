package marshie.straya.client.model;

import marshie.straya.Straya;
import marshie.straya.entities.KangarooEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KangarooModel extends AnimatedGeoModel<KangarooEntity> {
	public static final ResourceLocation[] KANGAROO_LOCATIONS = new ResourceLocation[]{
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo0.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo1.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo2.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo3.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo4.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo5.png"),
			new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo6.png")
			};

    @Override
    public ResourceLocation getModelResource(KangarooEntity entity) {
        return new ResourceLocation(Straya.MODID, "geo/kangaroo.geo.json");
    }

    public ResourceLocation getTextureResource(KangarooEntity entity) {
       return KANGAROO_LOCATIONS[entity.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(KangarooEntity animatable) {
        return new ResourceLocation(Straya.MODID, "animations/kangaroo.animation.json");
    }
}

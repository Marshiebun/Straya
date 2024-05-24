package marshie.straya.client.model;

import marshie.straya.Straya;
import marshie.straya.entities.KangarooEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KangarooModel extends AnimatedGeoModel<KangarooEntity> {

    @Override
    public ResourceLocation getModelResource(KangarooEntity object) {
        return new ResourceLocation(Straya.MODID, "geo/kangaroo.geo.json"); // Replace with your actual model file path
    }

    @Override
    public ResourceLocation getTextureResource(KangarooEntity object) {
        return new ResourceLocation(Straya.MODID, "textures/entity/kangaroo" + object.getTextureIndex() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(KangarooEntity animatable) {
        return new ResourceLocation(Straya.MODID, "animations/kangaroo.animation.json"); // Replace with your actual animation file path
    }
}

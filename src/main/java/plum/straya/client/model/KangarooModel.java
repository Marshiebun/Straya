package plum.straya.client.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import plum.straya.Straya;
import plum.straya.entity.KangarooEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

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
    
    @SuppressWarnings(value = { "rawtypes" })
	@Override
	public void setCustomAnimations(KangarooEntity animatable, int instanceId, AnimationEvent animationEvent) {
		super.setCustomAnimations(animatable, instanceId, animationEvent);
		IBone head = this.getAnimationProcessor().getBone("head");

		@SuppressWarnings("unchecked")
		EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
		if (head != null) {
			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
			if(animatable.isBaby()) {
	            final float headScale = 1.5F;
	            head.setScaleX(headScale);
	            head.setScaleY(headScale);
	            head.setScaleZ(headScale);
			} else {
	            final float headScale = 1F;
	            head.setScaleX(headScale);
	            head.setScaleY(headScale);
	            head.setScaleZ(headScale);
			}
		}
	}
    
    public IBone getBone(String boneName) {
        return this.getAnimationProcessor().getBone(boneName);
    }
}

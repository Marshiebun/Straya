package marshie.straya.client.renderer;

import marshie.straya.Straya;
import marshie.straya.client.model.KangarooModel;
import marshie.straya.entities.KangarooEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KangarooRenderer extends GeoEntityRenderer<KangarooEntity> {
    public KangarooRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KangarooModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(KangarooEntity entity) {
        return new ResourceLocation(Straya.MODID, "textures/entity/kangaroo/kangaroo" + entity.getTextureIndex() + ".png");
    }

    @Override
    public void render(KangarooEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.scale(1.0F, 1.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}

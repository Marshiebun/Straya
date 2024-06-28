package plum.straya.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import plum.straya.client.model.KangarooModel;
import plum.straya.entity.KangarooEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class KangarooRenderer extends GeoEntityRenderer<KangarooEntity> {
    public KangarooRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KangarooModel());
        this.shadowRadius = 0.6F;
    }

    @Override
    public void render(KangarooEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        if(entity.isBaby()) {
            final float scale = 0.5F;
            stack.scale(scale, scale, scale);
        }
        stack.scale(1.0F, 1.0F, 1.0F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }
}

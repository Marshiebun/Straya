package plum.straya.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.Straya;
import plum.straya.entity.KangarooEntity;

@Mod.EventBusSubscriber(modid = Straya.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StrayaEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
    		DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Straya.MODID);

    public static final RegistryObject<EntityType<KangarooEntity>> KANGAROO =
    		ENTITY_TYPES.register("kangaroo",
    				() -> EntityType.Builder.of(KangarooEntity::new, MobCategory.CREATURE)
                    	.sized(0.9F, 1.9F) // Hitbox size
                    	.build(Straya.MODID + ":kangaroo"));

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(KANGAROO.get(), createAttributes().build());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }
}
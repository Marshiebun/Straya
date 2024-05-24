package marshie.straya.init;

import marshie.straya.Straya;
import marshie.straya.entities.KangarooEntity;
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

@Mod.EventBusSubscriber(modid = Straya.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Straya.MODID);

    public static final RegistryObject<EntityType<KangarooEntity>> KANGAROO = ENTITY_TYPES.register("kangaroo",
            () -> EntityType.Builder.of(KangarooEntity::new, MobCategory.CREATURE)
                    .sized(0.6F, 1.95F) // Hitbox size
                    .build(Straya.MODID + ":kangaroo"));

    @SubscribeEvent
    public static void onAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(KANGAROO.get(), createAttributes().build());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }
}
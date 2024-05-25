package marshie.straya.init;

import marshie.straya.Straya;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StrayaItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Straya.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> KANGAROO_SPAWN_EGG = ITEMS.register("kangaroo_spawn_egg",
            () -> new ForgeSpawnEggItem(StrayaEntityTypes.KANGAROO, 0xFF55AA, 0x27DA9F, props().stacksTo(16)));

    private static Item.Properties props() {
        return new Item.Properties().tab(Straya.STRAYA_WILDLIFE);
    }
}
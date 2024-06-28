package plum.straya.init;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.Straya;
import plum.straya.item.FullHidePouchItem;

public class StrayaItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Straya.MODID);

    public static final RegistryObject<ForgeSpawnEggItem> KANGAROO_SPAWN_EGG = ITEMS.register("kangaroo_spawn_egg",
            () -> new ForgeSpawnEggItem(StrayaEntityTypes.KANGAROO, 0xFF55AA, 0x27DA9F, props().stacksTo(16)));

    public static final RegistryObject<Item> KANGAROO_HIDE = ITEMS.register("kangaroo_hide", 
            () -> new Item(new Item.Properties().tab(StrayaCreativeTabs.ITEMS)));
    public static final RegistryObject<Item> HIDE_POUCH = ITEMS.register("hide_pouch", 
            () -> new Item(new Item.Properties().tab(StrayaCreativeTabs.ITEMS).stacksTo(1)));
    public static final RegistryObject<Item> FULL_HIDE_POUCH = ITEMS.register("full_hide_pouch", 
            () -> new FullHidePouchItem(new Item.Properties().stacksTo(1)));

    private static Item.Properties props() {
        return new Item.Properties().tab(StrayaCreativeTabs.WILDLIFE);
    }
}

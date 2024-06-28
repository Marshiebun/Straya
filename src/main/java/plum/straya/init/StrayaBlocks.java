package plum.straya.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.Straya;
import plum.straya.block.KangarooPawBlock;

public class StrayaBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Straya.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Straya.MODID);

    public static final RegistryObject<Block> KANGAROO_PAW = BLOCKS.register("kangaroo_paw", KangarooPawBlock::new);

    public static final RegistryObject<Item> KANGAROO_PAW_ITEM = ITEMS.register("kangaroo_paw",
            () -> new BlockItem(KANGAROO_PAW.get(), new Item.Properties().tab(StrayaCreativeTabs.DECOR)));
}

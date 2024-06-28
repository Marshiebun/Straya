package plum.straya.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class StrayaCreativeTabs extends CreativeModeTab {

    public static final StrayaCreativeTabs WILDLIFE = new StrayaCreativeTabs(CreativeModeTab.TABS.length, "straya_wildlife");
    public static final StrayaCreativeTabs DECOR = new StrayaCreativeTabs(CreativeModeTab.TABS.length, "straya_decor");
    public static final StrayaCreativeTabs ITEMS = new StrayaCreativeTabs(CreativeModeTab.TABS.length, "straya_items");

    public StrayaCreativeTabs(int index, String label) {
        super(index, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(StrayaBlocks.KANGAROO_PAW_ITEM.get());
    }
}

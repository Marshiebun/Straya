package marshie.straya.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class StrayaCreativeTabs extends CreativeModeTab {

	public static final StrayaCreativeTabs MAIN = new StrayaCreativeTabs(CreativeModeTab.TABS.length, "straya_wildlife");
	
	public StrayaCreativeTabs(int length, String label) {
		super(length, label);
	}

	@Override
	public ItemStack makeIcon() {
		// TODO Auto-generated method stub
		return null;
	}

}

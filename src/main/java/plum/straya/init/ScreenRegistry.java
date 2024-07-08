package plum.straya.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import plum.straya.client.container.PouchScreen;

public class ScreenRegistry {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerTypeRegistry.POUCH_CONTAINER.get(), PouchScreen::new);
    }
}
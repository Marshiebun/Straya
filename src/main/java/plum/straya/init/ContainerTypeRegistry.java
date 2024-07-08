package plum.straya.init;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.Straya;
import plum.straya.client.container.PouchContainer;

public class ContainerTypeRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Straya.MODID);

    public static final RegistryObject<MenuType<PouchContainer>> POUCH_CONTAINER = CONTAINERS.register("pouch_container",
            () -> IForgeMenuType.create((windowId, inv, data) -> new PouchContainer(windowId, inv, new SimpleContainer(6))));
}

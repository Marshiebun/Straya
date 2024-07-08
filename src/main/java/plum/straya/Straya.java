package plum.straya;

import com.mojang.logging.LogUtils;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.client.container.PouchScreen;
import plum.straya.client.renderer.KangarooRenderer;
import plum.straya.entity.KangarooEntity;
import plum.straya.init.ContainerTypeRegistry;
import plum.straya.init.StrayaBlocks;
import plum.straya.init.StrayaEntityTypes;
import plum.straya.init.StrayaItems;
import plum.straya.world.feature.StrayaFeatures;
import plum.straya.world.feature.StrayaPlacedFeatures;
import software.bernie.geckolib3.GeckoLib;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;

import org.slf4j.Logger;

@Mod(Straya.MODID)
public class Straya
{
    public static final String MODID = "straya";
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public Straya()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ContainerTypeRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        bus.addListener(this::commonSetup);
        StrayaEntityTypes.ENTITY_TYPES.register(bus);
        StrayaBlocks.BLOCKS.register(bus);
        StrayaBlocks.ITEMS.register(bus);
        StrayaItems.ITEMS.register(bus);

        BLOCKS.register(bus);
        ITEMS.register(bus);

        StrayaFeatures.register(bus);
        StrayaPlacedFeatures.register(bus);
        
        bus.addListener(this::setup);
        bus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        
        GeckoLib.initialize();
    }

    @SuppressWarnings("deprecation")
	private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            SpawnPlacements.register(StrayaEntityTypes.KANGAROO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules);
        });
    }

    @SubscribeEvent
    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(StrayaEntityTypes.KANGAROO.get(), KangarooEntity.createLivingAttributes().build());
    }

    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = Straya.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEventBusSubscriber {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(StrayaEntityTypes.KANGAROO.get(), KangarooRenderer::new);
            MenuScreens.register(ContainerTypeRegistry.POUCH_CONTAINER.get(), PouchScreen::new);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {
        EntityRenderers.register(StrayaEntityTypes.KANGAROO.get(), KangarooRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Common setup code
    }
}

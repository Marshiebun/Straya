package marshie.straya;

import com.mojang.logging.LogUtils;

import marshie.straya.client.renderer.KangarooRenderer;
import marshie.straya.entities.KangarooEntity;
import marshie.straya.init.ModEntityTypes;
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
import software.bernie.geckolib3.GeckoLib;
import net.minecraft.world.entity.SpawnPlacements;
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
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        
        GeckoLib.initialize();
    }

    @SuppressWarnings("deprecation")
	private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntityTypes.KANGAROO.get(),
                    SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    KangarooEntity::checkKangarooSpawnRules);
        });
    }

    @SubscribeEvent
    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.KANGAROO.get(), KangarooEntity.createAttributes().build());
    }

    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = Straya.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public class ClientEventBusSubscriber {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntityTypes.KANGAROO.get(), KangarooRenderer::new);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntityTypes.KANGAROO.get(), KangarooRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Common setup code
    }
}

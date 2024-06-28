package plum.straya.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import plum.straya.Straya;

import java.util.List;

public class StrayaPlacedFeatures {
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Straya.MODID);

    public static final RegistryObject<PlacedFeature> KANGAROO_PAW_PLACED = PLACED_FEATURES.register("kangaroo_paw_placed",
            () -> new PlacedFeature(StrayaFeatures.KANGAROO_PAW.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(10),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
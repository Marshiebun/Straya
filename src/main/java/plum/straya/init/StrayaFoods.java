package plum.straya.init;

import net.minecraft.world.food.FoodProperties;

public class StrayaFoods {
    public static final FoodProperties RAW_ROO_SHANK = new FoodProperties.Builder()
            .nutrition(3)
            .saturationMod(0.3F)
            .meat()
            .build();

    public static final FoodProperties COOKED_ROO_SHANK = new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.8F)
            .meat()
            .build();
}

package plum.straya.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import plum.straya.entity.KangarooEntity;
import plum.straya.init.StrayaEntityTypes;
import plum.straya.init.StrayaItems;

public class FullHidePouchItem extends Item {

    public FullHidePouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide && level instanceof ServerLevel) {
            ItemStack itemStack = context.getItemInHand();
            CompoundTag tag = itemStack.getTag().getCompound("KangarooData");
            BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
            KangarooEntity kangaroo = StrayaEntityTypes.KANGAROO.get().create((ServerLevel) level);
            if (kangaroo != null) {
                kangaroo.readAdditionalSaveData(tag);
                kangaroo.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                level.addFreshEntity(kangaroo);
                if (context.getPlayer() != null) {
                    itemStack.shrink(1);
                    ItemStack hidePouch = new ItemStack(StrayaItems.HIDE_POUCH.get());
                    if (!context.getPlayer().addItem(hidePouch)) {
                        context.getPlayer().drop(hidePouch, false);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}

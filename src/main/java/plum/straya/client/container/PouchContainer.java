package plum.straya.client.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import plum.straya.init.ContainerTypeRegistry;
import net.minecraft.world.SimpleContainer;

public class PouchContainer extends AbstractContainerMenu {
    private final SimpleContainer pouchInventory;

    public PouchContainer(int windowId, Inventory playerInventory, SimpleContainer pouchInventory) {
        super(ContainerTypeRegistry.POUCH_CONTAINER.get(), windowId);
        this.pouchInventory = pouchInventory;

        // Add pouch inventory slots
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(pouchInventory, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.pouchInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.pouchInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.pouchInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}

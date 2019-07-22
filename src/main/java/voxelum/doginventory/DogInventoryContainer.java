package voxelum.doginventory;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DogInventoryContainer extends Container {
    public DogInventoryContainer(EntityPlayer player, EntityWolf wolf) {
        IItemHandler capability = wolf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // start at 14 16
        int baseX = 34, baseY = 14;
        for (int y = 0; y < 2; y++) {
            for (int i = 0; i < 6; i++) {
                // 0 1 2 3 4 5
                this.addSlotToContainer(new SlotItemHandler(capability, i + y * 6, baseX + i * 18, baseY + y * 18));
            }
        }


    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}

package voxelum.doginventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityExtraInventory {
    @CapabilityInject(ItemHandlerWrapper.class)
    public static Capability<ItemHandlerWrapper> CAPABILITY = null;

    public static class Impl extends ItemStackHandler implements ItemHandlerWrapper {
        public Impl() {
        }

        public Impl(int size) {
            super(size);
        }

        public Impl(NonNullList<ItemStack> stacks) {
            super(stacks);
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(ItemHandlerWrapper.class, new Capability.IStorage<ItemHandlerWrapper>() {
            @Override
            public NBTBase writeNBT(Capability<ItemHandlerWrapper> capability, ItemHandlerWrapper instance, EnumFacing side) {
                NBTTagList nbtTagList = new NBTTagList();
                int size = instance.getSlots();
                for (int i = 0; i < size; i++) {
                    ItemStack stack = instance.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        NBTTagCompound itemTag = new NBTTagCompound();
                        itemTag.setInteger("Slot", i);
                        stack.writeToNBT(itemTag);
                        nbtTagList.appendTag(itemTag);
                    }
                }
                return nbtTagList;
            }

            @Override
            public void readNBT(Capability<ItemHandlerWrapper> capability, ItemHandlerWrapper instance, EnumFacing side, NBTBase base) {
                if (!(instance instanceof IItemHandlerModifiable))
                    throw new RuntimeException("IItemHandler instance does not implement IItemHandlerModifiable");
                IItemHandlerModifiable itemHandlerModifiable = (IItemHandlerModifiable) instance;
                NBTTagList tagList = (NBTTagList) base;
                for (int i = 0; i < tagList.tagCount(); i++) {
                    NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
                    int j = itemTags.getInteger("Slot");

                    if (j >= 0 && j < instance.getSlots()) {
                        itemHandlerModifiable.setStackInSlot(j, new ItemStack(itemTags));
                    }
                }
            }
        }, Impl::new);

    }
}

package voxelum.doginventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(modid = "doginventory", name = "Dog Inventory")
@Mod.EventBusSubscriber
public class DogInventory {
    @SidedProxy
    private static CommonProxy proxy = null;

    @Mod.Instance
    private static DogInventory instance;

    public static int GUI_DOG_INVENTORY = 0;

    public static DogInventory instance() {
        return instance;
    }

    public static CommonProxy proxy() {
        return proxy;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static abstract class CommonProxy implements IGuiHandler {
        @SubscribeEvent
        public void onAttachEvent(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof EntityWolf)
                event.addCapability(new ResourceLocation("dog_inventory", "dog_inventory_container"), new ICapabilityProvider() {
                    private CapabilityExtraInventory.Impl impl = new CapabilityExtraInventory.Impl(9);

                    @Override
                    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                        return capability == CapabilityExtraInventory.CAPABILITY;
                    }

                    @Nullable
                    @Override
                    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                        if (hasCapability(capability, facing)) {
                            return (T) impl;
                        }
                        return null;
                    }
                });
        }

        @SubscribeEvent
        public void onPlayerInteract(PlayerInteractEvent.EntityInteractSpecific event) {
            EntityPlayer player = event.getEntityPlayer();
            player.openGui(instance, GUI_DOG_INVENTORY, event.getWorld(), event.getTarget().getEntityId(), 0, 0);
        }

        public void preInit(FMLPreInitializationEvent event) {
            MinecraftForge.EVENT_BUS.register(this);
            CapabilityExtraInventory.register();
        }

        public void init(FMLInitializationEvent event) {
            NetworkRegistry.INSTANCE.registerGuiHandler(DogInventory.instance(), this);
        }

        public void postInit(FMLPostInitializationEvent event) {

        }

        @Nullable
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

    }


    public static class ClientProxy extends CommonProxy {
        @Nullable
        @Override
        public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            if (ID == GUI_DOG_INVENTORY) {
                Entity entity = world.getEntityByID(x);
                if (entity instanceof EntityWolf) {
                    return new GuiDogInventoryContainer(new DogInventoryContainer(player, ((EntityWolf) entity)));
                }
            }
            return null;
        }
    }

    public static class ServerProxy extends CommonProxy {
        @Nullable
        @Override
        public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            if (ID == GUI_DOG_INVENTORY) {
                Entity entity = world.getEntityByID(x);
                if (entity instanceof EntityWolf) {
                    return new DogInventoryContainer(player, ((EntityWolf) entity));
                }
            }
            return null;
        }
    }

}

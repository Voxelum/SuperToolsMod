package voxelum.supertools;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod(modid = SuperToolsMod.MODID, name = SuperToolsMod.NAME, version = SuperToolsMod.VERSION)
public class SuperToolsMod {
    public static final String MODID = "supertools";
    public static final String NAME = "Super Tools Mod";
    public static final String VERSION = "1.0";

    public static final Item SUPER_PICKAXE = new ItemSuperPickaxe(Item.ToolMaterial.DIAMOND);
    public static final Item SUPER_SWORD = new ItemSuperSword(Item.ToolMaterial.DIAMOND);
    public static final Item SUPER_HOE = new ItemSuperHoe(Item.ToolMaterial.DIAMOND);
    public static final Item SUPER_AXE = new ItemSuperAxe(Item.ToolMaterial.DIAMOND);

    private static Logger logger;

    public static void destroyRange(World world, BlockPos center, int miX, int maX, int miY, int maY, int miZ, int maZ) {
        for (int x = miX; x <= maX; x++) {
            for (int y = miY; y <= maY; y++) {
                for (int z = miZ; z <= maZ; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (world.getBlockState(pos).getBlock() == Blocks.BEDROCK) continue;
                    world.destroyBlock(pos, true);
                }
            }
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(proxy);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        proxy.init(event);
    }

    @SidedProxy
    public static CommonProxy proxy;

    public static class CommonProxy {
        public void init(FMLInitializationEvent event) {

        }

        @SubscribeEvent
        public void regThings(RegistryEvent.Register<Item> itemRegEvent) {
            itemRegEvent.getRegistry().register(SUPER_PICKAXE);
            itemRegEvent.getRegistry().register(SUPER_HOE);
            itemRegEvent.getRegistry().register(SUPER_SWORD);
            itemRegEvent.getRegistry().register(SUPER_AXE);
        }

        public Explosion createExplosionServer(World world, @Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking, boolean damageEntity) {
            Explosion explosion = new Explosion(world, entityIn, x, y, z, strength, isFlaming, isSmoking);
            if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
            if (damageEntity) {
                explosion.doExplosionA();
            }
            explosion.doExplosionB(false);

            if (!isSmoking) {
                explosion.clearAffectedBlockPositions();
            }

            WorldServer worldServer = (WorldServer) world;
            for (EntityPlayer entityplayer : worldServer.playerEntities) {
                if (entityplayer.getDistanceSq(x, y, z) < 4096.0D) {
                    ((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
                }
            }

            return explosion;
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends CommonProxy {
        // used as client proxy

        @SubscribeEvent
        public void modelRegEvent(ModelRegistryEvent event) {
            ModelLoader.setCustomModelResourceLocation(SUPER_PICKAXE, 0, new ModelResourceLocation(SUPER_PICKAXE.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(SUPER_SWORD, 0, new ModelResourceLocation(SUPER_SWORD.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(SUPER_HOE, 0, new ModelResourceLocation(SUPER_HOE.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(SUPER_AXE, 0, new ModelResourceLocation(SUPER_AXE.getRegistryName(), "inventory"));

        }


        @Override
        public void init(FMLInitializationEvent event) {
            super.init(event);
        }
    }

    public static class ServerProxy extends CommonProxy {
        // server run

    }


    @SubscribeEvent
    public void handleCapabilityEvent(AttachCapabilitiesEvent<EntityWolf> event) {
        if (event.getObject() instanceof EntityWolf) {
            event.addCapability(new ResourceLocation("voxelum.supertools"), new ICapabilityProvider() {
                private ItemStackHandler handler = new ItemStackHandler(16);

                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return hasCapability(capability, facing) ? (T) handler : null;
                }
            });
        }
    }

}

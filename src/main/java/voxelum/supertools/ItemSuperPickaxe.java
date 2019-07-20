package voxelum.supertools;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemSuperPickaxe extends ItemPickaxe {
    public ItemSuperPickaxe(ToolMaterial material) {
        super(material);
        setUnlocalizedName("voxelum.superpickaxe");
        setRegistryName("super_pickaxe");
    }

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

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        // BREAK 5x1 block at front
        if (!worldIn.isRemote) {
            // server code
            Vec3d forward = entityLiving.getForward();
            if (state.getMaterial() == Material.ROCK && entityLiving instanceof EntityPlayer) {
                double ax = Math.abs(forward.x), ay = Math.abs(forward.y), az = Math.abs(forward.z);
                if (ax > ay && ax > az) { // x
                    int max, min;
                    if (ax > 0) {
                        min = 0;
                        max = 2;
                    } else {
                        min = -2;
                        max = 0;
                    }
                    destroyRange(worldIn, pos, min, max, -1, 1, -1, 1);
                } else if (ay > ax && ay > az) { // y
                    int max, min;
                    if (ay > 0) {
                        min = 0;
                        max = 2;
                    } else {
                        min = -2;
                        max = 0;
                    }
                    destroyRange(worldIn, pos, -1, 1, min, max, -1, 1);
                } else { // z
                    int max, min;
                    if (az > 0) {
                        min = 0;
                        max = 2;
                    } else {
                        min = -2;
                        max = 0;
                    }
                    destroyRange(worldIn, pos, -1, 1, -1, 1, min, max);
                }
                SuperToolsMod.proxy.createExplosionServer(worldIn, entityLiving, pos.getX(), pos.getY(), pos.getZ(), 2, false, false, false);
            }
        }

        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }


    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        // check 5x5 ore
        System.out.println("ITEM USE");
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}

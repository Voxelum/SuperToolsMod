package voxelum.supertools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ItemSuperHoe extends ItemHoe {
    public ItemSuperHoe(Item.ToolMaterial material) {
        super(material);
        this.setUnlocalizedName("voxelum.superhoe");
        this.setRegistryName("super_hoe");

    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos p, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        EnumActionResult result = super.onItemUse(player, worldIn, p, hand, facing, hitX, hitY, hitZ);
        ItemStack itemstack = player.getHeldItem(hand);
        if (result == EnumActionResult.SUCCESS) {
            Random rand = worldIn.rand;
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05, 0.04, rand.nextGaussian() * 0.05);
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05D, 0.04, rand.nextGaussian() * 0.05);
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05D, 0.04, rand.nextGaussian() * 0.05);
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05, 0.04, rand.nextGaussian() * 0.05);
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05, 0.04, rand.nextGaussian() * 0.05);
            player.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, p.getX(), p.getY() + 1, p.getZ(), rand.nextGaussian() * 0.05, 0.04, rand.nextGaussian() * 0.05);

            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    BlockPos pos = p.add(i, 0, j);
                    IBlockState iblockstate = worldIn.getBlockState(pos);
                    Block block = iblockstate.getBlock();


                    if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
                        if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                            continue;
                        }

                        if (block == Blocks.DIRT) {
                            switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                                case DIRT:
                                    this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                                    continue;
                                case COARSE_DIRT:
                                    this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                                    continue;
                            }
                        }
                    }
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return result;
    }

}


package voxelum.supertools;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSuperAxe extends ItemAxe {
    public ItemSuperAxe(Item.ToolMaterial material) {
        super(material);
        this.setUnlocalizedName("voxelum.superaxe");
        this.setRegistryName("super_axe");
    }


    public boolean onLeftClickEntity(ItemStack stack, IBlockState state, BlockPos pos, Entity entity, EntityPlayer player)
    {
        if (!player.world.isRemote && state.getMaterial()== Material.WOOD)
        {
            SuperToolsMod.proxy.createExplosionServer(player.world, player,pos.getX(),pos.getY(),pos.getZ(), 1, false, false, false);
        }

        return super.onLeftClickEntity(stack, player, entity);
    }
}

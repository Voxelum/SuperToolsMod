package voxelum.supertools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSuperSword extends ItemSword {
    public ItemSuperSword(ToolMaterial material) {
        super(material);
        this.setUnlocalizedName("voxelum.supersword");
        this.setRegistryName("super_sword");
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (!player.world.isRemote) {
            SuperToolsMod.proxy.createExplosionServer(player.world, player, entity.posX, entity.posY, entity.posZ, 1, false, false, true);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
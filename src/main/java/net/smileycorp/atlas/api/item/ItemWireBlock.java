package net.smileycorp.atlas.api.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockWire;

public class ItemWireBlock extends Item {
	
	protected final Block block;
	
	public ItemWireBlock(BlockWire block) {
		setRegistryName(block.getRegistryName());
		setUnlocalizedName(block.getUnlocalizedName());
		setCreativeTab(block.getCreativeTabToDisplayOn());
		this.block=block;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean flag = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
        BlockPos newpos = flag ? pos : pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.canPlayerEdit(newpos, facing, itemstack) && world.mayPlace(world.getBlockState(newpos).getBlock(), newpos, false, facing, player) && block.canPlaceBlockAt(world, newpos)) {
            world.setBlockState(newpos, block.getDefaultState());
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, newpos, itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

}

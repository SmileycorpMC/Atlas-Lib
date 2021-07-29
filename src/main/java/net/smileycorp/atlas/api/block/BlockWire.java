package net.smileycorp.atlas.api.block;

import java.lang.reflect.Field;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.ItemWireBlock;

public class BlockWire extends BlockBase {
	
	public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.<EnumAttachPosition>create("north", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.<EnumAttachPosition>create("east", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.<EnumAttachPosition>create("south", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.<EnumAttachPosition>create("west", EnumAttachPosition.class);
    
    protected static final AxisAlignedBB[] WIRE_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), 
    	new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    
    private final Item item;
    
	public BlockWire(String name, String modid, CreativeTabs tab, Field itemHolder) {
		super(name, modid, Material.ROCK, SoundType.STONE, 0.0F, 0.0F, null, 0, tab);
		item = new ItemWireBlock(this);
		try {
			itemHolder.set(null, item);
		} catch (Exception e) {}
	}
	
	public Item getItem() {
		return item;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return item;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(item);
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		int i = 0;
        boolean north = state.getValue(NORTH) != EnumAttachPosition.NONE;
        boolean east = state.getValue(EAST) != EnumAttachPosition.NONE;
        boolean south = state.getValue(SOUTH) != EnumAttachPosition.NONE;
        boolean west = state.getValue(WEST) != EnumAttachPosition.NONE;
        if (north || south && !north && !east && !west) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }
        if (east || west && !north && !east && !south) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }
        if (south || north && !east && !south && !west) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }
        if (west || east && !north && !south && !west) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }
        return WIRE_AABB[i];
    }

    @Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.withProperty(WEST, getAttachPosition(world, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, getAttachPosition(world, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, getAttachPosition(world, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, getAttachPosition(world, pos, EnumFacing.SOUTH));
        return state;
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess world, BlockPos pos, EnumFacing direction) {
        BlockPos newpos = pos.offset(direction);
        if (world.getBlockState(newpos).getBlock() == this) {
        	return EnumAttachPosition.SIDE;
        }
        if (world.getBlockState(newpos.up()).getBlock() == this && world.isSideSolid(pos.up(), EnumFacing.DOWN, false)) {
        	return EnumAttachPosition.UP;
        }
        return EnumAttachPosition.NONE;
    }

    @Override
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }
    
    @Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
	public boolean isFullCube(IBlockState state) {
        return false;
    }
	    
    @Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState down = world.getBlockState(pos.down());
        return down.isTopSolid() || down.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID || world.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }
	
	private void notifyWireNeighborsOfStateChange(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == this) {
            world.notifyNeighborsOfStateChange(pos, this, false);
            for (EnumFacing enumfacing : EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) {

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(world, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (world.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(world, blockpos.up());
                }
                else {
                    this.notifyWireNeighborsOfStateChange(world, blockpos.down());
                }
            }
        }
    }
	
	@Override
 	public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }
	
    @Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
            case FRONT_BACK:
                return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
            default:
                return super.withMirror(state, mirror);
        }
    }

    @Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST});
    }
	
	public static enum EnumAttachPosition implements IStringSerializable {
	        UP("up"),
	        SIDE("side"),
	        NONE("none");

	        private final String name;

	        private EnumAttachPosition(String name)
	        {
	            this.name = name;
	        }

	        @Override
			public String toString()
	        {
	            return this.getName();
	        }

	        @Override
			public String getName()
	        {
	            return this.name;
	        }
	}
	
}

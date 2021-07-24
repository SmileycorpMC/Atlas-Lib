package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;


public class BlockBase extends Block implements IBlockProperties {

	protected String name;
	protected final String modid;
	public boolean isFlamable;
	public ItemGroup tab;

	public BlockBase(String name, String modid, Material material, MaterialColor colour, SoundType sound, float h, float r, ToolType tool, int level, ItemGroup tab) {
		super(Properties.create(material, colour).hardnessAndResistance(h, r).harvestLevel(level).harvestTool(tool).sound(sound));
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		this.name=name;
		this.modid=modid;
		this.tab=tab;
		if (material == Material.WOOD) this.isFlamable = true;
	}

	public BlockBase(String name, String modid, Material material, MaterialColor colour, SoundType sound, float h, float r, int level, ItemGroup tab) {
		this(name, modid, material, null, sound, h, r, ToolType.PICKAXE, level, tab);
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return isFlamable ? 20 : super.getFlammability(state, world, pos, face);
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return isFlamable ? 5 : super.getFireSpreadSpeed(state, world, pos, face);
	}

	public String getName() {
		return name;
	}

	public String getMod() {
		return modid;
	}
	
	public ItemGroup getCreativeTab() {
		return tab;
	}

}

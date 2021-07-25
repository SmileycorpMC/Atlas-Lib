package net.smileycorp.atlas.api.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.IForgeRegistry;

public class ShapedBlock {
	
	protected final String name, modid;
	protected final CreativeModeTab tab;
	
	protected final Block base;
	protected final StairBlock stairs;
	protected final SlabBlock slab;
	
	protected final List<Block> blocks;
	
	@SuppressWarnings("deprecation")
	public ShapedBlock(String name, String modid, CreativeModeTab tab, BlockBehaviour.Properties properties) {
		this.name=name;
		this.modid=modid;
		this.tab=tab;
		base = new Block(properties);
		base.setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		stairs = new StairBlock(base.defaultBlockState(), properties);
		stairs.setRegistryName(new ResourceLocation(modid, name.toLowerCase()+"_stairs"));
		slab = new SlabBlock(properties);
		slab.setRegistryName(new ResourceLocation(modid, name.toLowerCase()+"_slab"));
		blocks = Arrays.asList(new Block[]{base, stairs, slab});
	}

	public Block getBase() {
		return base;
	}
	
	public Block getStairs() {
		return stairs;
	}
	
	public Block getSlab() {
		return slab;
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.registerAll(blocks.toArray(new Block[] {}));
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (Block block : blocks) {
			Item item = new BlockItem(block, new Item.Properties().tab(tab));
			item.setRegistryName(block.getRegistryName());
			registry.register(item);
		}
	}
	
}

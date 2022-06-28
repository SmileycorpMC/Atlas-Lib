package net.smileycorp.atlas.api.block;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ShapedBlock {

	protected final String name;
	protected final CreativeModeTab tab;

	protected final Map<BlockShape, RegistryObject<Block>> BLOCKS = Maps.newHashMap();

	public ShapedBlock(String name, CreativeModeTab tab, BlockBehaviour.Properties properties, DeferredRegister<Item> items, DeferredRegister<Block> blocks, boolean hasWall) {
		this.name=name;
		this.tab=tab;
		register(items, blocks, () -> new Block(properties), BlockShape.BASE);
		register(items, blocks, () -> new StairBlock(() -> getBase().defaultBlockState(), properties), BlockShape.STAIRS);;
		register(items, blocks, () -> new SlabBlock(properties), BlockShape.SLAB);
		if (hasWall) register(items, blocks, () -> new WallBlock(properties), BlockShape.WALL);
	}

	protected void register(DeferredRegister<Item> items, DeferredRegister<Block> blocks, Supplier<Block> supplier, BlockShape shape) {
		String name = this.name;
		if (shape != BlockShape.BASE) name += "_" + shape.name().toLowerCase();
		RegistryObject<Block> block = blocks.register(name, supplier);
		items.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
		BLOCKS.put(shape, block);
	}

	public Block get(BlockShape shape) {
		return BLOCKS.containsKey(shape) ? BLOCKS.get(shape).get() : null;
	}

	public Block getBase() {
		return get(BlockShape.BASE);
	}

	public StairBlock getStairs() {
		return (StairBlock) get(BlockShape.STAIRS);
	}

	public SlabBlock getSlab() {
		return (SlabBlock) get(BlockShape.SLAB);
	}

	public WallBlock getWall() {
		return (WallBlock) get(BlockShape.WALL);
	}


	public static enum BlockShape {
		BASE, STAIRS, SLAB, WALL;
	}
}

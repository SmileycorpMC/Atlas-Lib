package net.smileycorp.atlas.api.block;

import com.google.common.collect.Maps;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.Supplier;

public class ShapedBlock {

	protected final String name;
	protected final Supplier<CreativeModeTab> tab;

	protected final Map<BlockShape, DeferredHolder<Block, Block>> BLOCKS = Maps.newHashMap();

	public ShapedBlock(String name, Supplier<CreativeModeTab> tab, BlockBehaviour.Properties properties, DeferredRegister<Item> items, DeferredRegister<Block> blocks, boolean hasWall) {
		this.name=name;
		this.tab=tab;
		register(items, blocks, () -> new Block(properties), BlockShape.BASE);
		register(items, blocks, () -> new StairBlock(() -> getBase().defaultBlockState(), properties), BlockShape.STAIRS);
		register(items, blocks, () -> new SlabBlock(properties), BlockShape.SLAB);
		if (hasWall) register(items, blocks, () -> new WallBlock(properties), BlockShape.WALL);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	protected void register(DeferredRegister<Item> items, DeferredRegister<Block> blocks, Supplier<Block> supplier, BlockShape shape) {
		String name = this.name;
		if (shape != BlockShape.BASE) name += "_" + shape.getSerializedName();
		DeferredHolder<Block, Block> block = blocks.register(name, supplier);
		items.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		BLOCKS.put(shape, block);
	}

	@SubscribeEvent
	public void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == tab.get()) {
			event.accept(getBase().asItem());
			event.accept(getStairs().asItem());
			event.accept(getSlab().asItem());
			if (BLOCKS.containsKey(BlockShape.WALL)) event.accept(getWall().asItem());
		}
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


	public static enum BlockShape implements StringRepresentable {
		BASE("base"), STAIRS("stairs"), SLAB("slab"), WALL("wall");

		private final String name;

		BlockShape(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}

package net.smileycorp.atlas.api.block;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ShapedBlock {

	protected final String name;
	protected final CreativeModeTab tab;

	protected final RegistryObject<Block> base;
	protected final RegistryObject<Block> stairs;
	protected final RegistryObject<Block> slab;
	protected final RegistryObject<Block> wall;

	public ShapedBlock(String name, CreativeModeTab tab, BlockBehaviour.Properties properties, DeferredRegister<Item> items, DeferredRegister<Block> blocks, boolean hasWall) {
		this.name=name;
		this.tab=tab;
		base = register(items, blocks, () -> new Block(properties), "");
		stairs = register(items, blocks, () -> new StairBlock(()->base.get().defaultBlockState(), properties), "stairs");
		slab = register(items, blocks, () -> new SlabBlock(properties), "slab");
		wall = hasWall ? register(items, blocks, () -> new WallBlock(properties), "wall") : null;
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected RegistryObject<Block> register(DeferredRegister<Item> items, DeferredRegister<Block> blocks, Supplier<Block> supplier, String suffix) {
		RegistryObject<Block> block = register(blocks, supplier, suffix);
		register(items, () -> new BlockItem(supplier.get(), new Item.Properties()), suffix);
		return block;
	}

	protected <T> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> object, String suffix) {
		StringBuilder builder = new StringBuilder();
		builder.append(name.toLowerCase());
		if (!suffix.isBlank()) builder.append("_" + suffix);
		return registry.register(builder.toString(), object);
	}

	@SubscribeEvent
	public void addCreative(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() == tab) {
			event.m_246326_(base.get().asItem());
			event.m_246326_(stairs.get().asItem());
			event.m_246326_(slab.get().asItem());
			if (wall != null) event.m_246326_(wall.get().asItem());
		}
	}

	public Block getBase() {
		return base.get();
	}

	public StairBlock getStairs() {
		return (StairBlock) stairs.get();
	}

	public SlabBlock getSlab() {
		return (SlabBlock) slab.get();
	}

	public WallBlock getWall() {
		return wall == null ? null : (WallBlock) wall.get();
	}

}

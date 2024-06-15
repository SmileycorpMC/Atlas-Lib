package net.smileycorp.atlas.api.metals;

import com.google.common.base.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MetalShape<T extends ItemLike> {

	private final ResourceLocation name;
	private final Function<?, Supplier<T>> function;
	private final int shapeTier;
	private final boolean isBlock;

	private MetalShape(ResourceLocation name, Function<?, Supplier<T>> function, Predicate<Metal> predic, boolean isBlock) {
		this.name = name;
		this.function = function;
		this.shapeTier = 0;
		this.isBlock = isBlock;
	}

	public ResourceLocation getName() {
		return name;
	}

	public int getShapeTier() {
		return shapeTier;
	}

	public boolean isBlock() {
		return isBlock;
	}

	protected void registerItem(String name, DeferredRegister<Item> registry, Item.Properties props) {
		if (isBlock) return;
	}

	protected void registerBlock(String name, DeferredRegister<Item> itemRegistry, DeferredRegister<Block> blockRegistry, Item.Properties itemProps, Block.Properties blockProps) {
		if (!isBlock) return;
	}


	/*protected static MetalShape<Item> createItem(ResourceLocation name, Function<Item.Properties, Supplier<Item>> function, int shapeTier) {
		return new MetalShape<Item>(name, function, shapeTier, false);
	}

	protected static MetalShape<Block> createBlock (ResourceLocation name, Function<Block.Properties, Supplier<Block>> function, int shapeTier) {
		return new MetalShape<Block>(name, function, shapeTier, true);
	}*/

	public static enum ColourType {
		METAL,
		ORE,
		MOLTEN;
	}

}

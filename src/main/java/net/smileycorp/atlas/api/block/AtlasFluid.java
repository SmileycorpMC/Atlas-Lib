package net.smileycorp.atlas.api.block;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class AtlasFluid {

	private final String name;

	private RegistryObject<Fluid> source;
	private RegistryObject<Fluid> flowing;
	private RegistryObject<Block> block;
	private RegistryObject<Item> bucket;

	public AtlasFluid(String modid, String name, DeferredRegister<Fluid> fluids, DeferredRegister<Block> blocks, DeferredRegister<Item> items, BlockBehaviour.Properties properties, CreativeModeTab tab) {
		this.name=name;
		source = register(fluids, () -> new Source(new ForgeFlowingFluid.Properties(() -> source.get(), flowing,
				FluidAttributes.builder(new ResourceLocation(modid, "fluid/"+name), new ResourceLocation(modid, "fluid/"+name+"_flowing")))
				.bucket(bucket).block(()->(LiquidBlock)block.get()).levelDecreasePerBlock(3)), "");
		flowing = register(fluids, () -> new Flowing(new ForgeFlowingFluid.Properties(() -> source.get(), flowing,
				FluidAttributes.builder(new ResourceLocation(modid, "fluid/"+name), new ResourceLocation(modid, "fluid/"+name+"_flowing")))
				.bucket(bucket).block(()->(LiquidBlock)block.get()).levelDecreasePerBlock(3)), "flowing", "");
		block = register(blocks, () -> new LiquidBlock(() -> (FlowingFluid) source.get(), properties), "");
		bucket = register(items, () -> new BucketItem(source, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(tab)), "bucket");
	}

	protected <T extends IForgeRegistryEntry<T>> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String suffix) {
		return register(registry, supplier, "", suffix);
	}

	protected <T extends IForgeRegistryEntry<T>> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String prefix, String suffix) {
		StringBuilder builder = new StringBuilder();
		if (!prefix.isBlank()) builder.append(prefix + "_");
		builder.append(name.toLowerCase());
		if (!suffix.isBlank()) builder.append("_" + suffix);
		return registry.register(builder.toString(), supplier);
	}

	public Fluid getSource() {
		return source.get();
	}

	public Fluid getFlowing() {
		return flowing.get();
	}

	public Block getBlock() {
		return block.get();
	}

	public Item getBucket() {
		return bucket.get();
	}
}

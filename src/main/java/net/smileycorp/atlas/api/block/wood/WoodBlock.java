package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.client.WoodStateMapper;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.atlas.api.item.ItemBlockMeta;

import java.util.List;

public class WoodBlock<T extends Enum<T> & WoodEnum> {
	
	private final String modid;
	private final T[] types;
	private final List<BlockBasePlank<T>> planks = Lists.newArrayList();
	private final List<BlockBaseLog<T>> logs = Lists.newArrayList();
	private final List<BlockBaseLeaves<T>> leaves = Lists.newArrayList();
	private final List<BlockBaseSapling<T>> saplings = Lists.newArrayList();
	
	public WoodBlock(String modid, CreativeTabs tab, Class<T> types) {
		this.modid = modid;
		this.types = types.getEnumConstants();
		int size = this.types.length;
		for (int i = 0; i <= (size - 1) / 16; i++) {
			planks.add(BlockBasePlank.create("plank_" + (size - 2 > i * 16 ?  i : this.types[i * 16].getName()),
					modid, tab, types, i));
		}
		for (int i = 0; i <= (size - 1) / 4; i++) {
			String name = (size - 2 > i * 4 ? String.valueOf(i) : this.types[i * 4].getName());
			logs.add(BlockBaseLog.create("log_" + name, modid, tab, types, i));
			BlockBaseSapling sapling = BlockBaseSapling.create("sapling_" + name, modid, tab, types, i);
			leaves.add(BlockBaseLeaves.create("leaves_" + name, modid, tab, sapling, types, i));
			saplings.add(sapling);
		}
	}

	public ItemStack getPlankStack(T type, int amount) {
		return new ItemStack(planks.get(type.ordinal() / 16), amount, type.ordinal() % 16);
	}
	
	public IBlockState getPlankState(T type) {
		BlockBasePlank plank = planks.get(type.ordinal() / 16);
		return plank.getDefaultState().withProperty(plank.typeProperty(), type);
	}
	
	public ItemStack getLogStack(T type, int amount) {
		return new ItemStack(logs.get(type.ordinal() / 4), amount, type.ordinal() % 4);
	}
	
	public ItemStack getWoodStack(T type, int amount) {
		return new ItemStack(logs.get(type.ordinal() / 4), amount, (type.ordinal() % 4) + 12);
	}
	
	public IBlockState getLogState(T type, BlockLog.EnumAxis axis) {
		BlockBaseLog log = logs.get(type.ordinal() / 4);
		return log.getDefaultState().withProperty(log.typeProperty(), type).withProperty(BlockLog.LOG_AXIS, axis);
	}
	
	public ItemStack getLeavesStack(T type, int amount) {
		return new ItemStack(leaves.get(type.ordinal() / 4), amount, type.ordinal() % 4);
	}
	
	public IBlockState getLeavesState(T type, boolean decayable, boolean check_decay) {
		BlockBaseLeaves leaf = leaves.get(type.ordinal() / 4);
		return leaf.getDefaultState().withProperty(leaf.typeProperty(), type).withProperty(BlockLeaves.DECAYABLE, decayable)
				.withProperty(BlockLeaves.CHECK_DECAY, check_decay);
	}
	
	public ItemStack getSaplingStack(T type, int amount) {
		BlockBaseSapling sapling = saplings.get(type.ordinal() / 4);
		return sapling == null |! type.hasSapling() ? ItemStack.EMPTY : new ItemStack(sapling, amount, type.ordinal() % 4);
	}
	
	public IBlockState getSaplingState(T type, int stage) {
		BlockBaseSapling sapling = saplings.get(type.ordinal() / 4);
		return sapling == null |! type.hasSapling() ? Blocks.AIR.getDefaultState() :
				sapling.getDefaultState().withProperty(sapling.typeProperty(), type).withProperty(BlockSapling.STAGE, stage % 4);
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		for (BlockBasePlank plank : planks) registry.register(plank);
		for (BlockBaseLog log : logs) registry.register(log);
		for (BlockBaseLeaves leaves : leaves) registry.register(leaves);
		for (BlockBaseSapling sapling : saplings) if (sapling != null) registry.register(sapling);
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (BlockBasePlank plank : planks) registry.register(new ItemBlockMeta(plank));
		for (BlockBaseLog log : logs) registry.register(new ItemBlockMeta(log));
		for (BlockBaseLeaves leaves : leaves) registry.register(new ItemBlockMeta(leaves));
		for (BlockBaseSapling sapling : saplings) if (sapling != null) registry.register(new ItemBlockMeta(sapling));
	}
	
	public void registerModels() {
		for (BlockBasePlank plank : planks) {
			ModelLoader.setCustomStateMapper(plank, new WoodStateMapper(plank));
			registerModel(plank);
		}
		for (BlockBaseLog log : logs) {
			ModelLoader.setCustomStateMapper(log, new WoodStateMapper(log));
			Item item = Item.getItemFromBlock(log);
			registerModel(item);
			for (int i = 12; i < log.getMaxMeta() + 12; i++) {
				ModelResourceLocation loc = new ModelResourceLocation(modid + ":" + ((IMetaItem) item).byMeta(i));
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
			}
		}
		for (BlockBaseLeaves leaves : leaves) {
			ModelLoader.setCustomStateMapper(leaves, new WoodStateMapper(leaves));
			registerModel(leaves);
		}
		for (BlockBaseSapling sapling : saplings) if (sapling != null) {
			ModelLoader.setCustomStateMapper(sapling, new WoodStateMapper(sapling));
			registerModel(sapling);
		}
	}
	
	private void registerModel(Block block) {
		registerModel(Item.getItemFromBlock(block));
	}
	
	private void registerModel(Item item) {
		for (int i = 0; i < ((IMetaItem)item).getMaxMeta(); i++) {
			ModelResourceLocation loc = new ModelResourceLocation(modid + ":" + ((IMetaItem) item).byMeta(i));
			ModelLoader.setCustomModelResourceLocation(item, i, loc);
		}
	}
	
	public void registerRecipes() {
		//oredict/furnace
		for (BlockBasePlank plank : planks) OreDictionary.registerOre("plankWood", plank);
		for (BlockBaseLog log : logs) {
			OreDictionary.registerOre("logWood", log);
			GameRegistry.addSmelting(log, new ItemStack(Items.COAL, 1, 1), 0.15f);
		}
		for (BlockBaseLeaves leaf : leaves) OreDictionary.registerOre("treeLeaves", leaf);
		for (BlockBaseSapling sapling : saplings) OreDictionary.registerOre("treeSapling", sapling);
		//crafting recipes
		for (T type : types) {
			String name = type.getName();
			GameRegistry.addShapelessRecipe(new ResourceLocation(modid, name + "_plank"), new ResourceLocation(modid, name), getPlankStack(type, 4),
					Ingredient.fromStacks(getLogStack(type, 1)));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name + "_wood"), new ResourceLocation(modid, name), getWoodStack(type, 3),
                    "  #", " ##", "###", '#', getLogStack(type, 1));
		}
	}
	
}

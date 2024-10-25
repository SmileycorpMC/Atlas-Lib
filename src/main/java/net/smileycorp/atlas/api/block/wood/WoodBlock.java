package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockStairsBase;
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
	private final List<Tuple<BlockWoodSlab<T>, BlockWoodSlab<T>>> slabs = Lists.newArrayList();
	private final List<BlockStairsBase> stairs = Lists.newArrayList();
	
	public WoodBlock(String modid, CreativeTabs tab, Class<T> types) {
		this(modid, tab, types, false);
	}
	
	public WoodBlock(String modid, CreativeTabs tab, Class<T> types, boolean forceSimpleNames) {
		this.modid = modid;
		this.types = types.getEnumConstants();
		int size = this.types.length;
		for (int i = 0; i <= (size - 1) / 16; i++) {
			planks.add(BlockBasePlank.create("plank_" + ((forceSimpleNames || size - 2 > i * 16) ? i : this.types[i * 16].getName()),
					modid, tab, types, i));
		}
		for (int i = 0; i <= (size - 1) / 8; i++) {
			slabs.add(BlockWoodSlab.create("slab_" + (forceSimpleNames || (size - 2 > i * 8) ? String.valueOf(i) : this.types[i * 8].getName()),
					modid, tab, types, i));
		}
		for (int i = 0; i <= (size - 1) / 4; i++) {
			String name = (forceSimpleNames || (size - 2 > i * 4) ? String.valueOf(i) : this.types[i * 4].getName());
			logs.add(BlockBaseLog.create("log_" + name, modid, tab, types, i));
			BlockBaseSapling<T> sapling = BlockBaseSapling.create("sapling_" + name, modid, tab, types, i);
			leaves.add(BlockBaseLeaves.create("leaves_" + name, modid, tab, sapling, types, i));
			saplings.add(sapling);
		}
		for (T type : this.types) {
			stairs.add(new BlockStairsBase(type.name() + "_stairs", getPlankState(type)));
		}
	}

	public ItemStack getPlankStack(T type, int amount) {
		return new ItemStack(planks.get(type.ordinal() / 16), amount, type.ordinal() % 16);
	}
	
	public IBlockState getPlankState(T type) {
		BlockBasePlank plank = planks.get(type.ordinal() / 16);
		return plank.getDefaultState().withProperty(plank.getVariantProperty(), type);
	}
	
	public ItemStack getLogStack(T type, int amount) {
		return new ItemStack(logs.get(type.ordinal() / 4), amount, type.ordinal() % 4);
	}
	
	public ItemStack getWoodStack(T type, int amount) {
		return new ItemStack(logs.get(type.ordinal() / 4), amount, (type.ordinal() % 4) + 12);
	}
	
	public IBlockState getLogState(T type, BlockLog.EnumAxis axis) {
		BlockBaseLog log = logs.get(type.ordinal() / 4);
		return log.getDefaultState().withProperty(log.getVariantProperty(), type).withProperty(BlockLog.LOG_AXIS, axis);
	}
	
	public ItemStack getLeavesStack(T type, int amount) {
		return new ItemStack(leaves.get(type.ordinal() / 4), amount, type.ordinal() % 4);
	}
	
	public IBlockState getLeavesState(T type) {
		return getLeavesState(type, true, true);
	}
	
	public IBlockState getLeavesState(T type, boolean decayable, boolean check_decay) {
		BlockBaseLeaves leaf = leaves.get(type.ordinal() / 4);
		return leaf.getDefaultState().withProperty(leaf.getVariantProperty(), type).withProperty(BlockLeaves.DECAYABLE, decayable)
				.withProperty(BlockLeaves.CHECK_DECAY, check_decay);
	}
	
	public ItemStack getSaplingStack(T type, int amount) {
		BlockBaseSapling sapling = saplings.get(type.ordinal() / 4);
		return sapling == null |! type.hasSapling() ? ItemStack.EMPTY : new ItemStack(sapling, amount, type.ordinal() % 4);
	}
	
	public IBlockState getSaplingState(T type) {
		return getSaplingState(type, 0);
	}
	
	public IBlockState getSaplingState(T type, int stage) {
		BlockBaseSapling sapling = saplings.get(type.ordinal() / 4);
		return sapling == null |! type.hasSapling() ? Blocks.AIR.getDefaultState() :
				sapling.getDefaultState().withProperty(sapling.getVariantProperty(), type).withProperty(BlockSapling.STAGE, stage % 4);
	}
	
	public ItemStack getSlabStack(T type, int amount) {
		return new ItemStack(slabs.get(type.ordinal() / 8).getFirst(), amount, type.ordinal() % 8);
	}
	
	public IBlockState getSlabState(T type, BlockSlab.EnumBlockHalf half) {
		BlockWoodSlab<T> slab = slabs.get(type.ordinal() / 8).getFirst();
		return slab.getDefaultState().withProperty(slab.getVariantProperty(), type).withProperty(BlockSlab.HALF, half);
	}
	
	public IBlockState getDoubleSlabState(T type) {
		BlockWoodSlab<T> slab = slabs.get(type.ordinal() / 8).getSecond();
		return slab.getDefaultState().withProperty(slab.getVariantProperty(), type);
	}
	
	public ItemStack getStairStack(T type, int amount) {
		return new ItemStack(stairs.get(type.ordinal()), amount);
	}
	
	public IBlockState getStairState(T type, BlockStairs.EnumHalf half, BlockStairs.EnumShape shape, EnumFacing facing) {
		if (facing.getAxis() == EnumFacing.Axis.Y) facing = EnumFacing.NORTH;
		return stairs.get(type.ordinal()).getDefaultState().withProperty(BlockStairs.HALF, half)
				.withProperty(BlockStairs.SHAPE, shape).withProperty(BlockStairs.FACING, facing);
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		for (BlockBasePlank<T> plank : planks) registry.register(plank);
		for (BlockBaseLog<T> log : logs) registry.register(log);
		for (BlockBaseLeaves<T> leaves : leaves) registry.register(leaves);
		for (BlockBaseSapling<T> sapling : saplings) if (sapling != null) registry.register(sapling);
		for (Tuple<BlockWoodSlab<T>, BlockWoodSlab<T>> slab : slabs) {
			registry.register(slab.getFirst());
			registry.register(slab.getSecond());
		}
		for (BlockStairsBase stair : stairs) registry.register(stair);
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (BlockBasePlank<T> plank : planks) registry.register(new ItemBlockMeta(plank));
		for (BlockBaseLog<T> log : logs) registry.register(new ItemBlockMeta(log));
		for (BlockBaseLeaves<T> leaves : leaves) registry.register(new ItemBlockMeta(leaves));
		for (BlockBaseSapling<T> sapling : saplings) if (sapling != null) registry.register(new ItemBlockMeta(sapling));
		for (Tuple<BlockWoodSlab<T>, BlockWoodSlab<T>> slab : slabs) {
			BlockWoodSlab<T> single = slab.getFirst();
			ItemSlab item = new ItemSlab(single, single, slab.getSecond());
			item.setRegistryName(single.getRegistryName());
			item.setUnlocalizedName(single.getUnlocalizedName());
			registry.register(item);
		}
		for (BlockStairsBase stair : stairs) {
			ItemBlock item = new ItemBlock(stair);
			item.setRegistryName(stair.getRegistryName());
			item.setUnlocalizedName(stair.getUnlocalizedName());
			registry.register(item);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		for (BlockBasePlank<T> plank : planks) {
			ModelLoader.setCustomStateMapper(plank, new WoodStateMapper(plank));
			registerModel(plank);
		}
		for (BlockBaseLog<T> log : logs) {
			ModelLoader.setCustomStateMapper(log, new WoodStateMapper(log));
			Item item = Item.getItemFromBlock(log);
			registerModel(item);
			for (int i = 12; i < log.getMaxMeta() + 12; i++) {
				ModelResourceLocation loc = new ModelResourceLocation(modid + ":" + ((IMetaItem) item).byMeta(i));
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
			}
		}
		for (BlockBaseLeaves<T> leaves : leaves) {
			ModelLoader.setCustomStateMapper(leaves, new WoodStateMapper(leaves));
			registerModel(leaves);
		}
		for (BlockBaseSapling<T> sapling : saplings) if (sapling != null) {
			ModelLoader.setCustomStateMapper(sapling, new WoodStateMapper(sapling));
			registerModel(sapling);
		}
		for (Tuple<BlockWoodSlab<T>, BlockWoodSlab<T>> slab : slabs) registerModel(slab.getFirst());
		for (BlockStairsBase stair : stairs) registerModel(stair);
	}
	
	@SideOnly(Side.CLIENT)
	private void registerModel(Block block) {
		registerModel(Item.getItemFromBlock(block));
	}
	
	@SideOnly(Side.CLIENT)
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
		for (Tuple<BlockWoodSlab<T>, BlockWoodSlab<T>> slab : slabs) OreDictionary.registerOre("slabWood", slab.getFirst());
		for (BlockStairsBase stair : stairs) OreDictionary.registerOre("stairWood", stair);
		//crafting recipes
		for (T type : types) {
			String name = type.getName();
			GameRegistry.addShapelessRecipe(new ResourceLocation(modid, name + "_plank"), new ResourceLocation(modid, name), getPlankStack(type, 4),
					Ingredient.fromStacks(getLogStack(type, 1)));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name + "_wood"), new ResourceLocation(modid, name), getWoodStack(type, 3),
                    "  #", " ##", "###", '#', getLogStack(type, 1));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name + "_slab"), new ResourceLocation(modid, name), getSlabStack(type, 6),
					 "###", '#', getPlankStack(type, 1));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name + "_stair"), new ResourceLocation(modid, name), getStairStack(type, 4),
					"  #", " ##", "###", '#', getPlankStack(type, 1));
		}
	}
	
}

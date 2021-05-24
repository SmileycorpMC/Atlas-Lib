package net.smileycorp.atlas.api.block.wood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.smileycorp.atlas.api.block.BlockMetaBase;
import net.smileycorp.atlas.api.block.BlockStairsBase;

import com.google.common.collect.Lists;



public class WoodBlock {
	
	final List<String> variants;
	final List<List<String>> subvariants;
	
	final String name;
	final String modid;
	final BlockMetaBase plank;
	final List<BlockBaseLog> logs = new ArrayList<BlockBaseLog>();
	final Map<String, BlockStairsBase> stairs = new HashMap<String, BlockStairsBase>();
	final List<BlockBaseLeaves> leaves = new ArrayList<BlockBaseLeaves>();
	//final List<BlockMetaSlab> slabs = new ArrayList<BlockMetaSlab>();
	//final List<BlockMetaSlab> doubleSlabs = new ArrayList<BlockMetaSlab>();

	public WoodBlock(String name, String modid, CreativeTabs tab, Map<String, ItemStack> metaDefinitions, boolean isFlamable) {
		this.variants = new ArrayList<String>(metaDefinitions.keySet());
		this.subvariants = Lists.partition(variants, 4);
		List<List<ItemStack>> sapLists = Lists.partition(new ArrayList<ItemStack>(metaDefinitions.values()), 4);
		this.name=name;
		this.modid=modid;
		plank = new BlockMetaBase(name.isEmpty() ? "Plank":"Plank_"+name, modid, Material.WOOD, SoundType.WOOD, 2f, 5f, "axe", 0, tab, variants);
		plank.isFlamable=isFlamable;
		for (List<String> localVariants : subvariants) {
			int i = subvariants.indexOf(localVariants);
			String suffix = i == 1 ? "" : "_"+i;
			logs.add(new BlockBaseLog(name + suffix, modid, tab, localVariants, isFlamable));
			/*leaves.add(new BlockBaseLeaves(name + suffix, modid, tab, localVariants, sapLists.get(i)));
			slab = new BlockMetaSlab(name + suffix, modid, plank, localVariants, false);
			doubleSlab = new BlockMetaSlab(name + suffix, modid, plank, localVariants, true);*/
		}
		for (int i = 0; i < variants.size(); i++) {
			stairs.put(variants.get(i), new BlockStairsBase(variants.get(i)+"_Plank", plank.getStateFromMeta(i)));
		}
		
	}

	public Block getPlank() {
		return plank;
	}
	
	public Block getStairs(String variant) {
		return stairs.get(variant);
	}
	
	/*public Block getSlab() {
		return getSlab(false);
	}
	
	public Block getSlab(boolean getFull) {
		return getFull ? doubleSlab : slab;
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		slab.half=slab;
		doubleSlab.half=slab;
		registry.registerAll(blocks);
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (Block block : blocks) {
			if (block instanceof BlockSlabBase) continue;
			Item item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setUnlocalizedName(block.getUnlocalizedName());
			registry.register(item);
		}
		Item item = new ItemSlab(slab, slab, doubleSlab);
		item.setRegistryName(slab.getRegistryName());
		item.setUnlocalizedName(slab.getUnlocalizedName());
		registry.register(item);
	}
	
	public void registerModels() {
		for (Block block : blocks) {
			if (block==doubleSlab) continue;
			final ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(block);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(loc, "normal"));
		}
		ModelLoader.setCustomStateMapper(slab, new SlabStateMapper(slab));
		ModelLoader.setCustomStateMapper(doubleSlab, new CustomStateMapper(modid, name, "normal"));
	}
	
	public void registerRecipes() {
		OreDictionary.registerOre("logWood", log);
		OreDictionary.registerOre("plankWood", plank);
		OreDictionary.registerOre("slabWood", slab);
		OreDictionary.registerOre("stairWood", stairs);
		for (int i = 0; i < 4;) {
			String name = variants.get(i);
			GameRegistry.addShapelessRecipe(new ResourceLocation(modid, name +"_plank"), new ResourceLocation(modid, name), new ItemStack(plank, 4, i), Ingredient.fromStacks(new ItemStack(log, 1, i)));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_stairs"), new ResourceLocation(modid, name), new ItemStack(stairs, 4, i), 
					new Object[]{"  #", " ##", "###", '#', new ItemStack(plank, 1, i)});
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_slab"), new ResourceLocation(modid, name), new ItemStack(slab, 6, i), 
					new Object[]{"###", '#', new ItemStack(plank, 1, i)});
		}
	}*/
	
	public static enum EnumWood implements IStringSerializable {
		;
		
		protected final String name;
		
		EnumWood(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

	}
}

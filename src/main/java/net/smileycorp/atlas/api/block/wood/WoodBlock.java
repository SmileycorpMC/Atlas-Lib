package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockMetaBase;
import net.smileycorp.atlas.api.block.BlockStairsBase;
import net.smileycorp.atlas.api.item.ItemBlockMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	final List<Block> blocks = new ArrayList<Block>();

	public WoodBlock(String name, String modid, CreativeTabs tab, Map<String, ItemStack> metaDefinitions, boolean isFlamable) {
		variants = new ArrayList<String>(metaDefinitions.keySet());
		subvariants = Lists.partition(variants, 4);
		List<List<ItemStack>> sapLists = Lists.partition(new ArrayList<ItemStack>(metaDefinitions.values()), 4);
		this.name=name;
		this.modid=modid;
		plank = BlockMetaBase.create(name.isEmpty() ? "Plank":"Plank_"+name, modid, Material.WOOD, SoundType.WOOD, 2f, 5f, "axe", 0, tab, variants);
		plank.isFlamable=isFlamable;
		for (List<String> localVariants : subvariants) {
			int i = subvariants.indexOf(localVariants);
			String suffix = i == 1 ? "" : "_"+i;
			logs.add(BlockBaseLog.create(name + suffix, modid, tab, localVariants, isFlamable));
			leaves.add(BlockBaseLeaves.create(name + suffix, modid, tab, localVariants, sapLists.get(i), isFlamable));
			//slab = new BlockMetaSlab(name + suffix, modid, plank, localVariants, false);
			//doubleSlab = new BlockMetaSlab(name + suffix, modid, plank, localVariants, true);
		}
		for (int i = 0; i < variants.size(); i++) {
			stairs.put(variants.get(i), new BlockStairsBase(variants.get(i)+"_Plank", plank.getStateFromMeta(i)));
		}
		blocks.add(plank);
		blocks.addAll(logs);
		blocks.addAll(stairs.values());
		blocks.addAll(leaves);
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
	}*/

	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.registerAll(blocks.toArray(new Block[]{}));
	}

	public void registerItems(IForgeRegistry<Item> registry) {
		registry.register(new ItemBlockMeta<String>(plank, plank.getProperty(), "Plank"));
		for (BlockBaseLog log : logs) {
			registry.register(new ItemBlockMeta<String>(log, log.getProperty(), "Log"));
		}
		for (BlockBaseLeaves leaf : leaves) {
			registry.register(new ItemBlockMeta<String>(leaf, leaf.getProperty(), "Leaves"));
		}
		for (Block stair : stairs.values()) {
			Item item = new ItemBlock(stair);
			item.setRegistryName(stair.getRegistryName());
			item.setUnlocalizedName(stair.getUnlocalizedName());
			registry.register(item);
		}
	}

	public void registerModels() {
		for (Block block : blocks) {
			//if (block==doubleSlab) continue;
			final ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(block);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(loc, "normal"));
		}
	}

	public void registerRecipes() {
		for (Block log : logs) {
			OreDictionary.registerOre("logWood", log);
		}
		OreDictionary.registerOre("plankWood", plank);
		//OreDictionary.registerOre("slabWood", slab);
		for (Block stair : stairs.values()) {
			OreDictionary.registerOre("stairWood", stair);
		}
		for (int i = 0; i < 4;) {
			String name = variants.get(i);
			GameRegistry.addShapelessRecipe(new ResourceLocation(modid, name +"_plank"), new ResourceLocation(modid, name), new ItemStack(plank, 4, i),
					Ingredient.fromStacks(new ItemStack(logs.get((int) Math.floor(i / 4)), 1, i % 4)));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_stairs"), new ResourceLocation(modid, name), new ItemStack(stairs.get(name), 4, i),
					new Object[]{"  #", " ##", "###", '#', new ItemStack(plank, 1, i)});
			/*GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_slab"), new ResourceLocation(modid, name), new ItemStack(slab, 6, i),
					new Object[]{"###", '#', new ItemStack(plank, 1, i)});*/
		}
	}

}

package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import net.smileycorp.atlas.api.client.CustomStateMapper;
import net.smileycorp.atlas.api.client.SlabStateMapper;

public class ShapedBlock {
	
	final String name;
	final String modid;
	final BlockBase base;
	final BlockStairsBase stairs;
	final BlockSlabBase slab;
	final BlockSlabBase doubleSlab;
	
	Block[] blocks;
	
	public ShapedBlock(String name, String modid, Material material, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab) {
		this.name=name;
		this.modid=modid;
		base = new BlockBase(name, modid, material, sound, h, r, tool, level, tab);
		stairs = new BlockStairsBase(base);
		slab = new BlockSlabBase(base, h, r, tool, level, false);
		doubleSlab = new BlockSlabBase(base, h, r, tool, level, true);
		blocks = new Block[]{base, stairs, slab, doubleSlab};
	}

	public ShapedBlock(String name, String modid, Material material, SoundType sound, float h, float r, int level, CreativeTabs tab) {
		this(name, modid, material, sound, h, r, "pickaxe", level, tab); 
	}

	public Block getBase() {
		return base;
	}
	
	public Block getStairs() {
		return stairs;
	}
	
	public Block getSlab() {
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
		GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_stairs"), new ResourceLocation(modid, name), new ItemStack(stairs, 4), 
				new Object[]{"  #", " ##", "###", '#', new ItemStack(base)});
		GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_slab"), new ResourceLocation(modid, name), new ItemStack(slab, 6), 
				new Object[]{"###", '#', new ItemStack(base)});
	}
}

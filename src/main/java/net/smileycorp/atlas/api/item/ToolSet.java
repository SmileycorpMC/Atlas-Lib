package net.smileycorp.atlas.api.item;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("deprecation")
public class ToolSet {

	final String modid;
	final String name;
	final ToolMaterial material;
	
	Map<ToolType, Item> tools = new HashMap<ToolType, Item>();
	
	public ToolSet(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		this.name=name;
		this.modid=modid;
		this.material=material;
		for (ToolType type : ToolType.values()) {
			Item item = type.createItem(modid, name, material, tab);
			if (item!=null) tools.put(type, item);
		}
	}
	
	public ToolSet(String modid, String name, ToolMaterial material, CreativeTabs tab, float axedamage, float axespeed) {
		this.name=name;
		this.modid=modid;
		this.material=material;
		for (ToolType type : ToolType.values()) {
			Item item = type == ToolType.AXE ? new ToolItemAxe(modid, name, material, tab, axedamage, axespeed) : type.createItem(modid, name, material, tab);
			if (item!=null) tools.put(type, item);
		}
	}
	
	public String getModID() {
		return modid;
	}
	
	public String getName() {
		return name;
	}
	
	public ToolMaterial getMaterial() {
		return material;
	}
	
	public Item getItem(ToolType type) {
		return tools.get(type);
	}
	
	public Collection<Item> getItems() {
		return tools.values();
	}

	public void registerItems(IForgeRegistry<Item> registry) {
		for (Item tool:tools.values()) {
			registry.register(tool);
		}
	}
	
	public void registerModels() {
		for (Entry<ToolType, Item> tool:tools.entrySet()) {
			ModelLoader.setCustomModelResourceLocation(tool.getValue(), 0,
					new ModelResourceLocation(modid + ":items/"
					+ name.toLowerCase()+"_tools", tool.getKey().name()));
		}
	}
	
	public void registerRecipes() {
		ItemStack stack = this.material.getRepairItemStack();
		int[] ores = OreDictionary.getOreIDs(stack);
		Ingredient ingredient = (ores == null||ores.length==0) ? Ingredient.fromStacks(stack) : new OreIngredient(OreDictionary.getOreName(ores[0]));
		for (Entry<ToolType, Item> tool:tools.entrySet()) {
			tool.getKey().registerRecipe(modid, name, tool.getValue(), ingredient);
		}
	}
	
	public static enum ToolType {
		SWORD("sword", ToolItemSword.class, "M", "M", "S"),
		HOE("hoe", ToolItemHoe.class, "MM", " S", " S"),
		PICKAXE("pickaxe", ToolItemPickaxe.class, "MMM", " S ", " S "),
		AXE("axe", ToolItemAxe.class, "MM", "MS", " S"),
		SPADE("spade", ToolItemSpade.class, "M", "S", "S");
		
		final String name;
		final Class<? extends Item> clazz;
		final Object[] pattern;
		
		ToolType(String name, Class<? extends Item> clazz, Object... pattern) {
			this.name=name;
			this.clazz=clazz;
			this.pattern=pattern;
		}
		
		public Item createItem(String modid, String name, ToolMaterial material, CreativeTabs tab) {
			try {
				return ReflectionHelper.findConstructor(clazz, String.class, String.class, ToolMaterial.class, CreativeTabs.class).newInstance(modid, name, material, tab);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public void registerRecipe(String modid, String material, Item item, Ingredient ingredient) {
			Object[] recipe = {'M', ingredient, 'S', new OreIngredient("stickWood")};
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, material.toLowerCase()+"_"+name), new ResourceLocation(modid, material.toLowerCase()+"_"+name),
					new ItemStack(item), ArrayUtils.<Object>addAll(pattern, recipe));
		}
	}
	
}

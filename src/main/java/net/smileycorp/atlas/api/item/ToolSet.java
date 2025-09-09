package net.smileycorp.atlas.api.item;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ToolSet {

	final String modid;
	final String name;
	final ToolMaterial material;
	
	Map<ToolType, Item> tools = Maps.newLinkedHashMap();
	
	public ToolSet(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		this.name = name;
		this.modid = modid;
		this.material = material;
		for (ToolType type : ToolType.getTypes()) {
			Item item = type.createItem(modid, name, material, tab);
			if (item != null) tools.put(type, item);
		}
	}
	
	public ToolSet(String modid, String name, ToolMaterial material, CreativeTabs tab, float axedamage, float axespeed) {
		this.name = name;
		this.modid = modid;
		this.material = material;
		for (ToolType type : ToolType.getTypes()) {
			Item item = type == ToolType.AXE ? new ItemToolAxe(modid, name, material, tab, axedamage, axespeed) : type.createItem(modid, name, material, tab);
			if (item != null) tools.put(type, item);
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

	public Item getSword() {
		return getItem(ToolType.SWORD);
	}

	public Item getHoe() {
		return getItem(ToolType.HOE);
	}

	public Item getPickaxe() {
		return getItem(ToolType.PICKAXE);
	}

	public Item getAxe() {
		return getItem(ToolType.AXE);
	}

	public Item getSpade() {
		return getItem(ToolType.SPADE);
	}
	
	public Collection<Item> getItems() {
		return tools.values();
	}

	public void registerItems(IForgeRegistry<Item> registry) {
		tools.values().forEach(registry::register);
	}
	
	public void registerModels() {
		tools.entrySet().forEach(entry ->
			ModelLoader.setCustomModelResourceLocation(entry.getValue(), 0,
					new ModelResourceLocation(modid + ":items/"
					+ name.toLowerCase()+"_tools", entry.getKey().getName().toLowerCase(Locale.US))));
	}

	public void registerRecipes() {
		registerRecipes(true);
	}
	
	public void registerRecipes(boolean oredict) {
		ItemStack stack = material.getRepairItemStack();
		int[] ores = oredict ? OreDictionary.getOreIDs(stack) : new int[0];
		Ingredient ingredient = ores.length == 0 ? Ingredient.fromStacks(stack) : new OreIngredient(OreDictionary.getOreName(ores[0]));
		tools.entrySet().forEach(entry -> entry.getKey().registerRecipe(modid, name, entry.getValue(), ingredient));
	}
	
	public static class ToolType {

		private static final Map<String, ToolType> TYPES = Maps.newLinkedHashMap();

		public static final ToolType SWORD = register("sword", ItemToolSword::new, "M", "M", "S");
		public static final ToolType HOE = register("hoe", ItemToolHoe::new, "MM", " S", " S");
		public static final ToolType PICKAXE = register("pickaxe", ItemToolPickaxe::new, "MMM", " S ", " S ");
		public static final ToolType AXE = register("axe", ItemToolAxe::new, "MM", "MS", " S");
		public static final ToolType SPADE = register("shovel", ItemToolShovel::new, "M", "S", "S");
		
		private final String name;
		private final ToolConstructor constructor;
		private final Object[] pattern;
		
		private ToolType(String name, ToolConstructor constructor, Object... pattern) {
			this.name = name;
			this.constructor = constructor;
			this.pattern = pattern;
		}

		public String getName() {
			return name;
		}
		
		public Item createItem(String modid, String name, ToolMaterial material, CreativeTabs tab) {
			return constructor.create(modid, name, material, tab);
		}
		
		public void registerRecipe(String modid, String material, Item item, Ingredient ingredient) {
			if (pattern.length < 1) return;
			Object[] recipe = {'M', ingredient, 'S', new OreIngredient("stickWood")};
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, material.toLowerCase(Locale.US) + "_" + name),
					new ResourceLocation(modid, material.toLowerCase(Locale.US) + "_" + name),
					new ItemStack(item), ArrayUtils.addAll(pattern, recipe));
		}

		public static ToolType register(String name, ToolConstructor constructor, Object... pattern) {
			ToolType type = new ToolType(name, constructor, pattern);
			TYPES.put(name, type);
			return type;
		}

		public static Collection<ToolType> getTypes() {
			return TYPES.values();
		}

	}

	public interface ToolConstructor {

		Item create(String modid, String name, ToolMaterial material, CreativeTabs tab);

	}
	
}

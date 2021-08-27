package net.smileycorp.atlas.api.item;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class ToolSet {

	final String modid;
	final String name;
	final Tiers material;
	
	Map<ToolType, Item> tools = new HashMap<ToolType, Item>();
	
	public ToolSet(String modid, String name, Tiers material, CreativeModeTab tab) {
		this(modid, name, material, tab, tab);
	}
	
	public ToolSet(String modid, String name, Tiers material, CreativeModeTab toolTab, CreativeModeTab weaponTab) {
		this.name=name;
		this.modid=modid;
		this.material=material;
		for (ToolType type : ToolType.values()) {
			Item item = type.createItem(modid, name, material, type.isWeapon() ? weaponTab : toolTab);
			if (item!=null) tools.put(type, item);
		}
	}
	
	public String getModID() {
		return modid;
	}
	
	public String getName() {
		return name;
	}
	
	public Tier getMaterial() {
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
	
	public static class ToolType {
		
		static Set<ToolType> REGISTERED_TYPES = new HashSet<ToolType>();
		
		public static ToolType SWORD = register("sword", SwordItem.class, true);
		public static ToolType HOE = register("hoe", SwordItem.class);
		public static ToolType PICKAXE = register("pickaxe", SwordItem.class);
		public static ToolType AXE = register("axe", SwordItem.class);
		public static ToolType SHOVEL = register("shovel", SwordItem.class);
		
		final protected String name;
		final protected Class<? extends TieredItem> clazz;
		final protected boolean isWeapon;
		
		ToolType(String name, Class<? extends TieredItem> clazz, boolean isWeapon) {
			this.name=name;
			this.clazz=clazz;
			this.isWeapon=isWeapon;
		}
		
		public static Set<ToolType> values() {
			return REGISTERED_TYPES;
		}
		
		public static ToolType register(String name, Class<? extends TieredItem> clazz) {
			return register(name, clazz, false);
		}
		
		public static ToolType register(String name, Class<? extends TieredItem> clazz, boolean isWeapon) {
			ToolType type = new ToolType(name, clazz, isWeapon);
			REGISTERED_TYPES.add(type);
			return type;
		}
		
		public boolean isWeapon() {
			return isWeapon;
		}
		
		public String getName() {
			return name;
		}
		
		public TieredItem createItem(String modid, String name, Tier material, CreativeModeTab tab) {
			try {
				TieredItem item = ObfuscationReflectionHelper.findConstructor(clazz, Tier.class, Float.class, Float.class,  Properties.class).newInstance(material, 0, 0, new Properties().tab(tab));
				item.setRegistryName(new ResourceLocation(modid, name + "_" + this.name));
				return item;
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
}

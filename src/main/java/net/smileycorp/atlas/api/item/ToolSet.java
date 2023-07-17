package net.smileycorp.atlas.api.item;

import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ToolSet {

	final String name;
	final Tier material;
	protected final Supplier<CreativeModeTab> toolTab, weaponTab;

	Map<ToolType, RegistryObject<Item>> tools = new HashMap<ToolType, RegistryObject<Item>>();

	public ToolSet(String name, Tier material, Supplier<CreativeModeTab> tab, DeferredRegister<Item> registry) {
		this(name, material, tab, tab, registry);
	}

	public ToolSet(String name, Tier material, Supplier<CreativeModeTab> toolTab, Supplier<CreativeModeTab> weaponTab, DeferredRegister<Item> registry) {
		this.name=name;
		this.material=material;
		this.toolTab = toolTab;
		this.weaponTab = weaponTab;
		for (ToolType type : ToolType.values()) {
			RegistryObject<Item> item = type.createItem(name, material, registry);
			if (item!=null) tools.put(type, item);
		}
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == toolTab.get()) for (Entry<ToolType, RegistryObject<Item>> entry : tools.entrySet())
			if (!entry.getKey().isWeapon) event.accept(entry.getValue().get());
		if (event.getTab() == weaponTab.get()) for (Entry<ToolType, RegistryObject<Item>> entry : tools.entrySet())
			if (entry.getKey().isWeapon) event.accept(entry.getValue().get());
	}

	public String getName() {
		return name;
	}

	public Tier getMaterial() {
		return material;
	}

	public Item getItem(ToolType type) {
		return tools.get(type).get();
	}

	public Collection<Item> getItems() {
		return tools.values().stream().map((item)->item.get()).toList();
	}

	public static class ToolType {

		static Set<ToolType> REGISTERED_TYPES = new HashSet<ToolType>();

		public static ToolType SWORD = register("sword", (material, properties) -> new SwordItem(material, 0, 0, properties), true);
		public static ToolType HOE = register("hoe", (material, properties) -> new HoeItem(material, 0, 0, properties));
		public static ToolType PICKAXE = register("pickaxe", (material, properties) -> new PickaxeItem(material, 0, 0, properties));
		public static ToolType AXE = register("axe", (material, properties) -> new AxeItem(material, 0, 0, properties));
		public static ToolType SHOVEL = register("shovel", (material, properties) -> new ShovelItem(material, 0, 0, properties));

		final protected String name;
		final protected BiFunction<Tier, Properties, TieredItem> function;
		final protected boolean isWeapon;

		ToolType(String name, BiFunction<Tier, Properties, TieredItem> function, boolean isWeapon) {
			this.name=name;
			this.function=function;
			this.isWeapon=isWeapon;
		}

		public static Set<ToolType> values() {
			return REGISTERED_TYPES;
		}

		public static ToolType register(String name, BiFunction<Tier, Properties, TieredItem> function) {
			return register(name, function, false);
		}

		public static ToolType register(String name, BiFunction<Tier, Properties, TieredItem> function, boolean isWeapon) {
			ToolType type = new ToolType(name, function, isWeapon);
			REGISTERED_TYPES.add(type);
			return type;
		}

		public boolean isWeapon() {
			return isWeapon;
		}

		public String getName() {
			return name;
		}

		public RegistryObject<Item> createItem(String name, Tier material, DeferredRegister<Item> registry) {
			return registry.register(name + "_" + this.name, ()-> function.apply(material, new Properties()));
		}
	}

}

package net.smileycorp.atlas.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.entity.AtlasBoat;
import net.smileycorp.atlas.api.item.AtlasBoatItem;
import net.smileycorp.atlas.common.AtlasLib;

public class BoatRegistry  {

	private Map<ResourceLocation, Type> TYPES = new HashMap<ResourceLocation, Type>();

	public static BoatRegistry INSTANCE = new BoatRegistry();

	public static RegistryObject<EntityType<AtlasBoat>> BOAT_ENTITY;

	public Type register(String name, String modid, DeferredRegister<Item> register, CreativeModeTab creativeTab) {
		if (TYPES.isEmpty()) { BOAT_ENTITY = AtlasLib.ENTITIES.register("atlas_boat", ()-> EntityType.Builder.<AtlasBoat>of(AtlasBoat::new, MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build("atlas_boat"));
		}
		Type type = new Type(name, modid, register, creativeTab);
		TYPES.put(type.getRegistryName(), type);
		return type;
	}

	public Type get(ResourceLocation loc) {
		return TYPES.get(loc);
	}

	public Type get(String name, String modid) {
		return get(new ResourceLocation(modid, name));
	}


	public static class Type {

		private final String name, modid;
		private final RegistryObject<Item> boat;

		protected Type(String name, String modid, DeferredRegister<Item> register, CreativeModeTab creativeTab) {
			this.name = name;
			this.modid = modid;
			boat = register.register(name+"_boat", () -> new AtlasBoatItem(this, new Item.Properties().tab(creativeTab)));
		}

		public String getName() {
			return name;
		}

		public String getModid() {
			return modid;
		}

		public Item getItem() {
			return boat.get();
		}

		public ResourceLocation getRegistryName() {
			return new ResourceLocation(modid, name);
		}

	}
}

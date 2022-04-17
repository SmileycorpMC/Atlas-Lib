package net.smileycorp.atlas.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.client.entity.AtlasBoatRenderer;
import net.smileycorp.atlas.api.entity.AtlasBoat;
import net.smileycorp.atlas.common.AtlasLib;

public class BoatRegistry  {

	private Map<ResourceLocation, Type> TYPES = new HashMap<ResourceLocation, Type>();

	public static BoatRegistry INSTANCE = new BoatRegistry();

	public static RegistryObject<EntityType<AtlasBoat>> BOAT_ENTITY;

	public Type register(String name, String modid, Supplier<Block> planks) {
		if (TYPES.isEmpty()) { BOAT_ENTITY = AtlasLib.ENTITIES.register("atlas_boat", ()-> EntityType.Builder.<AtlasBoat>of(AtlasBoat::new, MobCategory.MISC)
				.sized(1.375F, 0.5625F).clientTrackingRange(10).build("atlas_boat"));
			if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT) AtlasBoatRenderer.register();
		}
		Type type = new Type(name, modid, planks);
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
		private final Supplier<Block> planks;

		protected Type(String name, String modid, Supplier<Block> planks) {
			this.name = name;
			this.modid = modid;
			this.planks = planks;
		}

		public String getName() {
			return name;
		}

		public String getModid() {
			return modid;
		}

		public Block getPlanks() {
			return planks.get();
		}

		public ResourceLocation getRegistryName() {
			return new ResourceLocation(modid, name);
		}

	}
}

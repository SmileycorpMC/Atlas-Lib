package net.smileycorp.atlas.api;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class BoatRegistry  {

	private Map<ResourceLocation, Type> TYPES = new HashMap<ResourceLocation, Type>();

	public static BoatRegistry INSTANCE = new BoatRegistry();

	public Type register(String name, String modid, Supplier<Block> planks) {
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

	public boolean hasValues() {
		return !TYPES.isEmpty();
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

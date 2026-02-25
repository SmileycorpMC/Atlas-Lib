package net.smileycorp.atlas.api.entity.boat;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.client.RenderAtlasBoat;
import net.smileycorp.atlas.common.AtlasLib;

import java.util.Map;

public class BoatRegistry  {

	private Map<ResourceLocation, Type> TYPES = Maps.newHashMap();

	public static BoatRegistry INSTANCE = new BoatRegistry();

	protected void register(ResourceLocation loc, ItemStack stack) {
		if (TYPES.isEmpty()) {
			EntityRegistry.registerModEntity(new ResourceLocation("atlaslib", "boat"),
					EntityAtlasBoat.class, "boat", 112, AtlasLib.INSTANCE, 60, 1, true);
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT) RenderingRegistry.registerEntityRenderingHandler(EntityAtlasBoat.class, RenderAtlasBoat::new);
		}
		TYPES.put(loc, new Type(loc, stack));
	}

	public Type get(ResourceLocation loc) {
		return TYPES.get(loc);
	}

	public Type get(String name, String modid) {
		return get(new ResourceLocation(modid, name));
	}


	public static class Type {

		private final ResourceLocation loc, texture;
		private final ItemStack stack;
		private final String entityName;

		protected Type(ResourceLocation loc, ItemStack stack) {
			this.loc = loc;
			this.stack = stack;
			this.texture = new ResourceLocation(loc.getResourceDomain(), "textures/entities/boat/" + loc.getResourcePath() +".png");
			this.entityName = "entity." + loc.getResourceDomain() + "." + loc.getResourcePath() + "_boat.name";
		}

		public ResourceLocation getRegistryName() {
			return loc;
		}

		public ItemStack getBoat() {
			return stack.copy();
		}

		public ResourceLocation getTexture() {
			return texture;
		}

		public String getEntityName() {
			return entityName;
		}

	}

}

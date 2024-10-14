package net.smileycorp.atlas.api.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.function.Predicate;

public class FluidModelLoader implements ICustomModelLoader {
	
	private static FluidModelLoader INSTANCE = null;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
		
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation location) {
		return location.getResourcePath().contains("atlasfluid");
	}

	@Override
	public IModel loadModel(ResourceLocation location) throws Exception {
		return new ModelFluid(FluidRegistry.getFluid(location.getResourcePath().split("\\.")[0]));
	}

	public static FluidModelLoader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FluidModelLoader();
			ModelLoaderRegistry.registerLoader(INSTANCE);
		}
		return INSTANCE;
		
	}

}

package net.smileycorp.atlas.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import net.smileycorp.atlas.api.client.FluidModelLoader;
import net.smileycorp.atlas.common.ModDefinitions;

@EventBusSubscriber(value = Side.CLIENT, modid = ModDefinitions.modid)
public class AtlasLibClient {
	
	static FluidModelLoader FLUID_LOADER = new FluidModelLoader();
	
	@SubscribeEvent
	public static void modelRegister(ModelRegistryEvent event) {
		ModelLoaderRegistry.registerLoader(FLUID_LOADER);
	}
}

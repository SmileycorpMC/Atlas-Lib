package net.smileycorp.atlas.api.client.entity;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.entity.AtlasBoat;

public class AtlasBoatRenderer extends BoatRenderer {

	private final Context ctx;

	public AtlasBoatRenderer(Context ctx) {
		super(ctx);
		this.ctx = ctx;
	}

	@Override
	public Pair<ResourceLocation, BoatModel> getModelWithLocation(Boat boat) {
		ResourceLocation registry = ((AtlasBoat) boat).getAtlasType().getRegistryName();
		ResourceLocation loc = new ResourceLocation(registry.getNamespace(), "textures/entity/boat/" + registry.getPath() + ".png");
		return Pair.<ResourceLocation, BoatModel>of(loc, new BoatModel(ctx.bakeLayer(new ModelLayerLocation(loc, "main"))));
	}

	public static void register() {
		System.out.println("[Atlaslib] registering boat renderer");
		EntityRenderers.register(BoatRegistry.BOAT_ENTITY.get(), AtlasBoatRenderer::new);
	}

}

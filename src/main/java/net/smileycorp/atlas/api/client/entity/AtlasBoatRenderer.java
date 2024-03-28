package net.smileycorp.atlas.api.client.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.BoatRegistry.Type;
import net.smileycorp.atlas.api.entity.AtlasBoat;

public class AtlasBoatRenderer extends BoatRenderer {

	private static boolean registered = false;

	private final Context ctx;
	private final boolean hasChest;

	public AtlasBoatRenderer(Context ctx, boolean hasChest) {
		super(ctx, hasChest);
		this.ctx = ctx;
		this.hasChest = hasChest;
	}

	@Override
	public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
		Type type = ((AtlasBoat) boat).getAtlasType();
		ResourceLocation registry = type == null ? new ResourceLocation("oak") : type.getRegistryName();
		ResourceLocation loc = new ResourceLocation(registry.getNamespace(), "textures/entity/boat/" + registry.getPath() + ".png");
		return Pair.<ResourceLocation, ListModel<Boat>>of(loc, hasChest ? new BoatModel(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation("boat/oak"), "main"))) :
				new ChestBoatModel(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation("boat/oak"), "main"))));
	}

	public static void register() {
		System.out.println("[Atlaslib] registering boat renderer");
		EntityRenderers.register(BoatRegistry.BOAT_ENTITY.get(), ctx -> new AtlasBoatRenderer(ctx, false));
		registered = true;
	}

	public static boolean isRegistered() {
		return registered;
	}

}

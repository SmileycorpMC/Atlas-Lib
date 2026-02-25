package net.smileycorp.atlas.api.client;

import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.atlas.api.entity.boat.BoatRegistry;
import net.smileycorp.atlas.api.entity.boat.EntityAtlasBoat;

public class RenderAtlasBoat extends RenderBoat {

    public RenderAtlasBoat(RenderManager rm) {
        super(rm);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBoat entity) {
        BoatRegistry.Type type = ((EntityAtlasBoat)entity).getType();
        return type == null ? super.getEntityTexture(entity) : type.getTexture();
    }

}

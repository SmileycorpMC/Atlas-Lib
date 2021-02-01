package net.smileycorp.atlas.api.client;

import java.awt.Color;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderingUtils {
	
	public static void renderPlanarQuad(BufferBuilder buffer, EnumFacing facing, double x, double y, double z, int layer, Color color, TextureAtlasSprite texture, int lightmapSkyLight, int lightmapBlockLight) {
		Vector4f[] plane = getQuadsFor(facing);
		Vector3f offset = getOffsetFor(facing, x, y, z, layer);
		int rgba = color.getRGB();
		for (int i = 0; i < 4; ++i) {
			Vector4f quadPos = plane[i];
			
			float r = ((rgba & 0xFF0000) >> 16) / 255F;
			float g = ((rgba & 0xFF00) >> 8) / 255F;
			float b = (rgba& 0xFF) / 255F;
			float a = ((rgba & 0xFF000000) >> 24) / 255F;
			
			float u = i < 2 ? texture.getMaxU() - (1F / 16F / 10000) : texture.getMinU() + (1F / 16F / 10000);
			float v = i == 1 || i == 2 ? texture.getMaxV() - (1F / 16F / 10000) : texture.getMinV() + (1F / 16F / 10000);
			
			buffer.pos(quadPos.x + offset.x, quadPos.y + offset.y, quadPos.z + offset.z).color(r, g, b, a).tex(u, v).lightmap(lightmapSkyLight, lightmapBlockLight).endVertex();
		}
	}

	private static Vector3f getOffsetFor(EnumFacing facing, double x, double y, double z, int layer) {
		Vector3f vector = new Vector3f((float)x, (float)y, (float)z);
		switch(facing) {
		case UP:
			vector.translate(0, offsetLayer(1, layer), 0);
			break;
		case DOWN:
			vector.translate(0, offsetLayer(0, -layer), 0);
			break;
		case NORTH:
			vector.translate(0, 0, offsetLayer(0, -layer));
			break;
		case SOUTH:
			vector.translate(0, 0, offsetLayer(1, layer));
			break;
		case EAST:
			vector.translate(offsetLayer(1, layer), 0, 0);
			break;
		case WEST:
			vector.translate(offsetLayer(0, -layer), 0, 0);
			break;
		}
		return vector;
	}

	private static float offsetLayer(float offset, int layer) {
		float layerOffset = 0.0001f*layer;
		return offset+layerOffset;
	}

	private static Vector4f[] getQuadsFor(EnumFacing facing) {
		switch(facing) {
		case DOWN:
			return new Vector4f[] {
					new Vector4f(1, 0, 1, 0),
					new Vector4f(1, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 1, 0)};
		case NORTH:
			return new Vector4f[] {
					new Vector4f(0, 1, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(1, 0, 0, 0),
					new Vector4f(1, 1, 0, 0)};
		case SOUTH:
			return new Vector4f[] {
					new Vector4f(1, 1, 0, 0),
					new Vector4f(1, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 1, 0, 0)};
		case EAST:
			return new Vector4f[] {
					new Vector4f(0, 1, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 1, 0),
					new Vector4f(0, 1, 1, 0)};
		case WEST:
			
			return new Vector4f[] {
					new Vector4f(0, 1, 1, 0),
					new Vector4f(0, 0, 1, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 1, 0, 0)};
		default:
			return new Vector4f[] {
					new Vector4f(1, 0, 0, 0),
					new Vector4f(1, 0, 1, 0),
					new Vector4f(0, 0, 1, 0),
					new Vector4f(0, 0, 0, 0)};
		}
	}
}

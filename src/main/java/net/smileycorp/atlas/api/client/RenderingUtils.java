package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;

public class RenderingUtils {

	public static List<BakedModel> replaceRegisteredModel(Map<ResourceLocation, BakedModel> map, ResourceLocation name, Function<BakedModel, BakedModel> function) {
		List<BakedModel> models = Lists.newArrayList();
		for (Map.Entry<ResourceLocation, BakedModel> entry : map.entrySet()) {
			ResourceLocation loc = entry.getKey();
			if (loc.getNamespace().equals(name.getNamespace()) && loc.getPath().split("#")[0].equals(name.getPath()))
				models.add(function.apply(entry.getValue()));
		}
		BakedModel model = function.apply(map.get(name));
		map.put(name, model);
		return models;
	}

	public static void renderCubeQuad(BufferBuilder buffer, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, Level world, int luminance, BlockPos pos) {
		for (Direction facing : Direction.values()) {
			renderPlanarQuad(buffer, facing, x, y, z, layer, colour, texture, world, luminance, pos);
		}
	}

	public static void renderPlanarQuad(BufferBuilder buffer, Direction facing, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, Level world, int luminance, BlockPos pos) {
		Vector3f[] plane = PlanarQuadRenderer.getQuadsFor(facing);
		Vector3f offset = (layer == 0 ? new Vector3f(0, 0, 0) : PlanarQuadRenderer.getOffsetFor(facing, x, y, z, layer));
		int rgba = colour.getRGB();
		for (int i = 0; i < 4; ++i) {
			Vector3f quadPos = plane[i];

			float r = ((rgba & 0xFF0000) >> 16) / 255F;
			float g = ((rgba & 0xFF00) >> 8) / 255F;
			float b = (rgba& 0xFF) / 255F;
			float a = ((rgba & 0xFF000000) >> 24) / 255F;

			float u = i < 2 ? texture.getU1() - (1F / 16F / 10000) : texture.getU0()+ (1F / 16F / 10000);
			float v = i == 1 || i == 2 ? texture.getV1() - (1F / 16F / 10000) : texture.getV0() + (1F / 16F / 10000);

			buffer.vertex(quadPos.x() + offset.x(), quadPos.y() + offset.y(), quadPos.z() + offset.z()).color(r, g, b, a).uv(u, v).endVertex();
		}
	}

	public static List<BakedQuad> getQuadsForCube(Color colour, TextureAtlasSprite sprite) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		for (Direction facing : Direction.values()) {
			quads.addAll(getQuadsForPlane(facing, colour, sprite));
		}
		return quads;
	}

	public static List<BakedQuad> getQuadsForPlane(Direction facing, Color colour, TextureAtlasSprite sprite) {
		return getQuadsForPlane(facing, colour, sprite, 0);
	}

	public static List<BakedQuad> getQuadsForPlane(Direction facing, Color colour, TextureAtlasSprite sprite, int layer) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		Vector3f[] vecs = PlanarQuadRenderer.getQuadsFor(facing);
		for(int i = 0; i < vecs.length; i++) vecs[i] = PlanarQuadRenderer.getOffsetFor(facing, vecs[i], layer);
		BakedQuadBuilder builder = new BakedQuadBuilder(sprite);
		builder.setQuadOrientation(facing);
		Vector3f normal = new Vector3f(vecs[2].x()-vecs[1].x(), vecs[2].y()-vecs[1].y(), vecs[2].z()-vecs[1].z());
		normal.cross(new Vector3f(vecs[0].x()-vecs[1].x(), vecs[0].y()-vecs[1].y(), vecs[0].z()-vecs[1].z()));
		normal.normalize();
		addVertex(builder, vecs[0].x(), vecs[0].y(), vecs[0].z(), 0, 0, normal, colour, sprite);
		addVertex(builder, vecs[1].x(), vecs[1].y(), vecs[1].z(), 0, sprite.getHeight(), normal, colour, sprite);
		addVertex(builder, vecs[2].x(), vecs[2].y(), vecs[2].z(), sprite.getWidth(), sprite.getHeight(), normal, colour, sprite);
		addVertex(builder, vecs[3].x(), vecs[3].y(), vecs[3].z(), sprite.getWidth(), 0, normal, colour, sprite);
		quads.add(builder.build());
		return quads;
	}

	private static void addVertex(BakedQuadBuilder builder, float x, float y, float z, float u, float v, Vector3f normal, Color colour, TextureAtlasSprite sprite) {
		for (int i = 0; i < builder.getVertexFormat().getElements().size(); i++) {
			VertexFormatElement element = builder.getVertexFormat().getElements().get(i);
			switch (element.getUsage()) {
			case POSITION:
				builder.put(i, x, y, z, 1);
				break;
			case COLOR:
				builder.put(i, colour.getRed()/255f, colour.getGreen()/255f, colour.getBlue()/255f, 1f);
				break;
			case UV:
				switch (element.getIndex()) {
				case 0:
					builder.put(i, sprite.getU(u), sprite.getV(v));
					break;
				case 2:
					builder.put(i, 0);
					break;
				default:
					builder.put(i);
					break;
				}
				break;
			case NORMAL:
				builder.put(i, normal.x(), normal.y(), normal.z());
				break;
			default:
				builder.put(i);
				break;
			}
		}
	}

	@Deprecated
	public static ResourceLocation getPlayerTexture(Optional<UUID> uuid, Type type) {
		return PlayerTextureRenderer.getTexture(uuid, type);
	}

	@Deprecated
	public static String getSkinType(Optional<UUID> uuid) {
		return PlayerTextureRenderer.getSkinType(uuid);
	}
}

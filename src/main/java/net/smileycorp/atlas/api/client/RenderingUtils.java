package net.smileycorp.atlas.api.client;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
		Vec3[] plane = PlanarQuadRenderer.getQuadsFor(facing);
		Vec3 offset = (layer == 0 ? new Vec3(0, 0, 0) : PlanarQuadRenderer.getOffsetFor(facing, x, y, z, layer));
		int rgba = colour.getRGB();
		for (int i = 0; i < 4; ++i) {
			Vec3 quadPos = plane[i];

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
		List<BakedQuad> quads = Lists.newArrayList();
		Vec3[] vecs = PlanarQuadRenderer.getQuadsFor(facing);
		//for(int i = 0; i < vecs.length; i++) vecs[i] = PlanarQuadRenderer.getOffsetFor(facing, vecs[i], layer);
		QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(quads::add);
		builder.setSprite(sprite);
		builder.setDirection(facing);
		Vec3 normal = new Vec3(vecs[2].x()-vecs[1].x(), vecs[2].y()-vecs[1].y(), vecs[2].z()-vecs[1].z());
		normal.cross(new Vec3(vecs[0].x()-vecs[1].x(), vecs[0].y()-vecs[1].y(), vecs[0].z()-vecs[1].z()));
		normal.normalize();
		addVertex(builder, (float)vecs[0].x(), (float)vecs[0].y(), (float)vecs[0].z(), 0, 0, normal, colour, sprite);
		addVertex(builder, (float)vecs[1].x(), (float)vecs[1].y(), (float)vecs[1].z(), 0, sprite.getY(), normal, colour, sprite);
		addVertex(builder, (float)vecs[2].x(), (float)vecs[2].y(), (float)vecs[2].z(), sprite.getX(), sprite.getY(), normal, colour, sprite);
		addVertex(builder, (float)vecs[3].x(), (float)vecs[3].y(), (float)vecs[3].z(), sprite.getX(), 0, normal, colour, sprite);
		return quads;
	}

	private static void addVertex(VertexConsumer builder, float x, float y, float z, float u, float v, Vec3 normal, Color colour, TextureAtlasSprite sprite) {
		builder.vertex(x, y, z)
				.uv(u, v)
				.uv2(0, 0)
				.color(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha())
				.normal((float) normal.x(), (float) normal.y(), (float) normal.z())
				.endVertex();
	}

	public static void drawText(PoseStack stack, Component text, float x, float y, int colour, boolean hasShadow) {
		Minecraft mc = Minecraft.getInstance();
		mc.font.drawInBatch(text, x, y, colour, hasShadow, stack.last().pose(), mc.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL,0, 15728880);
	}

	public static void drawText(PoseStack stack, String text, float x, float y, int colour, boolean hasShadow) {
		Minecraft mc = Minecraft.getInstance();
		mc.font.drawInBatch(text, x, y, colour, hasShadow, stack.last().pose(), mc.renderBuffers().bufferSource(), Font.DisplayMode.NORMAL,0, 15728880);
	}

}

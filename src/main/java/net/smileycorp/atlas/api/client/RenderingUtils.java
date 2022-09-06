package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RenderingUtils {

	public static BakedModel replaceRegisteredModel(Map<ResourceLocation, BakedModel> map, ResourceLocation name, Function<BakedModel, BakedModel> function) {
		BakedModel model = function.apply(map.get(name));
		map.put(name, model);
		return model;
	}

	public static void renderCubeQuad(BufferBuilder buffer, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, Level world, int luminance, BlockPos pos) {
		for (Direction facing : Direction.values()) {
			renderPlanarQuad(buffer, facing, x, y, z, layer, colour, texture, world, luminance, pos);
		}
	}

	public static void renderPlanarQuad(BufferBuilder buffer, Direction facing, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, Level world, int luminance, BlockPos pos) {
		Vector4f[] plane = PlanarQuadRenderer.getQuadsFor(facing);
		Vector3f offset = (layer == 0 ? new Vector3f(0, 0, 0) : PlanarQuadRenderer.getOffsetFor(facing, x, y, z, layer));
		int rgba = colour.getRGB();
		for (int i = 0; i < 4; ++i) {
			Vector4f quadPos = plane[i];

			float r = ((rgba & 0xFF0000) >> 16) / 255F;
			float g = ((rgba & 0xFF00) >> 8) / 255F;
			float b = (rgba& 0xFF) / 255F;
			float a = ((rgba & 0xFF000000) >> 24) / 255F;

			float u = i < 2 ? texture.getU1() - (1F / 16F / 10000) : texture.getU0()+ (1F / 16F / 10000);
			float v = i == 1 || i == 2 ? texture.getV1() - (1F / 16F / 10000) : texture.getV0() + (1F / 16F / 10000);

			buffer.vertex(quadPos.x() + offset.x(), quadPos.y() + offset.y(), quadPos.z() + offset.z()).color(r, g, b, a).uv(u, v).endVertex();
		}
	}

	/*public static List<BakedQuad> getQuadsForCube(Color colour, String spritename) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		for (Direction facing : Direction.VALUES) {
			quads.addAll(getQuadsForPlane(facing, colour, spritename));
		}
		return quads;
	}

	public static List<BakedQuad> getQuadsForPlane(Direction facing, Color colour, String spritename) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		FaceBakery bakery = new FaceBakery();
		Vector3f[] vecs = PlanarQuadRenderer.get3FQuadsFor(facing);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(spritename);
		BlockPartFace face = new BlockPartFace(null, 0, spritename, new BlockFaceUV(new float[]{0, 0, 16, 16}, 0));
		quads.add(bakery.makeBakedQuad(vecs[0], vecs[1], face, sprite, facing.getOpposite(), ModelRotation.X0_Y0, null, true, true));
		return quads;
	}

	public static void renderFont(String text, Vec3d fontvector, int color) {
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer fontrenderer = mc.fontRenderer;
		//fontrenderer.drawString(text, x, y, color);
	} */

	public static ResourceLocation getPlayerTexture(Optional<UUID> uuid, Type type) {
		return PlayerTextureRenderer.getTexture(uuid, type);
	}


	private static class PlanarQuadRenderer {

		private static Vector3f getOffsetFor(Direction facing, double x, double y, double z, int layer) {
			Vector3f vector = new Vector3f((float)x, (float)y, (float)z);
			switch(facing) {
			case UP:
				vector.add(0, offsetLayer(1, layer), 0);
				break;
			case DOWN:
				vector.add(0, offsetLayer(0, -layer), 0);
				break;
			case NORTH:
				vector.add(0, 0, offsetLayer(0, -layer));
				break;
			case SOUTH:
				vector.add(0, 0, offsetLayer(1, layer));
				break;
			case EAST:
				vector.add(offsetLayer(1, layer), 0, 0);
				break;
			case WEST:
				vector.add(offsetLayer(0, -layer), 0, 0);
				break;
			}
			return vector;
		}

		@SuppressWarnings("unused")
		private static Vector3f[] get3FQuadsFor(Direction facing) {
			if (facing!=null) {
				switch(facing) {
				case DOWN:
					return new Vector3f[] {
							new Vector3f(0, 0, 16),
							new Vector3f(16, 0, 0)};
				case NORTH:
					return new Vector3f[] {
							new Vector3f(16, 0, 0),
							new Vector3f(0, 16, 0)};
				case SOUTH:
					return new Vector3f[] {
							new Vector3f(16, 0, 16),
							new Vector3f(0, 16, 16)};
				case EAST:
					return new Vector3f[] {
							new Vector3f(16, 0, 16),
							new Vector3f(16, 16, 0)};
				case WEST:
					return new Vector3f[] {
							new Vector3f(0, 0, 16),
							new Vector3f(0, 16, 0)};
				default:
					return new Vector3f[] {
							new Vector3f(0, 16, 16),
							new Vector3f(16, 16, 0)};
				}
			}
			return new Vector3f[] {
					new Vector3f(0, 0, 0),
					new Vector3f(0, 0, 0)};
		}

		private static float offsetLayer(float offset, int layer) {
			float layerOffset = 0.001f*layer;
			return offset+layerOffset;
		}

		private static Vector4f[] getQuadsFor(Direction facing) {
			if (facing!=null) {
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
			return new Vector4f[] {
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0)};
		}
	}

	private static class PlayerTextureRenderer {

		private static final Map<UUID, GameProfile> PROFILES = Maps.newHashMap();

		private static ResourceLocation getTexture(Optional<UUID> optional, Type type) {
			if (optional.isEmpty()) {
				switch (type) {
				case SKIN:
					return DefaultPlayerSkin.getDefaultSkin();
				case ELYTRA:
					return new ResourceLocation("textures/entity/elytra.png");
				default:
					return null;
				}
			}
			UUID uuid = optional.get();
			PlayerInfo playerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
			if (playerinfo != null) switch (type) {
			case SKIN:
				return playerinfo.getSkinLocation();
			case CAPE:
				return playerinfo.getCapeLocation();
			case ELYTRA:
				return playerinfo.getElytraLocation();
			}

			Minecraft mc = Minecraft.getInstance();
			GameProfile profile;
			if (PROFILES.containsKey(uuid)) profile = PROFILES.get(uuid);
			else {
				profile = new GameProfile(uuid, null);
				mc.getMinecraftSessionService().fillProfileProperties(profile, true);
				PROFILES.put(uuid, profile);
			}
			SkinManager manager = mc.getSkinManager();
			Map<Type, MinecraftProfileTexture> textures = manager.getInsecureSkinInformation(profile);
			if (textures.containsKey(type)) return manager.registerTexture(textures.get(type), type);
			switch (type) {
			case SKIN:
				return DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(profile));
			case ELYTRA:
				return new ResourceLocation("textures/entity/elytra.png");
			default:
				return null;
			}
		}
	}
}

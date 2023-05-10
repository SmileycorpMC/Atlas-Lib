package net.smileycorp.atlas.api.client;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

public class PlayerTextureRenderer {

	private static final Map<UUID, GameProfile> PROFILES = Maps.newHashMap();

	public static ResourceLocation getTexture(Optional<UUID> optional, Type type) {
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
			ResourceLocation loc = playerinfo.getElytraLocation();
			return loc == null ? playerinfo.getCapeLocation() : loc;
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
		if (type == Type.ELYTRA && textures.containsKey(Type.CAPE)) return manager.registerTexture(textures.get(Type.CAPE), Type.CAPE);
		switch (type) {
		case SKIN:
			return DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(profile));
		case ELYTRA:
			return new ResourceLocation("textures/entity/elytra.png");
		default:
			return null;
		}
	}

	public static ResourceLocation getTexture(String username, Type type) {
		return getTexture(getUUID(username), type);
	}

	public static String getSkinType(Optional<UUID> optional) {
		if (optional.isEmpty()) return "default";
		UUID uuid = optional.get();
		PlayerInfo playerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
		if (playerinfo != null) return playerinfo.getModelName();
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
		if (textures.containsKey(Type.SKIN)) return textures.get(Type.SKIN).getMetadata("model");
		return DefaultPlayerSkin.getSkinModelName(UUIDUtil.getOrCreatePlayerUUID(profile));
	}

	public static String getSkinType(String username) {
		return getSkinType(getUUID(username));
	}

	public static Optional<UUID> getUUID(String username) {
		for(Entry<UUID, GameProfile> entry : PROFILES.entrySet()) {
			if (entry.getValue().getName().equals(username)) return Optional.of(entry.getKey());
		}
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			URI uri = new URI("https://api.mojang.com/users/profiles/minecraft/" + username);
			String message = EntityUtils.toString(client.execute(new HttpGet(uri)).getEntity());
			JsonObject json = (JsonObject) JsonParser.parseString(message);
			return Optional.of(UUID.fromString(json.get("id").getAsString()));
		} catch (Exception e) {}
		return Optional.empty();
	}

}
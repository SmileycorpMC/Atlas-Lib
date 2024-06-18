package net.smileycorp.atlas.api.client;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

public class PlayerTextureRenderer {

	private static final Map<UUID, GameProfile> PROFILES = Maps.newHashMap();

	public static ResourceLocation getTexture(Optional<UUID> optional, Type type) {
		if (optional.isEmpty()) {
			switch (type) {
			case SKIN:
				return DefaultPlayerSkin.getDefaultTexture();
			case ELYTRA:
				return ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
			default:
				return null;
			}
		}
		UUID uuid = optional.get();
		PlayerInfo playerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
		if (playerinfo != null) switch (type) {
		case SKIN:
			return playerinfo.getSkin().texture();
		case CAPE:
			return playerinfo.getSkin().capeTexture();
		case ELYTRA:
			ResourceLocation loc = playerinfo.getSkin().elytraTexture();
			return loc == null ? playerinfo.getSkin().capeTexture() : loc;
		}

		Minecraft mc = Minecraft.getInstance();
		GameProfile profile;
		if (PROFILES.containsKey(uuid)) profile = PROFILES.get(uuid);
		else {
			profile = mc.getMinecraftSessionService().fetchProfile(uuid, true).profile();
			PROFILES.put(uuid, profile);
		}
		SkinManager manager = mc.getSkinManager();
		PlayerSkin textures = manager.getInsecureSkin(profile);
		switch (type) {
		case SKIN:
			return textures.texture() != null ? textures.texture() : DefaultPlayerSkin.getDefaultTexture();
		case CAPE:
			return textures.capeTexture();
		case ELYTRA:
			return textures.elytraTexture() != null ? textures.elytraTexture() : textures.capeTexture() != null ? textures.capeTexture()
					: ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
		default:
			return null;
		}
	}

	public static ResourceLocation getTexture(String username, Type type) {
		return getTexture(getUUID(username), type);
	}

	public static PlayerSkin.Model getSkinType(Optional<UUID> optional) {
		if (optional.isEmpty()) return PlayerSkin.Model.WIDE;
		UUID uuid = optional.get();
		PlayerInfo playerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
		if (playerinfo != null) return playerinfo.getSkin().model();
		Minecraft mc = Minecraft.getInstance();
		GameProfile profile;
		if (PROFILES.containsKey(uuid)) profile = PROFILES.get(uuid);
		else {
			profile = mc.getMinecraftSessionService().fetchProfile(uuid, true).profile();
			PROFILES.put(uuid, profile);
		}
		SkinManager manager = mc.getSkinManager();
		PlayerSkin textures = manager.getInsecureSkin(profile);
		return textures.model();
	}

	public static PlayerSkin.Model getSkinType(String username) {
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
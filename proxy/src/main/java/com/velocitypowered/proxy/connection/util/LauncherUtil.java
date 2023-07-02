/*
 * Copyright (C) 2018-2023 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.connection.util;

import com.velocitypowered.api.util.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pro.gravit.launcher.Launcher;
import pro.gravit.launcher.profiles.PlayerProfile;
import pro.gravit.launcher.profiles.Texture;
import pro.gravit.utils.helper.SecurityHelper;

/**
 * LauncherUtil.
 */
public class LauncherUtil {
  /**
   * LauncherUtil#makeGameProfile.
   */
  public static GameProfile makeGameProfile(PlayerProfile profile) {
    List<GameProfile.Property> properties = new ArrayList<>();

    for (Map.Entry<String, String> e : profile.properties.entrySet()) {
      properties.add(new GameProfile.Property(e.getKey(), e.getValue(), ""));
    }

    GameProfileTextureProperties textureProperty = new GameProfileTextureProperties();
    textureProperty.profileId = profile.uuid.toString().replace("-", "");
    textureProperty.profileName = profile.username;
    textureProperty.timestamp = System.currentTimeMillis();

    for (Map.Entry<String, Texture> texture : profile.assets.entrySet()) {
      textureProperty.textures.put(texture.getKey(),
              new GameProfileTextureProperties.GameTexture(texture.getValue()));
    }

    properties.add(new GameProfile.Property("textures", Base64.getEncoder().encodeToString(
            Launcher.gsonManager.gson.toJson(textureProperty)
                    .getBytes(StandardCharsets.UTF_8)), ""));

    return new GameProfile(profile.uuid, profile.username, properties);
  }

  /**
   * GameProfileTextureProperties.
   */
  public static class GameProfileTextureProperties {
    public long timestamp;
    public String profileId;
    public String profileName;
    public Map<String, GameTexture> textures = new HashMap<>();

    /**
     * GameProfileTextureProperties#GameTexture.
     */
    public static class GameTexture {
      public String url;
      public String hash;
      public Map<String, String> metadata;

      /**
       * GameTexture.
       */
      public GameTexture(Texture texture) {
        this.url = texture.url;
        this.hash = texture.digest == null ? null : SecurityHelper.toHex(texture.digest);
        this.metadata = texture.metadata;
      }
    }
  }
}

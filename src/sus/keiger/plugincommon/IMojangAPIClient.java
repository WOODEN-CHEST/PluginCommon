package sus.keiger.plugincommon;

import sus.keiger.plugincommon.player.PlayerSkin;

import java.util.UUID;

public interface IMojangAPIClient
{
    // Methods.
    PlayerSkin GetSkin(String playerName);
    PlayerSkin GetSkin(UUID playerUUID);
}
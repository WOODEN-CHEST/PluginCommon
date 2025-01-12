package sus.keiger.plugincommon;

import org.bukkit.Bukkit;
import sus.keiger.plugincommon.player.PlayerSkin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class CachedMojangAPIClient implements IMojangAPIClient
{
    // Private fields.
    private final MojangAPISkinRetriever _skinRetreiver;
    private final Map<UUID, PlayerSkin> _cachedSkins = new HashMap<>();


    // Constructors.
    public CachedMojangAPIClient(Logger logger)
    {
        _skinRetreiver = new MojangAPISkinRetriever(logger);
    }


    // Methods.
    public void ClearCache()
    {
        _cachedSkins.clear();
    }


    // Inherited methods.
    @Override
    public PlayerSkin GetSkin(String playerName)
    {
        Objects.requireNonNull(playerName, "playerName is null");
        return GetSkin(Bukkit.getPlayerUniqueId(playerName));
    }

    @Override
    public PlayerSkin GetSkin(UUID playerUUID)
    {
        Objects.requireNonNull(playerUUID, "playerUUID is null");

        PlayerSkin CachedSkin = _cachedSkins.get(playerUUID);
        if (CachedSkin != null)
        {
            return CachedSkin;
        }

        PlayerSkin LoadedSkin = _skinRetreiver.GetSkin(playerUUID);
        if (LoadedSkin != null)
        {
            _cachedSkins.put(playerUUID, LoadedSkin);
        }
        return LoadedSkin;
    }
}
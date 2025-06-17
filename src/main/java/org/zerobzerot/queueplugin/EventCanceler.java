package org.zerobzerot.queueplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.google.common.collect.Lists;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventCanceler extends PacketAdapter implements Listener {

    public EventCanceler(JavaPlugin plugin) {
        super(plugin, getAllPacketTypes());
    }

    private static Iterable<? extends PacketType> getAllPacketTypes() {
        final Set<PacketType> types = new HashSet<>();
        types.addAll(PacketType.Play.Client.getInstance().values().stream().filter(PacketType::isSupported).collect(Collectors.toSet()));
        types.addAll(PacketType.Play.Server.getInstance().values().stream().filter(PacketType::isSupported).collect(Collectors.toSet()));
        return types;
    }

    @Override
    public void onPacketReceiving(PacketEvent ev) {
        if (ev.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) return;
        ev.setCancelled(true);
    }

    @Override
    public void onPacketSending(PacketEvent ev) {
        // these are needed or a notch-ian client will not join
        if (ev.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) return;
        if (ev.getPacketType() == PacketType.Play.Server.LOGIN) return;
        if (ev.getPacketType() == PacketType.Play.Server.POSITION) return;
        if (ev.getPacketType() == PacketType.Play.Server.GAME_STATE_CHANGE) return;

        // for chat notifications
        if (ev.getPacketType() == PacketType.Play.Server.CHAT) return;

        // this keeps the player from falling (flying ability)
        if (ev.getPacketType() == PacketType.Play.Server.ABILITIES) return;

        // this keeps clients from showing server as lagging
        if (ev.getPacketType() == PacketType.Play.Server.UPDATE_TIME) return;

        // if we don't send this, player has default skin lol
        if (ev.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
            List<PlayerInfoData> newInfoData = Lists.newArrayList();

            // only show the player in the tab-list
            for (PlayerInfoData infoData : ev.getPacket().getPlayerInfoDataLists().read(1)) {
                if (ev.getPlayer().getUniqueId() != infoData.getProfileId())
                    continue;

                newInfoData.add(new PlayerInfoData(
                    infoData.getProfile(),
                    infoData.getLatency(),
                    EnumWrappers.NativeGameMode.SPECTATOR,
                    infoData.getDisplayName()));
            }

            ev.getPacket().getPlayerInfoDataLists().write(1, newInfoData);
            return;
        }

        ev.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent ev) {
        if (ev.getEntityType() == EntityType.PLAYER) return;
        ev.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onChunkSave(ChunkUnloadEvent ev) {
        ev.setSaveChunk(false);
    }

}

package org.cloudanarchy.queueplugin;


import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public final class QueuePlugin extends JavaPlugin implements Listener {

    private void process(@NotNull Player player) {
        player.teleport(new Location(getServer().getWorlds().get(0), 0, 140, 0));
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setGameMode(GameMode.SPECTATOR);
        for (Player p : getServer().getOnlinePlayers()) {
            if (player.equals(p)) continue;
            player.hidePlayer(this, p);
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ev.joinMessage(Component.empty());
        process(ev.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        ev.setRespawnLocation(new Location(getServer().getWorlds().get(0), 0, 140, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                process(ev.getPlayer());
            }
        }.runTaskLater(this, 1);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent ev) {
        ev.quitMessage(Component.empty());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent ev) {
        if (ev.getEntityType() == EntityType.PLAYER) return;
        ev.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent ev) {
        ev.message(Component.empty());
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRecipeDiscover(PlayerRecipeDiscoverEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerStatisticIncrement(PlayerStatisticIncrementEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent ev) {
        ev.setCancelled(true);
    }

}

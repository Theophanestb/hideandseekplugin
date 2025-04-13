package com.petitpastis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.petitpastis.Plugin;

public class BlockListener implements Listener {

    private Plugin plugin;

    public BlockListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (plugin.canBuild) return;
        event.getPlayer().sendMessage("§cTouche pas à ça ou tu vas finir très mal mon copain");
        event.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak(BlockPlaceEvent event) {
        if (plugin.canBuild) return;
        event.setCancelled(true);
    }
}

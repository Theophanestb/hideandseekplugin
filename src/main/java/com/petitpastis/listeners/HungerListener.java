package com.petitpastis.listeners;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.petitpastis.Plugin;

public class HungerListener implements Listener {

    private Plugin plugin;

    public HungerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) 
    {
        event.setCancelled(true); 
    }

    @EventHandler
    public void DropItemEvent(PlayerDropItemEvent event) 
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void CantMoveEvent(PlayerMoveEvent event) 
    {
        Player player = event.getPlayer();
        if (plugin.isGamePaused || (plugin.isSeeker(player) && plugin.seekerWaiting))
            event.setCancelled(true);
    }

    private static final Set<Material> BLOCKED_BLOCKS = EnumSet.of(
            Material.OAK_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,
            Material.IRON_DOOR,

            Material.OAK_TRAPDOOR,
            Material.SPRUCE_TRAPDOOR,
            Material.BIRCH_TRAPDOOR,
            Material.JUNGLE_TRAPDOOR,
            Material.ACACIA_TRAPDOOR,
            Material.DARK_OAK_TRAPDOOR,
            Material.IRON_TRAPDOOR,
            Material.PALE_OAK_TRAPDOOR,

            Material.OAK_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE
    );

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        // Si le bloc cliqué est dans la liste des blocs interdits, on annule l'interaction
        if (BLOCKED_BLOCKS.contains(clickedBlock.getType())) {
            event.setCancelled(true);
        }
    }
}

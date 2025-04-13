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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

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

        if (BLOCKED_BLOCKS.contains(clickedBlock.getType())) {
            event.setCancelled(true);
        }
    }

        @EventHandler
    public void onArmorClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        // Interdit de retirer l'armure
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }

        // Empêche les clics sur l'armure avec shift-click ou drag
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && isArmor(currentItem.getType())) {
            if (event.getClick().isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        // Annule le drop d'item
        event.setCancelled(true);
    }

    private boolean isArmor(Material material) {
        String name = material.name().toLowerCase();
        return name.contains("Head") || name.contains("helmet") || name.contains("head") || name.contains("boots");
    }
}

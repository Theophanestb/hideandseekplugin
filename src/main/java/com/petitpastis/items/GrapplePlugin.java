package com.petitpastis.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.petitpastis.Plugin;

public class GrapplePlugin implements Listener {

    Plugin plugin;
    public GrapplePlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public static ItemStack getGrappleItem() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Grapple Rod");
        meta.setLore(List.of(ChatColor.GRAY + "Click-droit pour te propulser !", ChatColor.RED + "Cooldown: 60s"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        // Assure qu'on a bien un item et que c’est la canne custom
        if (item == null || item.getType() != Material.FISHING_ROD) return;
        if (!item.hasItemMeta() || !Objects.equals(item.getItemMeta().getDisplayName(), ChatColor.AQUA + "Grapple Rod"))
            return;

        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(uuid)) {
            long lastUse = cooldowns.get(uuid);
            if (currentTime - lastUse < 60_000) {
                long secondsLeft = (60_000 - (currentTime - lastUse)) / 1000;
                player.sendMessage(ChatColor.RED + "Tu dois attendre encore " + secondsLeft + " secondes !");
                return;
            }
        }

        // Appliquer la propulsion
        Vector direction = player.getLocation().getDirection().normalize().multiply(5);
        direction.setY(4);
        player.setVelocity(direction);
        player.sendMessage(ChatColor.GREEN + "Woush !");
        cooldowns.put(uuid, currentTime);
    }
}

package com.petitpastis.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.petitpastis.Plugin;

public class ItemsListener implements Listener {

    Plugin plugin;
    public ItemsListener(Plugin plugin) {
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
    public void onInteract(PlayerInteractEvent event) 
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
            return;

        String name = item.getItemMeta().getDisplayName();
        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // === GRAPPLE ROD ===
        if (item.getType() == Material.FISHING_ROD && name.equals(ChatColor.AQUA + "Grapple Rod")) {
            if (cooldowns.containsKey(uuid)) {
                long lastUse = cooldowns.get(uuid);
                if (currentTime - lastUse < 60_000) {
                    long secondsLeft = (60_000 - (currentTime - lastUse)) / 1000;
                    player.sendMessage(ChatColor.RED + "Tu dois attendre encore " + secondsLeft + " secondes !");
                    return;
                }
            }

            // Appliquer la propulsion
            Vector direction = player.getLocation().getDirection().normalize().multiply(8);
            direction.setY(3);
            player.setVelocity(direction);
            //player.sendMessage(ChatColor.GREEN + "Woush !");
            cooldowns.put(uuid, currentTime);
        }

        // === BATON DE TRANSPOSITION ===
        else if (item.getType() == Material.BLAZE_ROD && name.equals(ChatColor.LIGHT_PURPLE + "Bâton de Transposition")) {
            

            // Cherche un joueur ciblé dans un rayon de 50 blocs
            Player target = getTargetPlayer(player);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "Aucun joueur visé !");
                return;
            }

            player.getInventory().remove(item);

            Location loc1 = player.getLocation();
            Location loc2 = target.getLocation();

            player.teleport(loc2);
            target.teleport(loc1);

            player.getWorld().spawnParticle(Particle.PORTAL, loc1, 50);
            target.getWorld().spawnParticle(Particle.PORTAL, loc2, 50);
            player.getWorld().playSound(loc1, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            target.getWorld().playSound(loc2, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

            player.sendMessage(ChatColor.LIGHT_PURPLE + "Échange de position effectué avec " + target.getName() + " !");
            target.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " a échangé sa position avec toi !");

            // Consomme le bâton
            item.setAmount(item.getAmount() - 1);
            cooldowns.put(uuid, currentTime);
        }
    }

    private Player getTargetPlayer(Player player) 
    {
        for (Player target : Bukkit.getOnlinePlayers()) 
        {
            if (target != player && isInLineOfSight(player, target)) 
            {
                return target;
            }
        }
        return null;
    }

    private boolean isInLineOfSight(Player player, Player target) 
    {
        Location playerLocation = player.getEyeLocation();
        Location targetLocation = target.getLocation();
        Vector directionToTarget = targetLocation.toVector().subtract(playerLocation.toVector()).normalize();
        Vector playerDirection = player.getLocation().getDirection().normalize();
        double angle = playerDirection.angle(directionToTarget);
        return angle < Math.toRadians(30) && playerLocation.distance(targetLocation) < 400;
    }
}

package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import com.petitpastis.Plugin;
import com.petitpastis.enums.States;

import net.md_5.bungee.api.ChatColor;

public class DamageListener  implements org.bukkit.event.Listener{
    
    private Plugin plugin;

    public DamageListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() == null) return;
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL && player.getInventory().getBoots() != null) 
        {
            event.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().setBoots(null);
            }, 1L);
            
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5f, 1);
            player.sendMessage(org.bukkit.ChatColor.RED + "Vos bottes ont amortis votre chute mais ont disparues par la même occasion !");
        }
        else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL && plugin.isHider(player))
        {
            event.setCancelled(true);
            double damages = event.getDamage();
            if (damages / 2.5 > player.getHealth())
            {
                player.setHealth(0);
            } 
            else 
            {
                player.setHealth(player.getHealth() - (damages / 2.5));
                player.damage(0.01);
            }
        }
        else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL && plugin.isSeeker(player)) {
            event.setCancelled(true);
            double damages = event.getDamage();
            if (damages / 3 > player.getHealth())
            {
                player.setHealth(0);
            } 
            else 
            {
                player.setHealth(player.getHealth() - (damages / 3));
                player.damage(0.01);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) 
        {
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (plugin.getState() == States.WAITING)
            {
                event.setCancelled(true);
            } 
            else if (plugin.getState() == States.PAUSED) 
            {
                event.setCancelled(true);
            } 
            else if (plugin.getState() == States.PLAYING) 
            {
                if (plugin.isHider(victim) && damager instanceof Player && plugin.isSeeker(damager))
                {
                    MoveToSeeker(victim);
                    victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                    Bukkit.broadcastMessage(ChatColor.AQUA +victim.getName() +ChatColor.DARK_RED+ " s'est fait avoir par " +ChatColor.LIGHT_PURPLE+ damager.getName());
                    damager.setExp(damager.getLevel() + 1);
                    if (plugin.getSeekers().size() == 1)
                    {
                        launchFirework(victim);
                    }
                    plugin.isGameOver();
                }
                victim.damage(0);
                victim.setHealth(victim.getMaxHealth());
            }
        }
    }

    

    public void MoveToSeeker(Player player)
    {
        plugin.addSeeker(player);
    }

    public void launchFirework(Player victim) 
    {
        Firework firework = (Firework) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.RED)
                .withFade(Color.BLUE)
                .with(FireworkEffect.Type.BURST)
                .build();
        
        meta.addEffect(effect);
        meta.setPower(1);  // L'intensité du feu d'artifice (1 à 3)
        firework.setFireworkMeta(meta);
    }
}

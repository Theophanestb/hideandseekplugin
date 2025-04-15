package com.petitpastis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.petitpastis.enums.States;
import com.petitpastis.items.ItemsListener;

public class GameLauncher extends BukkitRunnable {

    private Plugin plugin;
    private int timer = 5;

    public GameLauncher(Plugin plugin) 
    {
        this.plugin = plugin;
    }

    @Override
    public void run() 
    {
        for(Player player : plugin.getPlayers())
        {
            player.setLevel(timer);
            if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1)
            {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_WOOD_PLACE, 0.3f, 1.0f);
            }
        }

        if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1)
        {
            Bukkit.broadcastMessage("§7§oça lance dans " + timer + "s");
        }

        if (timer == 0)
        {
            plugin.setState(States.PLAYING);
            TeleportToSpawns();
            cancel();
        }
        timer--;
    }

    private void TeleportToSpawns() 
    {
        for (Player player : plugin.getSeekers())
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1, false, false));
            player.getInventory().clear();
            player.getInventory().addItem(ItemsListener.getGrappleItem());
            player.teleport(plugin.getSeekerSpawn());
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 9, false, false));
            plugin.seekerWaiting = true;
            player.setInvulnerable(true);
            plugin.givePiglinHeadToPlayer(player);
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.seekerWaiting = false;
                    player.setInvulnerable(false);
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Le seeker est maintenant libre !");
                }
            }, 600L);
            
        }
        for (Player player : plugin.getHiders())
        {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999999, 1, false, false));
            player.getInventory().clear();
            player.setInvulnerable(false);
            player.teleport(plugin.getHiderSpawn());
        }
    }

}

package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.petitpastis.Plugin;
import com.petitpastis.enums.States;

public class PlayerListener implements Listener
{
    private Plugin plugin;

    public PlayerListener(Plugin plugin) 
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) 
    {
        Player player = event.getPlayer();

        World world = player.getWorld();
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        
        if (plugin.spawn == null)
        {
            plugin.spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
            if (plugin.spawn == null)
            {
                plugin.spawn = new org.bukkit.Location(Bukkit.getServer().getWorld("world"), -60, -60, -60);
            }
            player.teleport(plugin.spawn);
        }
        plugin.addPlayers(player);
        player.getInventory().clear();
        if (plugin.getState() == States.PLAYING)
        {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }
        player.setGameMode(GameMode.SURVIVAL);
        
        player.setInvulnerable(true);

        if (plugin.invisibleNameTeam.hasEntry(player.getName())) {
            plugin.invisibleNameTeam.removeEntry(player.getName());
        }
        
        player.setDisplayName(ChatColor.WHITE + player.getName());
        player.setPlayerListName(ChatColor.WHITE + player.getName());

        ItemStack compass = new ItemStack(Material.CLOCK);
        if (!plugin.isRandomize())
        {
            player.getInventory().setItem(4,compass);
        }
        player.sendMessage(ChatColor.YELLOW + "Clique sur la boussole pour choisir ton camp !");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) 
    {
        Player player = event.getPlayer();
        plugin.removePlayers(player);
        
        //plugin.clearTeams(player);
        if (plugin.getState() == States.PLAYING)
        {
            plugin.isGameOver();
        }
    }
}

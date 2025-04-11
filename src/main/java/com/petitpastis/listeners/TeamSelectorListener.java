package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.petitpastis.Plugin;

public class TeamSelectorListener implements Listener {

    private Plugin plugin;

    public TeamSelectorListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.CLOCK) {
            event.setCancelled(true);
            openTeamSelectorMenu(player);
            
        }
    }

    // Ouvre un menu d'équipe avec de la laine verte et rouge
    private void openTeamSelectorMenu(Player player) {
        Inventory inventory = plugin.getServer().createInventory(null, 9, ChatColor.GOLD + "Choisis ton équipe");

        // Crée l'élément laine verte pour les hiders
        ItemStack greenWool = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta greenMeta = greenWool.getItemMeta();
        greenMeta.setDisplayName(ChatColor.AQUA + "Rejoindre les Hiders");
        greenWool.setItemMeta(greenMeta);

        // Crée l'élément laine rouge pour les seekers
        ItemStack redWool = new ItemStack(Material.PIGLIN_HEAD);
        ItemMeta redMeta = redWool.getItemMeta();
        redMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Rejoindre les Seekers");
        redWool.setItemMeta(redMeta);

        // Ajoute les éléments dans le menu
        inventory.setItem(3, greenWool);
        inventory.setItem(5, redWool);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Vérifie si c'est le menu d'équipe
        if (!event.getView().getTitle().equals(ChatColor.GOLD + "Choisis ton équipe")) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        // Si l'élément est nul, on sort
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Si le joueur clique sur la laine verte, il rejoint l'équipe des Hiders
        if (clicked.getType() == Material.PLAYER_HEAD) 
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " a rejoint les Hiders !");
            plugin.addHider(player);
        }
        // Si le joueur clique sur la laine rouge, il rejoint l'équipe des Seekers
        else if (clicked.getType() == Material.PIGLIN_HEAD) 
        {
            if (plugin.getSeekers().size() == 1) 
            {
                player.sendMessage(ChatColor.RED + "Seul un joueur peut être Seeker");
                player.closeInventory();
                return;
            }
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + player.getName() + " devient Seeker !");
            plugin.addSeeker(player);
        }

        player.closeInventory();
    }
}

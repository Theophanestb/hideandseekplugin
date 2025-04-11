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
        if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            openTeamSelectorMenu(player);
            event.setCancelled(true);
        }
    }

    // Ouvre un menu d'équipe avec de la laine verte et rouge
    private void openTeamSelectorMenu(Player player) {
        Inventory inventory = plugin.getServer().createInventory(null, 9, ChatColor.GOLD + "Choisis ton équipe");

        // Crée l'élément laine verte pour les hiders
        ItemStack greenWool = new ItemStack(Material.GREEN_WOOL);
        ItemMeta greenMeta = greenWool.getItemMeta();
        greenMeta.setDisplayName(ChatColor.GREEN + "Rejoindre les Hiders");
        greenWool.setItemMeta(greenMeta);

        // Crée l'élément laine rouge pour les seekers
        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        ItemMeta redMeta = redWool.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "Rejoindre les Seekers");
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
        if (clicked.getType() == Material.GREEN_WOOL) 
        {
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " a rejoint les Hiders !");
            plugin.addHider(player);
        }
        // Si le joueur clique sur la laine rouge, il rejoint l'équipe des Seekers
        else if (clicked.getType() == Material.RED_WOOL) 
        {
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " a rejoint les Seekers !");
            plugin.addSeeker(player);
        }

        player.closeInventory();
    }
}

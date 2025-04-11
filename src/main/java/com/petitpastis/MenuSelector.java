package com.petitpastis;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuSelector {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Choisis ton équipe");

        ItemStack seekerItem = new ItemStack(Material.GREEN_WOOL, 1);
        ItemMeta seekerMeta = seekerItem.getItemMeta();
        seekerMeta.setDisplayName(ChatColor.GREEN + "Rejoindre les Seekers");
        seekerItem.setItemMeta(seekerMeta);

        ItemStack hiderItem = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta hiderMeta = hiderItem.getItemMeta();
        hiderMeta.setDisplayName(ChatColor.RED + "Rejoindre les Hiders");
        hiderItem.setItemMeta(hiderMeta);

        inv.setItem(3, seekerItem);
        inv.setItem(5, hiderItem);

        player.openInventory(inv);
    }
}


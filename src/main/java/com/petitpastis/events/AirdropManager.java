package com.petitpastis.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.petitpastis.Plugin;
import com.petitpastis.enums.States;

import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class AirdropManager {

    private final Plugin plugin;

    private List<ItemStack> items = new ArrayList<>();

    public AirdropManager(Plugin plugin) {
        this.plugin = plugin;
        items.add(createLimitedPunchBow());
        items.add(createSplashInvisibilityPotion());
        items.add(createTeleportSwapRod());
        items.add(createGhostPearl());
        items.add(createBoots());
    }

    public ItemStack createLimitedPunchBow() 
    {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Bow ");
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true); // Punch = Knockback sur arc
        meta.setUnbreakable(false);

        // Limite à 2 utilisations
        bow.setItemMeta(meta);
        bow.setDurability((short) (bow.getType().getMaxDurability() - 2));
        return bow;
    }

    public ItemStack createGhostPearl() 
    {
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = pearl.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Perle");
        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Téléportation silencieuse"));
        pearl.setItemMeta(meta);
        return pearl;
    }

    public ItemStack createBoots()
    {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta meta = boots.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "No Fall Damage Boots");
        meta.addEnchant(Enchantment.FEATHER_FALLING, 9, true);
        boots.setDurability((short) (boots.getType().getMaxDurability() - 2));
        boots.setItemMeta(meta);
        return boots;
    }

    public ItemStack createTeleportSwapRod() 
    {
        ItemStack rod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = rod.getItemMeta();

        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Bâton de Transposition");
        meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(Arrays.asList(
            ChatColor.GRAY + "Frappez un joueur pour échanger vos places",
            ChatColor.DARK_GRAY + "(usage unique)"
        ));

        rod.setItemMeta(meta);
        return rod;
    }

    public ItemStack createSplashInvisibilityPotion() 
    {
         ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.setDisplayName(ChatColor.AQUA + "Potion d'invisibilité (20s)");
        meta.setColor(Color.GRAY);

        // Ajoute un effet de 30 secondes (30 x 20 ticks)
        PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 20, 0, true, true);
        meta.addCustomEffect(effect, true);

        potion.setItemMeta(meta);
        return potion;
    }
    public void spawnAirdrop(Location startLocation) {
        World world = startLocation.getWorld();

        // Crée un falling_block de type chest
        BlockData chestBlockData = Bukkit.createBlockData(Material.CHEST);
        FallingBlock fallingChest = world.spawnFallingBlock(startLocation, chestBlockData);

        fallingChest.setDropItem(false);
        fallingChest.setVelocity(new Vector(0, -0.05, 0)); // descente lente
        fallingChest.setInvulnerable(true);

        // Quand le coffre touche le sol, transforme-le en bloc
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fallingChest.isOnGround()) {
                    Location chestLoc = fallingChest.getLocation().getBlock().getLocation();
                    fallingChest.remove();
                    chestLoc.getBlock().setType(Material.CHEST);

                    // Mets une canne à pêche dedans
                    Chest chest = (Chest) chestLoc.getBlock().getState();
                    Inventory inv = chest.getBlockInventory();
                    
                    int randomIndex = (int) (Math.random() * items.size());
                    if (randomIndex == 0)
                    {
                        inv.setItem(11, items.get(randomIndex));
                        inv.setItem(15, new ItemStack(Material.ARROW, 2));
                    }
                    else
                    {
                        inv.setItem(13, items.get(randomIndex));
                    }
                        
                    

                    // Supprime le coffre au bout de 60 secondes
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            chestLoc.getBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(plugin, 20 * 120); // 60s
                    new BukkitRunnable() {
                        int durationTicks = 20 * 120; // 1 minute (en ticks)
                        int elapsed = 0;

                        @Override
                        public void run() {
                            if (elapsed >= durationTicks || chest.getInventory().isEmpty() || plugin.getState() != States.PLAYING) {
                                chestLoc.getBlock().setType(Material.AIR);
                                cancel();
                                return;
                            }

                            if (chestLoc.getBlock().getType() != Material.CHEST) {
                                cancel();
                                return;
                            }

                            // Ajouter des particules au-dessus du coffre
                            Location particleLoc = chestLoc.clone().add(0.5, 1.2, 0.5);
                            world.spawnParticle(Particle.HAPPY_VILLAGER, particleLoc, 6, 0.2, 0.1, 0.2, 0.01);
                            world.spawnParticle(Particle.HAPPY_VILLAGER, particleLoc, 6, 0.2, 1.1, 0.2, 0.01);

                            elapsed += 5;
                        }
                    }.runTaskTimer(plugin, 0L, 5L);
                    
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }
}
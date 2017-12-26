package net.wolfgalaxy.main.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private static Items items;

    public static Items getItems() {
        if(items == null){
            items = new Items();
        }
        return items;
    }

    public ItemStack sith = new ItemStack(Material.RED_SHULKER_BOX, 1);
    {
        ItemMeta sth = sith.getItemMeta();
        sth.setDisplayName(ChatColor.DARK_RED + "Sith");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§4Join the dark side");
        sth.setLore(lore);
        sith.setItemMeta(sth);
    }

    public ItemStack jedi = new ItemStack(Material.GREEN_SHULKER_BOX, 1);
    {
        ItemMeta jd = jedi.getItemMeta();
        jd.setDisplayName(ChatColor.GREEN + "Jedi");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§aJoin the bright side");
        jd.setLore(lore);
        jedi.setItemMeta(jd);
    }

    public ItemStack mandalorian = new ItemStack(Material.RED_SHULKER_BOX, 1);
    {
        ItemMeta md = mandalorian.getItemMeta();
        md.setDisplayName(ChatColor.RED + "Mandalorian");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§cJoin the Mandalorian");
        md.setLore(lore);
        mandalorian.setItemMeta(md);
    }

    public ItemStack cis = new ItemStack(Material.YELLOW_SHULKER_BOX, 1);
    {
        ItemMeta cs = cis.getItemMeta();
        cs.setDisplayName(ChatColor.GOLD + "CIS");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§6Cconfederacy of Independant Systems");
        cs.setLore(lore);
        cis.setItemMeta(cs);
    }

    public ItemStack uf = new ItemStack(Material.BLUE_SHULKER_BOX, 1);
    {
        ItemMeta uf1 = uf.getItemMeta();
        uf1.setDisplayName(ChatColor.BLUE + "Undercover Force");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§9Join the Undercover Force");
        uf1.setLore(lore);
        uf.setItemMeta(uf1);
    }

    public ItemStack bh = new ItemStack(Material.PURPLE_SHULKER_BOX, 1);
    {
        ItemMeta bh1 = bh.getItemMeta();
        bh1.setDisplayName(ChatColor.DARK_PURPLE + "Bounty Hunters");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§5Join the Bounty Hunters");
        bh1.setLore(lore);
        bh.setItemMeta(bh1);
    }

    public ItemStack grey = new ItemStack(Material.GRAY_SHULKER_BOX, 1);
    {
        ItemMeta gr = grey.getItemMeta();
        gr.setDisplayName(ChatColor.GRAY + "Grey");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§o§7Join the Grey");
        gr.setLore(lore);
        grey.setItemMeta(gr);
    }

}

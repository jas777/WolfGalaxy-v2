package net.wolfgalaxy.main.Allegiances;

import net.wolfgalaxy.main.Achievements.AchievementManager;
import net.wolfgalaxy.main.Achievements.AchievementStates;
import net.wolfgalaxy.main.Main.Errors;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import net.wolfgalaxy.main.Utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Allegiances implements Listener {

    private Items items = Items.getItems();
    private WolfGalaxy plugin = WolfGalaxy.getInstance();
    private static Allegiances allegiances;
    private AchievementManager achievementManager = AchievementManager.getManager();

    public static Allegiances getAllegiances(){
        if(allegiances == null){
            allegiances = new Allegiances();
        }
        return allegiances;
    }

    public FileConfiguration getAllegianceData(){
        return WolfGalaxy.getAllegianceYml();
    }



    public String GetPlayerAllegiancePrefix(Player p){

        String prefix = getAllegianceData().getString(p.getName() + ".allegiance.prefix");

        if(prefix == null){
            prefix = "";
        }

        return prefix;
    }

    public String GetPlayerAllegianceName(Player p){

        String name = getAllegianceData().getString(p.getName() + ".allegiance.name");

        if(name == null){
            name = "";
        }

        return name;
    }

    private Inventory choose = Bukkit.createInventory(null, 9*3, ChatColor.RED + "Choose your allegiance");

    {
        choose.setItem(1, items.sith);
        choose.setItem(7, items.jedi);
        choose.setItem(10, items.mandalorian);
        choose.setItem(16, items.cis);
        choose.setItem(19, items.uf);
        choose.setItem(25, items.bh);
        choose.setItem(13, items.grey);
    }

    public Inventory getChoose() {
        return choose;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if(inventory.getName().equals(choose.getName())){
            FileConfiguration allegianceYml = getAllegianceData();
            if(allegianceYml.getString(player.getName() + ".allegiance.name") == null) {
                switch (clickedItem.getType()) {
                    case RED_SHULKER_BOX:
                        if(clickedItem.getItemMeta().getDisplayName().equals(items.sith.getItemMeta().getDisplayName())) {
                            event.setCancelled(true);
                            allegianceYml.set(player.getName() + ".allegiance.name", "Sith");
                            allegianceYml.set(player.getName() + ".allegiance.prefix", "&4■");
                            player.closeInventory(); // Red lines are theese lines that cause NullPointerException
                        } else if (clickedItem.getItemMeta().getDisplayName().equals(items.mandalorian.getItemMeta().getDisplayName())){
                            event.setCancelled(true);
                            allegianceYml.set(player.getName() + ".allegiance.name", "Mandalorian");
                            allegianceYml.set(player.getName() + ".allegiance.prefix", "&c■");
                            player.closeInventory();
                        }
                        break;
                    case YELLOW_SHULKER_BOX:
                        event.setCancelled(true);
                        allegianceYml.set(player.getName() + ".allegiance.name", "CIS");
                        allegianceYml.set(player.getName() + ".allegiance.prefix", "&6■");
                        player.closeInventory();
                        break;
                    case BLUE_SHULKER_BOX:
                        event.setCancelled(true);
                        allegianceYml.set(player.getName() + ".allegiance.name", "Undercover Force");
                        allegianceYml.set(player.getName() + ".allegiance.prefix", "&9■");
                        player.closeInventory();
                        break;
                    case PURPLE_SHULKER_BOX:
                        event.setCancelled(true);
                        allegianceYml.set(player.getName() + ".allegiance.name", "Bounty Hunters");
                        allegianceYml.set(player.getName() + ".allegiance.prefix", "&5■");
                        player.closeInventory();
                        break;
                    case GRAY_SHULKER_BOX:
                        event.setCancelled(true);
                        allegianceYml.set(player.getName() + ".allegiance.name", "Grey");
                        allegianceYml.set(player.getName() + ".allegiance.prefix", "&7■");
                        player.closeInventory();
                        break;
                    case GREEN_SHULKER_BOX:
                        event.setCancelled(true);
                        allegianceYml.set(player.getName() + ".allegiance.name", "Jedi");
                        allegianceYml.set(player.getName() + ".allegiance.prefix", "&a■");
                        player.closeInventory();
                        achievementManager.addAchievementToPlayer(player, "Join the bright side", AchievementStates.COMPLETED);
                        break;
                }
            } else {
                player.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.ALLEGIANCE_ALREADY_CHOSEN));
                player.closeInventory();
            }
            WolfGalaxy.saveAllegConfig(allegianceYml);

            // Example code \/

            //This is how it works:
            //FileConfiguration config = getAllegianceData(); // Get the file;
            //config.set("1",1);//set something
            //WolfGalaxy.saveAllegConfig(config);//you need to give the edited FileConfiguration (also please remember to save the config with the new data after like 5 to 8 values that you set)
        }
    }


}

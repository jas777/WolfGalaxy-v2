package net.wolfgalaxy.main.Achievements;

import net.wolfgalaxy.main.Events.PlayerAchieveEvent;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AchievementManager implements Listener {

    private WolfGalaxy plugin;

    private static AchievementManager manager;

    public static AchievementManager getManager() {
        if(manager == null){
            manager = new AchievementManager();
        }

        return manager;
    }

    public AchievementManager() {
        plugin = WolfGalaxy.getInstance();
    }

    public FileConfiguration getAchievementData(){
        return WolfGalaxy.getAchievementData();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(!getAchievementData().contains(p.getName())){
            FileConfiguration config = getAchievementData();
            config.set(p.getName(), new ArrayList<String>());
            WolfGalaxy.saveAchievConfig(config);
        }
    }

    public void addAchievementToPlayer(Player p, String achievementName, AchievementStates state){

        ArrayList<String> achievementList = (ArrayList<String>) getPlayerAchievements(p);
        FileConfiguration config = getAchievementData();

        // Nope, doesn't work, saves file but it isn't changed ( edited )
        achievementList.add(achievementName);
        // + for loop is for setting state to achievement

        for(String option : achievementList){
            if(option.equalsIgnoreCase(achievementName)) { // Now
                config.set(p.getName() + "." + option, state.toString());
            }
        }

        WolfGalaxy.saveAchievConfig(config);
    }

    public List getPlayerAchievements(Player player){//And does it work yet?

        ArrayList<String> achievementList = (ArrayList<String>) getAchievementData().getList(player.getName());

        return achievementList;
    }

    private ItemStack createItemForAchievement(String achievementName, AchievementStates state){

        byte color = 14;//Makes it red as standard

        switch (state){
            case COMPLETED:
                color = 13;
                break;
            case IN_PROGRESS:
                color = 4;
                break;
            case NOT_STARTED:
                color = 14;
                break;
            default:
                color = 14;
            break;
        }
        ItemStack item =  new ItemStack(Material.WOOL,1, (short) 0, color);//done
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + achievementName);

        ArrayList<String> lores = new ArrayList<String>();
        lores.add(state.colorCode+state.text);

        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;//Try this it will be fun ;)
    }

    public Inventory getPlayerAchievementInventory(Player player){

        int i = 0;
        FileConfiguration config = getAchievementData();
        List<String> achievementList = getPlayerAchievements(player);


        Inventory achievements = Bukkit.createInventory(null, 81, ChatColor.GREEN + "Achievements");
        {
            for(String achievement : achievementList){
                String stateString = getAchievementData().getString(player.getName()+"."+achievement);
                AchievementStates state = AchievementStates.valueOf(stateString);//This can fail if the config hasn't saved it correct
                achievements.setItem(i, createItemForAchievement(achievement, state));
                i++;
            }
        }


        return achievements;
    }

}

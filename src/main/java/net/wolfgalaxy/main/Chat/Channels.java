package net.wolfgalaxy.main.Chat;

import net.wolfgalaxy.main.Allegiances.Allegiances;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;

public class Channels {

    private WolfGalaxy plugin = WolfGalaxy.getInstance();
    private Allegiances allegiances = Allegiances.getAllegiances();
    private static Channels channels;

    public static Channels getChannels() {
        if(channels == null){
            channels = new Channels();
        }
        return channels;
    }

    public static ArrayList<Player> siths = new ArrayList<Player>();
    public static ArrayList<Player> jedis = new ArrayList<Player>();
    public static ArrayList<Player> leadership = new ArrayList<Player>();
    public static ArrayList<Player> staffgeneral = new ArrayList<Player>();
    public static ArrayList<Player> lowerstaff = new ArrayList<Player>();
    public static ArrayList<Player> higherstaff = new ArrayList<Player>();

    public ArrayList<Player> getHigherstaff() {
        return higherstaff;
    }

    public ArrayList<Player> getJedis() {
        return jedis;
    }

    public ArrayList<Player> getLeadership() {
        return leadership;
    }

    public ArrayList<Player> getLowerstaff() {
        return lowerstaff;
    }

    public ArrayList<Player> getSiths() {
        return siths;
    }

    public ArrayList<Player> getStaffgeneral() {
        return staffgeneral;
    }

    public boolean playerIsInChannel(Player p){
        if(siths.contains(p) || jedis.contains(p) || leadership.contains(p) || staffgeneral.contains(p) || lowerstaff.contains(p) || higherstaff.contains(p)){
            return true;
        } else {
            return false;
        }
    }

    public void sendToChannel(ArrayList<Player> channel, Player p, String message, String channelPrefix) {

        String prefix = PermissionsEx.getUser(p).getOwnPrefix();

        if (prefix == null) {
            prefix = PermissionsEx.getUser(p).getPrefix();
        }

        String suffix = PermissionsEx.getUser(p).getOwnSuffix();

        if (suffix == null) {
            suffix = PermissionsEx.getUser(p).getSuffix();
        }


        String format1 = plugin.getConfig().getString("chat.style").replace("%name%", p.getDisplayName());
        String format2 = format1.replace("%suffix%", suffix);
        String format3 = format2.replace("%prefix%", prefix);
        String format4 = format3.replace("%message%", message);
        String format5 = format4.replace("%allegiance%", allegiances.GetPlayerAllegiancePrefix(p));

        for (Player p1 : channel) {
            p1.sendMessage(ChatColor.translateAlternateColorCodes('&', channelPrefix + " " + format5));
        }
    }

    public void removeFromChannels(Player p){
        if(leadership.contains(p)){
            leadership.remove(p);
            for(Player p1 : leadership){
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        } else if(higherstaff.contains(p)) {
            higherstaff.remove(p);
            for (Player p1 : higherstaff) {
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        } else if(lowerstaff.contains(p)) {
            lowerstaff.remove(p);
            for (Player p1 : lowerstaff) {
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        } else if(staffgeneral.contains(p)) {
            staffgeneral.remove(p);
            for (Player p1 : staffgeneral) {
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        } else if(siths.contains(p)) {
            siths.remove(p);
            for (Player p1 : siths) {
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        } else if(jedis.contains(p)) {
            jedis.remove(p);
            for (Player p1 : jedis) {
                p1.sendMessage(ChatColor.RED + p.getName() + " has left the channel.");
            }
        }
    }


}

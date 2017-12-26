package net.wolfgalaxy.main.Main;

import org.bukkit.ChatColor;

public enum Prefixes {


    PREFIX_BROADCAST;

    public static String getPrefix(Prefixes type) {

        String prefix = "";

        if(type == PREFIX_BROADCAST){
            prefix = null;
        }

        return prefix;

    }


}

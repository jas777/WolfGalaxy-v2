package net.wolfgalaxy.main.Utils;

import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SeenUtil {

    public static SeenUtil util;

    public static SeenUtil getUtil() {
        if (util == null) {
            util = new SeenUtil();
        }
        return util;
    }

    public String lastSeenDate(Player player){
        long lastPlayed = player.getLastPlayed();
        Date date = new Date(lastPlayed);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
        String formatted = sdf.format(date);
        return formatted;
    }

}

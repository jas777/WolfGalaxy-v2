package net.wolfgalaxy.main.Utils;

import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChatUtil {

    private static ChatUtil util;

    public static ChatUtil getUtils() {
        if (util == null) {
            util = new ChatUtil();
        }
        return util;
    }

    public static void sendColoredMessage(Player player, String text){

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    public String sendArgsWithoutFirst(String[] args){

        String message;

        String[] messagearray = Arrays.copyOfRange(args, 1, args.length);
        message = StringUtils.join(messagearray, " ");

        return message;
    }



}
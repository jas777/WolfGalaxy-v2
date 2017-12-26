package net.wolfgalaxy.main.Utils;

import net.wolfgalaxy.main.API.Actionbar;
import net.wolfgalaxy.main.Main.Permissions;
import net.wolfgalaxy.main.Main.Prefixes;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import net.wolfgalaxy.main.Utils.ChatUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class BroadcastUtil {

    /**
     * Sends a message from the sender with pemission 'COMMAND_BROADCAST' to all players online!
     * @param message the message to be send (auto translates the & symbol to color codes)
     */
    public static void broadcastFrom2All(String message) {
            String toSend = StringUtils.join(message, " ");
            Actionbar actionbar = new Actionbar(toSend);
            actionbar.sendToAll();
            System.out.println(toSend);
    }

    /**
     * Sends a message from the sender with pemission 'COMMAND_BROADCAST' to the receiver!
     * @param sender the sender of the message.
     * @param receiver the receiver of the message.
     * @param message the message to be send. (auto translates the & symbol to color codes)
     */
    public static void broadcastFrom2Player(Player sender, Player receiver, String message) {
        if (sender.hasPermission(Permissions.getPermission(Permissions.COMMAND_BROADCAST))) {
            String toSend = StringUtils.join(message, " ");
            Actionbar actionbar = new Actionbar(ChatColor.translateAlternateColorCodes('&', toSend));
            actionbar.sendToPlayer(receiver);
        }
    }

    /**
     * Sends a message to the receiver!
     * @param receiver the receiver of the message.
     * @param message the message to be send. (auto translates the & symbol to color codes)
     */
    public static void broadcast2(Player receiver, String message) {
        Actionbar actionbar = new Actionbar(ChatColor.translateAlternateColorCodes('&', message));
        actionbar.sendToPlayer(receiver);
    }

    /**
     * Sends a message to all online players!
     * @param message the message to be send. (auto translates the & symbol to color codes)
     */
    public static void broadcast2All(String message) {
        Actionbar actionbar = new Actionbar(ChatColor.translateAlternateColorCodes('&', message));
        actionbar.sendToAll();
    }
}

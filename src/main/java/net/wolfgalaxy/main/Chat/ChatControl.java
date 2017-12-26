package net.wolfgalaxy.main.Chat;

import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatControl {

    private boolean canSwear = true;
    private boolean canChat = true;
    private boolean discordRelay = true;
    private WolfGalaxy plugin = WolfGalaxy.getInstance();

    private static ChatControl instance;

    public static ChatControl getInstance() {
        if(instance == null){
            instance = new ChatControl();
        }
        return instance;
    }

    public boolean isCanChat() {
        return canChat;
    }

    public void setCanChat(boolean canChat) {
        this.canChat = canChat;
    }

    public boolean isCanSwear() {
        return canSwear;
    }

    public void setCanSwear(boolean canSwear) {
        this.canSwear = canSwear;
    }

    public boolean isDiscordRelay() {
        return discordRelay;
    }

    public void setDiscordRelay(boolean discordRelay) {
        this.discordRelay = discordRelay;
    }

    public String censor(String toCensor){
        String censored = "";

        return censored;
    }

    public void clearChat(Player p){
        for(int x = 0; x < 100; x++){
            Bukkit.broadcastMessage(" ");
        }
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Chat has been cleared by " + ChatColor.RED + p.getName());
    }

}

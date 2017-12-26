package net.wolfgalaxy.main.Chat;

import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Allegiances.Allegiances;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Main.Messages;
import net.wolfgalaxy.main.Main.Permissions;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.awt.*;
import java.time.OffsetDateTime;

public class ChatMain implements Listener {

    private ChatControl chatControl = ChatControl.getInstance();
    private Discord discord = Discord.getManager();
    private WolfGalaxy plugin = WolfGalaxy.getInstance();
    private Allegiances allegiances = Allegiances.getAllegiances();
    private Channels channels = Channels.getChannels();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){

        Player p = e.getPlayer();

        String prefix = PermissionsEx.getUser(p).getOwnPrefix();

        if(prefix == null){
            prefix = PermissionsEx.getUser(p).getPrefix();
        }

        String suffix = PermissionsEx.getUser(p).getOwnSuffix();

        if(suffix == null){
            suffix = PermissionsEx.getUser(p).getSuffix();
        }

        if(chatControl.isDiscordRelay() && chatControl.isCanChat() && !(channels.playerIsInChannel(p))){
            discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
            .setColor(Color.CYAN)
            .addField(p.getName() + " | " + allegiances.GetPlayerAllegianceName(p), e.getMessage().replace(ChatColor.values().toString(), ""), false)
            .setTimestamp(OffsetDateTime.now())
            .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
        }

        if(!(channels.playerIsInChannel(p))){

            String format1 = plugin.getConfig().getString("chat.style").replace("%name%", p.getDisplayName());
            String format2 = format1.replace("%suffix%", suffix);
            String format3 = format2.replace("%prefix%", prefix);
            String format4 = format3.replace("%message%", e.getMessage());
            String format5 = format4.replace("%allegiance%", allegiances.GetPlayerAllegiancePrefix(p));

            if(chatControl.isCanSwear()){
                for(Player p1 : Bukkit.getOnlinePlayers()){
                    if(chatControl.isCanChat() && !(channels.playerIsInChannel(p1))){
                        p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat.channels.global.prefix") + " " + format5));
                        e.setCancelled(true);
                    } else {
                        if(!chatControl.isCanChat()) {
                            if (p1.hasPermission(Permissions.getPermission(Permissions.CHAT_BYPASS))) {
                                p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat.channels.global.prefix") + " " + format5));
                                e.setCancelled(true);
                            } else {
                                p.sendMessage(ChatColor.RED + "Chat is currently disabled");
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }

            if(channels.playerIsInChannel(p)){
                e.setCancelled(true);
            }
        }

        if(chatControl.isCanChat() && channels.playerIsInChannel(p)){
            if(channels.getJedis().contains(p)){
                channels.sendToChannel(channels.getJedis(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.jedi.prefix"));
                e.setCancelled(true);
            }
            if(channels.getSiths().contains(p)){
                channels.sendToChannel(channels.getSiths(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.sith.prefix"));
                e.setCancelled(true);
            }
            if(channels.getHigherstaff().contains(p)){
                channels.sendToChannel(channels.getHigherstaff(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.higherstaff.prefix"));
                e.setCancelled(true);
            }
            if(channels.getLowerstaff().contains(p)){
                channels.sendToChannel(channels.getLowerstaff(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.lowerstaff.prefix"));
                e.setCancelled(true);
            }
            if(channels.getLeadership().contains(p)){
                channels.sendToChannel(channels.getLeadership(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.leadership.prefix"));
                e.setCancelled(true);
            }
            if(channels.getStaffgeneral().contains(p)){
                channels.sendToChannel(channels.getStaffgeneral(), p, e.getMessage(), plugin.getConfig().getString("chat.channels.staff-general.prefix"));
                e.setCancelled(true);
            }
        }
    }

}

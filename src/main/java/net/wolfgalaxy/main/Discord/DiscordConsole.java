package net.wolfgalaxy.main.Discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.time.OffsetDateTime;

public class DiscordConsole extends ListenerAdapter implements Listener {

    private WolfGalaxy plugin = WolfGalaxy.getInstance();
    private Discord discord = Discord.getManager();
    private String consoleChannel = plugin.getConfig().getString("discord.channels.console");
    private ConsoleReader cr;

    @EventHandler
    public void commandTrack(PlayerCommandPreprocessEvent event){

        Player p = event.getPlayer();

        discord.sendEmbed(consoleChannel, new EmbedBuilder()
        .addField(p.getName(), event.getMessage(), false)
        .setColor(Color.RED)
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
        .setTimestamp(OffsetDateTime.now()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        discord.sendEmbed(consoleChannel, new EmbedBuilder()
                .addField(p.getName(), "Has joined the server!", false)
                .setColor(Color.ORANGE)
                .setTimestamp(OffsetDateTime.now())
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        discord.sendEmbed(consoleChannel, new EmbedBuilder()
                .addField(p.getName(), "Has left the server!", false)
                .setColor(Color.ORANGE)
                .setTimestamp(OffsetDateTime.now())
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();

        discord.sendEmbed(consoleChannel, new EmbedBuilder()
                .addField(p.getName(), ":skull_crossbones: Has died :skull_crossbones: ", false)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now())
                .setColor(Color.RED));

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getChannel().getId().equals(consoleChannel) && !(event.getAuthor().isBot())){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getMessage().getContentRaw());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event){
        String reason = event.getReason();
        Player p = event.getPlayer();

        discord.sendEmbed(consoleChannel, new EmbedBuilder()
        .addField(p.getName(), "Has been kicked because of: " + reason, false)
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
        .setTimestamp(OffsetDateTime.now())
        .setColor(Color.RED));
    }

}

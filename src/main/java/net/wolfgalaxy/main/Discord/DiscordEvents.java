package net.wolfgalaxy.main.Discord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.wolfgalaxy.main.Chat.Channels;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.time.OffsetDateTime;

public class DiscordEvents extends ListenerAdapter implements Listener {

    private Discord discord = Discord.getManager();
    private Channels channels = Channels.getChannels();
    private WolfGalaxy plugin = WolfGalaxy.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
        .addField(p.getName(), "Has joined the server!", false)
        .setColor(Color.ORANGE)
        .setTimestamp(OffsetDateTime.now())
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();

        discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
                .addField(p.getName(), "Has left the server!", false)
                .setColor(Color.ORANGE)
                .setTimestamp(OffsetDateTime.now())
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
        if(channels.playerIsInChannel(p)){
            channels.removeFromChannels(p);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();

        discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
        .addField(p.getName(), ":skull_crossbones: Has died :skull_crossbones: ", false)
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
        .setTimestamp(OffsetDateTime.now())
        .setColor(Color.RED));

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        User u = event.getUser();

        discord.sendEmbed(plugin.getConfig().getString("discord.channels.welcome"), new EmbedBuilder()
                .addField(u.getName() + " joined the server", plugin.getConfig().getString("discord.messages.join").replace("{MENTION}", u.getAsMention()), false)
                .setColor(Color.GREEN)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now()));

    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        User u = event.getUser();

        discord.sendEmbed(plugin.getConfig().getString("discord.channels.welcome"), new EmbedBuilder()
                .addField(u.getName() + " left the server", plugin.getConfig().getString("discord.messages.leave").replace("{MENTION}", u.getAsMention()), false)
                .setColor(Color.RED)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now()));

    }
}

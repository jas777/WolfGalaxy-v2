package net.wolfgalaxy.main.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class AFK implements Listener, CommandExecutor {

    private static ArrayList<Player> afk = new ArrayList<Player>();
    private Discord discord = Discord.getManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("wgbrb")) {

            Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.GRAY + " is now " + ChatColor.RED + "AFK" + ChatColor.GRAY + " because of: " + ChatColor.GREEN + StringUtils.join(args, " "));
            discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
                    .addField(player.getName(), "Is now AFK because of: **" + StringUtils.join(args, " ") + "**", false)
                    .setColor(Color.RED)
                    .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                    .setTimestamp(OffsetDateTime.now()));

            AFK.afk.add(player);

        }

        return false;
    }

    @EventHandler

    public void afkRemove(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();

        if (AFK.afk.contains(p)) {

            Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + ChatColor.GRAY + " has came back!");
            discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
                    .addField(p.getName(), "Has came back!", false)
                    .setColor(Color.RED)
                    .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                    .setTimestamp(OffsetDateTime.now()));
            AFK.afk.remove(p);

        }

    }

    @EventHandler

    public void afkRemove2(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        Location to = e.getTo();
        Location from = e.getFrom();
        if (afk.contains(p)) {
            if ((from.getBlockX() != to.getBlockX()) || (from.getBlockY() != to.getBlockY()) || (from.getBlockZ() != to.getBlockZ())) {
                Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + ChatColor.GRAY + " has came back!");
                discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.chat"), new EmbedBuilder()
                        .addField(p.getName(), "Has came back!", false)
                        .setColor(Color.RED)
                        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                        .setTimestamp(OffsetDateTime.now()));
                AFK.afk.remove(p);

            }
        }

    }

}

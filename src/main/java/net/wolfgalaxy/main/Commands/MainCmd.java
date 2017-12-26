package net.wolfgalaxy.main.Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Achievements.AchievementManager;
import net.wolfgalaxy.main.Allegiances.Allegiances;
import net.wolfgalaxy.main.Chat.Channels;
import net.wolfgalaxy.main.Chat.ChatControl;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Main.Errors;
import net.wolfgalaxy.main.Main.Permissions;
import net.wolfgalaxy.main.Main.WolfGalaxy;
import net.wolfgalaxy.main.Utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.OffsetDateTime;

public class MainCmd implements CommandExecutor {

    private Discord discord = Discord.getManager();
    private ChatUtil util = ChatUtil.getUtils();
    private Allegiances allegiances = Allegiances.getAllegiances();
    private Channels channels = Channels.getChannels();
    private ChatControl chatControl = ChatControl.getInstance();
    private WolfGalaxy plugin = WolfGalaxy.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("wg")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    p.sendMessage(ChatColor.GRAY + "WolfGalaxy main plugin\n" + ChatColor.GRAY + "Version: " + ChatColor.RED + "2.0\n" + ChatColor.GRAY + "For list of commands use " + ChatColor.RED + "/wg help");
                } else {
                    if (args[0].equalsIgnoreCase("help")) {
                        if(args.length == 1) {
                            p.sendMessage(

                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Help" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "---------------\n" +
                                            ChatColor.RED + "/wg help admin" + ChatColor.DARK_GRAY + " - Commands for administrators\n" +
                                            ChatColor.RED + "/wg help player" + ChatColor.DARK_GRAY + " - Commands for regular players\n" +
                                            ChatColor.RED + "/wg help channels" + ChatColor.DARK_GRAY + " - List of available chat channels" +
                                            ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Help" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "---------------\n"
                            );
                        }

                        if(args.length == 2 && args[1].equalsIgnoreCase("admin")){
                            p.sendMessage(
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Administrator commands" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------\n" +
                                    ChatColor.RED + "/wg chat on/off/toggle" + ChatColor.DARK_GRAY + " - Turn chat on/off\n" +
                                    ChatColor.RED + "/wg chat clear" + ChatColor.DARK_GRAY + " - Clears chat\n" +
                                    ChatColor.RED + "/wg announce" + ChatColor.DARK_GRAY + " - Send announcement to Discord\n" +
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Administrator commands" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------\n"
                            );
                        }

                        if(args.length == 2 && args[1].equalsIgnoreCase("player")){
                            p.sendMessage(
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Player commands" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------\n" +
                                    ChatColor.RED + "/wg side" + ChatColor.DARK_GRAY + " - Allows you to choose your allegiance\n" +
                                    ChatColor.RED + "/wgbrb <reason>" + ChatColor.DARK_GRAY + " - Our custom AFK command\n" +
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "--------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Player commands" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "--------\n"
                            );
                        }

                        if(args.length == 2 && args[1].equalsIgnoreCase("channels")){
                            p.sendMessage(
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Channels" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "---------------\n" +
                                    ChatColor.RED + "/wg channel leadership/ls" + ChatColor.DARK_GRAY + " - Channel for Leadership only\n" +
                                    ChatColor.RED + "/wg channel higherstaff/hs" + ChatColor.DARK_GRAY + " - Channel for Higher Staff\n" +
                                    ChatColor.RED + "/wg channel lowerstaff/los" + ChatColor.DARK_GRAY + " - Channel for Lower Staff\n" +
                                    ChatColor.RED + "/wg channel staffgeneral/sg" + ChatColor.DARK_GRAY + " - General channel for staff\n" +
                                    ChatColor.RED + "/wg channel sith/jedi" + ChatColor.DARK_GRAY + " - Allegiance channels\n" +
                                    ChatColor.RED + "/wg channel global/general" + ChatColor.DARK_GRAY + " - Global channel\n" +
                                    ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------" + ChatColor.RESET + ChatColor.DARK_GRAY + "< " + ChatColor.RED + "WolfGalaxy - Channels" + ChatColor.DARK_GRAY + " >" + "" + ChatColor.DARK_GRAY + ChatColor.STRIKETHROUGH + "---------------\n"
                            );
                        }
                    } else {
                        if (args[0].equalsIgnoreCase("broadcast")) {
                            if (sender.hasPermission(Permissions.getPermission(Permissions.COMMAND_BROADCAST))) {
                                p.sendMessage(ChatColor.RED + "Command currently disabled");
                            } else {
                                sender.sendMessage(Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                            }
                        } else if (args[0].equalsIgnoreCase("jas")) {
                            p.sendMessage(ChatColor.RED + "You called secret command created by Head Developer, jas777. One question... HOW DID YOU FIND IT?!");
                        } else if (args[0].equalsIgnoreCase("announce")) {
                            if(p.hasPermission(Permissions.getPermission(Permissions.DISCORD_ANNOUNCE))) {

                                discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.announcements"), new EmbedBuilder()
                                        .addField("Announcement", util.sendArgsWithoutFirst(args), false)
                                        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                                        .setTimestamp(OffsetDateTime.now())
                                        .setColor(Color.RED));
                            } else {
                                p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                            }
                        } else if (args[0].equalsIgnoreCase("side")) {
                            p.openInventory(allegiances.getChoose());
                        } else if (args[0].equalsIgnoreCase("channel")) {
                            if(!channels.playerIsInChannel(p)) {
                                if ((args[1].equalsIgnoreCase("leadership") || args[1].equalsIgnoreCase("ls"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_LEADERSHIP))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Leadership");
                                        channels.getLeadership().add(p);
                                    } else{
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                } else if ((args[1].equalsIgnoreCase("higherstaff") || args[1].equalsIgnoreCase("hs"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_HIGHERSTAFF))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Higher Staff");
                                        channels.getHigherstaff().add(p);
                                    } else {
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                } else if ((args[1].equalsIgnoreCase("lowerstaff") || args[1].equalsIgnoreCase("los"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_LOWERSTAFF))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Lower Staff");
                                        channels.getLowerstaff().add(p);
                                    } else{
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                } else if ((args[1].equalsIgnoreCase("staffgeneral") || args[1].equalsIgnoreCase("sg"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_STAFFGENERAL))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Staff General");
                                        channels.getStaffgeneral().add(p);
                                    } else{
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                } else if ((args[1].equalsIgnoreCase("sith") || args[1].equalsIgnoreCase("s"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_SITH))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Sith");
                                        channels.getSiths().add(p);
                                    } else{
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                } else if ((args[1].equalsIgnoreCase("jedi") || args[1].equalsIgnoreCase("j"))) {
                                    if(p.hasPermission(Permissions.getPermission(Permissions.CHANNEL_JEDI))) {
                                        p.sendMessage(ChatColor.GREEN + "Switched to Jedi");
                                        channels.getJedis().add(p);
                                    } else{
                                        p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                    }
                                }
                            } else if(args[1].equalsIgnoreCase("global") || args[1].equalsIgnoreCase("general")){
                                p.sendMessage(ChatColor.GREEN + "Switched to Global");
                                channels.removeFromChannels(p);
                            }
                    } else if (args[0].equalsIgnoreCase("chat")) {
                            if (args[1].equalsIgnoreCase("off")) {
                                chatControl.setCanChat(false);
                                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Chat has been " + ChatColor.RED + "disabled");
                            } else if (args[1].equalsIgnoreCase("on")) {
                                chatControl.setCanChat(true);
                                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Chat has been " + ChatColor.GREEN + "enabled");
                            } else if (args[1].equalsIgnoreCase("toggle")) {
                                if(p.hasPermission(Permissions.getPermission(Permissions.CHAT_CONTROL))) {
                                    if (chatControl.isCanChat()) {
                                        chatControl.setCanChat(false);
                                        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Chat has been " + ChatColor.RED + "disabled");
                                    }
                                } else if (!chatControl.isCanChat()) {
                                    chatControl.setCanChat(true);
                                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "Chat has been " + ChatColor.GREEN + "enabled");
                                }
                            } else if(args[1].equalsIgnoreCase("clear")){
                                if(p.hasPermission(Permissions.getPermission(Permissions.CHAT_CONTROL))){
                                    chatControl.clearChat(p);
                                } else {
                                    p.sendMessage(ChatColor.RED + Errors.getErrorMessage(Errors.NO_PERMISSIONS));
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("achievements")) {
                            p.openInventory(AchievementManager.getManager().getPlayerAchievementInventory(p));
                        }
                    }
                }
            } else {
                System.out.println(Errors.getErrorMessage(Errors.PLAYER_ONLY_COMMAND));
            if(args[0].equalsIgnoreCase("announce")){

                discord.sendEmbed(WolfGalaxy.getInstance().getConfig().getString("discord.channels.announcements"), new EmbedBuilder()
                        .addField("Announcement", util.sendArgsWithoutFirst(args), false)
                        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                        .setTimestamp(OffsetDateTime.now())
                        .setColor(Color.RED));

              }
            }
        }
        return true;
    }
}

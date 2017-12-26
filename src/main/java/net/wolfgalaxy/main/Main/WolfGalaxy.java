package net.wolfgalaxy.main.Main;

import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import net.dv8tion.jda.bot.JDABot;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.LootTables;
import net.wolfgalaxy.main.Achievements.AchievementManager;
import net.wolfgalaxy.main.Allegiances.Allegiances;
import net.wolfgalaxy.main.Chat.ChatControl;
import net.wolfgalaxy.main.Chat.ChatMain;
import net.wolfgalaxy.main.Commands.AFK;
import net.wolfgalaxy.main.Commands.MainCmd;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Discord.DiscordConsole;
import net.wolfgalaxy.main.Discord.DiscordEvents;
import net.wolfgalaxy.main.Discord.DiscordProfiles.Currency;
import net.wolfgalaxy.main.Utils.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;

public class WolfGalaxy extends JavaPlugin {

    private Discord discord;
    private JDA bot;
    private TextChannel tc;
    private static WolfGalaxy instance;
    private ChatControl control;
    private Allegiances allegiances;
    private CommandClientBuilder client = Discord.getClient();
    private static File allegianceData;
    private static File achievementData;
    private static File discordData;
    private static File discordProfilePicsFolder;
    private boolean a;
    private MySQL sql = MySQL.getSql();
    private Currency currency = Currency.getCurrency();

    @Override
    public void onEnable() {

        instance = this;
        discord = Discord.getManager();
        control = ChatControl.getInstance();
        allegiances = Allegiances.getAllegiances();

        registerCommands();
        registerEvents();

        getConfig().options().copyDefaults(true);
        saveConfig();

        sql.getConnection();
        sql.testConnection();



        discord.initializeDiscord(getConfig().getString("discord.token"));

        System.out.println(getConfig().getString("discord.channels.chat"));
        System.out.println(discord);

        discord.sendEmbed(getConfig().getString("discord.channels.chat"), new EmbedBuilder()
        .addField("WolfGalaxy", "Server is now online!", false)
        .setTimestamp(OffsetDateTime.now())
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
        .setColor(Color.GREEN));

        control.setDiscordRelay(true);

        loadFiles();

        BukkitScheduler bukkitScheduler = getServer().getScheduler();

         bukkitScheduler.scheduleSyncRepeatingTask(this, new Runnable() {
             @Override
             public void run() {

                 if(a){
                     discord.getBot().getPresence().setGame(Game.streaming("WolfGalaxy", "https://www.twitch.tv/alphamarek"));
                     a = !a;
                 } else {
                     discord.getBot().getPresence().setGame(Game.streaming("Run wg!help", "https://www.twitch.tv/alphamarek"));
                     a = !a;
                 }
             }
         }, 0, 220L);
    }

    public static WolfGalaxy getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

        discord.sendEmbed(getConfig().getString("discord.channels.chat"), new EmbedBuilder()
        .setColor(Color.RED)
        .addField("WolfGalaxy", "Server is now offline!", false)
        .setTimestamp(OffsetDateTime.now())
        .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG"));
        bot.shutdownNow();
    }

    public void registerCommands() {

        getCommand("wg").setExecutor(new MainCmd());
        getCommand("wgbrb").setExecutor(new AFK());

    }

    public void registerEvents() {

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChatMain(), this);
        pm.registerEvents(new DiscordEvents(), this);
        pm.registerEvents(new AFK(), this);
        pm.registerEvents(new DiscordConsole(), this);
        pm.registerEvents(new Allegiances(), this);
        pm.registerEvents(AchievementManager.getManager(), this);
    }
    public void loadFiles() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File dataFolder = new File (getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        discordData = new File(this.getDataFolder(), "discordData");
        if(!discordData.exists()){
            discordData.mkdir();
        }

        discordProfilePicsFolder = new File(discordData, "discordProfilePics");
        if(!discordProfilePicsFolder.exists()){
            discordProfilePicsFolder.mkdir();
        }


        allegianceData = new File(dataFolder, "allegianceData.yml");
        achievementData = new File(dataFolder, "achievementData.yml");

        if (!allegianceData.exists()) {
            try {
                allegianceData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            createDefaults();

        }

        if (!achievementData.exists()) {
            try {
                achievementData.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            createDefaults();

        }
    }

    private void createDefaults() {
        //Here you gonna make all the defaults that needs to be inside the config as standard for reading it.
        FileConfiguration config = getAllegianceYml();
        FileConfiguration achievConfig = getAchievementData();
        //config.set("Some.Path", (String) "Some value");
        saveAllegConfig(config);
        saveAchievConfig(achievConfig);
    }

    public static void saveAllegConfig(FileConfiguration data){
        try {
            data.save(allegianceData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAchievConfig(FileConfiguration data){
        try {
            data.save(achievementData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getAllegianceYml() {
        return YamlConfiguration.loadConfiguration(allegianceData);
    }

    public static FileConfiguration getAchievementData() {
        return YamlConfiguration.loadConfiguration(achievementData);
    }

     public File getDiscordData(){
        return discordData;
     }

     public File getDiscordProfilePicsFolder() {
         return discordProfilePicsFolder;
     }

}

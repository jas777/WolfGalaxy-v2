package net.wolfgalaxy.main.Discord;

import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.commandclient.examples.AboutCommand;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.wolfgalaxy.main.Discord.Commands.*;
import net.wolfgalaxy.main.Discord.DiscordProfiles.DiscordProfilesMain;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.logging.Logger;

public class Discord {

    private JDA bot;
    private TextChannel tc;
    private static Discord discord;
    private Logger logger;
    private static CommandClientBuilder client;

    public static Discord getManager() {
        if (discord == null) {
            discord = new Discord();
        }
        return discord;
    }

    public static CommandClientBuilder getClient() {
        return client;
    }

    public void initializeDiscord(String token){

        client = new CommandClientBuilder();

        EventWaiter waiter = new EventWaiter();

        client.setEmojis("\u2705", "\u26A0", "\u274C");

        client.setPrefix("wg_test");

        client.setOwnerId("210477510117294080");

        client.setGame(null);

        client.addCommands(
                new BalanceCommand(),
                new ChickenCommand(),
                new DailyCommand(),
                new ListCommand(),
                new MusicCommands(),
                new PingCommand(),
                new VoteCommand()
        );

        try {
            this.bot = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .addEventListener(new DiscordConsole())
                    .addEventListener(new DiscordProfilesMain())
                    .addEventListener(new DiscordEvents())
                    .addEventListener(client.build())
                    .setGame(Game.streaming("WolfGalaxy", "https://www.twitch.tv/alphamarek"))
                    .buildBlocking();
        } catch (LoginException | RateLimitedException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public JDA getBot() {
        return bot;
    }

    public void sendEmbed(String channelID, EmbedBuilder embed){

        bot.getTextChannelById(channelID).sendMessage(embed.build()).queue();

    }

    public void sendMessage(String message, String channelID){

        bot.getTextChannelById(channelID).sendMessage(message).queue();
    }

    public void sendToMinecraft(String message){
        Bukkit.broadcastMessage(message);
    }
}
package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Discord.DiscordProfiles.Currency;

import java.awt.*;
import java.time.OffsetDateTime;

public class BalanceCommand extends Command {

    private Discord discord = Discord.getManager();
    private Currency currency = Currency.getCurrency();

    public BalanceCommand(){
        this.name = "balance";
        this.aliases = new String[]{"bal", "money"};
        this.help = "Shows balance of your account.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        discord.sendEmbed(commandEvent.getTextChannel().getId(), new EmbedBuilder()
                .addField("Your balance:", currency.getBalance(commandEvent.getAuthor()) + " credits.", false)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now())
                .setColor(Color.CYAN));
    }
}

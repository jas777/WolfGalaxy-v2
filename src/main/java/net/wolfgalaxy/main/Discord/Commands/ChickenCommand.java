package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Discord.Discord;

import java.awt.*;
import java.time.OffsetDateTime;

public class ChickenCommand extends Command {

    private Discord discord = Discord.getManager();

    public ChickenCommand(){
        this.name = "chicken";
        this.help = "Better ping command.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        discord.sendEmbed(commandEvent.getTextChannel().getId(), new EmbedBuilder()
                .addField("Chicken!", "I just like chickens... lol", false)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now())
                .setColor(Color.CYAN));
    }
}

package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.wolfgalaxy.main.Discord.Discord;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListCommand extends Command {

    private Discord discord = Discord.getManager();

    public ListCommand(){
        this.name = "list";
        this.aliases = new String[]{"onserver"};
        this.help = "Shows list of players currently on server.";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<String> online = Bukkit.getOnlinePlayers().stream().map((Function<Player, String>) Player::getName).collect(Collectors.toList());

        discord.sendEmbed(commandEvent.getTextChannel().getId(), new EmbedBuilder()
                .addField("There are " + online.size() + " players online", StringUtils.join(online.toArray(), ", "), false)
                .setFooter("WolfGalaxy (c) 2017", "https://cdn.discordapp.com/attachments/336605222908461066/361126932722614292/IMG_0058.JPG")
                .setTimestamp(OffsetDateTime.now())
                .setColor(Color.CYAN));
    }
}

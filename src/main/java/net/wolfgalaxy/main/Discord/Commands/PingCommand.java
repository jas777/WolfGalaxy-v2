package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.wolfgalaxy.main.Discord.Discord;

public class PingCommand extends Command {

    private Discord discord = Discord.getManager();

    public PingCommand(){
        this.name = "ping";
        this.help = "Pong!";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

        discord.sendMessage("Pong! " + discord.getBot().getPing(), commandEvent.getChannel().getId());

    }
}

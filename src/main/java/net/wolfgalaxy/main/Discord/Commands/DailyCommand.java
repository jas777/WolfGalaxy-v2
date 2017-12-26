package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Discord.DiscordProfiles.Currency;
import net.wolfgalaxy.main.Utils.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.TimeZone;

public class DailyCommand extends Command {

    private Discord discord = Discord.getManager();
    private Currency currency = Currency.getCurrency();
    private MySQL sql = MySQL.getSql();

    public DailyCommand(){
        this.name = "daily";
        this.aliases = new String[]{"dailies"};
        this.help = "Get your daily 200 credits!";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String toExec = "SELECT * FROM mc454.currency WHERE id=" + commandEvent.getAuthor().getId();

        OffsetDateTime lastUse = null;

        try {
            Statement stmt = sql.getConnection().createStatement();

            ResultSet result = stmt.executeQuery(toExec);

            if (result.next()){
                lastUse = result.getTimestamp("last_update").toLocalDateTime().atOffset(OffsetDateTime.now().getOffset());
            } else {
                currency.setupDefaultBalance(commandEvent.getAuthor());
                discord.sendMessage(":credit_card: | You received your daily **200** credits!", commandEvent.getTextChannel().getId());
                currency.setBalance(commandEvent.getAuthor(), currency.getBalance(commandEvent.getAuthor()) + 200);
            }

            result.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        long lastUseToMils = lastUse.toInstant().toEpochMilli();

        long time = (((lastUseToMils + 86340000) - System.currentTimeMillis()) - 3600000);

        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("kk:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone(OffsetDateTime.now().getOffset()));
        String dateFormatted = formatter.format(date);

        if(lastUseToMils > System.currentTimeMillis() - (86340000 - 3600000)){
            discord.sendMessage("You can receive your daily reward in: " + "**" + dateFormatted + "**", commandEvent.getTextChannel().getId());
        } else {
            discord.sendMessage(":credit_card: | You received your daily **200** credits!", commandEvent.getTextChannel().getId());
            currency.setBalance(commandEvent.getAuthor(), currency.getBalance(commandEvent.getAuthor()) + 200);
        }

    }
}

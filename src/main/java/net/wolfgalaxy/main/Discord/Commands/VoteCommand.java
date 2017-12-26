package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;

import java.io.Serializable;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Main.WolfGalaxy;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class VoteCommand extends Command implements Serializable {

    public VoteCommand() {
        this.name = "vote";
        this.aliases = new String[]{"v"};
        this.cooldown = 10;
        this.help = "Commands for poll system.";
    }

    private static TextChannel channel;

    private static HashMap<Guild, Poll> polls = new HashMap<>();

    private static final String[] EMOTI = {":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:", ":keycap_ten:"};


    private class Poll implements Serializable {

        private String creator;
        private String heading;
        private List<String> answers;
        private HashMap<String, Integer> votes;

        private Poll(Member creator, String heading, List<String> answers) {
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.votes = new HashMap<>();
        }

        private Member getCreator(Guild guild) {
            return guild.getMember(guild.getJDA().getUserById(creator));
        }

    }


    private static void message(String content) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content);
        channel.sendMessage(eb.build()).queue();
    }

    private static void message(String content, Color color) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(eb.build()).queue();
    }

    private EmbedBuilder getParsedPoll(Poll poll, Guild guild) {

        StringBuilder ansSTR = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s -> {
            // int votescount = poll.votes.values().stream().filter(i -> i - 1 == count.get()).findFirst().orElse(0);
            /*
                So, wie im Video gesagt, die line oben macht absolut keinen Sinn ^^
                So wie es jetzt ist, nimmt er aus der HashMap<"UserID", "Vote Count"> die Keys, also User ID's und schaut nach,
                ob die dazugehörige vote ID die selbe ist, wie die die angezeigt werden soll. Dann wird der stream
                gezählt und ausgegeben.
             */
            long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
            ansSTR.append(EMOTI[count.get()] + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
            count.addAndGet(1);
        });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s poll.", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:   " + poll.heading + "\n\n" + ansSTR.toString())
                .setFooter("Enter '" + "wg!vote v <number>' to vote!", null)
                .setColor(Color.cyan);

    }

    private void createPoll(String[] args, MessageReceivedEvent event) {

        if (polls.containsKey(event.getGuild())) {
            message("There is already a vote running on this guild!", Color.red);
            return;
        }

        String argsSTRG = String.join(" ", new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
        List<String> content = Arrays.asList(argsSTRG.split("\\|"));
        String heading = content.get(0);
        List<String> answers = new ArrayList<>(content.subList(1, content.size()));

        Poll poll = new Poll(event.getMember(), heading, answers);
        polls.put(event.getGuild(), poll);

        channel.sendMessage(getParsedPoll(poll, event.getGuild()).build()).queue();

    }

    private void votePoll(String[] args, MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            message("There is currently no poll running to vote for!", Color.red);
            return;
        }

        Poll poll = polls.get(event.getGuild());

        int vote;
        try {
            vote = Integer.parseInt(args[1]);
            if (vote > poll.answers.size())
                throw new Exception();
        } catch (Exception e) {
            message("Please enter a valid number to vote for!", Color.red);
            return;
        }

        if (poll.votes.containsKey(event.getAuthor().getId())) {
            message("Sorry, but you can only vote **once** for a poll!", Color.red);
            return;
        }

        poll.votes.put(event.getAuthor().getId(), vote);
        polls.replace(event.getGuild(), poll);
        event.getMessage().delete().queue();

    }

    private void voteStats(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            message("There is currently no vote running!", Color.red);
            return;
        }
        channel.sendMessage(getParsedPoll(polls.get(event.getGuild()), event.getGuild()).build()).queue();

    }

    private void closeVote(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            message("There is currently no vote running!", Color.red);
            return;
        }

        Guild g = event.getGuild();
        Poll poll = polls.get(g);

        if (!poll.getCreator(g).equals(event.getMember())) {
            message("Only the creator of the poll (" + poll.getCreator(g).getAsMention() + ") can close this poll!", Color.red);
            return;
        }

        polls.remove(g);
        channel.sendMessage(getParsedPoll(poll, g).build()).queue();
        message("Poll closed by " + event.getAuthor().getAsMention() + ".", new Color(0xFF7000));

    }

    private void savePoll(Guild guild) throws IOException {

        if (!polls.containsKey(guild))
            return;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";
        Poll poll = polls.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(poll);
        oos.close();
    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {

        if (polls.containsKey(guild))
            return null;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";

        FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Poll out = (Poll) ois.readObject();
        ois.close();
        return out;
    }

    public static void loadPolls(JDA jda) {

        jda.getGuilds().forEach(g -> {

            File f = new File("SERVER_SETTINGS/" + g.getId() + "/vote.dat");
            if (f.exists())
                try {
                    polls.put(g, getPoll(g));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

        });

    }

    @Override
    protected void execute(CommandEvent event) {

        channel = event.getTextChannel();

        MessageReceivedEvent event1 = event.getEvent();

        if (event.getArgs().isEmpty()) {
            event.replyError("Please enter 'wg!help' for list of commands.");
            return;
        } else {

            String[] args = event.getArgs().split(" ");

            switch (args[0]) {

                case "create":
                    if(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                        createPoll(args, event1);
                    } else {
                        event.replyError("You don't have permission to use this command!");
                    }
                    break;

                case "v":
                    votePoll(args, event1);
                    break;

                case "stats":
                    voteStats(event1);
                    break;

                case "close":
                    if(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                        closeVote(event1);
                    } else {
                        event.replyError("You don't have permission to use this command!");
                    }
                    break;
            }
        }

        polls.forEach((g, poll) -> {

            File path = new File(WolfGalaxy.getInstance().getDataFolder() + "/" + "pollData" + "/");
            if (!path.exists())
                path.mkdirs();

            try {
                savePoll(g);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}

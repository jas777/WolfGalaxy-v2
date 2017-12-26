package net.wolfgalaxy.main.Discord.Commands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Discord.music.AudioInfo;
import net.wolfgalaxy.main.Discord.music.PlayerSendHandler;
import net.wolfgalaxy.main.Discord.music.TrackManager;
import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

public class MusicCommands extends Command {

    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();
    private static VoiceChannel musicChannel;
    private static TextChannel musicTextChannel;
    public static String _input;
    private static final String NOTE = ":musical_note:  ";
    private EmbedBuilder eb;

    public MusicCommands() {
        this.name = "music";
        this.aliases = new String[]{"m"};
        this.help = "Shows music commands";
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }

    private AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = MANAGER.createPlayer();
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(nPlayer));
        PLAYERS.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    private AudioEventListener audioEventListener = new AudioEventAdapter() {
        @Override
        public void onTrackStart(AudioPlayer player, AudioTrack track) {
            if (guild.getTextChannelsByName(musicTextChannel.getName(), true).size() > 0) {
                guild.getTextChannelsByName(musicTextChannel.getName(), true).get(0).getManager().setTopic(
                        track.getInfo().title
                ).queue();

                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                ArrayList<AudioInfo> tracks = new ArrayList<>();
                queue.forEach(tracks::add);

                try {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(NOTE + "   **Now Playing**   ")
                            .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false)
                            .addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                    guild.getTextChannelsByName(musicTextChannel.getName(), true).get(0).sendMessage(
                            eb.build()
                    ).queue();
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

            if(musicChannel.getMembers().size() < 2){
                musicTextChannel.sendMessage(":warning: Left channel due to inactivity...");
                musicChannel.getManager().getGuild().getAudioManager().closeAudioConnection();
            }

        }
    };

    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }

    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g.getId()).getKey();
        else
            return createPlayer(g);
    }

    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g.getId()).getValue();
    }

    private boolean isIdle(Guild guild, MessageReceivedEvent event) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.getTextChannel().sendMessage("No music is being played at the moment!").queue();
            return true;
        }
        return false;
    }

    private void loadTrackNext(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        MANAGER.setFrameBufferDuration(100);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                queuedTracks.remove(currentTrack);
                getTrackManager(guild).purgeQueue();
                getTrackManager(guild).queue(currentTrack.getTrack(), author);
                getTrackManager(guild).queue(track, author);
                queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                    Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                    queuedTracks.remove(currentTrack);
                    getTrackManager(guild).purgeQueue();
                    getTrackManager(guild).queue(currentTrack.getTrack(), author);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    private void loadTrack(String identifier, Member author, Message msg) {


        Guild guild = author.getGuild();
        getPlayer(guild);

        msg.getTextChannel().sendTyping().queue();
        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                getTrackManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }

    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    public static VoiceChannel getMusicChannel() {
        return musicChannel;
    }

    public static void setMusicChannel(VoiceChannel musicChannel) {
        MusicCommands.musicChannel = musicChannel;
    }

    public static TextChannel getMusicTextChannel() {
        return musicTextChannel;
    }

    public static void setMusicTextChannel(TextChannel musicTextChannel) {
        MusicCommands.musicTextChannel = musicTextChannel;
    }

    private TrackManager getTrackManager(Guild guild) {
        return PLAYERS.get(guild.getId()).getValue();
    }

    private void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
    }

    @Override
    protected void execute(CommandEvent commandEvent) {

        String[] args = commandEvent.getArgs().split(" ");
        guild = commandEvent.getGuild();

        if (commandEvent.getArgs().isEmpty()) {
            commandEvent.getTextChannel().sendMessage(new EmbedBuilder()
                    .setTitle("WolfGalaxy Music - Help")
                    .setDescription("wg!play [aliases: wg!p] - Adds song to queue.\n\n" +
                            "wg!stop - Stops music.\n\n" +
                            "wg!skip [aliases: wg!s] - Skips current song.\n\n" +
                            "wg!shuffle - Turns on shuffle mode.\n\n" +
                            "wg!now [aliases: wg!info] - Shows info about current played song.\n\n")
                    .build()
            ).queue();
            return;
        }

        switch (args.length) {

            case 1:
            case 2:

                switch (args[0]) {

                    case "n":
                    case "now":
                    case "current":
                    case "nowplaying":
                    case "info":
                        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
                            commandEvent.getTextChannel().sendMessage(NOTE + "No music currently playing!").queue();
                        } else {
                            AudioTrack track = getPlayer(guild).getPlayingTrack();
                            AudioTrackInfo info = track.getInfo();
                            eb
                                    .setColor(Color.orange)
                                    .setDescription(":musical_note:   **Current Track Info**")
                                    .addField(":cd:  Title", info.title, false)
                                    .addField(":stopwatch:  Duration", "`[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`", false)
                                    .addField(":microphone:  Channel / Author", info.author, false);
                            commandEvent.getTextChannel().sendMessage(
                                    eb.build()
                            ).queue();
                        }
                        break;

                    case "queue":
                        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
                            commandEvent.getTextChannel().sendMessage(NOTE + "The queue ist currently empty!").queue();
                        } else {

                            int SideNumbInput = 1;
                            if (args.length > 1)
                                SideNumbInput = Integer.parseInt(args[1]);

                            StringBuilder sb = new StringBuilder();
                            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                            ArrayList<String> tracks = new ArrayList<>();
                            List<String> tracksSublist;
                            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                            if (tracks.size() > 20)
                                tracksSublist = tracks.subList((SideNumbInput - 1) * 20, (SideNumbInput - 1) * 20 + 20);
                            else
                                tracksSublist = tracks;

                            tracksSublist.forEach(s -> sb.append(s));
                            int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;
                            int sideNumb = SideNumbInput;

                            EmbedBuilder eb = new EmbedBuilder();

                            eb.setColor(Color.GREEN).setDescription(
                                    NOTE + "**QUEUE**\n\n" +
                                            "*[" + queue.size() + " Tracks | Side " + sideNumb + "/" + sideNumbAll + "]*\n\n" +
                                            sb
                            );

                            commandEvent.getTextChannel().sendMessage(
                                    eb.build()
                            ).queue();

                        }
                        break;

                    case "s":
                    case "skip":
                        for (int skip = (args.length > 1 ? Integer.parseInt(args[1]) : 1); skip > 0; skip--) {
                            if (isIdle(guild, commandEvent.getEvent())) return;
                            forceSkipTrack(guild);
                        }
                        break;

                    case "stop":

                        getTrackManager(guild).purgeQueue();
                        forceSkipTrack(guild);
                        guild.getAudioManager().closeAudioConnection();

                        break;

                }
            default:

                String input = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (input != null && input.startsWith("http"))
                    _input = input;
                else
                    _input = "ytsearch: " + input;

                switch (args[0].toLowerCase()) {

                    case "p":
                    case "play":
                        if (args.length <= 1) {
                            commandEvent.replyError(":warning:  Please include a valid source.");
                        } else {
                            loadTrack(_input, commandEvent.getMember(), commandEvent.getMessage());

                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                            commandEvent.getTextChannel().sendMessage(
                                new EmbedBuilder().setDescription(NOTE + "Queued `" + tracks + "` Tracks.").setColor(new Color(0, 255, 151)).build()
                             ).queue();
                        }
                        break;

                    case "ps":
                    case "playshuffle":
                        if (args.length <= 1) {
                            commandEvent.replyError(":warning:  Please include a valid source.");
                        } else {
                            loadTrack(_input, commandEvent.getMember(), commandEvent.getMessage());

                            getTrackManager(guild).shuffleQueue();

                            if (getPlayer(guild).isPaused())
                                getPlayer(guild).setPaused(false);

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            commandEvent.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                    case "pn":
                    case "playnext":
                        if (args.length <= 1) {
                            commandEvent.replyError(":warning:  Please include a valid source.");
                        } else {
                            loadTrackNext(_input, commandEvent.getMember(), commandEvent.getMessage());

                            new Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            int tracks = getTrackManager(guild).getQueuedTracks().size();
                                            commandEvent.getTextChannel().sendMessage(
                                                    NOTE + "Queued `" + tracks + "` Tracks."
                                            ).queue();
                                        }
                                    },
                                    5000
                            );
                        }
                        break;

                }
                break;
        }
    }
}

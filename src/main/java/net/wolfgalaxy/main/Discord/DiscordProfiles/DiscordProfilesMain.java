package net.wolfgalaxy.main.Discord.DiscordProfiles;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.wolfgalaxy.main.Discord.Discord;
import net.wolfgalaxy.main.Main.WolfGalaxy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DiscordProfilesMain extends ListenerAdapter {

    private JDA jda = Discord.getManager().getBot();
    private WolfGalaxy plugin = WolfGalaxy.getInstance();
    private Discord discord = Discord.getManager();
    private InputStream in;
    private Image avatarImage = null;
    private Currency currency = Currency.getCurrency();

    public Image getAvatarImage(User user) {

        try {
            URL url = new URL(user.getAvatarUrl());
            URLConnection c = url.openConnection();
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");


            avatarImage = ImageIO.read(c.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avatarImage;

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){

        String[] args = e.getMessage().getContentRaw().split(" ");
        User user = e.getAuthor();


        switch (args[0]){

            case "wg!profile":

                File img = new File(plugin.getDiscordProfilePicsFolder(), user.getId() + ".jpg");
                try {
                    img.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                File defaultPattern = new File(plugin.getDiscordProfilePicsFolder(), "default.jpg");

                BufferedImage bufferedImage = null;
                try {
                bufferedImage = ImageIO.read(defaultPattern);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

                String balance = String.valueOf(currency.getBalance(user));

                ImageObserver observer = null;

                Graphics graphics = bufferedImage.getGraphics();
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Arial Black", Font.PLAIN , 23));
                graphics.drawString(user.getName() + "#" + user.getDiscriminator(), 187, 76);
                graphics.setFont(new Font("Arial Black", Font.PLAIN , 15));
                graphics.drawString("Credits: " + balance, 187, 200);
                graphics.drawImage(getAvatarImage(user).getScaledInstance(128, 128, 0), 21, 52, observer);

                try {
                    ImageIO.write(bufferedImage, "jpg", img);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                Message msg = new MessageBuilder().append(user.getAsMention() + "'s profile card").build();

                e.getChannel().sendFile(img, msg).queue();


        }
    }

}

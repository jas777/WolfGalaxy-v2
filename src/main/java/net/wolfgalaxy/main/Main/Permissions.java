package net.wolfgalaxy.main.Main;

public enum Permissions {

    COMMAND_BROADCAST,
    CHAT_BYPASS,
    DISCORD_ANNOUNCE,
    CHANNEL_STAFFGENERAL,
    CHANNEL_LOWERSTAFF,
    CHANNEL_HIGHERSTAFF,
    CHANNEL_LEADERSHIP,
    CHANNEL_SITH,
    CHANNEL_JEDI,
    CHAT_CONTROL;

    public static String getPermission(Permissions type) {
        String permission = "";

        switch (type){
            case COMMAND_BROADCAST:
                permission = "wg.commands.broadcast";
                break;
            case CHAT_BYPASS:
                permission = "wg.chat.bypass";
                break;
            case DISCORD_ANNOUNCE:
                permission = "wg.discord.announce";
                break;
            case CHANNEL_LEADERSHIP:
                permission = "wg.channels.leadership";
                break;
            case CHANNEL_HIGHERSTAFF:
                permission = "wg.channels.higherstaff";
                break;
            case CHANNEL_LOWERSTAFF:
                permission = "wg.channels.lowerstaff";
                break;
            case CHANNEL_STAFFGENERAL:
                permission = "wg.channels.staffgeneral";
                break;
            case CHANNEL_JEDI:
                permission = "wg.channels.jedi";
                break;
            case CHANNEL_SITH:
                permission = "wg.channels.sith";
                break;
            case CHAT_CONTROL:
                permission = "wg.chat.control";
                break;
        }

        return permission;
    }

}

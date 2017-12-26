package net.wolfgalaxy.main.Main;

public enum Messages {

    MESSAGE_CURSE,
    CHAT_DISABLED;

    private WolfGalaxy wolfGalaxy = WolfGalaxy.getPlugin(WolfGalaxy.class);

    public static String getMessage(Messages type, WolfGalaxy wolfGalaxy){

        String message = "";

        switch (type){
            case MESSAGE_CURSE:
                message = wolfGalaxy.getConfig().getString("messages.curse");
                break;
            case CHAT_DISABLED:
                message = wolfGalaxy.getConfig().getString("message.chatDisabled");
                break;
        }

        return message;

    }

}

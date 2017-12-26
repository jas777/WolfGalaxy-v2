package net.wolfgalaxy.main.Achievements;

import org.bukkit.ChatColor;

public enum AchievementStates {

    COMPLETED("Completed", ChatColor.DARK_GREEN),
    IN_PROGRESS("In Progress", ChatColor.YELLOW),
    NOT_STARTED("Not Started", ChatColor.RED);

    public ChatColor colorCode;
    public String text;

    AchievementStates(String _text, ChatColor _color) {
        this.text = _text;
        this.colorCode = _color;
    }

}

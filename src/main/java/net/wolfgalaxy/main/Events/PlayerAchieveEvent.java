package net.wolfgalaxy.main.Events;

import net.wolfgalaxy.main.Achievements.AchievementStates;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAchieveEvent extends Event implements Cancellable {

    private Player player;
    private String achievementName;
    private AchievementStates state;

    public PlayerAchieveEvent(Player player, String achievementName, AchievementStates state){

        this.player = player;
        this.achievementName = achievementName;
        this.state = state;

    }

    public Player getPlayer() {
        return player;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public AchievementStates getState() {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

}

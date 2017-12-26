package net.wolfgalaxy.main.Main;

public enum Errors {

    PLAYER_ONLY_COMMAND, NO_PERMISSIONS, TOO_FEW_ARGUMENTS, TOO_MANY_ARGUMENTS, FILE_DOES_NOT_EXIST, ITEM_IN_HAND_IS_EMPTY, PLAYER_DOES_NOT_EXIST, PLAYER_IS_NOT_ONLINE, VALUE_IS_NOT_NUMBER, VALUE_DOES_NOT_EXIST, FULL_INVENTORY, COMMAND_IS_INVALID, SIGN_LINE_INVALID, PLAYER_ALREADY_MUTED, PLAYER_MUTED, PLAYER_NOT_MUTED, ALLEGIANCE_ALREADY_CHOSEN;

    public static String getErrorMessage(Errors type) {
        String msg = "";

        if(type == PLAYER_ONLY_COMMAND) {
            msg = "Only players can use this command!";
        } else if(type == NO_PERMISSIONS) {
            msg = "You do not have permission to use this command";
        } else if(type == TOO_FEW_ARGUMENTS) {
            msg = "You've entered too few arguments!";
        } else if(type == TOO_MANY_ARGUMENTS) {
            msg = "You've entered too many arguments!";
        } else if(type == FILE_DOES_NOT_EXIST) {
            msg = "File does not exist!";
        } else if(type == ITEM_IN_HAND_IS_EMPTY) {
            msg = "The item in your hand is empty!";
        } else if(type == Errors.PLAYER_DOES_NOT_EXIST) {
            msg = "The entered player is invalid or is not online!";
        } else if(type == Errors.PLAYER_IS_NOT_ONLINE) {
            msg = "The entered player is not online!";
        } else if(type == Errors.VALUE_IS_NOT_NUMBER) {
            msg = "The entered value is not a valid number!";
        } else if(type == Errors.VALUE_DOES_NOT_EXIST) {
            msg = "The entered value does not exist!"; // My error enum XD
        } else if(type == Errors.FULL_INVENTORY) {
            msg = "The inventory is full!";
        } else if(type == Errors.COMMAND_IS_INVALID) {
            msg = "The entered command argument is invalid!";
        } else if(type == Errors.SIGN_LINE_INVALID) {
            msg = "One of the sign lines is invalid!";
        } else if(type == Errors.PLAYER_ALREADY_MUTED) {
            msg = "This player is already muted!";
        } else if(type == Errors.PLAYER_NOT_MUTED) {
            msg = "This player is already unmuted!";
        } else if(type == Errors.PLAYER_MUTED) {
            msg = "You are muted. You can't talk!";
        } else if(type == Errors.ALLEGIANCE_ALREADY_CHOSEN) {
            msg = "You can choose allegiance only once!";
        }
        return msg;
    }
}

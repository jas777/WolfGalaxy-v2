package net.wolfgalaxy.main.Discord.DiscordProfiles;

import net.dv8tion.jda.core.entities.User;
import net.wolfgalaxy.main.Utils.MySQL;

import java.sql.*;
import java.time.OffsetDateTime;

public class Currency {

    private int balance;
    private MySQL sql = MySQL.getSql();
    private static Currency currency;

    public static Currency getCurrency() {
        if(currency == null){
            currency = new Currency();
        }
        return currency;
    }

    public void setupDefaultBalance(User user){

        try {
            String _stmt = "INSERT INTO mc454.currency(id, name, balance, last_update) VALUES (?, ?, ?, ?)";
            Statement stmt = sql.getConnection().createStatement();

            PreparedStatement preparedStatement = sql.getConnection().prepareStatement(_stmt);
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setInt(3, 1000);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime()));
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
            stmt.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBalance(User user) {


        try {

            Statement stmt = sql.getConnection().createStatement();

            ResultSet myRs = stmt.executeQuery("SELECT * FROM mc454.currency WHERE id=" + user.getId());
            if(myRs.next()) {
                balance = myRs.getInt("balance");
            } else {
                setupDefaultBalance(user);
                balance = getBalance(user);
            }
            myRs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;

    }

    public void setBalance(User user, int amount){
        try{

            String sqlCode = "UPDATE mc454.currency SET name = ?, balance = ?, last_update = ? WHERE id = ?";

            PreparedStatement preparedStatement = sql.getConnection().prepareStatement(sqlCode);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, amount);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(OffsetDateTime.now().toLocalDateTime()));
            preparedStatement.setString(4, user.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

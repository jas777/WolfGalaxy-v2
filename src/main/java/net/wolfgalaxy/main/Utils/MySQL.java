package net.wolfgalaxy.main.Utils;

import java.sql.*;
import java.time.OffsetDateTime;

public class MySQL {

    private static MySQL instance;
    private Connection con;

    public static MySQL getSql(){
        if(instance == null){
            instance = new MySQL();
        }

        return instance;
    }

    public Connection getConnection(){

        try {
            con = DriverManager.getConnection("jdbc:mysql://142.4.206.183:3306/", "mc454", "49e0cf8e94");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }

    public void testConnection(){

        try {
            Statement stmt = con.createStatement();

            String insertTest = "INSERT INTO mc454.test(test1, test2) VALUES(?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(insertTest);
            preparedStatement.setString(1, OffsetDateTime.now().toString());
            preparedStatement.setString(2, OffsetDateTime.now().toString());
            preparedStatement.executeUpdate();

            ResultSet myRs = stmt.executeQuery("SELECT * FROM mc454.test");

            while (myRs.next()){
                System.out.println(myRs.getString("test1"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

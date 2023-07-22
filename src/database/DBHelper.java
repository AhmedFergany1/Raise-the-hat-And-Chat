package database;

import java.sql.*;
import java.util.ArrayList;

public class DBHelper {

    public static Connection get_DB_Connection() {
        final String DB_SERVER = "localhost";
        final String PORT = "3306";
        final String DB_NAME = "dsDB";
        final String DB_URL = "jdbc:mysql://" + DB_SERVER + ":" + PORT + "/" + DB_NAME;
        final String USER = "root";
        final String PASS = "mynewpassword";
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Database is not connected !");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean search(String username, String password) {
        boolean existed = false;
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "select username, password from users where username=? and password=?";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet results = stmt.executeQuery();
            existed = results.next();
            conn.close();
            return existed;
        } catch (SQLException e) {
            e.printStackTrace();
            return existed;
        }
    }

    public static boolean searchForUsername(String username) {
        boolean existed = false;
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "select userId from users where username=? ";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1, username);
            ResultSet results = stmt.executeQuery();
            existed = results.next();
            conn.close();
            return existed;
        } catch (SQLException e) {
            e.printStackTrace();
            return existed;
        }
    }

    public static String getLoggedInName(String username) {
        boolean existed = false;
        String name ="";
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "select name from users where username=? ";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1, username);
            ResultSet results = stmt.executeQuery();
            existed = results.next();
            name = results.getString("name");
            conn.close();
            if (existed) {
                return name;
            } else {
                return "NOBODY";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "NOBODY";
        }
    }

    public static void insert(String name, String username, String password) {
        try {
            boolean existed = search(username, password);
            if (existed) {
                System.out.println("User is already existed before !");
            } else {
                Connection conn = get_DB_Connection();

                String sql_query = "insert into users (name,username,password) values (?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql_query);
                stmt.setString(1, name);
                stmt.setString(2, username);
                stmt.setString(3, password);
                stmt.executeUpdate();
                System.out.println("Insertion is done");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePassword(String username, String password) {
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "update users set password=? where username=?";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Updating password is done");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String username, String password) {
        try {
            boolean existed = search(username, password);
            if (!existed) {
                System.out.println("User is not founded to be deleted !");
            } else {
                Connection conn = get_DB_Connection();
                String sql_query = "delete from users where username= ? and password=?";
                PreparedStatement stmt = conn.prepareStatement(sql_query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();
                System.out.println("Deleting is done");
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getAllUsers(String name) {
        ArrayList<String> allUsers = new ArrayList<>();
        boolean emptyTable = true;
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "select username from users WHERE name != ? ";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            stmt.setString(1,name);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                // Retrieve each row data
                allUsers.add(results.getString("username"));
                emptyTable = false;
            }
            if (emptyTable) {
                ArrayList<String> noUsers = new ArrayList<>();
                noUsers.add("No users yet");
                return noUsers;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public static void getAllData() {
        boolean emptyTable = true;
        try {
            Connection conn = get_DB_Connection();
            String sql_query = "select * from users";
            PreparedStatement stmt = conn.prepareStatement(sql_query);
            ResultSet results = stmt.executeQuery();
            System.out.println("****** Table data *****");
            while (results.next()) {
                // Retrieve each row data
                System.out.print("id: " + results.getLong("id"));
                System.out.print(" , username: " + results.getString("username"));
                System.out.println(" , password: " + results.getString("password"));
                emptyTable = false;
            }
            if (emptyTable) {
                System.out.println("Users table is empty !");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    public static void main(String[] args) {

        boolean existed = search("mustafa7egazi", "15234");
        if (existed) {
            System.out.println("this user is existed");
        } else {
            System.out.println("this user is not existed");
        }

        //insert("Mustafa reda","mustafa7egazi", "1234");

        //  update("Kareem", "9999");
//     delete("Kareem", "9999");
        //getAllData();
    }

     */
}

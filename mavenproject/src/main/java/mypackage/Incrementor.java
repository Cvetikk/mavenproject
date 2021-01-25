package mypackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Incrementor {

    private static final Connection c = getConnection();

    private static volatile int i = getFromBase();

    private static final String sql1 = "INSERT INTO counter(count,key) \n"
            + "VALUES (";
    private static final String sql2 = ",'key') \n"
            + " ON CONFLICT (key) DO UPDATE SET \n"
            + "count=";

    public static int addClickWithSave() {
        synchronized (Incrementor.class) {
            i++;
            save(sql1 + i + sql2 + i);
            return i;
        }
    }
    
    public static int addClick() {
        synchronized (Incrementor.class) {
            i++;
            return i;
        }
    }

    public static int getClick() {
        synchronized (Incrementor.class) {
            return i;
        }
    }

    private static void save(String sql) {
        synchronized (Incrementor.class) {
            try {
                Statement stmnt = c.createStatement();
                stmnt.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private static Connection getConnection()  {
        Map<String, String> mp = new HashMap();
        try (FileReader reader = new FileReader("props.txt");
                BufferedReader br = new BufferedReader(reader)) {
            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                String[] mas = line.split("=");
                mp.put(mas[0], mas[1]);                
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager
                    .getConnection(mp.get("DB_URL"), mp.get("USER"), mp.get("PASS"));
            return con;
        } catch (ClassNotFoundException|SQLException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return null;
        }
    }

    private static int getFromBase() {
        try {
            if (c == null) {
                return 0;
            }
            Statement stmnt = c.createStatement();
            ResultSet rs
                    = stmnt.executeQuery("select count from counter where key='key'");
            while (rs.next()) {
                int id = rs.getInt(1);
                return id;
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
        return 0;
    }

}

package de.unidue.inf.is.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.db2.jcc.DB2Driver;


public final class DBUtil {

    static {
        com.ibm.db2.jcc.DB2Driver driver = new DB2Driver();
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new Error("Laden des Datenbanktreiber nicht möglich");
        }
    }


    private DBUtil() {
    }

    public static Connection getConnection(String database) throws SQLException {
        final String url = "jdbc:db2:" + database;
        return DriverManager.getConnection(url);
    }


    public static Connection getExternalConnection(String database) throws SQLException {
        Properties properties = new Properties();
        System.out.println("1");
        properties.setProperty("securityMechanism",
                Integer.toString(com.ibm.db2.jcc.DB2BaseDataSource.CLEAR_TEXT_PASSWORD_SECURITY));
        properties.setProperty("user", "dbp51");
        properties.setProperty("password", "piha9ang");

        final String url = "jdbc:db2://helios.is.inf.uni-due.de:50051/" + database + ":currentSchema=<dbp51>;";
        System.out.println(url);
        Connection connection = DriverManager.getConnection(url, properties);
        return connection;
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.ibm.db2.jcc.DB2Driver");

        Properties properties = new Properties();
        properties.setProperty("securityMechanism", Integer.toString(com.ibm.db2.jcc.DB2BaseDataSource.USER_ONLY_SECURITY));
        properties.setProperty("user", "dbp51");
        properties.setProperty("password", "piha9ang");

        Connection connection = DriverManager.getConnection("jdbc:db2://helios.is.inf.uni-due.de:50051/babble:currentSchema=DBP51;", properties);

        return connection;
    }


    public static boolean checkDatabaseExistsExternal(String database) {
        // Nur für Demozwecke!
        boolean exists = false;

        try (Connection connection = getExternalConnection(database)) {
            exists = true;
        } catch (SQLException e) {
            exists = false;
            e.printStackTrace();
        }
        return exists;
    }


    public static boolean checkDatabaseExists(String database) {
        // Nur für Demozwecke!
        boolean exists = false;

        try (Connection connection = getConnection(database)) {
            exists = true;
        } catch (SQLException e) {
            exists = false;
            e.printStackTrace();
        }

        return exists;
    }

}

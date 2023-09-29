package test;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class Main {

//    static final String DB_URL = "jdbc:hsqldb:file:C:\\Program Files\\Collaborator Server\\tomcat/database";
//    static final String TARGET_DB = "jdbc:hsqldb:file:C:\\Users\\olkan\\IdeaProjects\\collab-core\\tomcat/database";
    static final String DB_URL = "jdbc:hsqldb:file:C:\\Users\\olkan\\IdeaProjects\\collab-core\\tomcat/database";
    static final String TARGET_DB = "jdbc:hsqldb:file:C:\\Program Files\\Collaborator Server\\tomcat/database";
    //  Database credentials
    static final String USER = "sa";
    static final String PASS = "";
    static  String DRIVER = "org.hsqldb.jdbcDriver";

    public static void main(String[] args) {
        try {
            Class.forName ("org.hsqldb.jdbcDriver").newInstance ();
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        List<String> columnNames = Arrays.asList(new String[] {"USER_PASSWORD", "USER_NAME", "USER_EMAIL", "USER_PHONE", "USER_INITIALS", "USER_GUID", "USER_DEPARTMENT",
        "USER_FIRSTNAME", "USER_LASTNAME", "USER_LOGIN"});
        List<String> booleans = Arrays.asList(new String[] {"USER_ADMIN", "USER_TUTORIALS", "USER_ACTIVE", "USER_NEXTPAGESCROLL", "USER_CREATECHILDGROUPS", "USER_EDITTEMPLATES",
            "USER_EDITCUSTOMFIELDS", "USER_EDITCHECKLISTS", "USER_EDITROLES", "USER_EDITAUTOMATICLINKS"});
        List<String> dates = Arrays.asList(new String[] { "USER_LASTLOGIN", "USER_LASTLOGOUT","USER_LASTACTIVITY"});
        String insertStmt = "insert into user (" + String.join(", ", columnNames) + ", " +
                String.join(", ", booleans) + ", " + String.join(", ", dates) + ") " + " VALUES (";
        StringBuilder questions = new StringBuilder();
        for(int i = 0; i < columnNames.size() + booleans.size() + dates.size() - 1; i++){
            questions.append("?, ") ;
        }
        questions.append("?)");
        insertStmt = insertStmt.concat(questions.toString());
        Connection conn = null;
        Statement stmt = null;
        List<String> columnNames = Arrays.asList(new String[] {"USER_PASSWORD", "USER_NAME", "USER_EMAIL", "USER_PHONE", "USER_INITIALS", "USER_GUID", "USER_DEPARTMENT",
                "USER_FIRSTNAME", "USER_LASTNAME", "USER_LOGIN"});
        List<String> booleans = Arrays.asList(new String[] {"USER_ADMIN", "USER_TUTORIALS", "USER_ACTIVE", "USER_NEXTPAGESCROLL", "USER_CREATECHILDGROUPS", "USER_EDITTEMPLATES",
                "USER_EDITCUSTOMFIELDS", "USER_EDITCHECKLISTS", "USER_EDITROLES", "USER_EDITAUTOMATICLINKS"});
        List<String> dates = Arrays.asList(new String[] { "USER_LASTLOGIN", "USER_LASTLOGOUT","USER_LASTACTIVITY"});
        String insertStmt = "insert into user (" + String.join(", ", columnNames) + ", " +
                String.join(", ", booleans) + ", " + String.join(", ", dates) + ") " + " VALUES (";
        StringBuilder questions = new StringBuilder();
        for(int i = 0; i < columnNames.size() + booleans.size() + dates.size() - 1; i++){
            questions.append("?, ") ;
        }
        questions.append("?)");
        insertStmt = insertStmt.concat(questions.toString());

        questions.append("?)");
        insertStmt = insertStmt.concat(questions.toString());
    }
        questions.append("?)");
    insertStmt = insertStmt.concat(questions.toString());
        try {
            // Create database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Create and execute statement
            stmt = conn.createStatement();
            ResultSet rs =  stmt.executeQuery("select * from user");
            List<Map<String, Object>> users = new ArrayList();
            // Loop through the data and print all artist names
            while(rs.next()) {
                Map<String, Object> user = new HashMap<>();
                for(String column: columnNames){
                    user.put(column, rs.getString(column));
                }
                for(String column: booleans){
                    user.put(column, rs.getString(column));
                }
                for(String column: dates){
                    user.put(column, rs.getDate(column));
                }
                users.add(user);
            }

            // Clean up
            rs.close();
            stmt.close();
            try(Connection conn2 = DriverManager.getConnection(TARGET_DB, USER, PASS)){
                try(PreparedStatement preparedStatement = conn2.prepareStatement(insertStmt)) {
                    for (Map<String, Object> values : users) {
                        if(!"admin".equals(values.get("USER_LOGIN")) && !"mariya".equals(values.get("USER_LOGIN"))){

                        int counter = 1;
                        for (String column : columnNames) {
                            preparedStatement.setString(counter++, (String) values.get(column));
                        }
                        for (String column : booleans) {
                            preparedStatement.setString(counter++, (String) values.get(column));
                        }
                        for (String column : dates) {
                            preparedStatement.setDate(counter++, (Date) values.get(column));
                        }
                        preparedStatement.addBatch();
                        }
                    }
                    preparedStatement.executeBatch();
                }

            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                // Close connection
                if (conn != null)
                    conn.close();
            }
            catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}

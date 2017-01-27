package webcrawler.db;/**
 * Created by Gabe Sargeant on 22/01/17.
 * This is the DB Handler thread. It takes care oh all mysql db interactions.
 */


import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import static webcrawler.Main.getDatabaseSettings;

public class DBHandler {
    private String host, port, dbUser, dbName, psw;

    private Properties dbproperties = new Properties();

    public DBHandler(){

        try {
            String databaseSettings[] = getDatabaseSettings();
            host = databaseSettings[0];
            port = databaseSettings[1];
            dbName = databaseSettings[2];
            dbUser = databaseSettings[3];
            psw = databaseSettings[4];

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnectionDB(){

        Connection con = null;

        String connectionString = "jdbc:mysql://"+host+":"+port+"/"+dbName+"?allowMultipleQueries=true";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connectionString, dbUser, psw);
        }catch (Exception e){
            Logger.error("There was an error connecting to the dataase" + e);
        }

        return con;
    }

    public boolean testConnection()
    {
        Connection con = getConnectionDB();
        if(con != null){
            Logger.info("Connection Tested OK!");
            return true;
        }
        else {
            Logger.error("Error with Connection to DB, Check DBproperties file OR MySQL DB status.");
            return false;
        }

    }

    public int existingCrawl(String URI){
        int ans = 0;
        try{
            String SQL = "select 1 from queue where uri = ? limit 1;";
            Connection con = getConnectionDB();

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, URI);

            ResultSet rs = stmt.executeQuery();
            ans = rs.getInt(0);

        }catch (SQLException e){
            Logger.error("There was and exception withe running sql"  +  e);
        }

        return ans;

    }

    //get the next target URL to Scrape from the DB.
    public String getNextTarget(String BASE_URI) {
        String nextTarget="";
        String pkey="";
        String SQL = "SELECT pkey, url FROM queue WHERE visited = false AND uri = ? limit 1";


        try {
            //prep statement
            Connection con = getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, BASE_URI);

            //execute and extract details
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                pkey = rs.getString(1);
                nextTarget = rs.getString(2);
            }

            //Logger.info(" The following URL will be targeted." + nextTarget);
            //if the setProcessed SQL can't execute then set the target to be
            //the base uri. essentially skipping

            if (!setProcessed(pkey)) {
                nextTarget = null;
            }

        }catch (Exception e){
            Logger.info("There was an error fetching the next Target URL from the queue " + e.getMessage());
        }

        return nextTarget;
    }

    public boolean setProcessed(String pkey) {
        Boolean result=false;
        try{
            String SQL = "UPDATE queue SET visited = true WHERE pkey = ?";
            //prep statement
            Connection con = getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1,pkey);

            //execute and extract details
            stmt.executeUpdate();
            result = true;

        }catch (Exception e){
            Logger.info("There was an error updating the status of the page visited" + e);
            result = false;
        }

        return result;
    }

    public void insertNewURL(String uri, ArrayList<String> arrayList) {

        try {

            //generic insert
            String SQL = "INSERT IGNORE INTO queue (pkey, uri, visited, url) VALUES (md5(?), ?, FALSE, ?)";

            Connection con = getConnectionDB();

//            PreparedStatement checkStmt = con.prepareStatement(SET_CHECK);
//            for(int j = 0; j < arrayList.size(); j++) {
//                checkStmt.setString(1, arrayList.get(j));
//                ResultSet rs = checkStmt.executeQuery();
//                if (rs.next()){
//                    if(rs.getInt(1) == 1) arrayList.remove(j);
//                }
//            }

            PreparedStatement stmt = con.prepareStatement(SQL);
            //bind values

            for(int i = 0; i < arrayList.size(); i++){
                stmt.setString(1,arrayList.get(i));
                stmt.setString(2,uri);
                stmt.setString(3,arrayList.get(i));
                //execute
                stmt.executeUpdate();
            }




        } catch (SQLException e) {
            Logger.info("There was an error inserting found URLS into the DB" + e);
        }


    }

    public int count(String uri) {
        int ans=0;
        try{
            String SQL = "SELECT COUNT(*) FROM queue WHERE uri = ?";

            Connection con = getConnectionDB();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, uri);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                ans = rs.getInt(1);
            }
        }catch (SQLException e){
            Logger.error("There was an error get a count from the queue " + e);
        }
        return ans;
    }
}

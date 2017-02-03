package webcrawler.db;/**
 * Created by Gabe Sargeant on 22/01/17.
 * This is the DB Handler thread. It takes care oh all mysql db interactions.
 */


import org.pmw.tinylog.Logger;
import webcrawler.digestpage.Digest;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static webcrawler.Main.getDatabaseSettings;

public class DBHandler {
    private String host, port, dbUser, dbName, psw, mig_host, mig_port, mig_dbName, mig_dbUser, mig_psw;

    private Properties dbproperties = new Properties();

    public DBHandler(){

        try {
            String databaseSettings[] = getDatabaseSettings();
            host = databaseSettings[0];
            port = databaseSettings[1];
            dbName = databaseSettings[2];
            dbUser = databaseSettings[3];
            psw = databaseSettings[4];

            //results db info
            mig_host=databaseSettings[5];
            mig_port=databaseSettings[6];
            mig_dbName=databaseSettings[7];
            mig_dbUser=databaseSettings[8];
            mig_psw=databaseSettings[9];

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

    public Connection getConnectionResultsDB() {

        Connection con = null;

        String connectionString = "jdbc:mysql://"+mig_host+":"+mig_port+"/"+mig_dbName+"?allowMultipleQueries=true";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connectionString, mig_dbUser, mig_psw);
        }catch (Exception e){
            Logger.error("There was an error connecting to the "+ mig_dbName + " dataase" + e);
        }


        return con;
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
            con.close();
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
            con.close();
        }catch (Exception e){
            Logger.error("There was an error fetching the next Target URL from the queue " + e.getMessage());
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
            con.close();
        }catch (Exception e){
            Logger.error("There was an error updating the status of the page visited" + e);
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


            con.close();

        } catch (SQLException e) {
            Logger.error("There was an error inserting found URLS into the DB" + e);
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
            con.close();
        }catch (SQLException e){
            Logger.error("There was an error get a count from the queue " + e);
        }
        return ans;
    }

    public void submitDigest(Digest digest) {
        String date = digest.getDate();
        String title = digest.getTitle();
        String url = digest.getURL();
        String country = digest.getCountry();
        String member = digest.getMember();
        String decisionText = digest.getDecisionText();
        String decisionResult = digest.getDecisionResult();
        String place = digest.getPlace();

        try{
            String SQL = "INSERT IGNORE INTO RRTA (pkey, title, date, url, country, member, place,  decision_text, decision) " +
                    "VALUES (md5(?),?,?,?,?,?,?,?,?)";

            Connection con = getConnectionResultsDB();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, url); //An MD5 has of the title will be the pkey
            stmt.setString(2, title);
            stmt.setString(3, date);
            stmt.setString(4, url);
            stmt.setString(5, country);
            stmt.setString(6, member);
            stmt.setString(7, place);
            stmt.setString(8, decisionText);
            stmt.setString(9, decisionResult);

            stmt.executeUpdate();
            Logger.debug("Digest put to db without error");
            con.close();
        }catch (SQLException e){
            Logger.error("There was an error with your sql inserting a page digest" +  e);
        }
    }
}

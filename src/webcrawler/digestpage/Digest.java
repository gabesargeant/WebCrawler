package webcrawler.digestpage;

import org.pmw.tinylog.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by voltron on 29/01/17.
 */
public class Digest {

    private Date date;
    private String title;
    private String URL;
    private String country;
    private String member;
    private String decisionText;
    private String decisionResult;
    private String place;

    public Digest()
    {
        date = null;
        title = "";
        URL = "";
        country = "";
        member = "";
        decisionText = "";
        decisionResult = "";
        place="";
    }

    public void setDate(Date date) {





        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setMember(String member) {

        if(member.length() > 250){
            member = member.substring(0,249);
        }
        this.member = member;
    }

    public void setDecisionText(String decisionText) {

        if(decisionText.length() > 999){
            decisionText = decisionText.substring(0,999);
        }
        this.decisionText = decisionText;
    }

    public void setDecisionResult(String decisionResult) {
        this.decisionResult = decisionResult;

    }

    public void setPlace(String place) {
        this.place = place;
    }



    //Start getters

    public String getDate() {
        String ans;
        String datetime ="";
        if(this.date != null)
        {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            datetime = sdf.format(date);
            Logger.info("#######################3 Date = " +  datetime);

        }else{


        }

        return datetime;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public String getCountry() {
        return country;
    }

    public String getMember() {
        return member;
    }

    public String getDecisionText() {
        return decisionText;
    }

    public String getDecisionResult() {
        return decisionResult;
    }

    public String getPlace() {
        return place;
    }

    public void print() {


    if(date != null){
        System.out.println("DATE" + date.toString());
    }
        System.out.println("TITLE " + title);
        System.out.println("URL " + URL);
        System.out.println("COUNTRY " + country);
        System.out.println("MEMBER" +member);
        System.out.println("DECISIONTEXT " + decisionText);
        System.out.println("DECISIONRESULT " + decisionResult);
        System.out.println("PLACE " + place);


    }
}

package webcrawler.digestpage;

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
        this.member = member;
    }

    public void setDecisionText(String decisionText) {
        this.decisionText = decisionText;
    }

    public void setDecisionResult(String decisionResult) {
        this.decisionResult = decisionResult;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
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

}

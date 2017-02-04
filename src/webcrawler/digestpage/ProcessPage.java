package webcrawler.digestpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;
import webcrawler.db.DBHandler;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Gabe Sargeant on 23/01/17.
 */
public class ProcessPage {

    private final String BASE_URI;
    private String TARGET_URL;
    private DBHandler dbHandler;
    private DigestPageRRTA digestPageRRTA;
    private DigestPageAATA digestPageAATA;



    public ProcessPage(String baseURI) {
        BASE_URI = baseURI;
        dbHandler = new DBHandler();

        //prepares page digest mech
        digestPageRRTA = new DigestPageRRTA();
        digestPageAATA = new DigestPageAATA();
    }

    public void next() {
        TARGET_URL = dbHandler.getNextTarget(BASE_URI);
    }

    public boolean run() {
        extractLinks(TARGET_URL);


        return true;
    }

    public int count() {
        return dbHandler.count(BASE_URI);
    }

    public void extractLinks(String target) {
        //Logger.info("Target URL" + target);

        Document doc = null;
        Digest digest = null;
        ArrayList<String> arrayList = new ArrayList<>();
        String roughURL="";
        try {
            doc = Jsoup.connect(target).get();
            //create a list of elements that are all urls
            Elements questions = doc.select("a[href]");

            for (Element link : questions) {
                if ((link.attr("abs:href").contains(BASE_URI)) && (!link.attr("abs:href").contains("#discourse-comments"))) {
                    roughURL = link.attr("abs:href");

                    arrayList.add(clean(roughURL));
                }
            }

            //Links are sorted.

            //##############################################################
            //now to scan the document and digest contents

            //########## RRTA #############################33
            //digest = digestPageRRTA.consume(doc, target);
            if(doc.title().toLowerCase().contains("refugee")){
                digest = digestPageAATA.consume(doc, target);
                dbHandler.submitDigestAATA(digest);
                digest.print();
            }



             //debug only.

            //this one for indexing the RRTA
            //dbHandler.submitDigestRRTA(digest);


            //This one for submitting the AATA



            //Insert newURL
            dbHandler.insertNewURL(BASE_URI, arrayList);

        } catch (IOException e) {
            Logger.error("There was an error trying to connect to the target URL " + target + e);

        }

    }

    private String clean(String roughURL) {
        String cleanURL;
        int end = roughURL.lastIndexOf("#");
        if(end == -1)
            end = roughURL.lastIndexOf("?");

        if(end == -1) {
            cleanURL = roughURL;
        }else{
            Logger.debug("Clean URL" + roughURL);
            cleanURL = roughURL.substring(0, end);
            Logger.debug("Clean URL" + cleanURL);
        }
        
        return  cleanURL;
    }


}

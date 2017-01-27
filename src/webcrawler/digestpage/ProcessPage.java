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

    private String TARGET_URL;
    private final String BASE_URI;
    private DBHandler dbHandler;

    public ProcessPage(String baseURI){
        BASE_URI = baseURI;
        dbHandler = new DBHandler();
        // fetch from db a URL that is yet to be explored.

    }

    public void next()
    {
        TARGET_URL = dbHandler.getNextTarget(BASE_URI);
    }

    public boolean run(){
        extractLinks(TARGET_URL);

        return true;
    }

    public int count()
    {
        return dbHandler.count(BASE_URI);
    }

    public void extractLinks(String target) {
        Logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>The target is ::" + target);

        ArrayList <String> arrayList = new ArrayList<>();
        try{
            Document doc = Jsoup.connect(target).get();
            //create a list of elements that are all urls
            Elements questions = doc.select("a[href]");

            for(Element link: questions) {
                if((link.attr("abs:href").contains(BASE_URI)) && (! link.attr("abs:href").contains("#discourse-comments"))){
                    arrayList.add(link.attr("abs:href"));
                }
            }

        } catch (IOException e) {
            Logger.error("There was an error trying to connect to the target URL " + target + e);
        }


        dbHandler.insertNewURL(BASE_URI,arrayList);
    }


}

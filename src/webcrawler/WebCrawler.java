package webcrawler;

import org.pmw.tinylog.Logger;
import webcrawler.digestpage.ProcessPage;

/**
 * Created by Gabe Sargeant on 22/01/17.
 */
public class WebCrawler {
    private String URI, WORDS;
    private String BASE_URI;

    WebCrawler(String uri, String words) {
        URI = uri;
        WORDS = words;

        if(uri.contains("http://www.")){
            BASE_URI = uri.replace("http://www.","");
        }
        else if(uri.contains("http://")){
            BASE_URI = uri.replace("http://","");
        }
        else
            BASE_URI = uri;

    }

    public void startCrawl(){
        Logger.info("URI ############# = " + BASE_URI);

            ProcessPage p = new ProcessPage(BASE_URI);
            p.extractLinks(BASE_URI);
        for (int i = 0; i < 1000; i++){
            p.next();
            p.run();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


       // crawl(URI);

        //checkDBforExistingURI
        //if existing queue. Spin off runnable
        //ifnot process first page then start crawling pages.





    }


}

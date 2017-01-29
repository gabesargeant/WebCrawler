package webcrawler;

import org.pmw.tinylog.Logger;
import webcrawler.digestpage.ProcessPage;

/**
 * Created by Gabe Sargeant on 22/01/17.
 */
public class WebCrawler {
    private String URI;
    private String BASE_URI;

    WebCrawler(String uri) {
        URI = uri;

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
        Logger.info(URI);

            ProcessPage p = new ProcessPage(URI);
            p.extractLinks(URI);
        for (int i = 0; i < 1000; i++){
            p.next();
            p.run();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }




    }
}

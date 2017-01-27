package webcrawler;

import org.apache.commons.cli.*;
import org.pmw.tinylog.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Gabe Sargeant on 22/01/17.
 * This is the main entry point for the webcrawler.WebCrawler.
 * This class sets things off and the webcrawler.WebCrawler Handles most else.
 */
public class Main {

    private static Options options = new Options();
    private static String uri, wordlist;


    public static void main(String args[]){

        setupOptions();

        if (parseArguments(args) == false){
            help();
        }
        else{
            Logger.info( uri + ":" + wordlist);

        }

        WebCrawler wc = new WebCrawler(uri, wordlist);
        wc.startCrawl();



    }

    public static boolean parseArguments(String args[]){
        CommandLineParser commandLineParser = new DefaultParser();
        boolean result = false;

        try{
            CommandLine line = commandLineParser.parse(options,args);
            if(line.hasOption("u") && line.hasOption("w")) {
                result = true;
                uri = line.getOptionValue("u");
                wordlist = line.getOptionValue("w");
            }

        }catch (Exception e)
        {
            Logger.error("there is an error with the command line arguments");
        }

        return result;
    }

    private static void help(){

        HelpFormatter helpFormatter = new HelpFormatter();
        String header="This is a simple and extensible web crawler. It uses a connection to a DB, Currently Mysql to hold status information on the search of particular pages that is crawls.";
        String footer="This was build by Gabriel Sargeant 2016. I built this and that, souce on github.com/gabesargeant/";
        String cmdSyntax = "java -jar WebCrawler.jar -u www.google.com -d database.properties -w wordlist.txt";

        helpFormatter.printHelp(cmdSyntax, header, options, footer, true);
    }
    //helper methods to build and create required options.
    //static because there will only be one!
    private static void setupOptions()
    {

        options.addOption(Option.builder("u")
                .required()
                .argName("Universal Resource Indicator")
                .hasArg()
                .numberOfArgs(1)
                .desc("Define the base URI that you want to crawl. Ie www.google.com will crawl everything with a URI of google.com")
                .longOpt("URI")
                .build());

        options.addOption(Option.builder("w")
                .required()
                .argName("Word List")
                .hasArg()
                .numberOfArgs(1)
                .desc("The full name of word list that will be searched for on pages crawled. ie words.txt")
                .longOpt("WORDLIST")
                .build());



    }


    public static String[] getDatabaseSettings() throws IOException{

        String databaseSettings[]  = new String[5];
        //to load application's properties, we use this class
        Properties mainProperties = new Properties();
        FileInputStream file;
        //the base folder is ./, the root of the main.properties file
        String path = "./database.properties";
        //load the file handle for main.properties
        file = new FileInputStream(path);
        //load all the properties from this file
        mainProperties.load(file);
        file.close();

        databaseSettings[0] = mainProperties.getProperty("host");
        databaseSettings[1] = mainProperties.getProperty("port");
        databaseSettings[2] = mainProperties.getProperty("dbName");
        databaseSettings[3] = mainProperties.getProperty("dbUser");
        databaseSettings[4] = mainProperties.getProperty("psw");

        return databaseSettings;
    }


}

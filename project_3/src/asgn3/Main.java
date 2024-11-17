package asgn3;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.net.URI;
import java.util.Optional;




public class Main {
    private static Logger logger;


    /**
     * Parses JSON-formatted inputs containing article(s) and displays them
     * or logs error(s) in displaying them
     * @param args  Currently unused
     */
    public static void main(String[] args) {
        setupLogger();

        SimpleParser simpleParser= new SimpleParser(logger);
        NewsAPIParser newsAPIParser = new NewsAPIParser(logger);

        SourceFormat simpleArticleFileFormat = new SourceFormat(Formats.SIMPLE,Sources.FILE,logger);
        SourceFormat newsAPIFileFormat = new SourceFormat(Formats.NEWSAPI,Sources.FILE,logger);
        SourceFormat newsAPIURLFormat = new SourceFormat(Formats.NEWSAPI,Sources.URL,logger);

        simpleArticleFileFormat.parse("inputs/simple.txt");
        newsAPIFileFormat.parse("inputs/newsapi.txt");
        newsAPIURLFormat.parse("http://newsapi.org/v2/top-headlines?country=us&apiKey=0d6b6fb6609c4389aac9a59513079ca4");
    }

    private static void setupLogger() {
        try {
            logger = Logger.getLogger("ArticleParserLogger");
            FileHandler fileHandler = new FileHandler("error.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            System.err.println("Error setting up logger: " + e.getMessage());
        }
    }
}

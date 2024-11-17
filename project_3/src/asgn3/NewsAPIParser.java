package asgn3;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

class NewsAPIParser extends Parser<ArticleList>{

    /**
     * "Visits" a JSON string - parsing and displaying it
     * @param jsonString a JSON-formatted string representing NewsAPI article(s)
     */
    void visit(String jsonString) {
        Optional<ArticleList> maybeParsed = parse(jsonString);
        if(maybeParsed.isPresent()) {
            new Displayer(getLogger()).display(maybeParsed.get());
        }
    }

    /**
     * Parses a JSON string into a new ArticleList object and displays it.
     * @param   jsonString a JSON-formatted string representing NewsAPI results
     */
    Optional<ArticleList> parse(String jsonString){
        try {
            ArticleList alist = new ObjectMapper().readValue(jsonString, ArticleList.class);
            return Optional.of(alist);
        } catch (Exception e) {
            getLogger().warning("Error parsing JSON: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Constructs a NewsAPIParser object
     * @param logger  a Logger to log parsing errors
     */
    NewsAPIParser(Logger logger) {
        super(logger);
    }


}

package asgn3;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class SimpleParser extends Parser<SimpleArticle>{

    /**
     * "Visits" a JSON string - parsing and displaying it
     * @param jsonString a JSON-formatted string representing a Simple article
     */
    void visit(String jsonString) {
        Optional<SimpleArticle> maybeParsed = parse(jsonString);
        if(maybeParsed.isPresent()) {
            new Displayer(getLogger()).display(maybeParsed.get());
        }
    }

    /**
     * Constructs a SimpleParser object
     * @param logger  a Logger to log parsing errors
     */
    SimpleParser(Logger logger) {
        super(logger);
    }

    /**
     * Parses a JSON string into a new SimpleArticle object and displays it.
     * @param   jsonString a JSON-formatted string representing a SimpleArticle
     */
    Optional<SimpleArticle> parse(String jsonString) {
        try {
            return Optional.of(new ObjectMapper().readValue(jsonString, SimpleArticle.class));
        } catch (Exception e) {
            getLogger().warning("Error parsing JSON: " + e.getMessage());
        }
        return Optional.empty();
    }



}

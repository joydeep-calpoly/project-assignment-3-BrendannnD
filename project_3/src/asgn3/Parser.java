package asgn3;

import java.io.File;
import java.util.Optional;
import java.util.logging.Logger;

abstract class Parser<T> {
    private final Logger logger;

    /**
     * Constructs a Parser with a logger
     * @param logger    a configured logger for debugging
     */
    Parser(Logger logger) {
        this.logger = logger;
    }

    /**
     * "Visits" a JSON string - parsing and displaying it
     * @param jsonString a JSON-formatted string representing article(s)
     */
    abstract void visit(String jsonString);

    /**
     * Gets the parser's logger for debugging purposes
     * @return  the parser's logger
     */
    Logger getLogger() {
        return logger;
    }

    /**
     * Helper function to parse a JSON string into some article object.
     * Note: not private to allow for unit testing
     * @param   jsonString a JSON-formatted string representing article(s)
     */
    abstract Optional<T> parse(String jsonString);

}

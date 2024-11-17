package asgn3;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

class SourceFormat {
    Formats format;
    Sources source;
    Logger logger;

    SourceFormat(Formats format, Sources source, Logger logger) {
        this.format = format;
        this.source = source;
        this.logger = logger;
    }

    /**
     * Accepts a JSON input and a visitor, calling the visitor's .visit method
     * Note: private to avoid misuse, e.g. passing a URL as the input before it's been formatted
     * @param input     a JSON-formatted string
     * @param visitor   a visitor that takes in a JSON-formatted string
     */
    private void accept(String input, Parser visitor) {
        visitor.visit(input);
    }

    /**
     * Formats an input into a JSON string and accepts the correct visitor
     * @param input     a string used to access an input, e.g. a file path or URL link
     */
    void parse(String input) {
        if (format == Formats.SIMPLE) {
            if (source == Sources.FILE) {
                accept(fileToStringFormat(input),new SimpleParser(logger));
            }
        }
        else if (format == Formats.NEWSAPI) {
            if (source == Sources.FILE) {
                accept(fileToStringFormat(input),new NewsAPIParser(logger));
            }
            else if (source == Sources.URL) {
                accept(urlToStringFormat(input),new NewsAPIParser(logger));
            }
        }
    }

    private String urlToStringFormat (String urlString) {
        try {
            URL url = new URI(urlString).toURL();
            try (InputStream stream = url.openConnection().getInputStream();
                 ByteArrayOutputStream result = new ByteArrayOutputStream())   {
                URLConnection connection = url.openConnection();
                byte[] buffer = new byte[1024];
                for (int length; (length = stream.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }
                return result.toString("UTF-8");
            } catch (Exception e) {
                logger.warning("Error parsing JSON from URL: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.warning("Error parsing JSON from URL: " + e.getMessage());
        }
        return "ERROR";
    }

    private String fileToStringFormat(String fileName) {
        try {
            Path filePath = Paths.get("inputs/simple.txt");
            return Files.readString(filePath);
        } catch (Exception e) {
            logger.warning("Error parsing JSON from file: " + e.getMessage());
        }
        return "ERROR";
    }
}

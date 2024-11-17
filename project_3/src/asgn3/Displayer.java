package asgn3;

import java.util.List;
import java.util.logging.Logger;

class Displayer {
    private final Logger logger;

    /**
     * Constructs a Displayer with a logger
     * @param logger    a configured logger for debugging
     */
    Displayer(Logger logger) {
        this.logger = logger;
    }

    /**
     * Displays all of articleList's Article objects to System.out if all fields are present.
     * @param articleList   an ArticleList object
     */
    void display(ArticleList articleList) {
        for (int i = 0; i < articleList.getArticles().size(); i++) {
            ArticleList.Article article = articleList.getArticles().get(i);
            List<String> invalids = article.getInvalidFields();
            if (!invalids.isEmpty()) {
                logger.warning("Missing essential argument(s) in article #" + i + ": " + String.join(", ", invalids));
            } else {
                System.out.println(article + "\n");
            }
        }
    }

    /**
     * Displays a SimpleArticle object to System.out if all fields are present.
     * @param simpleArticle   a SimpleArticle object
     */
    void display(SimpleArticle simpleArticle) {
        List<String> invalids = simpleArticle.getInvalidFields();
        if (!invalids.isEmpty()) {
            logger.warning("Missing essential argument(s) in simple article: " + String.join(", ", invalids));
        } else {
            System.out.println(simpleArticle + "\n");
        }
    }
}

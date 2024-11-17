package asgn3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

class ArticleList {
    static class Article {
        private static class Source {
            private final String id;
            private final String name;

            /**
             * Constructs a Source object
             * @param id    a source id
             * @param name  a source name
             */
            @JsonCreator
            Source(@JsonProperty("id") String id, @JsonProperty("name") String name) {
                this.id = id;
                this.name = name;
            }
        }

        private final Source source;

        private final String author;
        private final String title;
        private final String description;
        private final String url;
        private final String urlToImage;
        private final Date publishedAt;
        private final String content;

        /**
         * Constructs an Article object
         * @param source        a Source object
         * @param author        the article's author
         * @param title         the article's title
         * @param description   the article's description
         * @param url           url link to article
         * @param urlToImage    url link to article's image
         * @param publishedAt   date, time article was published at
         * @param content       article content
         */
        @JsonCreator
        Article(@JsonProperty("source") Source source,
                @JsonProperty("author") String author,
                @JsonProperty("title") String title,
                @JsonProperty("description") String description,
                @JsonProperty("url") String url,
                @JsonProperty("urlToImage") String urlToImage,
                @JsonProperty("publishedAt") Date publishedAt,
                @JsonProperty("content") String content) {
            this.source = source;
            this.author = author;
            this.title = title;
            this.description = description;
            this.url = url;
            this.urlToImage = urlToImage;
            this.publishedAt = (publishedAt == null) ? null : (Date) publishedAt.clone();
            this.content = content;
        }

        /**
         * Gets a list of missing or invalid fields for debugging and determining printability
         * @return  a list of missing or invalid fields
         */
        public List<String> getInvalidFields() {
            List<String> returnList = new LinkedList<String>();
            if (this.title == null) {
                returnList.add("title");
            }
            if (this.description == null) {
                returnList.add("description");
            }
            if(this.publishedAt == null) {
                returnList.add("publishedAt");
            }
            if(this.url == null) {
                returnList.add("url");
            }
            return returnList;
        }

        /**
         * Gets the article's description
         * @return  the article's description
         */
        String getDescription() { return this.description; }

        /**
         * Gets the article's title
         * @return  the article's title
         */
        String getTitle() { return this.title; }

        /**
         * Gets the article's URL
         * @return  the article's URL
         */
        String getURL() { return this.url; }

        /**
         * Gets a copy of the article's published Date
         * @return  a copy of the article's published Date
         */
        Date getPublishedAt() { return (publishedAt == null) ? null : (Date) publishedAt.clone();}

        /**
         * Gets the article's author
         * @return  the article's author
         */
        String getAuthor() {return author;}

        /**
         * Gets the article's image URL
         * @return  the article's image URL
         */
        String getUrlToImage() {return urlToImage;}

        /**
         * Gets the article's content
         * @return  the article's content
         */
        String getContent() {return content;}

        /**
         * Checks for equality based on the article's title, description, publishing date, and URL
         * @param o     an object to compare for equality
         * @return      whether the given object is equal to the article
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Article article = (Article) o;
            return Objects.equals(title, article.title) && Objects.equals(description, article.description)
                    && Objects.equals(url, article.url) && Objects.equals(publishedAt, article.publishedAt);
        }

        /**
         * Formats the article's title, description, URL, and publishing date as a string
         * @return  the article's primary fields as a string
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Title: ");
            sb.append(title);
            sb.append("\nDescription: ");
            sb.append(description);
            sb.append("\nPublished at: ");
            sb.append(publishedAt);
            sb.append("\nURL: ");
            sb.append(url);
            return sb.toString();
        }
    }

    private final List<Article> articles;
    private final String status;
    private final int totalResults;

    /**
     * Gets a copy of the list of the project1.ArticleList's articles
     * @return  a copy of the list of the project1.ArticleList's articles
     */
    List<Article> getArticles() {
        return new LinkedList<>(articles);
    }

    /**
     * Constructs a new ArticleList object
     * @param status        status of the list of articles
     * @param totalResults  total articles retrieved
     * @param articles      a list of Articles
     */
    @JsonCreator
    ArticleList(@JsonProperty("status") String status,
                @JsonProperty("totalResults") int totalResults,
                @JsonProperty("articles") List<Article> articles) {
        this.articles = new LinkedList<>(articles);
        this.status = status;
        this.totalResults = totalResults;
    }

}

package asgn3;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


class SimpleArticle {

    private final String title;
    private final String description;
    private final String url;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private final Date publishedAt;

    /**
     * Constructs a new SimpleArticle object.
     * @param title         the article's title
     * @param description   the article's description
     * @param url           the article's URL as a String
     * @param publishedAt   the article's publishing date
     */
    @JsonCreator
    SimpleArticle(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("url") String url,
            @JsonProperty("publishedAt") Date publishedAt) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.publishedAt = (publishedAt == null) ? null : (Date) publishedAt.clone();
    }

    /**
     * Formats the article's title, description, URL, and publishing date as a string
     * @return  the article's fields as a string
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

    /**
     * Gets a list of missing or invalid fields for debugging and determining printability
     * @return  a list of missing or invalid fields
     */
    public List<String> getInvalidFields() {
        List<String> returnList = new LinkedList<>();
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
     * Checks for equality based on the article's title, description, publishing date, and URL
     * @param o     an object to compare for equality
     * @return      whether the given object is equal to the article
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleArticle article = (SimpleArticle) o;
        return Objects.equals(title, article.title) && Objects.equals(description, article.description)
                && Objects.equals(url, article.url) && Objects.equals(publishedAt, article.publishedAt);
    }
}

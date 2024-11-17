package asgn3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class Tests {
    private Logger mockLogger;
    private NewsAPIParser newsAPIParser;

    private SimpleParser simpleParser;

    /**
     * Resets test parsers and loggers before each test.
     */
    @BeforeEach
    public void reset() {
        mockLogger = Mockito.mock(Logger.class);
        newsAPIParser = new NewsAPIParser(mockLogger);
        simpleParser = new SimpleParser(mockLogger);
    }

    /**
     * Tests ArticleList methods with a single valid article from a String
     */
    @Test
    public void testValidArticle(){
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 1,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"id\": \"cnn\",\n" +
                "        \"name\": \"CNN\"\n" +
                "      },\n" +
                "      \"author\": \"Test author\",\n" +
                "      \"title\": \"Test title\",\n" +
                "      \"description\": \"Test description\",\n" +
                "      \"url\": \"Test URL\",\n" +
                "      \"urlToImage\": \"Test image URL\",\n" +
                "      \"publishedAt\": \"2021-03-24T22:32:00Z\",\n" +
                "      \"content\": \"Test content\"\n" +
                "    }]}";
        ArticleList articleList = newsAPIParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        ArticleList.Article parsedArticle = articleList.getArticles().getFirst();
        assertEquals(1, articleList.getArticles().size());
        ArticleList.Article equalArticle = new ArticleList.Article(null, null, "Test title", "Test description", "Test URL", null, Date.from(Instant.parse("2021-03-24T22:32:00Z")), null);
        assertEquals(equalArticle, parsedArticle);
        assertNotEquals(new ArticleList.Article(null,null,null,null,null,null,null,null),
                articleList.getArticles().getFirst());
        assertEquals(parsedArticle.getTitle(), "Test title");
        assertEquals(parsedArticle.getDescription(), "Test description");
        assertEquals(parsedArticle.getURL(), "Test URL");
        assertEquals(parsedArticle.getPublishedAt(),Date.from(Instant.parse("2021-03-24T22:32:00Z")));
        assertEquals(parsedArticle.getAuthor(), "Test author");
        assertEquals(parsedArticle.getContent(), "Test content");
        assertEquals(parsedArticle.getUrlToImage(), "Test image URL");
        assertTrue(parsedArticle.getInvalidFields().isEmpty());
        assertEquals(parsedArticle.toString(), "Title: Test title\nDescription: Test description" +
                "\nPublished at: Wed Mar 24 15:32:00 PDT 2021\nURL: Test URL");
    }

    /**
     * Tests an ArticleList from a String with a single article with a few missing fields.
     */
    @Test
    public void testMostFieldsMissing() {
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 1,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"name\": \"CNN\"\n" +
                "      },\n" +
                "      \"url\": \"https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html\",\n" +
                "      \"urlToImage\": \"https://cdn.cnn.com/cnnnext/dam/assets/200213175739-03-coronavirus-0213-super-tease.jpg\",\n" +
                "      \"content\": \"A senior European diplomat is urging caution over the use of proposed new rules that would govern exports of Covid-19 vaccines to outside of the EU. The rules were announced by the European Commissio… [+2476 chars]\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ArticleList articleList = newsAPIParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        assertEquals(1, articleList.getArticles().size());
        ArticleList.Article equalArticle = new ArticleList.Article(null, null, null, null, "https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html", null, null, null);
        assertEquals(equalArticle, articleList.getArticles().getFirst());
        assertNull(equalArticle.getDescription());
        assertNull(equalArticle.getPublishedAt());
        assertNull(equalArticle.getTitle());
        assertEquals(equalArticle.getURL(), "https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html");
        assertEquals(equalArticle.getInvalidFields(), Arrays.asList("title", "description", "publishedAt"));
    }

    /**
     * Tests an ArticleList from a String with an article missing all essential fields.
     */
    @Test
    public void testMissingAllFields(){
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 1,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"name\": \"CNN\"\n" +
                "      },\n" +
                "      \"urlToImage\": \"https://cdn.cnn.com/cnnnext/dam/assets/200213175739-03-coronavirus-0213-super-tease.jpg\",\n" +
                "      \"content\": \"A senior European diplomat is urging caution over the use of proposed new rules that would govern exports of Covid-19 vaccines to outside of the EU. The rules were announced by the European Commissio… [+2476 chars]\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        ArticleList articleList = newsAPIParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        ArticleList.Article equalArticle = new ArticleList.Article(null, null, null, null, null, null, null, null);
        assertEquals(equalArticle, articleList.getArticles().getFirst());
        assertNull(equalArticle.getDescription());
        assertNull(equalArticle.getPublishedAt());
        assertNull(equalArticle.getTitle());
        assertNull(equalArticle.getURL());
        assertEquals(equalArticle.getInvalidFields(), Arrays.asList("title", "description", "publishedAt","url"));
    }

    /**
     * Tests an ArticleList with multiple valid articles.
     */
    @Test
    public void testMultipleValidArticlesFullText() {
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 2,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"id\": \"cnn\",\n" +
                "        \"name\": \"CNN\"\n" +
                "      },\n" +
                "      \"author\": \"By <a href=\\\"/profiles/julia-hollingsworth\\\">Julia Hollingsworth</a>, CNN\",\n" +
                "      \"title\": \"The latest on the coronavirus pandemic and vaccines: Live updates - CNN\",\n" +
                "      \"description\": \"The coronavirus pandemic has brought countries to a standstill. Meanwhile, vaccinations have already started in some countries as cases continue to rise. Follow here for the latest.\",\n" +
                "      \"url\": \"https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html\",\n" +
                "      \"urlToImage\": \"https://cdn.cnn.com/cnnnext/dam/assets/200213175739-03-coronavirus-0213-super-tease.jpg\",\n" +
                "      \"publishedAt\": \"2021-03-24T22:32:00Z\",\n" +
                "      \"content\": \"A senior European diplomat is urging caution over the use of proposed new rules that would govern exports of Covid-19 vaccines to outside of the EU. The rules were announced by the European Commissio… [+2476 chars]\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"id\": \"cnn\",\n" +
                "        \"name\": \"CNN\"\n" +
                "      },\n" +
                "      \"author\": \"Ralph Ellis, CNN\",\n" +
                "      \"title\": \"People line the streets of Boulder to honor slain police Officer Eric Talley - CNN\",\n" +
                "      \"description\": \"Boulder, Colorado, continued to mourn fallen Officer Eric Talley on Wednesday.\",\n" +
                "      \"url\": \"https://www.cnn.com/2021/03/24/us/officer-talley-procession/index.html\",\n" +
                "      \"urlToImage\": \"https://cdn.cnn.com/cnnnext/dam/assets/210322232935-officer-eric-talley-headshot-super-tease.jpg\",\n" +
                "      \"publishedAt\": \"2021-03-24T22:20:12Z\",\n" +
                "      \"content\": null\n" +
                "    }]}";
        ArticleList articleList = newsAPIParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        assertEquals(2, articleList.getArticles().size());
        ArticleList.Article firstArticle = new ArticleList.Article(null, null, "The latest on the coronavirus pandemic and vaccines: Live updates - CNN", "The coronavirus pandemic has brought countries to a standstill. Meanwhile, vaccinations have already started in some countries as cases continue to rise. Follow here for the latest.", "https://www.cnn.com/world/live-news/coronavirus-pandemic-vaccine-updates-03-24-21/index.html", null, Date.from(Instant.parse("2021-03-24T22:32:00Z")), null);
        assertEquals(firstArticle, articleList.getArticles().getFirst());
        for (ArticleList.Article article : articleList.getArticles()) {
            assertTrue(article.getInvalidFields().isEmpty());
        }
    }

    /**
     * Tests an ArticleList from a String with an empty article lists.
     */
    @Test
    public void testEmptyArticleList() {
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 0,\n" +
                "  \"articles\": []\n" +
                "}";

        ArticleList articleList = newsAPIParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        assertEquals(0, articleList.getArticles().size());
    }

    /**
     * Tests an ArticleList from a String with an invalid format.
     */
    @Test
    public void testInvalidJsonFormat() {
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 1,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": {\n" +
                "        \"id\": \"cnn\",\n" +
                "        \"name\": \"CNN\"\n" +
                "      }\n" +
                "      \"title\": \"Missing a comma\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Optional<ArticleList> articleList = newsAPIParser.parse(jsonInput);
        if(articleList.isPresent()) fail();
        verify(mockLogger, times(1)).warning(contains("Error parsing JSON"));
    }

    /**
     * Tests an ArticleList from a String with an extra field.
     */
    @Test
    public void testArticleWithExtraFields() {
        String jsonInput = "{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"totalResults\": 1,\n" +
                "  \"articles\": [\n" +
                "    {\n" +
                "      \"source\": { \"id\": \"cnn\", \"name\": \"CNN\" },\n" +
                "      \"author\": \"CNN\",\n" +
                "      \"title\": \"Article with extra field\",\n" +
                "      \"description\": \"This article has an extra field.\",\n" +
                "      \"url\": \"https://www.cnn.com/article\",\n" +
                "      \"publishedAt\": \"2021-03-24T22:32:00Z\",\n" +
                "      \"extraField\": \"This should be ignored\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        Optional<ArticleList> articleList = newsAPIParser.parse(jsonInput);
        if(articleList.isPresent()) fail();
        verify(mockLogger, times(1)).warning(contains("Error parsing JSON"));
    }

    /**
     * Tests a valid simple article as a string
     */
    @Test
    public void testValidSimpleArticle() {
        String jsonInput = "{\n" +
                "  \"description\": \"Description\",\n" +
                "  \"publishedAt\": \"2021-04-16 09:53:23.000000\",\n" +
                "  \"title\": \"Title\",\n" +
                "  \"url\": \"URL\"\n" +
                "}";
        SimpleArticle simpleArticle = simpleParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        SimpleArticle equalArticle = new SimpleArticle("Title", "Description", "URL", Date.from(Instant.parse("2021-04-16T09:53:23Z")));
        assertEquals(equalArticle, simpleArticle);
        assertNotEquals(new SimpleArticle(null,null,null,null),
                simpleArticle);
        assertEquals(simpleArticle.getTitle(), "Title");
        assertEquals(simpleArticle.getDescription(), "Description");
        assertEquals(simpleArticle.getURL(), "URL");
        assertEquals(simpleArticle.getPublishedAt(),Date.from(Instant.parse("2021-04-16T09:53:23Z")));
        assertTrue(simpleArticle.getInvalidFields().isEmpty());
        assertEquals(simpleArticle.toString(), "Title: Title\nDescription: Description" +
                "\nPublished at: Fri Apr 16 02:53:23 PDT 2021\nURL: URL");
    }

    /**
     * Tests a simple article from a String missing half the fields.
     */
    @Test
    public void testSomeMissingFieldsSimpleArticle() {
        String jsonInput = "{\n" +
                "  \"description\": \"Description\",\n" +
                "  \"url\": \"URL\"\n" +
                "}";
        SimpleArticle simpleArticle = simpleParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        SimpleArticle equalArticle = new SimpleArticle(null, "Description", "URL",null);
        assertEquals(equalArticle, simpleArticle);
        assertNotEquals(new SimpleArticle(null,null,null,null),
                simpleArticle);
        assertNull(simpleArticle.getTitle());
        assertEquals(simpleArticle.getDescription(), "Description");
        assertEquals(simpleArticle.getURL(), "URL");
        assertNull(simpleArticle.getPublishedAt());
        assertEquals(simpleArticle.getInvalidFields(), Arrays.asList("title", "publishedAt"));
    }

    /**
     * Tests a simple article from a String missing all fields.
     */
    @Test
    public void testAllMissingFieldsSimpleArticle() {
        String jsonInput = "{\n" +
                "}";
        SimpleArticle simpleArticle = simpleParser.parse(jsonInput).orElseThrow(() -> new AssertionError("Parse failed"));
        SimpleArticle equalArticle = new SimpleArticle(null, null, null,null);
        assertEquals(equalArticle, simpleArticle);
        assertNotEquals(new SimpleArticle(null,"Description",null,null),
                simpleArticle);
        assertNull(simpleArticle.getTitle());
        assertNull(simpleArticle.getDescription());
        assertNull(simpleArticle.getURL());
        assertNull(simpleArticle.getPublishedAt());
        assertEquals(simpleArticle.getInvalidFields(), Arrays.asList("title", "description","publishedAt","url"));
    }

    /**
     * Tests an invalid format simple article from a String.
     */
    @Test
    public void testInvalidFormatSimpleArticle() {
        String jsonInput = "title : Title, description - Description, URL : url, publishedAt : 10/22/2024";
        Optional<SimpleArticle> simpleArticle = simpleParser.parse(jsonInput);
        if (simpleArticle.isPresent()) fail();
        verify(mockLogger, times(1)).warning(contains("Error parsing JSON"));
    }

    /**
     * Tests a simple article from a String with an extra field
     */
    @Test
    public void testExtraFieldSimpleArticle() {
        String jsonInput = "{\n" +
                "  \"description\": \"Description\",\n" +
                "  \"publishedAt\": \"2021-04-16 09:53:23.000000\",\n" +
                "  \"title\": \"Title\",\n" +
                "  \"url\": \"URL\"\n" +
                "  \"publisher\": \"CNN\"\n" +
                "}";
        Optional<SimpleArticle> simpleArticle = simpleParser.parse(jsonInput);
        if (simpleArticle.isPresent()) fail();
        verify(mockLogger, times(1)).warning(contains("Error parsing JSON"));
    }
}

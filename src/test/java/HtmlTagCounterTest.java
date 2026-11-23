
import web.HtmlTagCounter;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HtmlTagCounterTest {

    private final HtmlTagCounter counter = new HtmlTagCounter();

    @Test
    void countsTagsCorrectly() {
        String html = "<html><head><title>Page</title></head>" +
                "<body><div><p>First</p><p>Second</p></div>" +
                "<div><span>Text</span></div></body></html>";

        Map<String, Integer> counts = counter.countTags(html);

        assertEquals(2, counts.get("div").intValue());
        assertEquals(2, counts.get("p").intValue());
        assertEquals(1, counts.get("span").intValue());
        assertEquals(1, counts.get("html").intValue());
        assertEquals(1, counts.get("body").intValue());
    }

    @Test
    void doesNotCountDocumentRoot() {
        Map<String, Integer> counts =
                counter.countTags("<html><body>Hello</body></html>");

        assertFalse(counts.containsKey("#root"));
    }

    @Test
    void sortsTagsAlphabetically() {
        Map<String, Integer> counts = Map.of(
                "p", 2,
                "a", 3,
                "div", 1
        );

        List<String> names = counter.sortByName(counts)
                .stream()
                .map(Map.Entry::getKey)
                .toList();

        assertEquals(List.of("a", "div", "p"), names);
    }

    @Test
    void sortsTagsByFrequency() {
        Map<String, Integer> counts = Map.of(
                "div", 4,
                "p", 2,
                "span", 3
        );

        List<String> names = counter.sortByFrequency(counts)
                .stream()
                .map(Map.Entry::getKey)
                .toList();

        assertEquals(List.of("p", "span", "div"), names);
    }

    @Test
    void sortsEqualFrequenciesAlphabetically() {
        Map<String, Integer> counts = Map.of(
                "span", 1,
                "a", 1,
                "div", 2,
                "p", 1
        );

        List<String> names = counter.sortByFrequency(counts)
                .stream()
                .map(Map.Entry::getKey)
                .toList();

        assertEquals(
                List.of("a", "p", "span", "div"),
                names
        );
    }

    @Test
    void handlesUppercaseTags() {
        String html = "<HTML><BODY><DIV>Text</DIV></BODY></HTML>";

        Map<String, Integer> counts = counter.countTags(html);

        assertEquals(1, counts.get("div").intValue());
        assertFalse(counts.containsKey("DIV"));
    }
}

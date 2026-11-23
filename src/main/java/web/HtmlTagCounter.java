
package web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlTagCounter {

    public Map<String, Integer> countTagsFromUrl(String url)
            throws IOException {

        Document document = Jsoup.connect(url)
                .timeout(15000)
                .get();

        return countElements(document);
    }

    public Map<String, Integer> countTags(String html) {
        Document document = Jsoup.parse(html);
        return countElements(document);
    }

    private Map<String, Integer> countElements(Document document) {
        Map<String, Integer> counts = new HashMap<>();

        for (Element element : document.select("*")) {
            if (element == document) {
                continue;
            }

            String tag = element.tagName();
            counts.merge(tag, 1, Integer::sum);
        }

        return counts;
    }

    public List<Map.Entry<String, Integer>> sortByName(
            Map<String, Integer> counts) {

        List<Map.Entry<String, Integer>> result =
                new ArrayList<>(counts.entrySet());

        result.sort(Map.Entry.comparingByKey());

        return result;
    }

    public List<Map.Entry<String, Integer>> sortByFrequency(
            Map<String, Integer> counts) {

        List<Map.Entry<String, Integer>> result =
                new ArrayList<>(counts.entrySet());

        result.sort(Comparator
                .comparingInt(
                        (Map.Entry<String, Integer> entry) ->
                                entry.getValue())
                .thenComparing(Map.Entry::getKey));

        return result;
    }
}

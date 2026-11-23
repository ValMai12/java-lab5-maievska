
import web.HtmlTagCounter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HtmlTagApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HtmlTagCounter counter = new HtmlTagCounter();

        System.out.print("Введіть URL вебсторінки: ");
        String url = scanner.nextLine().trim();

        if (url.isEmpty()) {
            System.out.println("URL не може бути порожнім.");
            return;
        }

        try {
            Map<String, Integer> counts =
                    counter.countTagsFromUrl(url);

            if (counts.isEmpty()) {
                System.out.println("HTML-теги не знайдено.");
                return;
            }

            System.out.println("\nТеги в лексикографічному порядку:");
            printResults(counter.sortByName(counts));

            System.out.println("\nТеги за зростанням частоти:");
            printResults(counter.sortByFrequency(counts));

            int total = 0;

            for (int count : counts.values()) {
                total += count;
            }

            System.out.println("\nУсього тегів: " + total);
            System.out.println("Різних тегів: " + counts.size());

        } catch (IllegalArgumentException e) {
            System.out.println("Некоректний URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Помилка завантаження сторінки: "
                    + e.getMessage());
        }
    }

    private static void printResults(
            List<Map.Entry<String, Integer>> entries) {

        for (Map.Entry<String, Integer> entry : entries) {
            System.out.printf("%-16s %d%n",
                    entry.getKey(), entry.getValue());
        }
    }
}

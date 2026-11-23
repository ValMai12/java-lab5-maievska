
import files.FileLineFinder;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class TextFileApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть шлях до текстового файлу: ");
        String filePath = scanner.nextLine().trim();

        if (filePath.isEmpty()) {
            System.out.println("Шлях до файлу не може бути порожнім.");
            return;
        }

        try {
            FileLineFinder finder = new FileLineFinder();
            String result = finder.findLineWithMostWords(Path.of(filePath));

            if (result == null) {
                System.out.println("Файл порожній.");
            } else {
                System.out.println("Рядок з максимальною кількістю слів:");
                System.out.println(result);
            }

        } catch (InvalidPathException e) {
            System.out.println("Некоректний шлях до файлу.");
        } catch (IOException e) {
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}

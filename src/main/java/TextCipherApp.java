
import files.TextCipherFileManager;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Scanner;

public class TextCipherApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TextCipherFileManager manager = new TextCipherFileManager();

        boolean running = true;

        while (running) {
            System.out.println("\nМеню шифрування:");
            System.out.println("1. Зашифрувати файл");
            System.out.println("2. Розшифрувати файл");
            System.out.println("0. Вийти");
            System.out.print("Виберіть пункт: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Роботу завершено.");
                running = false;
                continue;
            }

            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Невірний пункт меню.");
                continue;
            }

            System.out.print("Введіть шлях до вхідного файлу: ");
            String input = scanner.nextLine().trim();

            System.out.print("Введіть шлях до вихідного файлу: ");
            String output = scanner.nextLine().trim();

            if (input.isEmpty() || output.isEmpty()) {
                System.out.println("Шлях до файлу не може бути порожнім.");
                continue;
            }

            System.out.print("Введіть ключовий символ: ");
            String keyInput = scanner.nextLine();

            if (keyInput.length() != 1) {
                System.out.println("Ключ повинен містити один символ.");
                continue;
            }

            char key = keyInput.charAt(0);

            try {
                Path inputFile = Path.of(input);
                Path outputFile = Path.of(output);

                if (choice.equals("1")) {
                    manager.encryptFile(inputFile, outputFile, key);
                    System.out.println("Файл успішно зашифровано.");
                } else {
                    manager.decryptFile(inputFile, outputFile, key);
                    System.out.println("Файл успішно розшифровано.");
                }

            } catch (InvalidPathException e) {
                System.out.println("Некоректний шлях до файлу.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
}

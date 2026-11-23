
package controller;

import files.BookFileManager;
import model.Book;
import view.BookView;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BookController {

    private Book[] books;
    private final BookView view;
    private final BookFileManager fileManager = new BookFileManager();

    public BookController(Book[] books, BookView view) {
        this.books = books;
        this.view = view;
    }

    public Book[] findByAuthor(String author) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }

        return result.toArray(new Book[0]);
    }

    public Book[] findByPublisher(String publisher) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getPublisher().equalsIgnoreCase(publisher)) {
                result.add(book);
            }
        }

        return result.toArray(new Book[0]);
    }

    public Book[] findAfterYear(int year) {
        List<Book> result = new ArrayList<>();

        for (Book book : books) {
            if (book.getYear() > year) {
                result.add(book);
            }
        }

        return result.toArray(new Book[0]);
    }

    public void sortByPublisher() {
        Comparator<Book> byPublisher = Comparator.comparing(
                Book::getPublisher,
                String.CASE_INSENSITIVE_ORDER
        );

        Arrays.sort(books, byPublisher);
    }

    private void saveBooksToFile(Scanner scanner) {
        System.out.print("Введіть шлях та ім'я файлу: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            view.showMessage("Шлях до файлу не може бути порожнім.");
            return;
        }

        try {
            fileManager.saveBooks(books, Path.of(fileName));
            view.showMessage("Книги успішно збережено.");
        } catch (InvalidPathException e) {
            view.showMessage("Некоректний шлях до файлу.");
        } catch (IOException e) {
            view.showMessage("Помилка збереження: " + e.getMessage());
        }
    }

    private void loadBooksFromFile(Scanner scanner) {
        System.out.print("Введіть шлях та ім'я файлу: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            view.showMessage("Шлях до файлу не може бути порожнім.");
            return;
        }

        try {
            Book[] loadedBooks = fileManager.loadBooks(Path.of(fileName));
            books = loadedBooks;

            view.showMessage("Книги успішно завантажено.");
            view.showBooks("Завантажені книги:", books);
        } catch (InvalidPathException e) {
            view.showMessage("Некоректний шлях до файлу.");
        } catch (IOException | ClassNotFoundException e) {
            view.showMessage("Помилка завантаження: " + e.getMessage());
        }
    }

    public void run() {
        view.showBooks("Початковий масив книг:", books);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            view.showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    view.showBooks("Усі книги:", books);
                    break;

                case "2": {
                    System.out.print("Введіть автора: ");
                    String author = scanner.nextLine().trim();

                    view.showBooks("Результат пошуку:",
                            findByAuthor(author));
                    break;
                }

                case "3": {
                    System.out.print("Введіть видавництво: ");
                    String publisher = scanner.nextLine().trim();

                    view.showBooks("Результат пошуку:",
                            findByPublisher(publisher));
                    break;
                }

                case "4": {
                    System.out.print("Введіть рік: ");
                    String input = scanner.nextLine().trim();

                    try {
                        int year = Integer.parseInt(input);

                        view.showBooks(
                                "Книги, видані після " + year + " року:",
                                findAfterYear(year)
                        );
                    } catch (NumberFormatException e) {
                        view.showMessage("Некоректний рік.");
                    }

                    break;
                }

                case "5":
                    sortByPublisher();
                    view.showBooks(
                            "Книги, відсортовані за видавництвом:",
                            books
                    );
                    break;

                case "6":
                    saveBooksToFile(scanner);
                    break;

                case "7":
                    loadBooksFromFile(scanner);
                    break;

                case "0":
                    view.showMessage("Роботу завершено.");
                    running = false;
                    break;

                default:
                    view.showMessage("Невірний пункт меню.");
            }
        }
    }
}

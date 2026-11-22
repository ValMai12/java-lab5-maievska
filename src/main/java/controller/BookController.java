
package controller;

import model.Book;
import view.BookView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BookController {

    private final Book[] books;
    private final BookView view;
    private final Random random = new Random();

    private final String[] authors = {
        "Леся Українка",
        "Михайло Коцюбинський",
        "Іван Франко",
        "Невідомий автор"
    };

    private final String[] publishers = {
        "Освіта",
        "Фоліо",
        "Ранок",
        "Невідоме видавництво"
    };

    private final int[] years = {
        2010, 2015, 2018, 2023, 2025
    };

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
                    String author = authors[random.nextInt(authors.length)];
                    view.showMessage("Обраний автор: " + author);
                    view.showBooks("Результат пошуку:", findByAuthor(author));
                    break;
                }

                case "3": {
                    String publisher = publishers[random.nextInt(publishers.length)];
                    view.showMessage("Обране видавництво: " + publisher);
                    view.showBooks("Результат пошуку:", findByPublisher(publisher));
                    break;
                }

                case "4": {
                    int year = years[random.nextInt(years.length)];
                    view.showMessage("Обраний рік: " + year);
                    view.showBooks("Книги, видані після " + year + " року:",
                            findAfterYear(year));
                    break;
                }

                case "5":
                    sortByPublisher();
                    view.showBooks("Книги, відсортовані за видавництвом:", books);
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

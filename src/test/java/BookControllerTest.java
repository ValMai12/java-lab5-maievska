
import controller.BookController;
import model.Book;
import model.BookData;
import view.BookView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BookControllerTest {

    private Book[] books;
    private BookController controller;

    @BeforeEach
    void setUp() {
        books = BookData.createBooks();
        controller = new BookController(books, new BookView());
    }

    @Test
    void testBookData() {
        assertEquals(12, books.length);
        assertEquals("Місто", books[0].getTitle());
        assertEquals("Валер'ян Підмогильний", books[0].getAuthor());
        assertEquals("Освіта", books[0].getPublisher());
        assertEquals(2018, books[0].getYear());
        assertEquals(320, books[0].getPages());
        assertEquals(240.0, books[0].getPrice());
    }

    @Test
    void testFindByAuthor() {
        Book[] result = controller.findByAuthor("Леся Українка");

        assertEquals(3, result.length);

        for (Book book : result) {
            assertEquals("Леся Українка", book.getAuthor());
        }
    }

    @Test
    void testFindByPublisher() {
        Book[] result = controller.findByPublisher("Фоліо");

        assertEquals(4, result.length);

        for (Book book : result) {
            assertEquals("Фоліо", book.getPublisher());
        }
    }

    @Test
    void testFindAfterYear() {
        Book[] result = controller.findAfterYear(2018);

        assertEquals(5, result.length);

        for (Book book : result) {
            assertTrue(book.getYear() > 2018);
        }
    }

    @Test
    void testNoResults() {
        assertEquals(0,
                controller.findByAuthor("Невідомий автор").length);

        assertEquals(0,
                controller.findByPublisher("Невідоме видавництво").length);

        assertEquals(0,
                controller.findAfterYear(2025).length);
    }

    @Test
    void testSortByPublisher() {
        controller.sortByPublisher();

        for (int i = 1; i < books.length; i++) {
            String previous = books[i - 1].getPublisher();
            String current = books[i].getPublisher();

            assertTrue(
                    String.CASE_INSENSITIVE_ORDER.compare(previous, current) <= 0
            );
        }
    }

    @Test
    void testSortKeepsAllBooks() {
        String[] before = Arrays.stream(books)
                .map(Book::getTitle)
                .sorted()
                .toArray(String[]::new);

        controller.sortByPublisher();

        String[] after = Arrays.stream(books)
                .map(Book::getTitle)
                .sorted()
                .toArray(String[]::new);

        assertArrayEquals(before, after);
    }

    @Test
    void testSearchAfterSorting() {
        controller.sortByPublisher();

        Book[] result = controller.findByAuthor("Леся Українка");

        assertEquals(3, result.length);
    }
}

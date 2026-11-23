
import files.BookFileManager;
import model.Book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class BookFileManagerTest {

    @TempDir
    Path tempDir;

    private final BookFileManager manager = new BookFileManager();

    @Test
    void savesAndLoadsBooks() throws IOException, ClassNotFoundException {
        Book[] books = {
            new Book("Місто", "Валер'ян Підмогильний",
                    "Освіта", 2018, 320, 240),
            new Book("Кобзар", "Тарас Шевченко",
                    "Фоліо", 2010, 448, 310)
        };

        Path file = tempDir.resolve("books.dat");

        manager.saveBooks(books, file);
        Book[] loaded = manager.loadBooks(file);

        assertEquals(2, loaded.length);
        assertEquals("Місто", loaded[0].getTitle());
        assertEquals("Валер'ян Підмогильний", loaded[0].getAuthor());
        assertEquals("Освіта", loaded[0].getPublisher());
        assertEquals(2018, loaded[0].getYear());
        assertEquals(320, loaded[0].getPages());
        assertEquals(240.0, loaded[0].getPrice());
        assertEquals("Кобзар", loaded[1].getTitle());
        assertNotSame(books[0], loaded[0]);
    }

    @Test
    void savesEmptyArray() throws IOException, ClassNotFoundException {
        Path file = tempDir.resolve("empty.dat");

        manager.saveBooks(new Book[0], file);

        assertEquals(0, manager.loadBooks(file).length);
    }

    @Test
    void createsDirectoryForFile() throws IOException {
        Path file = tempDir.resolve("data/books.dat");

        manager.saveBooks(new Book[0], file);

        assertTrue(Files.exists(file));
    }

    @Test
    void handlesMissingFile() {
        Path file = tempDir.resolve("missing.dat");

        assertThrows(IOException.class,
                () -> manager.loadBooks(file));
    }

    @Test
    void handlesInvalidFile() throws IOException {
        Path file = tempDir.resolve("invalid.dat");
        Files.writeString(file, "Invalid data", StandardCharsets.UTF_8);

        assertThrows(IOException.class,
                () -> manager.loadBooks(file));
    }
}

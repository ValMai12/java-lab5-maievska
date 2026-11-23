
package files;

import model.Book;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BookFileManager {

    public void saveBooks(Book[] books, Path filePath) throws IOException {
        Path parent = filePath.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                Files.newOutputStream(filePath))) {
            output.writeObject(books);
        }
    }

    public Book[] loadBooks(Path filePath)
            throws IOException, ClassNotFoundException {

        try (ObjectInputStream input = new ObjectInputStream(
                Files.newInputStream(filePath))) {

            Object data = input.readObject();

            if (!(data instanceof Book[])) {
                throw new IOException("Файл не містить масив книг.");
            }

            return (Book[]) data;
        }
    }
}

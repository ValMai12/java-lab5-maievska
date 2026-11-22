
import controller.BookController;
import model.Book;
import model.BookData;
import view.BookView;

public class Main {
    public static void main(String[] args) {
        Book[] books = BookData.createBooks();
        BookView view = new BookView();
        BookController controller = new BookController(books, view);

        controller.run();
    }
}

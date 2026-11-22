
package view;

import model.Book;

public class BookView {

    public void showBooks(String title, Book[] books) {
        System.out.println("\n" + title);

        if (books.length == 0) {
            System.out.println("Книг не знайдено.");
            return;
        }

        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Показати всі книги");
        System.out.println("2. Знайти книги за автором");
        System.out.println("3. Знайти книги за видавництвом");
        System.out.println("4. Знайти книги, видані після заданого року");
        System.out.println("5. Відсортувати книги за видавництвом");
        System.out.println("0. Вийти");
        System.out.print("Виберіть пункт: ");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}

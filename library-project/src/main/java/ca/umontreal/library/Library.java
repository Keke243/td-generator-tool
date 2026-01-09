
package ca.umontreal.library;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        books.add(book);
    }

    public int getNumberOfBooks() {
        return books.size();
    }

    public Book findBookByTitle(String title) {
        for (Book b : books) {
            if (b.getTitle().equals(title)) {
                return b;
            }
        }
        return null;
    }

    public void borrowBook(String title) {
        Book book = findBookByTitle(title);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        if (book.isBorrowed()) {
            throw new IllegalStateException("Book already borrowed");
        }
        book.setBorrowed(true);
    }

    public boolean isAvailable(String title) {
        Book book = findBookByTitle(title);
        return book != null && !book.isBorrowed();
    }
}

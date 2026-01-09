package ca.umontreal.library;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }

    public boolean isAvailable(String title) {
        Book book = findBookByTitle(title);
        return book != null && !book.isBorrowed();
    }
}

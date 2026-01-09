
package ca.umontreal.library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    @Test
    void addBook_increasesSize() {
        Library lib = new Library();
        lib.addBook(new Book("Java"));
        assertEquals(1, lib.getNumberOfBooks());
    }

    @Test
    void findBookByTitle_returnsBook() {
        Library lib = new Library();
        lib.addBook(new Book("Java"));
        assertNotNull(lib.findBookByTitle("Java"));
    }

    @Test
    void borrowBook_marksBookAsBorrowed() {
        Library lib = new Library();
        lib.addBook(new Book("Java"));
        lib.borrowBook("Java");
        assertTrue(lib.findBookByTitle("Java").isBorrowed());
    }

    @Test
    void borrowBook_twice_throwsException() {
        Library lib = new Library();
        lib.addBook(new Book("Java"));
        lib.borrowBook("Java");

        assertThrows(IllegalStateException.class, () -> {
            lib.borrowBook("Java");
        });
    }

    @Test
    void isAvailable_returnsFalseWhenBorrowed() {
        Library lib = new Library();
        lib.addBook(new Book("Java"));
        lib.borrowBook("Java");
        assertFalse(lib.isAvailable("Java"));
    }
}

package me.vrublevsky.carolinatest.carolinatest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiController {

    public record Book(
            String isbn,
            String title,
            String author,
            int price
    ) {
    }

    private static final List<Book> BOOKS = new ArrayList<>();

    @GetMapping("/api/books")
    public List<Book> getAll() {
        return BOOKS;
    }

    @PostMapping("/api/books")
    public ResponseEntity<Void> addBook(@RequestBody Book bookToAdd) {
        var foundBook = BOOKS.stream()
                .filter((book -> book.isbn.equals(bookToAdd.isbn)))
                .findFirst();
        if (foundBook.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to find book");
        }

        BOOKS.add(bookToAdd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/books/{isbn}")
    public Book findBook(@PathVariable String isbn) {
        var foundBook = BOOKS.stream()
                .filter((book -> book.isbn.equals(isbn)))
                .findFirst();

        if (foundBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book");
        }

        return foundBook.get();
    }

    @DeleteMapping("/api/books/{isbn}")
    public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
        BOOKS.removeIf(book -> book.isbn.equals(isbn));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

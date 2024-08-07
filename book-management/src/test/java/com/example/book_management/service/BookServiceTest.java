package com.example.book_management.service;

import com.example.book_management.model.Book;
import com.example.book_management.repo.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Title1", "Author1", 1, "232434");
        book2 = new Book("2", "Title2", "Author2", 2, "4342");
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals(book1, books.get(0));
        assertEquals(book2, books.get(1));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(anyString())).thenReturn(Optional.of(book1));

        Optional<Book> book = bookService.getBookById("1");

        assertTrue(book.isPresent());
        assertEquals(book1, book.get());
        verify(bookRepository, times(1)).findById(anyString());
    }

    @Test
    void testCreateBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book savedBook = bookService.createBook(book1);

        assertNotNull(savedBook);
        assertEquals(book1, savedBook);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        when(bookRepository.existsById(anyString())).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book updatedBook = bookService.updateBook("1", book1);

        assertNotNull(updatedBook);
        assertEquals(book1, updatedBook);
        verify(bookRepository, times(1)).existsById(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() {
        when(bookRepository.existsById(anyString())).thenReturn(false);

        Book updatedBook = bookService.updateBook("1", book1);

        assertNull(updatedBook);
        verify(bookRepository, times(1)).existsById(anyString());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(anyString());

        bookService.deleteBook("1");

        verify(bookRepository, times(1)).deleteById(anyString());
    }

    @Test
    void testSearchBooks() {
        when(bookRepository.findByTitleContainingOrAuthorContaining(anyString(), anyString()))
                .thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.searchBooks("Title", "Author");

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals(book1, books.get(0));
        assertEquals(book2, books.get(1));
        verify(bookRepository, times(1))
                .findByTitleContainingOrAuthorContaining(anyString(), anyString());
    }
}

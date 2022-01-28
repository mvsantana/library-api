package com.projetinho.libraryapi.resource.model.repository;

import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.model.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueIsbnExists() {
        //cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);
        //execução
        boolean exists = repository.existsByIsbn("123");

        //verificação
        Assertions.assertThat(exists).isTrue();
    }

    public static Book createNewBook(String isbn) {
        return Book.builder().title("As aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    public void findByIdTest() {
        Book book = createNewBook("123");
        entityManager.persist(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        Assertions.assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = createNewBook("123");

        Book savedBook = repository.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest() {
        Book book = createNewBook("123");
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        repository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());
        Assertions.assertThat(deletedBook).isNull();

    }
}
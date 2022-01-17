package com.projetinho.libraryapi.service;


import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.impl.BookServiceImpl;
import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.model.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookSeviceTest {

  BookService service;

  @MockBean
  BookRepository repository;

    @BeforeEach
  public void setUp() {
      this.service = new BookServiceImpl(repository);
  }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenário
        Book book = creatValidBook();
        Mockito.when(repository.save(book) )
                .thenReturn(
                        Book.builder().id(11)
                                .isbn("123")
                                .author("Fulano")
                                .title("As aventuras").build());
        //execução
        Book savedBook = service.save(book);

        //verificação
        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private Book creatValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicadoISBN(){
        // cenário
        Book book = creatValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exeption = Assertions.catchThrowable(()->service.save(book));
        Assertions.assertThat(exeption)
                //verificações
                .isInstanceOf(BusinessExeption.class).hasMessage("Isbn já cadastrada.");

        Mockito.verify(repository,Mockito.never()).save(book);
    }


}

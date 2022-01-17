package com.projetinho.libraryapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetinho.libraryapi.api.BookDTO;
import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

     static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;
    @MockBean
    BookService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void creatBookTest() throws Exception {

        BookDTO dto = creatNewBook();
        Book savedBook = Book.builder().id(101).author("Arthur").title("As aventuras").isbn("001").build();
        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status() .isCreated() )
                .andExpect(jsonPath("id").isNotEmpty() )
                .andExpect(jsonPath("title").value("As aventuras") )
                .andExpect(jsonPath("author").value("Arthur") )
                .andExpect(jsonPath("isbn").value("001") )

                ;

     }



    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
    public void creatInvalidBookTest() throws Exception {


        String json = new ObjectMapper().writeValueAsString(new BookDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(3)));
    }
    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já ultilizado por outro.")
    public void creatBookWithDuplicatedIsbn() throws Exception {

       BookDTO dto = creatNewBook();
       String json = new ObjectMapper().writeValueAsString(dto);
       String mensagemErro = "isbn já cadastrado";
       BDDMockito.given(service.save(Mockito.any(Book.class)))
               .willThrow(new BusinessExeption (mensagemErro) );

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));


    }
    private BookDTO creatNewBook() {
        return BookDTO.builder().author("Arthur").title("As aventuras").isbn("001").build();
    }
}

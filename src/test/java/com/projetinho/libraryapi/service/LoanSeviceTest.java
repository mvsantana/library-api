package com.projetinho.libraryapi.service;

import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.model.entity.Loan;
import com.projetinho.libraryapi.model.repository.LoanRepository;
import com.projetinho.libraryapi.service.implement.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanSeviceTest {

    LoanService service;


    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        service = new LoanServiceImpl(repository);

    }


    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest() {

        Book book = Book.builder().id(1L).build();
        String customer = "Fulano";

        Loan savingLoan =
                Loan.builder()
                        .book(book)
                        .customer(customer)
                        .loanDate(LocalDate.now())
                        .build();

        Loan savedLoan = Loan.builder()
                .id(1L)
                .loanDate(LocalDate.now())
                .customer(customer)
                .book(book).build();
        when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
        Mockito.when(repository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = service.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Deve lancar um erro ao salvar um empréstimo com um livro já emprestado")
    public void LoanedsaveLoanTest() {

        Book book = Book.builder().id(1L).build();
        String customer = "Fulano";

        Loan savingLoan =
                Loan.builder()
                        .book(book)
                        .customer(customer)
                        .loanDate(LocalDate.now())
                        .build();

        when(repository.existsByBookAndNotReturned(book)).thenReturn(true);

       Throwable exeption = catchThrowable( ()-> service.save(savingLoan));

       assertThat(exeption).isInstanceOf(BusinessExeption.class)
               .hasMessage("Book already loaned");

       Mockito.verify(repository, never()).save(savingLoan);
    }
}

package com.projetinho.libraryapi.model.repository;

import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository< Loan, Long > {

    boolean existsByBookAndNotReturned(Book book);
}

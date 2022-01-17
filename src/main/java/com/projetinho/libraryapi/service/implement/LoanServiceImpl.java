package com.projetinho.libraryapi.service.implement;

import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.model.entity.Loan;
import com.projetinho.libraryapi.model.repository.LoanRepository;
import com.projetinho.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;

    public LoanServiceImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        if (repository.existsByBookAndNotReturned(loan.getBook())){
            throw new BusinessExeption("Book already loaned");
        }
        return repository.save(loan);
    }
}

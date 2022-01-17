package com.projetinho.libraryapi.impl;

import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.model.repository.BookRepository;
import com.projetinho.libraryapi.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
        throw new BusinessExeption("Isbn já cadastrada.");
        }
        return repository.save(book);
    }
}

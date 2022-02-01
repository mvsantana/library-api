package com.projetinho.libraryapi.service.implement;

import com.projetinho.libraryapi.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BookServiceImpl implements com.projetinho.libraryapi.service.BookService{
    @Override
    public Book save(Book any) {
        return null;
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
       return null;
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return Optional.empty();

    }




}

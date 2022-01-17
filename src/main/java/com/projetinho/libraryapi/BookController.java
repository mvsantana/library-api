package com.projetinho.libraryapi;

import com.projetinho.libraryapi.api.BookDTO;
import com.projetinho.libraryapi.api.exception.ApiErros;
import com.projetinho.libraryapi.api.exception.BusinessExeption;
import com.projetinho.libraryapi.model.entity.Book;
import com.projetinho.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/books")
@RestController
public class BookController {

    public BookService service;
    public ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
                this.modelMapper = mapper;
                this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {
    Book entity = modelMapper.map(dto, Book.class);
    entity = service.save(entity);
    return modelMapper.map(entity,BookDTO.class);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErros(bindingResult);
    }
    @ExceptionHandler(BusinessExeption.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleBusinessExeption(BusinessExeption ex){
        return new ApiErros(ex);
    }

    }


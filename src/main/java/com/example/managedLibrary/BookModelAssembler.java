package com.example.managedLibrary;



import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class BookModelAssembler implements RepresentationModelAssembler<OneBook, EntityModel<OneBook>> {

    @Override
    public EntityModel<OneBook> toModel(OneBook book) {

        return EntityModel.of(book, //
                linkTo(methodOn(BookController.class).getOneBook(book.getId())).withSelfRel(),
                linkTo(methodOn(BookController.class).getAllBooks()).withRel("books"));
    }
}


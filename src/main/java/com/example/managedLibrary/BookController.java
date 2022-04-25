package com.example.managedLibrary;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class BookController {

    private final BookRepository repository;

    private final BookModelAssembler assembler;

    BookController(BookRepository repository, BookModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping("/library/books")
    ResponseEntity<EntityModel<OneBook>> newBook(@RequestBody OneBook book) {

        EntityModel<OneBook> entityModel = assembler.toModel(repository.save(book));


        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
    @GetMapping("/library/books/{id}")
    EntityModel<OneBook> getOneBook(@PathVariable Long id) {

        OneBook book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return assembler.toModel(book);
    }

    @GetMapping("/library/books")
    CollectionModel<EntityModel<OneBook>> getAllBooks() {

        List<EntityModel<OneBook>> books = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(books, linkTo(methodOn(BookController.class).getAllBooks()).withSelfRel());
    }

    @DeleteMapping("/library/books/{id}")
    ResponseEntity<EntityModel<OneBook>>  deleteBook(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/library/api/hello")
    public String hello() {
        return "Hello, the time at the server is now " + new Date() + "\n";
    }
}

package com.example.managedLibrary;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class LoanController {

    private final LoanRepository loanRepository;
    private final LoanModelAssembler loanAssembler;
    private final BookRepository bookRepository;
    private final BookModelAssembler bookAssembler;

    LoanController(LoanRepository loanRepository, LoanModelAssembler loanAssembler, BookRepository bookRepository, BookModelAssembler bookAssembler) {
        this.loanRepository = loanRepository;
        this.loanAssembler = loanAssembler;
        this.bookRepository = bookRepository;
        this.bookAssembler = bookAssembler;
    }

    @GetMapping("/library/loans")
    CollectionModel<EntityModel<Loan>> getAllLoans() {

        List<EntityModel<Loan>> orders = loanRepository.findAll().stream()
                .map(loanAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(LoanController.class).getAllLoans()).withSelfRel());
    }

    @GetMapping("/library/loans/{id}")
    EntityModel<Loan> getOneLoan(@PathVariable Long id) {

        Loan order = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Could not find book "));

        return loanAssembler.toModel(order);
    }

    @PostMapping("/library/loans")
    ResponseEntity<EntityModel<Loan>> newLoan (@RequestBody Loan loan) {
        List<OneBook> booksList = bookRepository.findAll();
        boolean isPresent = false;

        for(OneBook book: booksList){
            if(book.getNameAuthor().equals(loan.getDescription())) {
                isPresent = true;
                break;
            }
        }
        if(!isPresent) {
            throw new LoanNotFoundException("Could not find book ");
        }
        List<Loan> loanList = loanRepository.findAll();
        for(Loan l: loanList){
            if(l.getDescription().equals(loan.getDescription()) )
                if(l.getStatus() == LoanStatus.BORROWED)
                    throw new LoanNotFoundException("The book is already on loan ");
                else if(l.getStatus() == LoanStatus.REMOVE)
                    throw new LoanNotFoundException("The book was lost ");
        }
            loan.setStatus(LoanStatus.BORROWED);
            Loan newLoan = loanRepository.save(loan);

            return ResponseEntity
                    .created(linkTo(methodOn(LoanController.class).getOneLoan(newLoan.getId())).toUri())
                    .body(loanAssembler.toModel(newLoan));

    }

    @DeleteMapping("/library/loans/{id}/lost")
    ResponseEntity<?> lostBook(@PathVariable Long id) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Could not find book "));

        if (loan.getStatus() == LoanStatus.INBOOKCASE || loan.getStatus() == LoanStatus.BORROWED ) {
            loan.setStatus(LoanStatus.REMOVE);
            return ResponseEntity.ok(loanAssembler.toModel(loanRepository.save(loan)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't cancel a loan that is in the " + loan.getStatus() + " status"));
    }

    @PutMapping("/library/loans/{id}/returned")
    ResponseEntity<?> returnedBook(@PathVariable Long id) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException( "Could not find book "));

        if (loan.getStatus() == LoanStatus.BORROWED) {
            loan.setStatus(LoanStatus.INBOOKCASE);
            return ResponseEntity.ok(loanAssembler.toModel(loanRepository.save(loan)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't complete an loan that is in the " + loan.getStatus() + " status"));
    }
}
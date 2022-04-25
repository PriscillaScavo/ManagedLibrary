package com.example.managedLibrary;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class LoanModelAssembler implements RepresentationModelAssembler<Loan, EntityModel<Loan>> {

    @Override
    public EntityModel<Loan> toModel(Loan order) {

        EntityModel<Loan> orderModel = EntityModel.of(order,
                linkTo(methodOn(LoanController.class).getOneLoan(order.getId())).withSelfRel(),
                linkTo(methodOn(LoanController.class).getAllLoans()).withRel("Loans"));

        if (order.getStatus() == LoanStatus.BORROWED) {
            orderModel.add(linkTo(methodOn(LoanController.class).lostBook(order.getId())).withRel("lost"));
            orderModel.add(linkTo(methodOn(LoanController.class).returnedBook(order.getId())).withRel("returned"));
        }

        return orderModel;
    }
}
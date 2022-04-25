package com.example.managedLibrary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;

import static com.example.managedLibrary.LoanStatus.BORROWED;
import static com.example.managedLibrary.LoanStatus.INBOOKCASE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class ManagedLibraryApplicationTests {
	private static final ObjectMapper om = new ObjectMapper();
	@Autowired
	private MockMvc mvc;

	@MockBean
	private BookController serviceBook;

	@MockBean
	private BookRepository repositoryBook;

	@MockBean
	private LoanController serviceLoan;

	@MockBean
	private LoanRepository repositoryLoan;

	@Test
	public void getOneBook()
			throws Exception {
		EntityModel<OneBook> book1 = EntityModel.of(new OneBook("Ulysses", "James Joyce"));
		EntityModel<OneBook> book2 = EntityModel.of(new OneBook("One Hundred Years of Solitude", "Gabriel Garcia Marquez"));
		Collection<EntityModel<OneBook>> books = Arrays.asList(book1, book2);
		CollectionModel<EntityModel<OneBook>> allBooks = CollectionModel.of(books);
		long id1 = 1;
		long id2= 2;
		given(serviceBook.getOneBook(id1)).willReturn(book1);
		given(serviceBook.getOneBook(id2)).willReturn(book2);

		mvc.perform(get("/library/books/2")
				.contentType(new MediaType("application", "hal+json")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$",aMapWithSize(4)))
				.andExpect(jsonPath("$.name").value(book2.getContent().getName()))
				.andReturn();
	}
	@Test
	public void getAllBooks()
			throws Exception {
		EntityModel<OneBook> book1 = EntityModel.of(new OneBook("Ulysses", "James Joyce"));
		EntityModel<OneBook> book2 = EntityModel.of(new OneBook("One Hundred Years of Solitude", "Gabriel Garcia Marquez"));
		Collection<EntityModel<OneBook>> books = Arrays.asList(book1, book2);
		CollectionModel<EntityModel<OneBook>> allBooks = CollectionModel.of(books);

		given(serviceBook.getAllBooks()).willReturn(allBooks);

		mvc.perform(get("/library/books")
				.contentType(new MediaType("application", "hal+json")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.oneBooks.*",hasSize(2)))
				.andExpect(jsonPath("$._embedded.oneBooks.[0].name").value(book1.getContent().getName()))
				.andReturn();
	}
	@Test
	public void postBook() throws Exception {
		OneBook b1 = new OneBook("Ulysses", "James Joyce");
		EntityModel<OneBook> entitybook1 = EntityModel.of(b1);
		ResponseEntity<EntityModel<OneBook>> responseEntitybook1 = new ResponseEntity<EntityModel<OneBook>>(entitybook1,HttpStatus.CREATED);
		when(serviceBook.newBook(Mockito.any(OneBook.class))).thenReturn(responseEntitybook1);
		mvc.perform(MockMvcRequestBuilders.post("/library/books")
				.content(om.writeValueAsString(b1))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("Ulysses")))
				.andExpect(jsonPath("$.author", is("James Joyce")))
				.andReturn();

	}

	@Test
	public void deleteBook() throws Exception {
		OneBook b1 = new OneBook("Ulysses", "James Joyce");
		EntityModel<OneBook> book1 = EntityModel.of(b1);
		ResponseEntity<EntityModel<OneBook>> responseEntitybook1 = new ResponseEntity<EntityModel<OneBook>>(book1, HttpStatus.OK);
		long id1 = 1;
		mvc.perform(MockMvcRequestBuilders
				.delete("/library/books/1")
				.content(om.writeValueAsString(b1))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void getOneLoan() throws Exception {
		EntityModel<Loan> loan1 = EntityModel.of(new Loan("Ulysses by James Joyce", INBOOKCASE));
		EntityModel<Loan> loan2 = EntityModel.of(new Loan("One Hundred Years of Solitude by Gabriel Garcia Marquez", INBOOKCASE));
		Collection<EntityModel<Loan>> loans = Arrays.asList(loan1, loan2);
		CollectionModel<EntityModel<Loan>> allLoans = CollectionModel.of(loans);
		long id1 = 1;
		long id2= 2;
		given(serviceLoan.getOneLoan(id1)).willReturn(loan1);
		given(serviceLoan.getOneLoan(id2)).willReturn(loan2);

		mvc.perform(get("/library/loans/2")
				.contentType(new MediaType("application", "hal+json")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$",aMapWithSize(3)))
				.andExpect(jsonPath("$.description").value(loan2.getContent().getDescription()))
				.andReturn();
	}
	@Test
	public void getAllLoans() throws Exception {
		EntityModel<Loan> loan1 = EntityModel.of(new Loan("Ulysses by James Joyce", INBOOKCASE));
		EntityModel<Loan> loan2 = EntityModel.of(new Loan("One Hundred Years of Solitude by Gabriel Garcia Marquez", INBOOKCASE));
		Collection<EntityModel<Loan>> loans = Arrays.asList(loan1, loan2);
		CollectionModel<EntityModel<Loan>> allLoans = CollectionModel.of(loans);
		given(serviceLoan.getAllLoans()).willReturn(allLoans);

		mvc.perform(get("/library/loans")
				.contentType(new MediaType("application", "hal+json")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.loans.*",hasSize(2)))
				.andExpect(jsonPath("$._embedded.loans.[0].description").value(loan1.getContent().getDescription()))
				.andReturn();
	}
	@Test
	public void postLoan() throws Exception {
		Loan l1 = new Loan("Ulysses by James Joyce", INBOOKCASE);
		EntityModel<Loan> entityLoan1 = EntityModel.of(l1);
		ResponseEntity<EntityModel<Loan>> responseEntityLoan1 = new ResponseEntity<EntityModel<Loan>>(entityLoan1,HttpStatus.CREATED);
		when(serviceLoan.newLoan(Mockito.any(Loan.class))).thenReturn(responseEntityLoan1);
		mvc.perform(MockMvcRequestBuilders.post("/library/loans")
				.content(om.writeValueAsString(l1))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.description", is("Ulysses by James Joyce")))
				.andExpect(jsonPath("$.status", is("INBOOKCASE")))
				.andReturn();

	}

	@Test
	public void deleteLoan() throws Exception {
		Loan l1 = new Loan("Ulysses by James Joyce", INBOOKCASE);
		EntityModel<Loan> loan1 = EntityModel.of(l1);
		ResponseEntity<EntityModel<Loan>> responseEntityLoan1 = new ResponseEntity<EntityModel<Loan>>(loan1, HttpStatus.OK);
		long id1 = 1;
		given(serviceLoan.getOneLoan(id1)).willReturn(loan1);
		mvc.perform(MockMvcRequestBuilders
				.delete("/library/loans/1/lost")
				.content(om.writeValueAsString(l1))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

}

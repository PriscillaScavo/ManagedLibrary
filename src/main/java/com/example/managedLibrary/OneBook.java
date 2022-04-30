package com.example.managedLibrary;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
class OneBook {

    private @Id @GeneratedValue Long id;
    private String name ;
    private String author;
    private Long idFE;


    OneBook() {}

    OneBook(String name, String author, Long idFE) {
        this.name = name;
        this.author = author;
        this.idFE = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFE() {
        return id;
    }

    public void setIdFE(Long idFE) {
        this.idFE = idFE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }
    public String getNameAuthor() {
        return this.name + " by " + this.author;
    }

    public void setNameAuthor(String nameAuthor) {
        String[] parts = nameAuthor.split(" by ");
        this.name = parts[0];
        this.author = parts[1];
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OneBook))
            return false;
        OneBook book = (OneBook) o;
        return Objects.equals(this.id, book.id) && Objects.equals(this.name, book.name)
                && Objects.equals(this.author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.author);
    }

    @Override
    public String toString() {
        return "OneBook{" + "id=" + this.id + ", name='" + this.name + '\'' + ", author='" + this.author + '\'' + "idFE=" + this.idFE + '}' ;
    }
}
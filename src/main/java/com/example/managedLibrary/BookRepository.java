package com.example.managedLibrary;


import org.springframework.data.jpa.repository.JpaRepository;

interface BookRepository extends JpaRepository<OneBook, Long>{

}
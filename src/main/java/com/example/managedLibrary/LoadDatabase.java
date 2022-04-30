package com.example.managedLibrary;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {

        return args -> {
            bookRepository.save(new OneBook("Ulysses", "James Joyce",1L));
            bookRepository.save(new OneBook("One Hundred Years of Solitude", "Gabriel Garcia Marquez", 2L));

            bookRepository.findAll().forEach(book -> log.info("Preloaded " + book));

            };

        };
    }

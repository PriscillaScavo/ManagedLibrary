package com.example.managedLibrary;

import org.springframework.data.jpa.repository.JpaRepository;

interface LoanRepository extends JpaRepository<Loan, Long> {
}
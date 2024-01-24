package com.telusko.bankingapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.telusko.bankingapplication.bankingObjects.Loan;
import com.telusko.bankingapplication.bankingObjects.User;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}

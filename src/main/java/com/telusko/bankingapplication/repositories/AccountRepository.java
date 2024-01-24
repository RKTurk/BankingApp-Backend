package com.telusko.bankingapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.telusko.bankingapplication.bankingObjects.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

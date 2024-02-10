package com.telusko.bankingapplication.controllers;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.telusko.bankingapplication.bankingObjects.Account;
import com.telusko.bankingapplication.bankingObjects.Amount;
import com.telusko.bankingapplication.bankingObjects.Loan;
import com.telusko.bankingapplication.bankingObjects.User;
import com.telusko.bankingapplication.exceptions.ResourceNotFoundException;
import com.telusko.bankingapplication.repositories.AccountRepository;
import com.telusko.bankingapplication.repositories.LoanRepository;
import com.telusko.bankingapplication.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") final Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            return ResponseEntity.ok().body(user);
        }
        return null;
    }

    @RequestMapping("/adduser")
    public User createUser(@RequestParam("name") String name, @RequestParam("cnic") String cnic) {
        User user = new User();
        user.setName(name);
        user.setCnic(cnic);
        return userRepository.save(user);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable(value = "id") final Long accountId) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not for for this id :: " + accountId));
        return ResponseEntity.ok().body(account);
    }

    @RequestMapping("/addaccount")
    public Account createAccount(@RequestParam("userId") long userId, @RequestParam("balance") Double balance) {
        List<Account> accounts = accountRepository.findAll();
        List<Account> userAccounts = accounts.stream().filter(account1 -> account1.getUserId() == userId).collect(Collectors.toList());
        if (userAccounts.size()>=3) {
            System.out.println("More than 3 accounts found for userID " + userId);
            return null;
        }
        Account account = new Account();
        account.setBalance(balance);
        account.setUserId(userId);
        account.setAccountId(generateRandom10DigitNumber());
        return accountRepository.save(account);
    }
    // Method to generate a random 10-digit number for Account Id
    private String generateRandom10DigitNumber() {
        Random random = new Random();
        long randomNumber = random.nextLong() % 10000000000L; // Ensure it's 10 digits
        if(randomNumber < 0) {
            randomNumber *= -1;
        }
        return String.format("%010d", randomNumber);
    }

    @RequestMapping(value = "/depositmoney")
    public Account depositMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not for for this id :: " + accountId));
        double initialBalance = account.getBalance();
        account.setBalance(initialBalance+amount);
        return accountRepository.save(account);
    }

    @RequestMapping(value = "/withdrawmoney")
    public Account withdrawMoney(@RequestParam(value = "accountId") long accountId, @RequestParam("amount") double amount) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not for for this id :: " + accountId));
        double initialBalance = account.getBalance();
        if (amount > initialBalance) {
            System.out.println("Withdrawal amount exceeded");
            return null;
        }
        account.setBalance(initialBalance-amount);
        return accountRepository.save(account);
    }

    @RequestMapping("/addloan")
    public Loan getLoan(@RequestParam("userId") long userId, @RequestParam("sanctionAmount") Double sanctionAmount) {
        List<Account> accounts = accountRepository.findAll();
        List<Account> userAccounts = accounts.stream().filter(account1 -> account1.getUserId() == userId).collect(Collectors.toList());
        double totalBalance = 0;
        for (Account userAccount : userAccounts) {
            totalBalance += userAccount.getBalance();
        }
        if (2*sanctionAmount < totalBalance) {
            Loan loan = new Loan();
            loan.setSanctionAmount(sanctionAmount);
            loan.setUserId(userId);
            return loanRepository.save(loan);
        }
        System.out.println("Loan amount requested exceeds limit!");
        return null;
    }

    @GetMapping("/loans")
    public List<Loan> getAllLoans() {
        return (List<Loan>) loanRepository.findAll();
    }


}

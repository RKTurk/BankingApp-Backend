package com.telusko.bankingapplication.bankingObjects;

import jakarta.persistence.*;


@Entity
@Table(name = "Accounts")
public class Account {
    private long id;
    private long userId;
    private double balance;
    private String accountId;


    public Account() {
    }

    public Account(long id, long userId, double balance, String accountId) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.accountId = accountId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    @Column(name = "user_id", nullable = false)
    public long getUserId(){
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "balance", nullable = false)
    public double getBalance(){
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getAccountId() {
        return accountId;
    }
}

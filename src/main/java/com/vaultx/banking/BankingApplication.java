package com.vaultx.banking;

import com.vaultx.banking.service.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    /**
     * Seeds demo data on startup — mirrors the scenarios from the original Main.java
     */
    @Bean
    CommandLineRunner seedData(BankService bank) {
        return args -> {
            var salman  = bank.createSavingsAccount("Salman Khan", 10000);
            var priya   = bank.createSavingsAccount("Priya Sharma", 5000);
            var startup = bank.createCurrentAccount("Startup Corp", 20000);

            bank.deposit(salman.getAccountNumber(), 3000);
            bank.deposit(startup.getAccountNumber(), 15000);

            bank.withdraw(salman.getAccountNumber(), 2000);
            bank.withdraw(startup.getAccountNumber(), 8000);

            bank.deposit(salman.getAccountNumber(), 5000);
            bank.transfer(salman.getAccountNumber(), priya.getAccountNumber(), 1500);

            System.out.println("\n  ✔ Demo data seeded. API ready at http://localhost:8080/api\n");
        };
    }
}

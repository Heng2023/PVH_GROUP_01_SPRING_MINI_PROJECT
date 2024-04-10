package org.example.expensetracking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Expense Tracking",
        description = "Tracking your expenses is like shining a light on your financial path; it illuminates where your money goes, guiding you towards better financial decisions.",
        version = "1.0"))

@SpringBootApplication
public class ExpenseTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackingApplication.class, args);
    }

}

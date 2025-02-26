package com.tutorialseu.expensetrackerbackend.controller;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Expense;
import com.tutorialseu.expensetrackerbackend.service.ExpenseService;
import com.tutorialseu.expensetrackerbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ExpenseController {
    private final UserService userService;
    private final ExpenseService expenseService;

    public ExpenseController(UserService userService, ExpenseService expenseService){
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/expenses/day/{date}")
    public ResponseEntity<List<Expense>> getExpenseByDay(@PathVariable String date, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        List<Expense> expenses = expenseService.getExpenseByDay(date, appUser.getId());

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/expenses/category/{category}/month")
    public ResponseEntity<List<Expense>> getExpenseByCategoryandMonth(
            @PathVariable String category,
            @RequestParam String month, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        List<Expense> expenses= expenseService.getExpenseByCategoryAndMonth(category, month, appUser.getId());
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/expenses/categories")
    public ResponseEntity<List<String>> getAllExpenseCategories(Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        List<String> categories = expenseService.getAllCategories(appUser.getId());

        if(categories.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<Optional<Expense>> getExpenseById(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        return ResponseEntity.ok(expenseService.getExpenseById(id, appUser.getId()));
    }

    @PostMapping("/expenses")
    public ResponseEntity<Expense> addExpense(@RequestBody Expense expense, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);

        Expense newExpense = expenseService.addExpense(expense, appUser.getId());
        return new ResponseEntity<>(newExpense, HttpStatus.CREATED);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long id,
            @RequestBody Expense expense, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        expense.setId(id);
        boolean isUpdated = expenseService.updateExpense(expense, appUser.getId());
        if(isUpdated){
            return new ResponseEntity<>(expense, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Expense> deleteExpense(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();
        AppUser appUser = userService.findByUsername(username);
        boolean isDeleted = expenseService.deleteExpense(id, appUser.getId());
        if(isDeleted){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

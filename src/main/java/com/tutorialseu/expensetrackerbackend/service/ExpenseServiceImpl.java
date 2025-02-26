package com.tutorialseu.expensetrackerbackend.service;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Expense;
import com.tutorialseu.expensetrackerbackend.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserService userService){
        this.expenseRepository = expenseRepository;
        this.userService = userService;
    }

    @Override
    public List<Expense> getAllUserExpenses(Long userId) {
        return new ArrayList<>(expenseRepository.findByUserIdOrderByDateDesc(userId));
    }

    @Override
    public List<Expense> getExpenseByDay(String date, Long userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId)
                .stream().filter(expense -> expense.getDate()
                        .equals(date)).toList();
    }

    @Override
    public List<Expense> getExpenseByCategoryAndMonth(String category, String month, Long userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId)
                .stream().filter(
                        expense -> expense.getCategory()
                                .equalsIgnoreCase(category)
                                && expense.getDate().startsWith(month)
                ).toList();
    }

    @Override
    public List<String> getAllCategories(Long userId) {
        return expenseRepository.findByUserIdOrderByDateDesc(userId)// Get all the expenses
                /*
                Converts the list of expenses
                (List<Expense>) into a stream.
                A Stream is an abstraction introduced in Java 8 that allows you to perform
                a sequence of operations like filtering, mapping, reducing etc. on elements of a collection
                 */
                .stream()
                /*
                map operation is used to transform elements in the stream.
                Here, it takes each Expense object and transforms it by applying
                the getCategory() method to each one
                 */
                // Expense::getCategory is a method reference, short for (expense) -> expense.getCategory().
                .map(Expense::getCategory)
                /*
                The distinct() operation removes duplicate elements.
                It ensures that each category only appears once in the resulting stream
                 */
                .distinct()
                //Collects all elements in the stream and turns them into a List.
                .toList();
    }

    @Override
    public Optional<Expense> getExpenseById(Long id, Long userId) {
        return expenseRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public Expense addExpense(Expense expense, Long userId) {
        Optional<AppUser> userOptional = userService.findUserById(userId);
        if(userOptional.isPresent()){
            AppUser user = userOptional.get();
            expense.setUser(user);
            return expenseRepository.save(expense);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public boolean updateExpense(Expense updatedExpense, Long userId) {
        Optional<Expense> existingExpense =
                expenseRepository.findByIdAndUserId(
                updatedExpense.getId(), userId);
        if(existingExpense.isPresent()){
            updatedExpense.setUser(existingExpense.get().getUser());
            expenseRepository.save(updatedExpense);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteExpense(Long id, Long userId) {
        Optional<Expense> existingExpense =
                expenseRepository.findByIdAndUserId(id, userId);
        if(existingExpense.isPresent()){
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

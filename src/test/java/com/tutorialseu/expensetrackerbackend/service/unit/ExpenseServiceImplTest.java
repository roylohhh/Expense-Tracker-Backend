package com.tutorialseu.expensetrackerbackend.service.unit;

import com.tutorialseu.expensetrackerbackend.model.AppUser;
import com.tutorialseu.expensetrackerbackend.model.Expense;
import com.tutorialseu.expensetrackerbackend.repository.ExpenseRepository;
import com.tutorialseu.expensetrackerbackend.service.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense expense1;
    private Expense expense2;
    private AppUser user;

    @BeforeEach
    void setUp(){

        user = new AppUser();
        user.setId(1L);
        user.setUsername("JohnDoe");

        expense1 = new Expense();
        expense1.setId(1L);
        expense1.setExpenseType(0);
        expense1.setDate("2025-02-18");
        expense1.setAmount(300.50);
        expense1.setCategory("Groceries");
        expense1.setAccount("cash");
        expense1.setUser(user);

        expense2 = new Expense();
        expense2.setId(2L);
        expense2.setExpenseType(1);
        expense2.setDate("2025-02-18");
        expense2.setAmount(60.23);
        expense2.setCategory("Transport");
        expense2.setAccount("credit");
        expense2.setUser(user);
    }

    @Test
    void testGetAllUserExpenses(){
        when(expenseRepository.findByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Arrays.asList(expense1, expense2));
        List<Expense> expenses = expenseService.getAllUserExpenses(user.getId());
        assertEquals(2, expenses.size());
        verify(expenseRepository, times(1)).findByUserIdOrderByDateDesc(user.getId());
    }

    @Test
    void TestGetExpenseByDay() {
        when(expenseRepository.findByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Arrays.asList(expense1, expense2));

        List<Expense> expenses = expenseService.getExpenseByDay("2025-02-18", user.getId());

        assertEquals(2, expenses.size());
    }

    @Test
    void TestGetExpenseByCategoryAndMonth(){
        when(expenseRepository.findByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Arrays.asList(expense1, expense2));

        List<Expense> expenses = expenseService.getExpenseByCategoryAndMonth("Groceries", "2025-02", user.getId());
        assertEquals(1, expenses.size());
    }

    @Test
    void TestGetAllCategories(){
        when(expenseRepository.findByUserIdOrderByDateDesc(user.getId()))
                .thenReturn(Arrays.asList(expense1, expense2));

        List<String> categories = expenseService.getAllCategories(user.getId());
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Groceries"));
        assertTrue(categories.contains("Transport"));
    }

    @Test
    void TestGetExpenseById(){
        when(expenseRepository.findByIdAndUserId(expense1.getId(), user.getId()))
                .thenReturn(Optional.of(expense1));

        Optional<Expense> foundExpense = expenseService.getExpenseById(expense1.getId(), user.getId());
        assertTrue(foundExpense.isPresent());
        assertEquals(expense1.getId(), foundExpense.get().getId());
    }

    @Test
    void TestUpdateExpenseIfExists() {
        when(expenseRepository.findByIdAndUserId(expense1.getId(), user.getId()))
                .thenReturn(Optional.of(expense1));
        when(expenseRepository.save(expense1)).thenReturn(expense1);

        boolean updated = expenseService.updateExpense(expense1, user.getId());

        assertTrue(updated);
        verify(expenseRepository, times(1)).save(expense1);
    }

    @Test
    void TestShouldNotUpdateExpenseIfNotFound() {
        when(expenseRepository.findByIdAndUserId(expense1.getId(), user.getId()))
                .thenReturn(Optional.empty());

        boolean updated = expenseService.updateExpense(expense1, user.getId());

        assertFalse(updated);
    }

    @Test
    void TestDeleteExpenseIfExists() {
        when(expenseRepository.findByIdAndUserId(expense1.getId(), user.getId()))
                .thenReturn(Optional.of(expense1));

        boolean deleted = expenseService.deleteExpense(expense1.getId(), user.getId());

        assertTrue(deleted);
        verify(expenseRepository, times(1)).deleteById(expense1.getId());
    }

    @Test
    void TestShouldNotDeleteExpenseIfNotFound() {
        when(expenseRepository.findByIdAndUserId(expense1.getId(), user.getId()))
                .thenReturn(Optional.empty());

        boolean deleted = expenseService.deleteExpense(expense1.getId(), user.getId());

        assertFalse(deleted);
        verify(expenseRepository, never()).deleteById(anyLong());
    }
}


package com.luxfacta.treinamento.desafioandroidparte4.ModelDAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;

import java.util.List;


@Dao
public interface ExpenseDAO {
    @Query("SELECT descricao FROM Despesas WHERE id = :id")
    String getDespesaById(int id);

    @Query("SELECT * FROM Despesas")
    List<Despesas> getAllExpenses();

    @Insert
    void insertExpense(Despesas... despesas);

    @Update
    void updateExpense(Despesas... despesas);

    @Delete
    void deleteExpense(Despesas despesas);

    @Query("DELETE FROM Despesas")
    void deleteAllDespesas();


}

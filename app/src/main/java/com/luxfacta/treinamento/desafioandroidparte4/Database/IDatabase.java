package com.luxfacta.treinamento.desafioandroidparte4.Database;


import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;

import java.util.List;

import io.reactivex.Flowable;

public interface IDatabase {

    Flowable<Despesas> getDespesaById(int despsId);
    Flowable<List<Despesas>> getAllDespesas();
    void inserDespesas(Despesas... despesas);
    void updateDespesa(Despesas... despesas);
    void deleteAllDespesas(Despesas... despesas);


}

package com.luxfacta.treinamento.desafioandroidparte4.Application;

import android.app.Application;


import com.luxfacta.treinamento.desafioandroidparte4.Database.Database;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;

import java.util.List;

public class MyApplication extends Application {
    private Database db;

    @Override
    public void onCreate() {
        super.onCreate();

        db = Database.getInstance(getApplicationContext());
    }

    public MyApplication() {

    }

    public List<Despesas> getAllExpenses(){
        return db.despesaDAO().getAllExpenses();
    }

    public boolean insertDespesas(final Despesas despesas){
        try {
            db.despesaDAO().insertExpense(despesas);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean updateExpense(final Despesas despesas){
        try {
            db.despesaDAO().updateExpense(despesas);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean deleteAll(final Despesas despesas){
        try {
            db.despesaDAO().deleteExpense(despesas);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

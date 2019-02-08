package com.luxfacta.treinamento.desafioandroidparte4.Database;


import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import com.luxfacta.treinamento.desafioandroidparte4.ModelDAO.ExpenseDAO;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;

import java.util.List;

import io.reactivex.Flowable;

@android.arch.persistence.room.Database(entities = {Despesas.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase implements IDatabase {

        private static Database mInstance;
        private IDatabase mLocalDatabase;

        public abstract ExpenseDAO despesaDAO();

        public static Database getInstance(Context context){
            if(mInstance == null) mInstance = Room.databaseBuilder(context, Database.class, "despesaDB").allowMainThreadQueries().build();
            return mInstance;
        }


    @Override
    public Flowable<Despesas> getDespesaById(int despsId) {
        return mLocalDatabase.getDespesaById(despsId);
    }

    @Override
    public Flowable<List<Despesas>> getAllDespesas() {
        return mLocalDatabase.getAllDespesas();
    }

    @Override
    public void inserDespesas(Despesas... despesas) {
        mLocalDatabase.inserDespesas();
    }

    @Override
    public void updateDespesa(Despesas... despesas) {
        mLocalDatabase.updateDespesa();
    }

    @Override
    public void deleteAllDespesas(Despesas... despesas) {
        mLocalDatabase.deleteAllDespesas();
    }
}

package com.luxfacta.treinamento.desafioandroidparte4.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.luxfacta.treinamento.desafioandroidparte4.Adapter.Adapter;
import com.luxfacta.treinamento.desafioandroidparte4.Application.MyApplication;
import com.luxfacta.treinamento.desafioandroidparte4.Database.Database;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDAO.ExpenseDAO;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;
import com.luxfacta.treinamento.desafioandroidparte4.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


public class ListaFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private ExpenseDAO expenseDAO;
    private Database database;
    private List<Despesas> listaDespesas;
    private RecyclerView.Adapter despesasAdapter;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.listaDespesas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        carregaLista();


        return view;
    }


    public void carregaLista() {

        listaDespesas = new ArrayList<>();
        Database db = Database.getInstance(getContext());
        listaDespesas.addAll(db.despesaDAO().getAllExpenses());

        despesasAdapter = new Adapter(getActivity(), listaDespesas);
        recyclerView.setAdapter(despesasAdapter);
    }


    public void notifyInsert(Despesas despesas) {

        List<Despesas> list = ((MyApplication) getActivity().getApplication()).getAllExpenses();
        int position = list.size();

        listaDespesas.add(despesas);
        despesasAdapter.notifyItemInserted(position);
        despesasAdapter.notifyItemRangeChanged(position, listaDespesas.size());
    }

    public void notifyUpdate(int position) {
        despesasAdapter.notifyItemChanged(position);
    }



}



package com.luxfacta.treinamento.desafioandroidparte4.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.luxfacta.treinamento.desafioandroidparte4.Adapter.TabsAdapter;
import com.luxfacta.treinamento.desafioandroidparte4.Database.Database;
import com.luxfacta.treinamento.desafioandroidparte4.Fragment.CadastroFragment;
import com.luxfacta.treinamento.desafioandroidparte4.Fragment.INotifyInsert;
import com.luxfacta.treinamento.desafioandroidparte4.Fragment.ListaFragment;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;
import com.luxfacta.treinamento.desafioandroidparte4.R;


public class MainActivity extends AppCompatActivity implements INotifyInsert {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListaFragment listaFragment;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyUpdate();

        viewPager = findViewById(R.id.vp_pagina_id);
        tabLayout = findViewById(R.id.tabLayout);

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager());

        listaFragment = new ListaFragment();
        tabsAdapter.addFragment(listaFragment, "Lista");
        tabsAdapter.addFragment(new CadastroFragment(), "Cadastro");

        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void notifyInsert(Despesas despesas) {
        listaFragment.notifyInsert(despesas);
    }
    public void verifyUpdate(){
        Intent i = getIntent();
        if(i.hasExtra("position")){
            int position = i.getIntExtra("position", -1);
            if(position != -1){
                listaFragment.notifyUpdate(position);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
         return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_apagar:

                database = Database.getInstance(MainActivity.this);
                database.despesaDAO().deleteAllDespesas();
                listaFragment.carregaLista();

                break;
        }



        return super.onOptionsItemSelected(item);
    }
}



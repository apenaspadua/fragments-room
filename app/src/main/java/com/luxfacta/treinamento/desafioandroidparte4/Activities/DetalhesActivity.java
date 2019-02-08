package com.luxfacta.treinamento.desafioandroidparte4.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase.Despesas;
import com.luxfacta.treinamento.desafioandroidparte4.R;


public class DetalhesActivity extends AppCompatActivity {

    TextView descricao, tipo, valor;
    private ImageView imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        descricao = findViewById(R.id.tvNome);
        tipo = findViewById(R.id.tvTipo);
        valor = findViewById(R.id.tvValor);
        imagem = findViewById(R.id.imageViewDetalhe);

        Intent intent = getIntent();
        Despesas despesas = (Despesas) intent.getSerializableExtra("expense");

        descricao.setText(despesas.getDescricao());
        tipo.setText(despesas.getTipo());
        valor.setText("R$ " + String.valueOf(despesas.getValor()));

        if (!despesas.getImagem().equals("Default")) {
            Glide.with(this).load(despesas.getImagem()).into(imagem);
        }
        findViewById(R.id.btnVoltar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


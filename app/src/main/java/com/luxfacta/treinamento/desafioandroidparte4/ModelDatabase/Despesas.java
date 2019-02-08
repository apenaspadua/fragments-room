package com.luxfacta.treinamento.desafioandroidparte4.ModelDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
    public class Despesas implements Serializable {
        private static Despesas mInstance;

        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        private int id;

        @ColumnInfo(name = "descricao")
        private String descricao;

        @ColumnInfo(name = "valor")
        private Double valor;

        @ColumnInfo(name = "tipo")
        private String tipo;

        @ColumnInfo(name = "imagem")
        private String imagem;

        public static synchronized Despesas getInstance(){
            if(mInstance == null) mInstance = new Despesas();

            return mInstance;
        }
        public Despesas(int i, String steam, double v, String pessoal, String s){

         }
        public Despesas(){

        }

        @Ignore
        public Despesas(String descricao, Double valor, String tipo) {
            this.descricao = descricao;
            this.valor = valor;
            this.tipo = tipo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public Double getValor() {
            return valor;
        }

        public void setValor(Double valor) {
            this.valor = valor;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getImagem() {
            return imagem;
        }

        public void setImagem(String imagem) {
            this.imagem = imagem;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString();
        }

    }

package com.example.appagenda;

import java.io.Serializable;

public class DbAmigo implements Serializable {

    private int id;
    private String nome;
    private String celular;
    private int status;

    public DbAmigo(int id, String nome, String celular, int status){
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.status = status;
    }
    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCelular(){
        return this.celular;
    }

    public int getStatus(){
        return this.status;
    }
    @Override
    public boolean equals(Object o){
        return this.id == ((DbAmigo)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}


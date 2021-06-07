package com.example.aplicativo_jogo.Datasource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context): SQLiteOpenHelper (context, DATABASE_NAME,null,  DATABASE_VERSION){

    //Somente a primeira vez que é excutado a aplicação
    // create a tabela
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE_JOGO)
    }

    //Quando muda o DATABASE_VERSION  de 1 para 2 então executa o código abaixo
    //criando um campo IMAGEM na tabela
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //val ALTER_TABLE = " ALTER TABLE ${DatabaseDefinitions.jogo.TABLE_NAME}" +
          //                "ADD COLUMN  ${DatabaseDefinitions.jogo.Columns.IMAGEM} BLOB"
         //db.execSQL(ALTER_TABLE)
        //não funcionou então inclui na tabela create o campo imagem e reiniciei o emulador
    }


    companion object{
        private const val DATABASE_NAME = "jogo.db"
        private const val DATABASE_VERSION = 1
        private const val CREATE_TABLE_JOGO = "CREATE TABLE ${DatabaseDefinitions.jogo.TABLE_NAME}(" +
                "${DatabaseDefinitions.jogo.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DatabaseDefinitions.jogo.Columns.TITULO} TEXT, " +
                "${DatabaseDefinitions.jogo.Columns.PRODUTORA} TEXT," +
                "${DatabaseDefinitions.jogo.Columns.NOTA} REAL, " +
                "${DatabaseDefinitions.jogo.Columns.CONSOLE} TEXT," +
                "${DatabaseDefinitions.jogo.Columns.ZERADO} INTEGER," +
                "${DatabaseDefinitions.jogo.Columns.IMAGEM} BLOB );"
    }
}
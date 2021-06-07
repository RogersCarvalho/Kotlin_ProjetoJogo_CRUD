package com.example.aplicativo_jogo.Repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.aplicativo_jogo.Datasource.DatabaseDefinitions
import com.example.aplicativo_jogo.Datasource.DatabaseHelper
import com.example.aplicativo_jogo.Model.Jogo
import java.io.ByteArrayOutputStream


class JogoRepository (context: Context) {

    private val dbHelper = DatabaseHelper(context)

    //Insere dados no BD
    fun save(jogo: Jogo): Int {
        val db = dbHelper.writableDatabase
        val valores = ContentValues()

        valores.put(DatabaseDefinitions.jogo.Columns.TITULO, jogo.nomeJogo)
        valores.put(DatabaseDefinitions.jogo.Columns.PRODUTORA, jogo.produtora)
        valores.put(DatabaseDefinitions.jogo.Columns.NOTA, jogo.notaJogo)
        valores.put(DatabaseDefinitions.jogo.Columns.CONSOLE, jogo.console)
        valores.put(DatabaseDefinitions.jogo.Columns.ZERADO, jogo.zerado)

        //Transforma a imagem em bytes para gravar no Banco---------------------//
        val stream = ByteArrayOutputStream()
        jogo.imagem?.compress(Bitmap.CompressFormat.PNG,100,stream)
        val imageArray = stream.toByteArray()
        valores.put(DatabaseDefinitions.jogo.Columns.IMAGEM, imageArray)
        //-----------------------------------------------------------------------//

        val id = db.insert(DatabaseDefinitions.jogo.TABLE_NAME, null, valores)
        return id.toInt()
    }


    //Selececiona todos dados no banco
    fun getJogos(): ArrayList<Jogo> {

        val db = dbHelper.readableDatabase
        val ordenacao = "${DatabaseDefinitions.jogo.Columns.TITULO} ASC"

        // val projecao = ArrayOf (DatabaseDefinitions.jogo.Columns.ID, )
        val projection = arrayOf(
                DatabaseDefinitions.jogo.Columns.ID,
                DatabaseDefinitions.jogo.Columns.TITULO,
                DatabaseDefinitions.jogo.Columns.PRODUTORA,
                DatabaseDefinitions.jogo.Columns.NOTA,
                DatabaseDefinitions.jogo.Columns.CONSOLE,
                DatabaseDefinitions.jogo.Columns.ZERADO,
                DatabaseDefinitions.jogo.Columns.IMAGEM)


        //Cursor resultado de uma consulta Cursor vira uma tabela
        val cursor = db.query(DatabaseDefinitions.jogo.TABLE_NAME,projection,
                null, null, null, null, ordenacao)

        val lista_jogos = ArrayList<Jogo>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                     var jogo = Jogo(
                        id = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.ID)),
                        nomeJogo = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.TITULO)),
                        produtora = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.PRODUTORA)),
                        notaJogo = cursor.getFloat(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.NOTA)),
                        console = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.CONSOLE)),
                        zerado = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.ZERADO)) == 1,
                        imagem= BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.IMAGEM)),0,(cursor.getBlob(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.IMAGEM))).size
                        )
                    )
                lista_jogos.add(jogo)
            }
        }
        return lista_jogos
    }




    //Busca um jogo especifico
    fun get_um_jogo(id: Int): Jogo {

        val db = dbHelper.readableDatabase
        val projection = arrayOf(
                DatabaseDefinitions.jogo.Columns.ID,
                DatabaseDefinitions.jogo.Columns.TITULO,
                DatabaseDefinitions.jogo.Columns.PRODUTORA,
                DatabaseDefinitions.jogo.Columns.NOTA,
                DatabaseDefinitions.jogo.Columns.CONSOLE,
                DatabaseDefinitions.jogo.Columns.ZERADO,
                DatabaseDefinitions.jogo.Columns.IMAGEM
        )
        val selection = "${DatabaseDefinitions.jogo.Columns.ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
                DatabaseDefinitions.jogo.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null)

        val jogo = Jogo()
        if (cursor != null) {
            cursor.moveToNext()
            jogo.id = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.ID))
            jogo.nomeJogo = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.TITULO))
            jogo.produtora = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.PRODUTORA))
            jogo.notaJogo = cursor.getFloat(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.NOTA))
            jogo.console = cursor.getString(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.CONSOLE))
            jogo.zerado = cursor.getInt(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.ZERADO)) == 1

            //Tratamento imagem
            val imagem= cursor.getBlob(cursor.getColumnIndex(DatabaseDefinitions.jogo.Columns.IMAGEM))
            if (imagem != null) {
                //Transforma de bytes pra bitmap
                val bitmap = BitmapFactory.decodeByteArray(imagem,0,imagem.size)
                jogo.imagem =  bitmap
            }
        }
        return jogo
    }



    //Exclue um registro
    fun delete(id: Int): Int {
        //Colocar o banco de dados em modo escrita
        val db = dbHelper.writableDatabase
        //Definir a clausula WHERE
        val selection = "${DatabaseDefinitions.jogo.Columns.ID}= ?"
        //Argumentos da instrução Delete
        val selectionargs = arrayOf(id.toString())
        val qtde_linhas_deletadas = db.delete(DatabaseDefinitions.jogo.TABLE_NAME, selection, selectionargs)
        return qtde_linhas_deletadas
    }


    fun update(jogo:Jogo) : Int{
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DatabaseDefinitions.jogo.Columns.TITULO, jogo.nomeJogo)
            put(DatabaseDefinitions.jogo.Columns.PRODUTORA, jogo.produtora)
            put(DatabaseDefinitions.jogo.Columns.NOTA, jogo.notaJogo)
            put(DatabaseDefinitions.jogo.Columns.CONSOLE, jogo.console)
            put(DatabaseDefinitions.jogo.Columns.ZERADO, jogo.zerado)
        }
        val selection = "${DatabaseDefinitions.jogo.Columns.ID} = ?"
        val selectionArgs = arrayOf(jogo.id.toString())
        val count = db.update(DatabaseDefinitions.jogo.TABLE_NAME, valores, selection, selectionArgs)

        return count
    }

}







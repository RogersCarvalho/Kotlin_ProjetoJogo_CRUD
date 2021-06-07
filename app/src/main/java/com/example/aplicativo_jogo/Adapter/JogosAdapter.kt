package com.example.aplicativo_jogo.Adapter

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicativo_jogo.Constants.Constants
import com.example.aplicativo_jogo.Datasource.DatabaseDefinitions
import com.example.aplicativo_jogo.MainActivity
import com.example.aplicativo_jogo.Model.Jogo
import com.example.aplicativo_jogo.R
import com.example.aplicativo_jogo.Repository.JogoRepository
import com.example.aplicativo_jogo.UsuarioInterface.CadastroJogoActivity
import kotlinx.android.synthetic.main.activity_cadastro_jogo.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_list_jogos.view.*


class JogosAdapter(var listaJogos: ArrayList<Jogo>) : RecyclerView.Adapter <JogosAdapter.JogoViewHolder>() {


    //---------------------Métodos herdados ao extender a classe Adapter-------------------------//

    //Este método cria cada item e infla preparando para populá-lo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_jogos, parent, false)
        return JogoViewHolder(itemView) }

    //Primeiro método executado que verifica o tamanho da lista
    //dessa forma o Adapter sabe quantas itens serão impressos
    override fun getItemCount(): Int {
        return listaJogos.size }


    //Responsável por pegar cada item e preencher na tela
    //Popula o item na recyclerview
    //Lê o item no array e popula na recyclerview
    override fun onBindViewHolder(holder: JogoViewHolder, position: Int) {
        val jogo = listaJogos[position]
        holder.bind(jogo,position) }
    //---------------------------------------------------------------------------------------------------//

    //A classe ViewHolder controla os itens da recyclerview
    //Inner indica que JogoViewHolder é uma classe interna a classe JogosAdapter
    inner class JogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(jogo: Jogo, position:Int) {

            itemView.text_nome_jogo.text = jogo.nomeJogo
            itemView.text_console.text = jogo.console
            itemView.notajogo.rating = jogo.notaJogo

            itemView.imagem.setImageBitmap(jogo.imagem)




            //Botão detalhe
            itemView.button_detalhes.setOnClickListener() {
                val intent = Intent(itemView.context, CadastroJogoActivity::class.java)
                intent.putExtra("operacao", Constants.OPERACAO_CONSULTAR)
                intent.putExtra("id", jogo.id)
                itemView.context.startActivities(arrayOf(intent))
            }

            //Excluir
            itemView.setOnClickListener(){
                AlertDialog.Builder(itemView.context)
                        .setTitle("Exclusão")
                        .setMessage("Confima a exclusão do jogo ${jogo.nomeJogo}")
                        .setPositiveButton("SIM") {dialog, which ->
                            val rep = JogoRepository(itemView.context)
                            rep.delete(jogo.id)
                            //Informa a posição do item para exclusão
                            listaJogos.removeAt(position)
                            //Atualiza a recyclerview
                            notifyDataSetChanged()
                        }
                        .setNegativeButton("Não"){dialog, which -> }.show()
            }



        }
    }


}
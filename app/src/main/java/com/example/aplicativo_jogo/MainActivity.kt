package com.example.aplicativo_jogo

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicativo_jogo.Adapter.JogosAdapter
import com.example.aplicativo_jogo.Constants.Constants
import com.example.aplicativo_jogo.Datasource.Datasource
import com.example.aplicativo_jogo.Repository.JogoRepository
import com.example.aplicativo_jogo.UsuarioInterface.CadastroJogoActivity
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.toobar.*

class MainActivity : AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamente usa-se no oncreate depois no onresume
        iniciarRecyclerView()

        inserttoolbar()

        //Cria o botão flutuante
        buttonCadastrarJogo.setOnClickListener(this)
    }







    //Fica no onresume pra atualizar a recyclerview quando fechar a tela de cadastro
    override fun onResume() {
        super.onResume()
        iniciarRecyclerView()
    }



    private fun inserttoolbar() {
        setSupportActionBar(toobar)
        supportActionBar!!.title = "MY GAME"
    }


    //Ao clicar em qualquer view chama a fun onclick
    override fun onClick(v: View) {

        //Botão flutuante
        if (v.id == R.id.buttonCadastrarJogo) {
            val exibetela= Intent(this, CadastroJogoActivity::class.java)
            exibetela.putExtra("operacao", Constants.OPERACAO_NOVO_CADASTRO)
            startActivity(exibetela)
        }

    }




   private fun iniciarRecyclerView() {


       //Carrega a recyclerview indicando que tipo de lista deseja, no caso será lista (LinearLayoutManager)
       recyclerViewJogos.layoutManager = LinearLayoutManager(this)

       val rep = JogoRepository(this)
       recyclerViewJogos.adapter = JogosAdapter(rep.getJogos())

   }




}
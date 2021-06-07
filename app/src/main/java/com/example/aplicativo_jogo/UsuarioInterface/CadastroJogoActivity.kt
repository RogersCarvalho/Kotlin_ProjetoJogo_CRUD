package com.example.aplicativo_jogo.UsuarioInterface

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicativo_jogo.Adapter.JogosAdapter
import com.example.aplicativo_jogo.Constants.Constants
import com.example.aplicativo_jogo.Datasource.DatabaseDefinitions
import com.example.aplicativo_jogo.Model.Jogo
import com.example.aplicativo_jogo.R
import com.example.aplicativo_jogo.Repository.JogoRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_cadastro_jogo.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_list_jogos.*
import kotlinx.android.synthetic.main.toobar.*

//a classe cadastroJogoActivity herda os métodos da classe appcompatactivity
//O método oncreate  é herdado da classe classe appcompatactivity
class CadastroJogoActivity : AppCompatActivity() , View.OnClickListener{


    //variável global trata console(formato de exibição da caixa)
    private lateinit var adapter: ArrayAdapter<CharSequence>
    //variável global
    private lateinit var operacao: String
    //variavel a nivel de classe
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_jogo)
        //Tratar console
        preencherSpinerConsole()
        inserttoolbar()
        //Pega o tipo de operação inicializada ao criar uma tela
        operacao = intent.getStringExtra("operacao").toString()

        //Se a operação for consulta invoca chama a função
        if ( operacao != Constants.OPERACAO_NOVO_CADASTRO){
            preencherform()
        }

        //Ao clicar no botão permite procurar pelo onclick abaixo
        button_abrirGaleria.setOnClickListener(this)
    }



    //Trata imagem
    override fun onClick(view: View) {
        val id = view.id
        when (id){
            R.id.button_abrirGaleria -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 5000) } }
    }

    //Trata imagem (após retornar com  imagem o android busca esse o método onActivityReult)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Verifica o retorno quando seleciona uma imagem (-1) e quando não traz uma imagem (0)
        // Toast.makeText(this, "RequestCode=$requestCode | resulCode=$resultCode", Toast.LENGTH_LONG).show()
        if (data != null) {
            //abre a imagem recebida
            val inputStream = contentResolver.openInputStream(data.data!!)
            //converte a imagem recebida de bytes para bitmap
            bitmap = BitmapFactory.decodeStream(inputStream)
            //centraliza a imagem
            imageViewFotoDoJogo.scaleType = ImageView.ScaleType.CENTER_CROP
            imageViewFotoDoJogo.setImageBitmap(bitmap)

        }
    }


    //Altera o tipo de combobox para selecionar os consoles para verificar como é sem comente
    private fun preencherSpinerConsole() {
        adapter = ArrayAdapter.createFromResource(this, R.array.console,android.R.layout.simple_spinner_item)
        spinerconsole.adapter = adapter
    }


    private fun preencherform() {
        var jogo = Jogo()

        //Pega o id passado
        var id = intent.getIntExtra("id",0)

        val rep = JogoRepository(this)
        jogo = rep.get_um_jogo(id)


        //Preencher as informações dos campos
        edittext_NomeDoJogo.setText(jogo.nomeJogo)
        edit_text_ProdutoraDoJogo.setText(jogo.produtora)
        checkbox_zerado.isChecked = jogo.zerado
        ratinBarNotaJogo.rating = jogo.notaJogo

         if (jogo.imagem != null){
          imageViewFotoDoJogo.setImageBitmap(jogo.imagem)
         } else {imageViewFotoDoJogo.setImageResource(R.drawable.ic_clear)}

        //Busca a posição de jogo.console (mega drive) e manda selecionar
        val position = adapter.getPosition(jogo.console)
        spinerconsole.setSelection(position)
    }


    //cria a toolbar (barrinha na parte superior) mostrando um titulo
    // criando um seta para voltar para tela anterior
    private fun inserttoolbar() {
        setSupportActionBar(toobar)
        supportActionBar!!.title = intent.getStringExtra("operacao")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    //o método onCreateOptionsMenu também é herdado da classe appcompatactivity
    //cria um menu menu_game criado no arquivo menu_game.xml
    //O método recebe um menu e retorna um true
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game,menu)
        return true
    }

      //Quando clica em algum item no menu chama o método abaixo
      // esse método recebe como paramentro o item do menu que foi clicado
      override fun onOptionsItemSelected(item: MenuItem): Boolean {
          when (item.itemId) {
              R.id.menu_cancelar -> { alert() }
              R.id.menu_salvar -> {
                  if (validarformulario()) {
                      if (operacao == Constants.OPERACAO_NOVO_CADASTRO) {
                          SalvarJogo()
                      }
                      if (operacao == Constants.OPERACAO_CONSULTAR) {
                          atualizarJogo()
                      }
                  }

                  //if (validarformulario() ) {
                    //  SalvarJogo()
                 // }
              }
              else -> { onBackPressed() }
          }
          return true
    }


    private fun atualizarJogo() {

        //Cria um objeto passando valores, incluive o id
        val jogo = Jogo(
                id = intent.getIntExtra("id",0),
                nomeJogo = edittext_NomeDoJogo.text.toString(),
                produtora = edit_text_ProdutoraDoJogo.text.toString(),
                notaJogo = ratinBarNotaJogo.rating,
                console = spinerconsole.selectedItem.toString(),
                zerado = checkbox_zerado.isChecked
        )

        val rep = JogoRepository(this)
        val count = rep.update(jogo)

        //Exibir confirmação de inclusão************************
        if (count > 0 ){
            AlertDialog.Builder(this)
            .setTitle("Menssagem")
            .setMessage("Seu jogo foi gravado com sucesso ?")
            .setIcon(R.drawable.ic_done)
            .setNegativeButton("OK") {_,_ -> onBackPressed()}
            .show()
        }
    }


    private fun SalvarJogo() {

        //Criar objeto jogo
        val jogo = Jogo(
                nomeJogo = edittext_NomeDoJogo.text.toString(),
                produtora = edit_text_ProdutoraDoJogo.text.toString(),
                notaJogo = ratinBarNotaJogo.rating,
                console = spinerconsole.selectedItem.toString(),
                zerado = checkbox_zerado.isChecked,
                imagem = bitmap
        )

        //Criar uma instancia da classe Repository
        //chama a função save passando os dados (objeto)
        //O repositorio é responsável por manipulação no BD
        val rep = JogoRepository(this)
        val id = rep.save(jogo)
        println("Registro criado*************:$id")

        //Exibir confirmação de inclusão************************
        if (id > 0 ){
            val msg = AlertDialog.Builder(this)
            msg.setTitle("Menssagem")
            msg.setMessage("Seu jogo foi gravado com sucesso. Deseja continuar ?")
            msg.setIcon(R.drawable.ic_done)
            msg.setPositiveButton("Sim") {_,_ -> limpaform() }
            msg.setNegativeButton("Não") {_,_ -> onBackPressed()}
            msg.show()
        }
    }


    private fun limpaform() {
        edittext_NomeDoJogo.setText("")
        edit_text_ProdutoraDoJogo.setText("")
        checkbox_zerado.isChecked = false
        edittext_NomeDoJogo.requestFocus()

    }


    //Valida formulario ao cadastrar e alterar jogo
    private fun validarformulario(): Boolean {

        var valida = true
        if (edittext_NomeDoJogo.length() < 3 ){
            til_NomeDoJogo.isErrorEnabled = true
            til_NomeDoJogo.error = "campo obrigatório"
            valida = false
        }
        else{
            til_NomeDoJogo.isErrorEnabled = false
            til_NomeDoJogo.error = null
        }
        if (edit_text_ProdutoraDoJogo.length() < 3 ){
            til_ProdutoraJogo.isErrorEnabled = true
            til_ProdutoraJogo.error = "campo obrigatório"
            valida = false
        }
        else{
            til_ProdutoraJogo.isErrorEnabled = false
            til_ProdutoraJogo.error = null
        }



        return valida
    }




    private fun alert() {
        var msg = AlertDialog.Builder(this)
         msg.setTitle("Cancelar cadastro")
         msg.setMessage("Tem certeza que deseja cancelar o cadastro ?")
         msg.setPositiveButton("Sim") { _ , _  -> onBackPressed()}
         msg.setNegativeButton("Não") { _ , _  -> edittext_NomeDoJogo.requestFocus()}
         msg.show()
    }




}









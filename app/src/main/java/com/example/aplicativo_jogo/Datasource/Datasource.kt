package com.example.aplicativo_jogo.Datasource

import com.example.aplicativo_jogo.Model.Jogo


class Datasource {

companion object{

    fun getJogos(): ArrayList<Jogo>{

        var jogos = ArrayList<Jogo>()
        jogos.add(Jogo(100,"Resident Evil","",1.0f,"xxx"))
        jogos.add(Jogo(300,"Batalha naval","",4.0f,"XBOX"))


        return jogos
    }


}

}
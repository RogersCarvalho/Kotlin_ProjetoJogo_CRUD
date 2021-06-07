package com.example.aplicativo_jogo.Datasource

class DatabaseDefinitions {

    object jogo{

        const val TABLE_NAME = "tbl_jogo"

        object Columns {
            const val  ID = "id"
            const val  TITULO = "nomeJogo"
            const val  PRODUTORA = "produtora"
            const val  NOTA = "notaJogo"
            const val  CONSOLE = "console"
            const val  ZERADO = "zerado"
            const val  IMAGEM = "imagem"

        }
    }
}


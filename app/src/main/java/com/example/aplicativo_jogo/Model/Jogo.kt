package com.example.aplicativo_jogo.Model

import android.graphics.Bitmap

//O Bitmap pode ser nulo por isso o sinal de "?"
//ja recebe nulo
data class Jogo (var id: Int = 0,
                 var nomeJogo: String= "",
                 var produtora: String = "",
                 var notaJogo: Float = 0.0f,
                 var console: String = "",
                 var zerado: Boolean = false,
                 var imagem: Bitmap? = null

)

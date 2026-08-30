package br.com.projeto.elo.data.local

import androidx.room.TypeConverter
import br.com.projeto.elo.dominio.modelo.TipoTransacao

class ConversoresRoom {

    // Traduz de Enum para Texto (para salvar no banco)
    @TypeConverter
    fun deTipoTransacao(tipo: TipoTransacao): String {
        return tipo.name
    }

    // Traduz de Texto para Enum (para ler do banco para a tela)
    @TypeConverter
    fun paraTipoTransacao(nome: String): TipoTransacao {
        return TipoTransacao.valueOf(nome)
    }
}
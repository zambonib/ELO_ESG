package br.com.projeto.elo.dominio.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabela_transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: String = "",        // ← Separa dados por usuário (item 2)
    val descricao: String,
    val valor: Double,
    val categoria: String,
    val tipo: TipoTransacao,
    val data: Long = System.currentTimeMillis() // Timestamp para filtrar por mês (item 4)
)

enum class TipoTransacao {
    RECEITA,
    DESPESA
}
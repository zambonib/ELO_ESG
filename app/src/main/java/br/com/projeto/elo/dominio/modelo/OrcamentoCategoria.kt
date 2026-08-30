package br.com.projeto.elo.dominio.modelo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Meta de gasto mensal que o usuário define para uma categoria (educação financeira).
// Índice único por (usuarioId, categoria) para permitir "definir/atualizar" o limite.
@Entity(
    tableName = "tabela_orcamentos",
    indices = [Index(value = ["usuarioId", "categoria"], unique = true)]
)
data class OrcamentoCategoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: String = "",
    val categoria: String,
    val limite: Double
)

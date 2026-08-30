package br.com.projeto.elo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.projeto.elo.dominio.modelo.OrcamentoCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface OrcamentoDao {

    // READ — todos os orçamentos do usuário (reativo)
    @Query("SELECT * FROM tabela_orcamentos WHERE usuarioId = :uid")
    fun obterOrcamentos(uid: String): Flow<List<OrcamentoCategoria>>

    // CREATE/UPDATE — o índice único (usuarioId, categoria) faz o REPLACE atualizar o limite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun definirOrcamento(orcamento: OrcamentoCategoria)

    // DELETE — remove o limite de uma categoria
    @Query("DELETE FROM tabela_orcamentos WHERE usuarioId = :uid AND categoria = :categoria")
    suspend fun removerOrcamento(uid: String, categoria: String)
}

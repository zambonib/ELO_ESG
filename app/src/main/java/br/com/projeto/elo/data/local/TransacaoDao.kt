package br.com.projeto.elo.data.local

import androidx.room.*
import br.com.projeto.elo.dominio.modelo.Transacao
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    // CREATE
    @Insert
    suspend fun inserirTransacao(transacao: Transacao)

    // READ — todas do usuário, ordenadas pela mais recente
    @Query("""
        SELECT * FROM tabela_transacoes 
        WHERE usuarioId = :uid 
        ORDER BY data DESC
    """)
    fun obterTodasTransacoes(uid: String): Flow<List<Transacao>>

    // READ — busca por texto (nome, categoria) OU por valor numérico
    @Query("""
        SELECT * FROM tabela_transacoes 
        WHERE usuarioId = :uid 
        AND (
            descricao LIKE '%' || :busca || '%' 
            OR categoria LIKE '%' || :busca || '%'
            OR CAST(valor AS TEXT) LIKE '%' || :busca || '%'
        )
        ORDER BY data DESC
    """)
    fun buscarTransacoes(uid: String, busca: String): Flow<List<Transacao>>

    // READ — soma de receitas do mês atual do usuário
    @Query("""
        SELECT COALESCE(SUM(valor), 0) FROM tabela_transacoes 
        WHERE usuarioId = :uid 
        AND tipo = 'RECEITA'
        AND data >= :inicioDoMes
    """)
    fun obterReceitaDoMes(uid: String, inicioDoMes: Long): Flow<Double>

    // READ — soma de despesas do mês atual do usuário
    @Query("""
        SELECT COALESCE(SUM(valor), 0) FROM tabela_transacoes 
        WHERE usuarioId = :uid 
        AND tipo = 'DESPESA'
        AND data >= :inicioDoMes
    """)
    fun obterDespesaDoMes(uid: String, inicioDoMes: Long): Flow<Double>

    // UPDATE
    @Update
    suspend fun atualizarTransacao(transacao: Transacao)

    // DELETE
    @Delete
    suspend fun excluirTransacao(transacao: Transacao)
}
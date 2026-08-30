package br.com.projeto.elo.data.local

import androidx.room.*
<<<<<<< HEAD
import br.com.projeto.elo.dominio.modelo.CategoriaTotal
=======
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
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

<<<<<<< HEAD
    // READ — soma de receitas dentro de um período arbitrário (usado na tela de Finanças)
    @Query("""
        SELECT COALESCE(SUM(valor), 0) FROM tabela_transacoes
        WHERE usuarioId = :uid
        AND tipo = 'RECEITA'
        AND data BETWEEN :inicio AND :fim
    """)
    fun obterReceitaPorPeriodo(uid: String, inicio: Long, fim: Long): Flow<Double>

    // READ — soma de despesas dentro de um período arbitrário
    @Query("""
        SELECT COALESCE(SUM(valor), 0) FROM tabela_transacoes
        WHERE usuarioId = :uid
        AND tipo = 'DESPESA'
        AND data BETWEEN :inicio AND :fim
    """)
    fun obterDespesaPorPeriodo(uid: String, inicio: Long, fim: Long): Flow<Double>

    // READ — total gasto agrupado por categoria (apenas DESPESAS) no período
    @Query("""
        SELECT categoria, COALESCE(SUM(valor), 0) AS total FROM tabela_transacoes
        WHERE usuarioId = :uid
        AND tipo = 'DESPESA'
        AND data BETWEEN :inicio AND :fim
        GROUP BY categoria
        ORDER BY total DESC
    """)
    fun obterGastosPorCategoria(uid: String, inicio: Long, fim: Long): Flow<List<CategoriaTotal>>

=======
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
    // UPDATE
    @Update
    suspend fun atualizarTransacao(transacao: Transacao)

    // DELETE
    @Delete
    suspend fun excluirTransacao(transacao: Transacao)
}
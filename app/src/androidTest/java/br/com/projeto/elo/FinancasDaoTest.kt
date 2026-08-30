package br.com.projeto.elo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.projeto.elo.data.local.AppDatabase
import br.com.projeto.elo.data.local.OrcamentoDao
import br.com.projeto.elo.data.local.TransacaoDao
import br.com.projeto.elo.dominio.modelo.OrcamentoCategoria
import br.com.projeto.elo.dominio.modelo.TipoTransacao
import br.com.projeto.elo.dominio.modelo.Transacao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FinancasDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var transacaoDao: TransacaoDao
    private lateinit var orcamentoDao: OrcamentoDao

    private val inicio = 0L
    private val fim = Long.MAX_VALUE

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        transacaoDao = db.transacaoDao()
        orcamentoDao = db.orcamentoDao()
    }

    @After
    fun teardown() { db.close() }

    // TC-F07: Gastos são agrupados e somados por categoria (apenas despesas), maior primeiro
    @Test
    fun gastos_por_categoria_agrupa_e_ordena() = runTest {
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "u1", descricao = "Mercado", valor = 100.0,
                categoria = "Alimentação", tipo = TipoTransacao.DESPESA, data = 10L)
        )
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "u1", descricao = "Feira", valor = 50.0,
                categoria = "Alimentação", tipo = TipoTransacao.DESPESA, data = 20L)
        )
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "u1", descricao = "Ônibus", valor = 30.0,
                categoria = "Transporte", tipo = TipoTransacao.DESPESA, data = 30L)
        )
        // Receita não deve entrar no agrupamento de gastos
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "u1", descricao = "Salário", valor = 2000.0,
                categoria = "Renda", tipo = TipoTransacao.RECEITA, data = 40L)
        )

        val gastos = transacaoDao.obterGastosPorCategoria("u1", inicio, fim).first()
        assertEquals(2, gastos.size)
        assertEquals("Alimentação", gastos[0].categoria)
        assertEquals(150.0, gastos[0].total, 0.001)
        assertEquals("Transporte", gastos[1].categoria)
        assertEquals(30.0, gastos[1].total, 0.001)
    }

    // TC-F08: Somatórios por período isolam por usuário
    @Test
    fun somatorios_por_periodo_isolam_por_usuario() = runTest {
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "maria", descricao = "Salário", valor = 1000.0,
                categoria = "Renda", tipo = TipoTransacao.RECEITA, data = 10L)
        )
        transacaoDao.inserirTransacao(
            Transacao(usuarioId = "marcelo", descricao = "Aluguel", valor = 800.0,
                categoria = "Moradia", tipo = TipoTransacao.DESPESA, data = 10L)
        )

        val receitaMaria = transacaoDao.obterReceitaPorPeriodo("maria", inicio, fim).first()
        val despesaMaria = transacaoDao.obterDespesaPorPeriodo("maria", inicio, fim).first()

        assertEquals(1000.0, receitaMaria, 0.001)
        assertEquals(0.0, despesaMaria, 0.001)
    }

    // TC-F09: Definir o mesmo par (usuário, categoria) atualiza o limite em vez de duplicar
    @Test
    fun definir_orcamento_faz_upsert_da_categoria() = runTest {
        orcamentoDao.definirOrcamento(OrcamentoCategoria(usuarioId = "u1", categoria = "Lazer", limite = 100.0))
        orcamentoDao.definirOrcamento(OrcamentoCategoria(usuarioId = "u1", categoria = "Lazer", limite = 250.0))

        val orcamentos = orcamentoDao.obterOrcamentos("u1").first()
        assertEquals(1, orcamentos.size)
        assertEquals(250.0, orcamentos[0].limite, 0.001)
    }

    // TC-F10: Remover meta apaga o registro do usuário
    @Test
    fun remover_orcamento_apaga_meta() = runTest {
        orcamentoDao.definirOrcamento(OrcamentoCategoria(usuarioId = "u1", categoria = "Transporte", limite = 200.0))
        orcamentoDao.removerOrcamento("u1", "Transporte")

        val orcamentos = orcamentoDao.obterOrcamentos("u1").first()
        assertEquals(0, orcamentos.size)
    }
}

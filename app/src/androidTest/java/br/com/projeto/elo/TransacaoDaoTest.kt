package br.com.projeto.elo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.projeto.elo.data.local.AppDatabase
import br.com.projeto.elo.data.local.TransacaoDao
import br.com.projeto.elo.dominio.modelo.TipoTransacao
import br.com.projeto.elo.dominio.modelo.Transacao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransacaoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TransacaoDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.transacaoDao()
    }

    @After
    fun teardown() { db.close() }

    // TC-001: Inserir e buscar
    @Test
    fun inserir_transacao_e_verificar_na_lista() = runTest {
        val t = Transacao(usuarioId = "uid1", descricao = "Mercado", valor = 50.0,
            categoria = "Alimentação", tipo = TipoTransacao.DESPESA)
        dao.inserirTransacao(t)
        val lista = dao.obterTodasTransacoes("uid1").first()
        assertEquals(1, lista.size)
        assertEquals("Mercado", lista[0].descricao)
    }

    // TC-002: Isolamento por usuário
    @Test
    fun transacoes_de_usuarios_diferentes_ficam_isoladas() = runTest {
        dao.inserirTransacao(Transacao(usuarioId = "maria", descricao = "Salário",
            valor = 2000.0, categoria = "Renda", tipo = TipoTransacao.RECEITA))
        dao.inserirTransacao(Transacao(usuarioId = "marcelo", descricao = "Aluguel",
            valor = 800.0, categoria = "Moradia", tipo = TipoTransacao.DESPESA))

        val listaMaria = dao.obterTodasTransacoes("maria").first()
        val listaMarcelo = dao.obterTodasTransacoes("marcelo").first()

        assertEquals(1, listaMaria.size)
        assertEquals(1, listaMarcelo.size)
        assertEquals("Salário", listaMaria[0].descricao)
        assertEquals("Aluguel", listaMarcelo[0].descricao)
    }

    // TC-004: Busca por texto e valor
    @Test
    fun busca_por_valor_retorna_resultado_correto() = runTest {
        dao.inserirTransacao(Transacao(usuarioId = "uid1", descricao = "Padaria",
            valor = 12.5, categoria = "Alimentação", tipo = TipoTransacao.DESPESA))
        dao.inserirTransacao(Transacao(usuarioId = "uid1", descricao = "Salário",
            valor = 2000.0, categoria = "Renda", tipo = TipoTransacao.RECEITA))

        val resultado = dao.buscarTransacoes("uid1", "12").first()
        assertEquals(1, resultado.size)
        assertEquals("Padaria", resultado[0].descricao)
    }

    // TC-005: Update persiste mudanças
    @Test
    fun atualizar_transacao_persiste_as_mudancas() = runTest {
        val t = Transacao(usuarioId = "uid1", descricao = "Café",
            valor = 5.0, categoria = "Alimentação", tipo = TipoTransacao.DESPESA)
        dao.inserirTransacao(t)
        val salvo = dao.obterTodasTransacoes("uid1").first()[0]
        dao.atualizarTransacao(salvo.copy(descricao = "Café Especial", valor = 8.0))
        val atualizado = dao.obterTodasTransacoes("uid1").first()[0]
        assertEquals("Café Especial", atualizado.descricao)
        assertEquals(8.0, atualizado.valor, 0.001)
    }

    // TC-006: Delete remove o registro
    @Test
    fun excluir_transacao_remove_da_lista() = runTest {
        val t = Transacao(usuarioId = "uid1", descricao = "Teste",
            valor = 10.0, categoria = "Outros", tipo = TipoTransacao.DESPESA)
        dao.inserirTransacao(t)
        val salvo = dao.obterTodasTransacoes("uid1").first()[0]
        dao.excluirTransacao(salvo)
        val lista = dao.obterTodasTransacoes("uid1").first()
        assertTrue(lista.isEmpty())
    }
}
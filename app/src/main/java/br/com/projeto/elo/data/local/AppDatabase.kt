package br.com.projeto.elo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
<<<<<<< HEAD
import br.com.projeto.elo.dominio.modelo.OrcamentoCategoria
=======
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89
import br.com.projeto.elo.dominio.modelo.Transacao

// Migração da versão 1 para 2: adiciona a coluna usuarioId
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE tabela_transacoes ADD COLUMN usuarioId TEXT NOT NULL DEFAULT ''"
        )
    }
}

<<<<<<< HEAD
// Migração da versão 2 para 3: cria a tabela de orçamentos por categoria (tela de Finanças)
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS tabela_orcamentos (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                usuarioId TEXT NOT NULL,
                categoria TEXT NOT NULL,
                limite REAL NOT NULL
            )
            """.trimIndent()
        )
        database.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS index_tabela_orcamentos_usuarioId_categoria
            ON tabela_orcamentos (usuarioId, categoria)
            """.trimIndent()
        )
    }
}

@Database(entities = [Transacao::class, OrcamentoCategoria::class], version = 3, exportSchema = false)
@TypeConverters(ConversoresRoom::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
    abstract fun orcamentoDao(): OrcamentoDao
}
=======
@Database(entities = [Transacao::class], version = 2, exportSchema = false)
@TypeConverters(ConversoresRoom::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
}
>>>>>>> 3df5725dd0882ba487761f3c383b1179530a7e89

package br.com.projeto.elo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.projeto.elo.dominio.modelo.Transacao

// Migração da versão 1 para 2: adiciona a coluna usuarioId
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE tabela_transacoes ADD COLUMN usuarioId TEXT NOT NULL DEFAULT ''"
        )
    }
}

@Database(entities = [Transacao::class], version = 2, exportSchema = false)
@TypeConverters(ConversoresRoom::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
}
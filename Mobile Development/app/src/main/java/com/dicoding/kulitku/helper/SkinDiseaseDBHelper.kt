import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SkinDiseaseDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "skin_disease.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "obat"
        private const val COLUMN_PENYAKIT = "nama_penyakit"
        private const val COLUMN_OBAT = "obat_rekomendasi"

        private const val CREATE_TABLE_SQL =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_PENYAKIT TEXT PRIMARY KEY, " +
                    "$COLUMN_OBAT TEXT)"

        private const val SELECT_MEDICATION_SQL =
            "SELECT $COLUMN_OBAT FROM $TABLE_NAME WHERE $COLUMN_PENYAKIT = ?"
    }

    private val mContext: Context = context

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SQL)
        // Impor data dari file SQL di assets ke database SQLite
        importDataFromSQLFile(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun importDataFromSQLFile(db: SQLiteDatabase) {
        try {
            val inputStream = mContext.assets.open("rekomendasi_obat.sql")
            val bufferedReader = inputStream.bufferedReader()
            val statements = bufferedReader.use { it.readText() }.split(";")

            for (statement in statements) {
                db.execSQL(statement)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMedicationForDisease(diseaseName: String): String {
        val db = readableDatabase
        var medication = ""

        val cursor = db.rawQuery(SELECT_MEDICATION_SQL, arrayOf(diseaseName))
        if (cursor.moveToFirst()) {
            medication = cursor.getString(cursor.getColumnIndex(COLUMN_OBAT))
        }
        cursor.close()
        db.close()

        return medication
    }
}

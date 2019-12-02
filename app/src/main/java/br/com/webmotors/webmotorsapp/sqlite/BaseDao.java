package br.com.webmotors.webmotorsapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDao extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "webmotors";
    private static final int VERSAO_BD = 1;

    /**
     * Cria/abre banco de dados
     * @param context nome do contexto
     */
    BaseDao(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BD);
    }

    /**
     * Cria tabelas do banco de dados
     *
     * @param bd banco de dados
     */
    @Override
    public void onCreate(SQLiteDatabase bd) {
        bd.execSQL("CREATE TABLE IF NOT EXISTS Favoritos (ID INTEGER PRIMARY KEY AUTOINCREMENT, idveiculo INTEGER);");
    }

    /**
     * Atualiza o banco de dados se houver alteração na revisão
     *
     * @param bd nome do banco de dados
     * @param oldVersion número da versão antiga
     * @param newVersion número da versão nova
     */
    @Override
    public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion) {
        bd.execSQL("DROP TABLE IF EXISTS Favoritos");
        onCreate(bd);
    }
}
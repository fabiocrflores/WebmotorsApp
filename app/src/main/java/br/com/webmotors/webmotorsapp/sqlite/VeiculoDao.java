package br.com.webmotors.webmotorsapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VeiculoDao {

    private BaseDao auxBd;
    private SQLiteDatabase bd;

    /**
     * Passa o contexto
     *
     * @param context contexto
     */
    public VeiculoDao(Context context){
        auxBd = new BaseDao(context);
    }

    /**
     * Insere veículo favorito no banco SQLite
     *
     * @param idVeiculo id do veículo
     */
    public void inserirFavorito(int idVeiculo) {
        bd = auxBd.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idveiculo", idVeiculo);
        bd.insert("Favoritos", null, cv);
        bd.close();
    }

    /**
     * Remove veículo favorito do banco SQLite
     *
     * @param idVeiculo id do veículo selecionado
     */
    public void removerFavorito(int idVeiculo) {
        bd = auxBd.getWritableDatabase();
        bd.delete("Favoritos", "idveiculo = '" + idVeiculo + "'", null);
        bd.close();
    }

    /**
     * Verifica se já existe o veículo cadastrado no banco SQLite
     *
     * @param idVeiculo id do veículo
     * @return se não existe cadastro
     */
    public boolean verificarFavorito(int idVeiculo){
        boolean favorito = false;
        bd = auxBd.getReadableDatabase();
        Cursor cursor = bd.rawQuery("SELECT id FROM Favoritos WHERE idveiculo = " + idVeiculo, null);
        if (cursor != null) {
            if (cursor.getCount() > 0){
                favorito = true;
            }
            cursor.close();
        }
        bd.close();
        return favorito;
    }
}

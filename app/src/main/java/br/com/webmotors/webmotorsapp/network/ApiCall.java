package br.com.webmotors.webmotorsapp.network;

import java.util.List;

import br.com.webmotors.webmotorsapp.model.Veiculo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface responsável para fazer a chamada com a API
 */
public interface ApiCall {

    @GET("Vehicles")
    Call<List<Veiculo>> listarVeiculo(@Query("Page") int pagina);

}

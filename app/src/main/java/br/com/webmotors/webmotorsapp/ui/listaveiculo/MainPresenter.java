package br.com.webmotors.webmotorsapp.ui.listaveiculo;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import br.com.webmotors.webmotorsapp.R;
import br.com.webmotors.webmotorsapp.model.Veiculo;
import br.com.webmotors.webmotorsapp.network.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter extends RetrofitConfig implements MainContrato.Presenter {

    private MainContrato.View view;
    private VeiculoAdapter veiculoAdapter;
    private final int N_REGISTRO_PAGINA = 10;
    private int pagina;
    private boolean carregando = true, paginaFinal;

    MainPresenter(MainContrato.View view, VeiculoAdapter veiculoAdapter){
        this.view = view;
        this.veiculoAdapter = veiculoAdapter;
    }

    /**
     * Faz a chamada para carregar os veículos
     *
     * @param _pagina número da página a ser carregada
     */
    @Override
    public void buscarVeiculo(int _pagina) {
        pagina = _pagina;

        Call<List<Veiculo>> call = apiCall().listarVeiculo(_pagina);
        //noinspection NullableProblems
        call.enqueue(new Callback<List<Veiculo>>() {
            @Override
            public void onResponse(Call<List<Veiculo>> call, Response<List<Veiculo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().size() < 10){
                            paginaFinal = true;
                            veiculoAdapter.setPaginaFinal();
                        }
                        view.carregarListaVeiculo(response.body());
                    }else {
                        paginaFinal = true;
                        veiculoAdapter.setPaginaFinal();
                    }
                } else {
                    setErro();
                }
                carregando = false;
            }

            @Override
            public void onFailure(Call<List<Veiculo>> call, Throwable t) {
                setErro();
            }
        });
    }

    /**
     * Faz a chamada para carregar novos veículos quando atingir o final da lista
     *
     * @param linearLayoutManager linearlayoutmanager
     */
    @Override
    public void scrolledRvVeiculo(LinearLayoutManager linearLayoutManager) {
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!carregando && !paginaFinal) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= N_REGISTRO_PAGINA) {

                carregando = true;
                pagina ++;
                buscarVeiculo(pagina);
            }
        }
    }

    /**
     * Seta os dados do erro
     */
    private void setErro(){
        view.mostrarMensagemErro(R.string.erro_ws_sem_conexao, carregando, pagina);
        if (carregando){
            pagina = pagina - 1;
        }
    }
}

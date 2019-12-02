package br.com.webmotors.webmotorsapp.ui.listaveiculo;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import br.com.webmotors.webmotorsapp.model.Veiculo;

public interface MainContrato {

    interface View{
        void inicializar();
        void setRecyclerViewVeiculo();
        void carregarListaVeiculo(List<Veiculo> _listaVeiculo);
        void abrirDetalhesVeiculo(Veiculo veiculo);
        void mostrarMensagemErro(int mensagem, boolean carregando, int pagina);
    }

    interface Presenter{
        void buscarVeiculo(int _pagina);
        void scrolledRvVeiculo(LinearLayoutManager linearLayoutManager);
    }
}

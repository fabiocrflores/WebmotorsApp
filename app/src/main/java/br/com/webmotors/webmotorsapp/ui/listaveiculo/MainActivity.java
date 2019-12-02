package br.com.webmotors.webmotorsapp.ui.listaveiculo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.webmotors.webmotorsapp.R;
import br.com.webmotors.webmotorsapp.model.Veiculo;
import br.com.webmotors.webmotorsapp.sqlite.VeiculoDao;
import br.com.webmotors.webmotorsapp.ui.detalhesveiculo.DetalhesVeiculoActivity;
import br.com.webmotors.webmotorsapp.util.FormataCampoUtil;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContrato.View {

    private ConstraintLayout constraintLayout;
    private RecyclerView rvVeiculo;
    private ProgressBar pbVeiculo;
    private MainContrato.Presenter presenter;
    private List<Veiculo> listaVeiculo = new ArrayList<>();
    private VeiculoAdapter veiculoAdapter;
    private LinearLayoutManager linearLayoutManager;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        inicializar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Fecha conexão com o banco de dados SQLite
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.close();
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Instância a classe presenter
     * Altera o título da toolbar
     * Chama método para setar os parâmetros da recyclerview
     * Chama método para setar os dados do spinner
     */
    @Override
    public void inicializar() {
        constraintLayout = findViewById(R.id.constraintLayout);
        rvVeiculo = findViewById(R.id.rvVeiculo);
        pbVeiculo = findViewById(R.id.pbVeiculo);
        pbVeiculo.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        ButterKnife.bind(this);
        bd = openOrCreateDatabase("WebMotorsApp.db", Context.MODE_PRIVATE, null);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.veiculos));
        }
        setRecyclerViewVeiculo();

        presenter = new MainPresenter(this, veiculoAdapter);
        presenter.buscarVeiculo(1);
    }

    /**
     * Seta parâmetros da recyclerview
     */
    @Override
    public void setRecyclerViewVeiculo() {
        linearLayoutManager = new LinearLayoutManager(this);
        rvVeiculo.setLayoutManager(linearLayoutManager);
        rvVeiculo.setHasFixedSize(true);
        veiculoAdapter = new VeiculoAdapter(listaVeiculo, this, new VeiculoDao(this));
        rvVeiculo.setAdapter(veiculoAdapter);
        rvVeiculo.addOnScrollListener(recyclerViewOnScrollListener);
    }

    /**
     * Carrega a lista de veículos
     *
     * @param _listaVeiculo lista de veículos
     */
    @Override
    public void carregarListaVeiculo(List<Veiculo> _listaVeiculo) {
        runOnUiThread(() -> {
            listaVeiculo.addAll(_listaVeiculo);
            veiculoAdapter.notifyDataSetChanged();
            pbVeiculo.setVisibility(View.GONE);
        });
    }

    /**
     * Abre a tela com os detalhes do veículo selecionado na lista
     *
     * @param veiculo objeto com os dados do veículo retornado do webservice
     */
    @Override
    public void abrirDetalhesVeiculo(Veiculo veiculo) {
        Intent intent = new Intent(this, DetalhesVeiculoActivity.class);
        intent.putExtra("imagem", veiculo.getImagem());
        intent.putExtra("preco", FormataCampoUtil.formatarMoeda(Double.parseDouble(veiculo.getPreco().replace(",", "."))));
        intent.putExtra("modelo", getString(R.string.modelo, veiculo.getFabricante(), veiculo.getModelo()));
        intent.putExtra("versao", veiculo.getVersao());
        intent.putExtra("ano", getString(R.string.anofab_anomod, String.valueOf(veiculo.getAnoFabricacao()), String.valueOf(veiculo.getAnoModelo())));
        intent.putExtra("km", getString(R.string.km, FormataCampoUtil.formatarInteiro(veiculo.getKilometragem())));
        intent.putExtra("cor", veiculo.getCor());
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    /**
     * Mostra mensagem de erro para o usuário com possibilidade de tentar carregar a lista novamente
     *
     * @param mensagem   id da mensagem
     * @param carregando informa se o erro ocorreu ao tentar carragar nova página
     * @param pagina     número da página
     */
    @Override
    public void mostrarMensagemErro(int mensagem, boolean carregando, int pagina) {
        runOnUiThread(() -> {
            pbVeiculo.setVisibility(View.GONE);
            if (carregando) {
                listaVeiculo.add(new Veiculo());
                int position = listaVeiculo.size() - 1;
                listaVeiculo.remove(position);
                veiculoAdapter.notifyItemRemoved(position);
            }

            Snackbar snackbar = Snackbar.make(constraintLayout, getString(R.string.erro_ws_sem_conexao), Snackbar.LENGTH_LONG);
            snackbar.setAction(getString(R.string.tentar_novamente), (View v) -> {
                pbVeiculo.setVisibility(View.VISIBLE);
                presenter.buscarVeiculo(pagina);
                snackbar.dismiss();
            });

            View snackView = snackbar.getView();
            snackView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            TextView snackTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            snackTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
            TextView snackActionView = snackView.findViewById(com.google.android.material.R.id.snackbar_action);
            snackActionView.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));

            snackbar.show();
        });
    }

    /**
     * Controle do scroll do recyclerview
     */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            presenter.scrolledRvVeiculo(linearLayoutManager);
        }
    };
}

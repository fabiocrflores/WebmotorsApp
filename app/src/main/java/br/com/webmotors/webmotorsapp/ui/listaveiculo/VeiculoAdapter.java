package br.com.webmotors.webmotorsapp.ui.listaveiculo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import br.com.webmotors.webmotorsapp.R;
import br.com.webmotors.webmotorsapp.model.Veiculo;
import br.com.webmotors.webmotorsapp.sqlite.VeiculoDao;
import br.com.webmotors.webmotorsapp.util.FormataCampoUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VeiculoAdapter extends RecyclerView.Adapter<VeiculoAdapter.VeiculoViewHolder> {

    private List<Veiculo> listaVeiculo;
    private Context context;
    private VeiculoDao veiculoDao;
    private final int VIEW_TYPE_LOADING = 0;
    private boolean paginaFinal;

    VeiculoAdapter(List<Veiculo> listaVeiculo, Context context, VeiculoDao veiculoDao) {
        this.listaVeiculo = listaVeiculo;
        this.context = context;
        this.veiculoDao = veiculoDao;
    }

    @NonNull
    @Override
    public VeiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i == VIEW_TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_carregando, parent, false));
        }else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_veiculo, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VeiculoViewHolder holder, final int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        int VIEW_TYPE_ITEM = 1;
        return position == listaVeiculo.size() - 1 && !paginaFinal ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return listaVeiculo.size();
    }

    void setPaginaFinal(){
        this.paginaFinal = true;
    }

    class ViewHolder extends VeiculoViewHolder {

        private SimpleDraweeView imgFoto;
        private ImageView imgFavorito;
        private TextView txtPreco, txtModelo, txtVersao, txtAno, txtKm, txtCor;

        ViewHolder(View view) {
            super(view);
            imgFoto = view.findViewById(R.id.imgFoto);
            imgFavorito = view.findViewById(R.id.imgFavorito);
            txtPreco = view.findViewById(R.id.txtPreco);
            txtModelo = view.findViewById(R.id.txtModelo);
            txtVersao = view.findViewById(R.id.txtVersao);
            txtAno = view.findViewById(R.id.txtAno);
            txtKm = view.findViewById(R.id.txtKm);
            txtCor = view.findViewById(R.id.txtCor);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Veiculo veiculo = listaVeiculo.get(position);
            imgFoto.setImageURI(veiculo.getImagem());
            if (veiculoDao.verificarFavorito(veiculo.getId())){
                setFavorito(true, veiculo);
            }else{
                setFavorito(false, veiculo);
            }
            txtPreco.setText(FormataCampoUtil.formatarMoeda(Double.parseDouble(veiculo.getPreco().replace(",", "."))));
            txtModelo.setText(context.getString(R.string.modelo, veiculo.getFabricante(), veiculo.getModelo()));
            txtVersao.setText(veiculo.getVersao());
            txtAno.setText(context.getString(R.string.anofab_anomod, String.valueOf(veiculo.getAnoFabricacao()), String.valueOf(veiculo.getAnoModelo())));
            txtKm.setText(context.getString(R.string.km, FormataCampoUtil.formatarInteiro(veiculo.getKilometragem())));
            txtCor.setText(veiculo.getCor());

            imgFavorito.setOnClickListener(v -> {
                if (veiculo.getFavorito()){
                    setFavorito(false, veiculo);
                    veiculoDao.removerFavorito(veiculo.getId());
                }else{
                    setFavorito(true, veiculo);
                    veiculoDao.inserirFavorito(veiculo.getId());
                }
            });

            itemView.setOnClickListener((View v) -> {
                MainContrato.View view = (MainContrato.View) context;
                view.abrirDetalhesVeiculo(veiculo);
            });
        }

        void setFavorito(boolean favorito, Veiculo veiculo){
            veiculo.setFavorito(favorito);
            if (favorito){
                imgFavorito.setImageResource(R.drawable.ic_favorito_on);
            }else {
                imgFavorito.setImageResource(R.drawable.ic_favorito_off);
            }
        }
    }

    public class LoadingViewHolder extends VeiculoViewHolder {

        @BindView(R.id.pbCarregando)
        ProgressBar pbCarregando;

        LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        protected void clear() {

        }
    }

    public abstract class VeiculoViewHolder extends RecyclerView.ViewHolder{

        private VeiculoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void clear();

        public void onBind(int position) {
            clear();
        }

    }
}

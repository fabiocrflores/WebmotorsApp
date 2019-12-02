package br.com.webmotors.webmotorsapp.ui.detalhesveiculo

import android.os.Bundle
import android.transition.Explode
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.com.webmotors.webmotorsapp.R
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_detalhes_veiculo.*

class DetalhesVeiculoActivity : AppCompatActivity(), DetalhesVeiculoContrato.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_detalhes_veiculo)
        inicializar()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Explode()
        }
    }

    /**
     * Volta para a tela anterior ao clicar no botão
     *
     * @param menuItem menu de itens
     * @return verdadeiro
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    /**
     * Referencia os objetos
     * Configura a toolbar
     * Faz chamada do método para carregar os dados do veículo
     */
    override fun inicializar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_voltar)
        }
        carregarDados()
    }

    /**
     * Carrega os dados do veículo
     */
    private fun carregarDados() {
        val bundle = intent.extras!!
        bundle.let {
            imgFoto.setImageURI(it.getString("imagem"))
            txtModelo.text = it.getString("modelo")
            txtPreco.text = it.getString("preco")
            txtVersao.text = it.getString("versao")
            txtAno.text = it.getString("ano")
            txtKm.text = it.getString("km")
            txtCor.text = it.getString("cor")
        }
    }
}

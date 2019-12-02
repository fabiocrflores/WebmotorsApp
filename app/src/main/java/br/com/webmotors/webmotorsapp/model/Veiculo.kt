package br.com.webmotors.webmotorsapp.model

import com.google.gson.annotations.SerializedName

class Veiculo {

    @SerializedName("ID")
    var id: Int = 0
    @SerializedName("Make")
    lateinit var fabricante: String
    @SerializedName("Model")
    lateinit var modelo: String
    @SerializedName("Version")
    lateinit var versao: String
    @SerializedName("Image")
    lateinit var imagem: String
    @SerializedName("KM")
    var kilometragem: Int = 0
    @SerializedName("Price")
    lateinit var preco: String
    @SerializedName("YearModel")
    var anoModelo: Int = 0
    @SerializedName("YearFab")
    var anoFabricacao: Int = 0
    @SerializedName("Color")
    lateinit var cor: String
    var favorito: Boolean = false
}
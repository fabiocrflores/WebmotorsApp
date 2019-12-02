package br.com.webmotors.webmotorsapp.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormataCampoUtil {

    private static Locale ptBr = new Locale("pt", "BR");

    /**
     * @param valor valor para ser formatado
     * @return retorna o valor formatado
     */
    public static String formatarMoeda(double valor){
        NumberFormat nf = NumberFormat.getCurrencyInstance(ptBr);
        return nf.format(valor);
    }

    /**
     * @param valor valor para ser formatado
     * @return retorna o valor formatado
     */
    public static String formatarInteiro(int valor){
        NumberFormat nf = NumberFormat.getIntegerInstance(ptBr);
        return nf.format(valor);
    }
}

package benicio.soluces.nanacatsitter.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import benicio.soluces.nanacatsitter.model.ProdutoModel;

public class CarrinhoSave {

    private static String pref_name = "carrinho_pref";
    private static String key = "produtos";

    public  static void saveProdutos(Context c, List<ProdutoModel> lista){
        SharedPreferences sharedPreferences = c.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        editor.putString(key, gson.toJson(lista));
        editor.apply();
    }

    public static List<ProdutoModel> loadProdutos(Context c){
        SharedPreferences sharedPreferences = c.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, "");

        Gson gson = new Gson();

        Type type = new TypeToken<List<ProdutoModel>>(){}.getType();

        List<ProdutoModel> produtos = gson.fromJson(json, type);

        if ( produtos == null ){
            return  new ArrayList<>();
        }

        return produtos;

    }
}

package com.example.ti_projeto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EstatisticasActivity extends AppCompatActivity {

    private TextView tvDadosReceitas;
    private ImageButton btnHomeEstatisticas;
    private DatabaseHelper dbHelper;
    private Spinner spinnerMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        tvDadosReceitas = findViewById(R.id.tvDadosReceitas);
        btnHomeEstatisticas = findViewById(R.id.btnHomeEstatisticas);
        spinnerMes = findViewById(R.id.spinnerMes);
        dbHelper = new DatabaseHelper(this);

        // Carregar estatísticas para o mês atual no início
        String mesAtual = getMesAtual();
        mostrarEstatisticas(mesAtual);

        // Listener para o spinner do de mês
        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Converten a posição selecionada no spinner para o formato de data (YYYY-MM)
                String mesSelecionado = formatarMesParaAnoMes(position + 1);
                mostrarEstatisticas(mesSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não fazer nada se nada for selecionado
            }
        });


        // Botão para voltar para o menu principal
        btnHomeEstatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volta para a tela anterior
            }
        });
    }

    @SuppressLint("Range")
    private void mostrarEstatisticas(String mes) {
        double totalLitrosLeite = 0;
        int totalAnimaisVendidos = 0;
        double totalSubsidios = 0;
        double totalReceitaLeite = 0;
        double totalReceitaBorrego = 0;
        double totalReceitas = 0;
        int totalSacosRacao = 0;
        int totalFardos = 0;
        double totalOutrasDespesas = 0;
        double totalDespesaRacao = 0;
        double totalDespesas = 0;
        double totalDespesaFardo = 0;


        // Recupera dados das receitas para o mês selecionado
        Cursor cursor = dbHelper.getReceitasPorMes(mes);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                double litrosLeite = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LITROS_LEITE));
                int animaisVendidos = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ANIMAIS_VENDIDOS));
                double subsidios = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_SUBSIDIOS));

                // Adiciona aos totais
                totalLitrosLeite += litrosLeite;
                totalAnimaisVendidos += animaisVendidos;
                totalSubsidios += subsidios;

                // Os preços para a receita atual
                double precoLitroLeite = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_PRECO_LITRO_LEITE));
                double precoBorrego = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_PRECO_BORREGO));

                // Calcula as receitas baseadas nos preços armazenados
                totalReceitaLeite += litrosLeite * precoLitroLeite;
                totalReceitaBorrego += animaisVendidos * precoBorrego;
                totalReceitas = totalReceitaBorrego+totalSubsidios+totalReceitaLeite;

            } while (cursor.moveToNext());

            cursor.close();
        }

        Cursor print = dbHelper.getDespesasPorMes(mes);
        if (print != null && print.getCount() > 0) {
            print.moveToFirst();
            do {
                double sacosracao = print.getDouble(print.getColumnIndex(DatabaseHelper.COL_SACOS_RACAO));
                int fardos = print.getInt(print.getColumnIndex(DatabaseHelper.COL_FARDOS));
                double outrasdespesas = print.getDouble(print.getColumnIndex(DatabaseHelper.COL_OUTRAS_DESPESAS));

                // Adiciona aos totais
                totalSacosRacao += sacosracao;
                totalFardos += fardos;
                totalOutrasDespesas += outrasdespesas;

                // Os preços para a receita atual
                double precoSacoRacao = print.getDouble(print.getColumnIndex(DatabaseHelper.COL_PRECO_RACAO));
                double precoFardo = print.getDouble(print.getColumnIndex(DatabaseHelper.COL_PRECO_FARDO));

                // Calcula as receitas baseadas nos preços armazenados
                totalDespesaRacao += sacosracao * precoSacoRacao;
                totalDespesaFardo += fardos * precoFardo;
                totalDespesas = totalDespesaRacao+totalDespesaFardo+totalOutrasDespesas;


            } while (print.moveToNext());

            print.close();
        }

        double diferenca=totalReceitas-totalDespesas;

        // Mostrar estatísticas
        String estatisticas = "Estatísticas do mês: " + mes + "\n\n" +
                "Total Litros de Leite: " + totalLitrosLeite + "\n" +
                "Total Animais Vendidos: " + totalAnimaisVendidos + "\n" +
                "Total Subsidios: " + totalSubsidios + "€\n" +
                "Total Receita Leite: " + totalReceitaLeite + "€\n" +
                "Total Receita Borrego: " + totalReceitaBorrego + "€\n\n" +
                "RECEITA: "+ totalReceitas+"€\n\n\n" +
                "Total Sacos de Ração: " + totalSacosRacao + "\n" +
                "Total Fardos: " + totalFardos + "\n" +
                "Total Outras Despesas: " + totalOutrasDespesas + "€\n" +
                "Total Despesa Ração: " + totalDespesaRacao + "€\n" +
                "Total Despesa Fardos: " + totalDespesaFardo + "€\n\n" +
                "DESPESA: "+ totalDespesas+"€\n\n\n" +
                "SALDO de " + mes + ": "+ diferenca +"€\n\n";

        tvDadosReceitas.setText(estatisticas);
    }

    // Função para obter o mês atual no formato "YYYY-MM"
    private String getMesAtual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    // Função para formatar a posição do spinner para "YYYY-MM"
    private String formatarMesParaAnoMes(int mes) {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        return String.format(Locale.getDefault(), "%d-%02d", ano, mes);
    }


}

package com.example.ti_projeto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão Receitas
        Button btnReceitas = findViewById(R.id.btnReceitas);
        btnReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceitasActivity.class);
                startActivity(intent);
            }
        });

        // Botão Despesas
        Button btnDespesas = findViewById(R.id.btnDespesas);
        btnDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DespesasActivity.class);
                startActivity(intent);
            }
        });

        // Botão Estatísticas
        Button btnEstatisticas = findViewById(R.id.btnEstatisticas);
        btnEstatisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EstatisticasActivity.class);
                startActivity(intent);
            }
        });

        // Botão Historico
        Button btnHistorico = findViewById(R.id.btnHistorico);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, menuhisActivity.class);
                startActivity(intent);
            }
        });



        // Botão de Configurações (ícone no canto superior direito)
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
                startActivity(intent);
            }
        });
    }
}

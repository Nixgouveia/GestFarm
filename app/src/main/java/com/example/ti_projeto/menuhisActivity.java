package com.example.ti_projeto;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class menuhisActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_historico);

        // Botão Receitas Historico
        Button btnReceitas = findViewById(R.id.btnReceitash);
        btnReceitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuhisActivity.this, HistoricoActivity.class);
                startActivity(intent);
            }
        });

        // Botão Despesas Historico
        Button btnDespesas = findViewById(R.id.btnDespesash);
        btnDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menuhisActivity.this, HisActivity.class);
                startActivity(intent);
            }
        });



    }
    public void goBack(View view) {finish();}
}

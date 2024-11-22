package com.example.ti_projeto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReceitasActivity extends AppCompatActivity {

    private EditText editLitrosLeite, editAnimaisVendidos, editSubsidiosRecebidos;
    private Button btnGuardarLitrosLeite, btnGuardarAnimaisVendidos, btnGuardarSubsidiosRecebidos;
    private ImageButton btnHomeReceitas;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        // Inicializar as variaveis
        editLitrosLeite = findViewById(R.id.editLitrosLeite);
        editAnimaisVendidos = findViewById(R.id.editAnimaisVendidos);
        editSubsidiosRecebidos = findViewById(R.id.editSubsidiosRecebidos);
        btnGuardarLitrosLeite = findViewById(R.id.btnGuardarLitrosLeite);
        btnGuardarAnimaisVendidos = findViewById(R.id.btnGuardarAnimaisVendidos);
        btnGuardarSubsidiosRecebidos = findViewById(R.id.btnGuardarSubsidiosRecebidos);
        btnHomeReceitas = findViewById(R.id.btnHome);

        // Inicializar a base de dados
        dbHelper = new DatabaseHelper(this);

        // Botão para guardar os litros de leite
        btnGuardarLitrosLeite.setOnClickListener(v -> {
            String litrosLeite = editLitrosLeite.getText().toString();
            if (!litrosLeite.isEmpty()) {
                dbHelper.inserirReceita(Double.parseDouble(litrosLeite), 0, 0);
                Toast.makeText(ReceitasActivity.this, "Litros de Leite Guardados!", Toast.LENGTH_SHORT).show();
                editLitrosLeite.setText("");
            }else {
                Toast.makeText(ReceitasActivity.this, "Preencha o campo de litros de leite!", Toast.LENGTH_SHORT).show();
            }
        });

        // Botão para guardar o número de animais vendidos
        btnGuardarAnimaisVendidos.setOnClickListener(v -> {
            String animaisVendidos = editAnimaisVendidos.getText().toString();
            if (!animaisVendidos.isEmpty()) {
                dbHelper.inserirReceita(0, Integer.parseInt(animaisVendidos), 0);
                Toast.makeText(ReceitasActivity.this, "Animais Vendidos Guardados!", Toast.LENGTH_SHORT).show();
                editAnimaisVendidos.setText("");
            }
            else {
                Toast.makeText(ReceitasActivity.this, "Preencha o campo de animais vendidos!", Toast.LENGTH_SHORT).show();
            }
        });

        // Botão para guardar o valor dos subsídios recebidos
        btnGuardarSubsidiosRecebidos.setOnClickListener(v -> {
            String subsidios = editSubsidiosRecebidos.getText().toString();
            if (!subsidios.isEmpty()) {
                dbHelper.inserirReceita(0, 0, Double.parseDouble(subsidios));
                Toast.makeText(ReceitasActivity.this, "Subsídios Guardados!", Toast.LENGTH_SHORT).show();
                editSubsidiosRecebidos.setText("");
            }else {
                Toast.makeText(ReceitasActivity.this, "Preencha o campo de subsídios!", Toast.LENGTH_SHORT).show();
            }
        });

        // Botão Home para voltar à home
        btnHomeReceitas.setOnClickListener(v -> {
            Intent intent = new Intent(ReceitasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

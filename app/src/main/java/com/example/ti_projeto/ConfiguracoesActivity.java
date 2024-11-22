package com.example.ti_projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracoesActivity extends AppCompatActivity {

    private EditText editPrecoLitroLeite, editPrecoBorrego, editPrecoRacao, editPrecoFardo;
    private Button btnGuardarPrecos, btnSalvarP2;
    private ImageButton btnHomeConfiguracoes;
    private TextView tvPrecosAtuais;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        // Inicializar os componentes
        editPrecoLitroLeite = findViewById(R.id.editPrecoLitroLeite);
        editPrecoBorrego = findViewById(R.id.editPrecoBorrego);
        editPrecoRacao = findViewById(R.id.editPrecoRacao);
        editPrecoFardo = findViewById(R.id.editPrecoFardo);
        btnGuardarPrecos = findViewById(R.id.btnGuardarPrecos);
        btnHomeConfiguracoes = findViewById(R.id.btnHomeConfiguracoes);
        tvPrecosAtuais = findViewById(R.id.tvPrecosAtuais);

        // Inicializar o banco de dados
        dbHelper = new DatabaseHelper(this);

        // Salvar preços dos produtos
        btnGuardarPrecos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String precoLitroLeite = editPrecoLitroLeite.getText().toString();
                String precoBorrego = editPrecoBorrego.getText().toString();
                String precoRacao = editPrecoRacao.getText().toString();
                String precoFardo = editPrecoFardo.getText().toString();


                if (!precoLitroLeite.isEmpty() && !precoBorrego.isEmpty()) {
                    dbHelper.guardarPrecos(Double.parseDouble(precoLitroLeite), Double.parseDouble(precoBorrego),Double.parseDouble(precoRacao), Double.parseDouble(precoFardo));
                    Toast.makeText(ConfiguracoesActivity.this, "Preços Guardados!", Toast.LENGTH_SHORT).show();
                    carregarPrecos(); // Atualiza os preços na tela após salvar
                } else {
                    Toast.makeText(ConfiguracoesActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Botão para voltar à tela principal
        btnHomeConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Carregar preços atuais
        carregarPrecos();
    }

    private void carregarPrecos() {
        double[] precos = dbHelper.getPrecos(); // Este método deve ser adaptado para retornar todos os preços necessários
        editPrecoLitroLeite.setText(String.valueOf(precos[0]));
        editPrecoBorrego.setText(String.valueOf(precos[1]));
        editPrecoRacao.setText(String.valueOf(precos[2]));
        editPrecoFardo.setText(String.valueOf(precos[3]));
        tvPrecosAtuais.setText(String.format("Preços Atuais:\nLitro de Leite: %.2f\nBorrego: %.2f\nRação: %.2f\nFardo: %.2f", precos[0], precos[1], precos[2], precos[3]));
    }
}


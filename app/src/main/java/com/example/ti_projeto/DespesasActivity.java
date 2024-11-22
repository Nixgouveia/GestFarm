package com.example.ti_projeto;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DespesasActivity extends AppCompatActivity {

    private EditText editSacosRacao, editFardosRec, editod;
    private Button btnSaveSacosRacao, btnSaveFardos, btnSalvarOutrasDespesas;
    private ImageButton btnHome;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas); // Certifique-se de que o layout é o correto

        // Inicializar os componentes
        editSacosRacao = findViewById(R.id.editSacosRacao);
        editFardosRec = findViewById(R.id.editFardosRec);
        editod = findViewById(R.id.editod);
        btnSaveSacosRacao = findViewById(R.id.btnSaveSacosRacao);
        btnSaveFardos = findViewById(R.id.btnSaveFardos);
        btnSalvarOutrasDespesas = findViewById(R.id.btnSalvarOutrasDespesas);
        btnHome = findViewById(R.id.btnHome);

        // Inicializar a base de dados
        dbHelper = new DatabaseHelper(this);

        // Configurar listeners para os botões
        btnSaveSacosRacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sacosRacao = editSacosRacao.getText().toString();
                if (!sacosRacao.isEmpty()) {
                    dbHelper.inserirDespesa(Integer.parseInt(sacosRacao),0,0);
                    Toast.makeText(DespesasActivity.this, "Despesas de Ração Guardadas!", Toast.LENGTH_SHORT).show();
                    editSacosRacao.setText(""); // Limpa o campo
                } else {
                    Toast.makeText(DespesasActivity.this, "Preencha o campo de sacos de ração!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSaveFardos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fardos = editFardosRec.getText().toString();
                if (!fardos.isEmpty()) {
                    dbHelper.inserirDespesa(0,Integer.parseInt(fardos),0);
                    Toast.makeText(DespesasActivity.this, "Despesas de Fardos Guardadas!", Toast.LENGTH_SHORT).show();
                    editFardosRec.setText(""); // Limpa o campo
                } else {
                    Toast.makeText(DespesasActivity.this, "Preencha o campo de fardos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSalvarOutrasDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outrasDespesas = editod.getText().toString();
                if (!outrasDespesas.isEmpty()) {
                    dbHelper.inserirDespesa(0,0,Double.parseDouble(outrasDespesas));
                    Toast.makeText(DespesasActivity.this, "Outras Despesas Guardadas!", Toast.LENGTH_SHORT).show();
                    editod.setText(""); // Limpa o campo
                } else {
                    Toast.makeText(DespesasActivity.this, "Preencha o campo de outras despesas!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para voltar para a tela principal
    public void goBack(View view) {
        finish();
    }
}


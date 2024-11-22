package com.example.ti_projeto;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HisActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HisAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his);

        recyclerView = findViewById(R.id.recyclerViewHistorico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        refreshHistorico();
    }

    public void refreshHistorico() {
        // Obtendo o cursor de despesas da BD
        Cursor despesasCursor = dbHelper.getDespesas();

        if (adapter == null) {
            // Inicializa o adapter apenas se ele ainda não foi criado
            adapter = new HisAdapter(despesasCursor, dbHelper, this);
            recyclerView.setAdapter(adapter);
        } else {
            // Atualiza o cursor no adaptador caso ele já exista
            adapter.swapCursor(despesasCursor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
        if (adapter != null) {
            adapter.closeCursor(); // Fechar o cursor no adaptador
        }
    }

    public void goBack(View view) {
        finish();
    }
}


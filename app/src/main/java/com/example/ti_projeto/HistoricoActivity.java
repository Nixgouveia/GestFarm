package com.example.ti_projeto;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoricoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoricoAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        recyclerView = findViewById(R.id.recyclerViewHistorico);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        refreshReceitas();
    }

    public void refreshReceitas() {
        Cursor receitasCursor = dbHelper.getReceitas();

        if (adapter == null) {
            adapter = new HistoricoAdapter(receitasCursor, dbHelper, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapCursor(receitasCursor);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.closeCursor();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public void goBack(View view) {
        finish();
    }
}



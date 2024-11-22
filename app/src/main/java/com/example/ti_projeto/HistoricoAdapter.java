package com.example.ti_projeto;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.ReceitaViewHolder> {

    private Cursor receitasCursor;
    private DatabaseHelper dbHelper;
    private Context context;

    public HistoricoAdapter(Cursor receitasCursor, DatabaseHelper dbHelper, Context context) {
        this.receitasCursor = receitasCursor;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    public void swapCursor(Cursor newCursor) {
        if (receitasCursor != null) receitasCursor.close();
        receitasCursor = newCursor;
        notifyDataSetChanged();
    }

    public void closeCursor() {
        if (receitasCursor != null && !receitasCursor.isClosed()) {
            receitasCursor.close();
        }
    }

    @NonNull
    @Override
    public ReceitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico, parent, false);
        return new ReceitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceitaViewHolder holder, int position) {
        if (receitasCursor.moveToPosition(position)) {
            holder.bindData(receitasCursor, DatabaseHelper.TABLE_RECEITAS);
        }
    }

    @Override
    public int getItemCount() {
        return (receitasCursor != null) ? receitasCursor.getCount() : 0;
    }

    public class ReceitaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtData, txtValor;
        private Button btnDelete;

        public ReceitaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtData);
            txtValor = itemView.findViewById(R.id.txtValor);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bindData(Cursor cursor, String tableName) {
            String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATA));
            txtData.setText(data);

            String valorLeite = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LITROS_LEITE));
            String valorVendas = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ANIMAIS_VENDIDOS));
            String valorSubsidios = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SUBSIDIOS));

            if (valorLeite != null && !valorLeite.isEmpty() && Double.parseDouble(valorLeite) != 0) {
                txtValor.setText(valorLeite + " litros");
            } else if (valorVendas != null && !valorVendas.isEmpty() && Integer.parseInt(valorVendas) != 0) {
                txtValor.setText(valorVendas + " vendas");
            } else if (valorSubsidios != null && !valorSubsidios.isEmpty() && Double.parseDouble(valorSubsidios) != 0.0) {
                txtValor.setText(valorSubsidios + " subsídios");
            } else {
                txtValor.setText("Sem valor");
            }

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteById(tableName, id);
                Toast.makeText(context, "Item apagado", Toast.LENGTH_SHORT).show();
                ((HistoricoActivity) context).refreshReceitas();
            });
        }
    }
}

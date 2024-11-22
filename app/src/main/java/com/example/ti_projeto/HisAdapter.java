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

public class HisAdapter extends RecyclerView.Adapter<HisAdapter.HistoricoViewHolder> {

    private Cursor despesasCursor;
    private DatabaseHelper dbHelper;
    private Context context;

    //Contrutor
    public HisAdapter(Cursor despesasCursor, DatabaseHelper dbHelper, Context context) {
        this.despesasCursor = despesasCursor;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    public void swapCursor(Cursor newCursor) {
        if (despesasCursor != null) despesasCursor.close();
        despesasCursor = newCursor;
        notifyDataSetChanged();
    }

    public void closeCursor() {
        if (despesasCursor != null && !despesasCursor.isClosed()) {
            despesasCursor.close();
        }
    }

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico, parent, false);
        return new HistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        try {
            // Movendo o cursor para a posição do item na lista de despesas
            if (despesasCursor.moveToPosition(position)) {
                holder.bindData(despesasCursor, DatabaseHelper.TABLE_DESPESAS);
            }
        } catch (Exception e) {
            Log.e("HisAdapter", "Erro ao ligar dados: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return (despesasCursor != null) ? despesasCursor.getCount() : 0;
    }


    public class HistoricoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtData, txtValor;
        private Button btnDelete;

        public HistoricoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txtData);
            txtValor = itemView.findViewById(R.id.txtValor);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bindData(Cursor cursor, String tableName) {
            try {
                String data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATA));
                txtData.setText(data);

                // Lendo valores tabela de despesas
                String valorFardos = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FARDOS));
                String valorRacao = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SACOS_RACAO));
                String valorOutras = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_OUTRAS_DESPESAS));


                if (valorFardos != null && !valorFardos.isEmpty() && Integer.parseInt(valorFardos) != 0) {
                    txtValor.setText(valorFardos + " fardos");
                } else if (valorRacao != null && !valorRacao.isEmpty() && Integer.parseInt(valorRacao) != 0) {
                    txtValor.setText(valorRacao + " sacos");
                } else if (valorOutras != null && !valorOutras.isEmpty() && Double.parseDouble(valorOutras) != 0.0) {
                    txtValor.setText(valorOutras + " outras despesas");
                } else {
                    txtValor.setText("Sem valor");
                }

                // Configurando o botão de exclusão
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                btnDelete.setOnClickListener(v -> {
                    dbHelper.deleteById(tableName, id);
                    Toast.makeText(context, "Item apagado", Toast.LENGTH_SHORT).show();
                    ((HistoricoActivity) context).refreshReceitas();
                });

            } catch (Exception e) {
                Log.e("HisAdapter", "Erro ao mostrar os dados: " + e.getMessage());
                txtValor.setText("Erro de valor");
            }
        }
    }
}

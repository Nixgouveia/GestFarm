package com.example.ti_projeto;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nome da base de dados e version
    private static final String DATABASE_NAME = "Farm.db";
    private static final int DATABASE_VERSION = 6;

    // Nome da tabela e colunas (agora públicas)
    public static final String TABLE_RECEITAS = "receitas";
    public static final String TABLE_DESPESAS = "despesas";
    public static final String COL_ID = "id";
    public static final String COL_LITROS_LEITE = "litros_leite";
    public static final String COL_ANIMAIS_VENDIDOS = "animais_vendidos";
    public static final String COL_SUBSIDIOS = "subsidios";
    public static final String COL_SACOS_RACAO = "sacos_racao";
    public static final String COL_FARDOS = "fardos";
    public static final String COL_OUTRAS_DESPESAS = "outras_despesas";
    public static final String COL_DATA = "data"; // Coluna para data
    public static final String TABLE_CONFIGURACOES = "configuracoes";
    public static final String COL_PRECO_LITRO_LEITE = "preco_litro_leite";
    public static final String COL_PRECO_BORREGO = "preco_borrego";
    public static final String COL_PRECO_RACAO = "preco_racao";
    public static final String COL_PRECO_FARDO = "preco_fardo";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Cria as tabelas quando a base de dados é criado pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DatabaseHelper", "Criando tabelas");
        // Criação da tabela de receitas
        String CREATE_TABLE_RECEITAS = "CREATE TABLE " + TABLE_RECEITAS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_LITROS_LEITE + " REAL, " +
                COL_ANIMAIS_VENDIDOS + " INTEGER, " +
                COL_SUBSIDIOS + " REAL, " +
                COL_DATA + " TEXT, " + // Coluna de data
                COL_PRECO_LITRO_LEITE + " REAL, " + // Coluna para armazenar o preço do litro de leite
                COL_PRECO_BORREGO + " REAL" + // Coluna para armazenar o preço do borrego
                ")";
        db.execSQL(CREATE_TABLE_RECEITAS);

        String CREATE_TABLE_DESPESAS = "CREATE TABLE " + TABLE_DESPESAS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SACOS_RACAO + " INTEGER, " +
                COL_FARDOS + " INTEGER, " +
                COL_OUTRAS_DESPESAS + " REAL, " +
                COL_DATA + " TEXT, " + // Coluna de data
                COL_PRECO_RACAO + " REAL, " + // Coluna para armazenar o preço do saco
                COL_PRECO_FARDO + " REAL" + // Coluna para armazenar o preço do fardo
                ")";
        db.execSQL(CREATE_TABLE_DESPESAS);

        String CREATE_TABLE_CONFIGURACOES = "CREATE TABLE " + TABLE_CONFIGURACOES + " (" +
                COL_PRECO_LITRO_LEITE + " REAL, " +
                COL_PRECO_BORREGO + " REAL," +
                COL_PRECO_RACAO + " REAL," +
                COL_PRECO_FARDO + " REAL"+
                ")";
        db.execSQL(CREATE_TABLE_CONFIGURACOES);
    }

    // Atualiza a base de dados quando há mudanças na versão
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DatabaseHelper", "Atualizando banco de dados da versão " + oldVersion + " para " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESPESAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURACOES); // Remover tabela de configurações
        onCreate(db);
    }

    // Função para inserir os dados de receitas, incluindo a data
    public boolean inserirReceita(double litrosLeite, int animaisVendidos, double subsidios) {
        Log.d("DatabaseHelper", "Inserindo receita");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_LITROS_LEITE, litrosLeite);
        values.put(COL_ANIMAIS_VENDIDOS, animaisVendidos);
        values.put(COL_SUBSIDIOS, subsidios);

        // Data atual do sistema
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(COL_DATA, currentDate); // Insere a data atual

        // Recupera os preços atuais
        double[] precos = getPrecos();
        values.put(COL_PRECO_LITRO_LEITE, precos[0]); // Insere o preço do litro de leite
        values.put(COL_PRECO_BORREGO, precos[1]); // Insere o preço do borrego


        // Inserir linha
        long result = db.insert(TABLE_RECEITAS, null, values);
        db.close();

        return result != -1;
    }


    // Função para recuperar todos os dados de receitas
    public Cursor getReceitas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RECEITAS;
        return db.rawQuery(query, null);
    }

    public boolean inserirDespesa(int sacosRacao, int fardos, double outrasDespesas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SACOS_RACAO, sacosRacao);
        values.put(COL_FARDOS, fardos);
        values.put(COL_OUTRAS_DESPESAS, outrasDespesas);

        // Captura a data atual do sistema
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(COL_DATA, currentDate); // Insere a data atual

        // Recupera os preços atuais
        double[] precos = getPrecos();
        values.put(COL_PRECO_RACAO, precos[2]); // preço do saco
        values.put(COL_PRECO_FARDO, precos[3]); // preço do fardo


        // Inserir linha
        long result = db.insert(TABLE_DESPESAS, null, values);
        db.close();

        return result != -1;
    }

    // Função para recuperar todos os dados de despesas
    public Cursor getDespesas() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DESPESAS;
        return db.rawQuery(query, null);
    }



    // Método para guardar preços
    public void guardarPrecos(double precoLitroLeite, double precoBorrego, double precoRacao, double precoFardo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRECO_LITRO_LEITE, precoLitroLeite);
        values.put(COL_PRECO_BORREGO, precoBorrego);
        values.put(COL_PRECO_RACAO, precoRacao);
        values.put(COL_PRECO_FARDO, precoFardo);

        // Limpar a tabela antes de inserir novos preços
        db.delete(TABLE_CONFIGURACOES, null, null);
        db.insert(TABLE_CONFIGURACOES, null, values);
        db.close();
    }


    // Método para recuperar preços
    @SuppressLint("Range")
    public double[] getPrecos() {
        double[] precos = new double[4]; // 0: preco leite, 1: preco borrego
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONFIGURACOES, null);

        if (cursor.moveToFirst()) {
            precos[0] = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_LITRO_LEITE));
            precos[1] = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_BORREGO));
            precos[2] = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_RACAO));
            precos[3] = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_FARDO));
        } else {
            precos[0] = 0; // Valor padrão se não houver dados
            precos[1] = 0; // Valor padrão se não houver dados
            precos[2] = 0; // Valor padrão se não houver dados
            precos[3] = 0; // Valor padrão se não houver dados
        }

        cursor.close();
        return precos;
    }

    // Função para recuperar receitas de um mês específico
    public Cursor getReceitasPorMes(String mes) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RECEITAS + " WHERE strftime('%Y-%m', " + COL_DATA + ") = ?";
        return db.rawQuery(query, new String[]{mes});
    }


// Função para recuperar despesas de um mês específico
public Cursor getDespesasPorMes(String mes) {
    SQLiteDatabase db = this.getReadableDatabase();
    String query = "SELECT * FROM " + TABLE_DESPESAS + " WHERE strftime('%Y-%m', " + COL_DATA + ") = ?";
    return db.rawQuery(query, new String[]{mes});
}

public boolean deleteById(String tableName, int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.delete(tableName, COL_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
}




    // Função para calcular o lucro de cada mês
    public float[] getLucroMensal() {
        float[] lucros = new float[12]; // Array para armazenar o lucro de cada mês (de Janeiro a Dezembro)

        for (int mes = 1; mes <= 12; mes++) {
            // Formato da data para o mês atual (e.g., "2024-01" para Janeiro de 2024)
            String mesFormatado = String.format(Locale.getDefault(), "%d-%02d", Calendar.getInstance().get(Calendar.YEAR), mes);

            // Calcular receitas e despesas para o mês atual
            double totalReceitas = calcularTotalReceitasPorMes(mesFormatado);
            double totalDespesas = calcularTotalDespesasPorMes(mesFormatado);

            // Calcular lucro (receitas - despesas)
            lucros[mes - 1] = (float) (totalReceitas - totalDespesas);
        }

        return lucros;
    }

    // Função auxiliar para calcular o total de receitas em um mês específico
    private double calcularTotalReceitasPorMes(String mes) {
        double totalReceitas = 0.0;
        Cursor cursor = getReceitasPorMes(mes);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") double litrosLeite = cursor.getDouble(cursor.getColumnIndex(COL_LITROS_LEITE));
                @SuppressLint("Range") int animaisVendidos = cursor.getInt(cursor.getColumnIndex(COL_ANIMAIS_VENDIDOS));
                @SuppressLint("Range") double subsidios = cursor.getDouble(cursor.getColumnIndex(COL_SUBSIDIOS));
                @SuppressLint("Range") double precoLeite = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_LITRO_LEITE));
                @SuppressLint("Range") double precoBorrego = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_BORREGO));

                // Cálculo da receita mensal com base nos valores e preços
                totalReceitas += (litrosLeite * precoLeite) + (animaisVendidos * precoBorrego) + subsidios;
            }
            cursor.close();
        }
        return totalReceitas;
    }

    // Função auxiliar para calcular o total de despesas em um mês específico
    private double calcularTotalDespesasPorMes(String mes) {
        double totalDespesas = 0.0;
        Cursor cursor = getDespesasPorMes(mes);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int sacosRacao = cursor.getInt(cursor.getColumnIndex(COL_SACOS_RACAO));
                @SuppressLint("Range") int fardos = cursor.getInt(cursor.getColumnIndex(COL_FARDOS));
                @SuppressLint("Range") double outrasDespesas = cursor.getDouble(cursor.getColumnIndex(COL_OUTRAS_DESPESAS));
                @SuppressLint("Range") double precoRacao = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_RACAO));
                @SuppressLint("Range") double precoFardo = cursor.getDouble(cursor.getColumnIndex(COL_PRECO_FARDO));

                // Cálculo das despesas mensais com base nos valores e preços
                totalDespesas += (sacosRacao * precoRacao) + (fardos * precoFardo) + outrasDespesas;
            }
            cursor.close();
        }
        return totalDespesas;
    }









}

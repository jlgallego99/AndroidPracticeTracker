package com.example.androidpracticetracker.ui.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentDashboardBinding;
import com.example.androidpracticetracker.ui.practica.Obra;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorObras;
    private Gson gson = new Gson();

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    private LineChart lineChart;
    private LineData lineData;
    private LineDataSet lineDataSet;

    private TextView textoHorasTotal;
    private TextView textoUltimaObra;
    private TextView textoObjetivo;

    private CardView cardUltimaObra;
    private CardView cardObjetivoDiario;

    private ArrayList<String> semanaEjeX = new ArrayList<>(Arrays.asList("L", "M", "X", "J", "V", "S", "D"));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editorObras = sharedPreferences.edit();
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Si es lunes, y es nueva semana, reiniciar la semana
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && !sharedPreferences.getBoolean("Reiniciado", false)) {
            // Si vuelve a ser lunes, es nueva semana
            if (!sharedPreferences.getBoolean("NuevaSemana", false)) {
                editorObras.putBoolean("NuevaSemana", true);
                editorObras.putBoolean("Reiniciado", true);
                reiniciarSemana();
            }
        }

        // Si ya ha empezado la nueva semana (es martes), se desactiva el flag de nuevasemana
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            editorObras.putBoolean("NuevaSemana", false);
        }

        textoHorasTotal = root.findViewById(R.id.textoHorasTotal);
        float total = totalSemanal();
        int horas = (int) total / 3600;
        int minutos = (int) (total % 3600) / 60;
        int segundos = (int) total % 60;
        textoHorasTotal.setText(horas + "h " + minutos + "m " + segundos + "s ");

        textoUltimaObra = root.findViewById(R.id.textoUltimaObra);
        Obra ultimaObra = gson.fromJson(sharedPreferences.getString("UltimaObra", ""), Obra.class);
        if (ultimaObra != null) {
            textoUltimaObra.setText(ultimaObra.getNombre() + " - " + ultimaObra.getAutor());
        }

        textoObjetivo = root.findViewById(R.id.textoObjetivo);
        textoObjetivo.setText("Objetivo diario: " + sharedPreferences.getString("Objetivo", ""));

        actualizarGraficas(root);

        cardUltimaObra = root.findViewById(R.id.cardUltimaObra);
        cardUltimaObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("obra", ultimaObra);
                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_obraDetalleFragment, bundle);
            }
        });

        cardObjetivoDiario = root.findViewById(R.id.cardObjetivoDiario);
        cardObjetivoDiario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_objetivo, null);
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                alertDialog.setTitle("Cambiar objetivo diario");
                alertDialog.setCancelable(false);

                final EditText editObjetivo = (EditText) view.findViewById(R.id.editarObjetivo);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nuevoObjetivo = editObjetivo.getText().toString();

                        if (nuevoObjetivo.isEmpty()) {
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), "No se ha cambiado el objetivo", Toast.LENGTH_SHORT).show();
                        } else {
                            editorObras.putString("Objetivo", nuevoObjetivo);
                            editorObras.apply();
                            Toast.makeText(getContext(), "Nuevo objetivo: " + nuevoObjetivo + " minutos", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });


                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(view);
                alertDialog.show();
            }
        });

        return root;
    }

    private void actualizarGraficas(View view) {
        Obra[] obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        // Inicializar semana
        ArrayList<Integer> datosObras = new ArrayList<Integer>(Collections.nCopies(7, 0));
        ArrayList datos = new ArrayList<>();
        if (obras != null) {
            // Sacar lo necesario de todas las obras estudiadas
            for (int i = 0; i < obras.length; i++) {
                for (int j = 0; j < 7; j++) {
                    datosObras.set(j, datosObras.get(j) + obras[i].getEstudio(j));
                }
            }

            // Añadir datos para la gráfica
            for (int i = 0; i < 7; i++) {
                datos.add(new BarEntry(i, datosObras.get(i)));
            }
        }

        barDataSet = new BarDataSet(datos, "Segundos estudiados");
        barData = new BarData(barDataSet);

        barChart = view.findViewById(R.id.graficaSemanal);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(-1);
        xAxis.setAxisMaximum(7);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(semanaEjeX));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);

        // Poner objetivo diario
        LimitLine objetivoDiario = new LimitLine(Float.parseFloat(sharedPreferences.getString("Objetivo", "0")));
        objetivoDiario.setLineColor(Color.GREEN);
        objetivoDiario.setLineWidth(1f);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(objetivoDiario);

        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
    }

    private void reiniciarSemana() {
        editorObras.putString("UltimaObra", "");
        editorObras.apply();
    }

    protected float totalSemanal() {
        float total = 0f;
        Obra[] obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        if (obras != null) {
            for (int i = 0; i < obras.length; i++) {
                for (int j = 0; j < 7; j++) {
                    total += obras[i].getEstudio(j);
                }
            }
        }

        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
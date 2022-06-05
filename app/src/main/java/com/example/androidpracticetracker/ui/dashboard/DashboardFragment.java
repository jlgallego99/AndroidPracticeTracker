package com.example.androidpracticetracker.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.github.mikephil.charting.components.XAxis;
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
import java.util.Collections;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    private LineChart lineChart;
    private LineData lineData;
    private LineDataSet lineDataSet;

    private TextView textoHorasTotal;
    private TextView textoUltimaObra;

    private CardView cardUltimaObra;

    private ArrayList<String> semanaEjeX = new ArrayList<>(Arrays.asList("L", "M", "X", "J", "V", "S", "D"));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textoHorasTotal = root.findViewById(R.id.textoHorasTotal);
        float total = totalSemanal();
        int horas = (int) total / 3600;
        int minutos = (int) (total % 3600) / 60;
        int segundos = (int) total % 60;
        textoHorasTotal.setText(horas + "h " + minutos + "m " + segundos + "s ");

        textoUltimaObra = root.findViewById(R.id.textoUltimaObra);
        Obra ultimaObra = gson.fromJson(sharedPreferences.getString("UltimaObra", ""), Obra.class);
        textoUltimaObra.setText(ultimaObra.getNombre() + " - " + ultimaObra.getAutor());

        cardUltimaObra = root.findViewById(R.id.cardUltimaObra);
        cardUltimaObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("obra", ultimaObra);
                Navigation.findNavController(v).navigate(R.id.action_navigation_dashboard_to_obraDetalleFragment, bundle);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actualizarGraficas(view);
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

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
    }

    protected float totalSemanal() {
        float total = 0f;
        Obra[] obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        for (int i = 0; i < obras.length; i++) {
            total += obras[i].getTiempoEstudiado();
        }

        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
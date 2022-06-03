package com.example.androidpracticetracker.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.Arrays;

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

    private ArrayList<String> semanaEjeX = new ArrayList<>(Arrays.asList("L", "M", "X", "J", "V", "S", "D"));

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        ArrayList<Integer> datosObras = new ArrayList<>();
        if (obras != null) {
            for (int i = 1; i <= 7; i++) {
                int estudiadoDia = 0;

                for (int j = 0; j < obras.length; j++) {
                    if (obras[j].getTiempoEstudiado() > 0 && obras[j].getUltimoEstudio() == i) {
                        estudiadoDia += obras[j].getTiempoEstudiado();
                    }
                }

                datosObras.add(estudiadoDia);
            }
        }

        ArrayList datos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            datos.add(new BarEntry(i, datosObras.get(i)));
        }

        barDataSet = new BarDataSet(datos, "Segundos estudiados");
        barData = new BarData(barDataSet);

        barChart = view.findViewById(R.id.graficaSemanal);
        barChart.setExtraBottomOffset(100f);
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(6);
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(semanaEjeX));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        //xAxis.setAvoidFirstLastClipping(true);

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
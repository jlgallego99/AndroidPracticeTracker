package com.example.androidpracticetracker.ui.practica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentObraDetalleBinding;

import java.util.Calendar;

public class ObraDetalleFragment extends Fragment {
    // Atributos para controlar el cronómetro de estudio
    private Chronometer simpleChronometer;
    private boolean stopped;    // Indica si el cronómetro está parado o no
    private long offset;        // Tiempo que lleva parado el cronómetro

    private TextView textoAutor, textoNombre;
    private FragmentObraDetalleBinding binding;

    public ObraDetalleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentObraDetalleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button startButton = root.findViewById(R.id.startChronometer);
        Button stopButton = root.findViewById(R.id.stopChronometer);
        Button restartButton = root.findViewById(R.id.restartChronometer);
        simpleChronometer = root.findViewById(R.id.simpleChronometer);

        // Por defecto el cronómetro está parado hasta que se inicie a contar
        // Necesita una base de tiempo de la que partir
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        offset = 0;
        stopped = true;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Al usar start, el cronómetro muestra el tiempo que lleva contado cuando se llama al método
                // Es decir, el cronómetro está contando en segundo plano aunque no se le haya dado a start
                if (stopped) {
                    // Para iniciar desde donde se quedó por última vez calculamos el tiempo total que ha transcurrido
                    // y le restamos el último tiempo medido
                    simpleChronometer.setBase(SystemClock.elapsedRealtime() - offset);
                    simpleChronometer.start();

                    stopped = false;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!stopped) {
                    // Al parar el cronómetro almacenamos el tiempo transcurrido en milisegundos
                    offset = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                    simpleChronometer.stop();
                    startButton.setText(R.string.startChronometer2);

                    stopped = true;

                    // Crear Calendar con el tiempo medido
                    Calendar last = Calendar.getInstance();

                    // Guardar cada tiempo medido

                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                startButton.setText(R.string.startChronometer);
                offset = 0;
            }
        });

        Obra o = getArguments().getParcelable("obra");
        textoAutor = root.findViewById(R.id.textoAutor);
        textoNombre = root.findViewById(R.id.textoNombre);
        textoAutor.setText(o.getAutor());
        textoNombre.setText(o.getNombre());

        return root;
    }
}
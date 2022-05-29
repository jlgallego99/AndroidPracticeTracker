package com.example.androidpracticetracker.ui.practica;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentObraDetalleBinding;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;

public class ObraDetalleFragment extends Fragment {
    // Atributos para controlar el cronómetro de estudio
    private Chronometer simpleChronometer;
    private boolean stopped;    // Indica si el cronómetro está parado o no
    private long offset;        // Tiempo que lleva parado el cronómetro

    private TextView textoAutor, textoNombre;
    private FragmentObraDetalleBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorObras;
    private Gson gson = new Gson();

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

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editorObras = sharedPreferences.edit();
        binding = FragmentObraDetalleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button startButton = root.findViewById(R.id.startChronometer);
        Button stopButton = root.findViewById(R.id.stopChronometer);
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

                    startButton.setText(R.string.pausarCronometro);
                    startButton.setBackgroundColor(Color.parseColor("#ffff8800"));
                } else {
                    // Al parar el cronómetro almacenamos el tiempo transcurrido en milisegundos
                    offset = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                    simpleChronometer.stop();
                    stopped = true;

                    startButton.setText(R.string.reanudarCronometro);
                    startButton.setBackgroundColor(Color.parseColor("#ff99cc00"));
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Al parar el cronómetro almacenamos el tiempo transcurrido en milisegundos
                offset = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                if (offset/1000 > 0) {
                    simpleChronometer.stop();
                    stopped = true;

                    // Crear Calendar con el tiempo medido
                    Calendar last = Calendar.getInstance();

                    // Guardar cada tiempo medido
                    guardarTiempoEstudio(last);

                    startButton.setText(R.string.iniciarCronometro);
                    startButton.setBackgroundColor(Color.parseColor("#ff669900"));

                    Toast.makeText(getContext(), "Nuevo estudio: " + offset/1000 + " segundos", Toast.LENGTH_SHORT).show();

                    // Reiniciar cronómetro
                    simpleChronometer.setBase(SystemClock.elapsedRealtime());
                    offset = 0;
                }
            }
        });

        Obra o = getArguments().getParcelable("obra");
        textoAutor = root.findViewById(R.id.textoAutor);
        textoNombre = root.findViewById(R.id.textoNombre);
        textoAutor.setText(o.getAutor());
        textoNombre.setText(o.getNombre());

        return root;
    }

    private void guardarTiempoEstudio(Calendar t) {
        Obra[] obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        // Actualizar el tiempo de la obra
        if (obras != null) {
            for (int i = 0; i < obras.length; i++) {
                if (textoNombre.getText().equals(obras[i].getNombre())) {
                    obras[i].addTiempoEstudiado(offset/1000);
                    obras[i].setUltimoEstudio(t.DAY_OF_WEEK);
                }
            }

            editorObras.putString("Obras", gson.toJson(obras));
            editorObras.apply();
        }
    }
}
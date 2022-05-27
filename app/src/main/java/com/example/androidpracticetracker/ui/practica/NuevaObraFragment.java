package com.example.androidpracticetracker.ui.practica;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentNuevaObraBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


public class NuevaObraFragment extends Fragment {
    private FragmentNuevaObraBinding binding;
    TextView textNombre, textAutor, textEtiquetas;
    SharedPreferences obrasPreferences;
    SharedPreferences.Editor editorObras;

    public NuevaObraFragment() {
        // Required empty public constructor
    }

    public static NuevaObraFragment newInstance() {
        NuevaObraFragment fragment = new NuevaObraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obrasPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editorObras = obrasPreferences.edit();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getView()).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNuevaObraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button botonSubmit = root.findViewById(R.id.botonSubmit);
        textAutor = root.findViewById(R.id.editarAutor);
        textNombre = root.findViewById(R.id.editarNombre);
        textEtiquetas = root.findViewById(R.id.editarEtiquetas);

        botonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Obra[] obras = gson.fromJson(obrasPreferences.getString("Obras", ""), Obra[].class);
                Obra nuevaObra = new Obra(textAutor.getText().toString(), textNombre.getText().toString(), textEtiquetas.getText().toString());

                if (obras == null) {
                    obras = new Obra[1];
                    obras[0] = nuevaObra;
                } else {
                    obras = Arrays.copyOf(obras, obras.length + 1);
                    obras[obras.length - 1] = nuevaObra;
                }

                editorObras.putString("Obras", gson.toJson(obras));
                editorObras.apply();

                Navigation.findNavController(getView()).popBackStack();
            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
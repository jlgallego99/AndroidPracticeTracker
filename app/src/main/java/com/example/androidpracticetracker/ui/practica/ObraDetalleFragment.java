package com.example.androidpracticetracker.ui.practica;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentObraDetalleBinding;

public class ObraDetalleFragment extends Fragment {
    TextView texto;
    FragmentObraDetalleBinding binding;

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

        texto = root.findViewById(R.id.textoObra);

        //Obra o = getArguments().getParcelable("obra");
        String o = getArguments().getString("obra");
        if (o == null) {
            texto.setText("Es nulo");
        } else {
            texto.setText(o);
        }

        return root;
    }
}
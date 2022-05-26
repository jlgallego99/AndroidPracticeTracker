package com.example.androidpracticetracker.ui.practica;

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


public class NuevaObraFragment extends Fragment {
    private FragmentNuevaObraBinding binding;

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

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("ADIOS!");
                Navigation.findNavController(getView()).navigate(R.id.action_nuevaObra_to_navigation_practica);
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
        botonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("HOLA!");
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
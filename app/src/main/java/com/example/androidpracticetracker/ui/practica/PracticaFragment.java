package com.example.androidpracticetracker.ui.practica;

import androidx.annotation.Nullable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.navigation.Navigation;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentPracticaBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PracticaFragment extends ListFragment {

    private FragmentPracticaBinding binding;
    private ObraArrayAdapter adapter;
    private SharedPreferences sharedPreferences;
    private Obra[] obras;
    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        binding = FragmentPracticaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_practica_to_nuevaObra);
            }
        });

        adapter = new ObraArrayAdapter(root.getContext().getApplicationContext(), R.layout.item_lista, sharedPreferences);
        crearListaObras();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("obra", obras[i]);
                Navigation.findNavController(view).navigate(R.id.action_navigation_practica_to_obraDetalleFragment, bundle);
            }
        });
    }

    private void crearListaObras() {
        obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        if (obras != null) {
            for (int i = 0; i < obras.length; i++) {
                adapter.add(obras[i]);
            }

            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
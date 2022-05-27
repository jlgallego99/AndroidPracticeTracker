package com.example.androidpracticetracker.ui.practica;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.androidpracticetracker.R;
import com.example.androidpracticetracker.databinding.FragmentPracticaBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PracticaFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private FragmentPracticaBinding binding;
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    ArrayList<String> listaObras = new ArrayList<String>();
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
                // Create new fragment and transaction
                /*FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);

                // Replace whatever is in the fragment_container view with this fragment
                transaction.replace(R.id.container, NuevaObraFragment.class, null);

                // Commit the transaction
                transaction.commit();*/

            }
        });

        crearListaObras();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
    }

    private void crearListaObras() {
        listaObras = new ArrayList<>();

        Obra[] obras = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);

        if (obras != null) {
            for (int i = 0; i < obras.length; i++) {
                Obra o = obras[i];
                listaObras.add(o.getNombre() + " " + o.getAutor() + " " + o.getEtiquetas());
            }

            adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_lista, listaObras);
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("HOLA", "DENTRO");
        Toast.makeText(getActivity(), "Item: " + listaObras.get(i), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
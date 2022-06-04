package com.example.androidpracticetracker.ui.practica;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidpracticetracker.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ObraArrayAdapter extends ArrayAdapter<Obra> {
    private ArrayList<Obra> obras = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editorObras;
    private Gson gson = new Gson();

    static class ObraViewHolder {
        TextView nombre;
        TextView etiquetas;
        //ImageView borrar;
    }

    public ObraArrayAdapter(@NonNull Context context, int resource, SharedPreferences sharedPreferences) {
        super(context, resource);
        this.sharedPreferences = sharedPreferences;
        editorObras = sharedPreferences.edit();
    }

    @Override
    public void add(Obra o) {
        obras.add(o);
        super.add(o);
    }

    @Override
    public int getCount() {
        return this.obras.size();
    }

    @Override
    public Obra getItem(int i) {
        return this.obras.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ObraViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_lista, parent, false);
            viewHolder = new ObraViewHolder();
            viewHolder.nombre = (TextView) row.findViewById(R.id.vh_nombre);
            viewHolder.etiquetas = (TextView) row.findViewById(R.id.vh_etiquetas);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ObraViewHolder) row.getTag();
        }

        Obra o = getItem(position);
        if (o != null) {
            viewHolder.nombre.setText(o.getNombre());
            viewHolder.etiquetas.setText(o.getEtiquetas());
        }

        // Controlar botón ed borrar obra
        ImageButton boton_borrar = row.findViewById(R.id.imageButton);
        boton_borrar.setTag(position);
        boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Obra[] obs = gson.fromJson(sharedPreferences.getString("Obras", ""), Obra[].class);
                Obra[] nuevas_obras = new Obra[obs.length - 1];
                for (int i = 0; i < obs.length; i++) {
                    if (i != position) {
                        nuevas_obras[i] = obs[i];
                    }
                }

                editorObras.putString("Obras", gson.toJson(nuevas_obras));
                editorObras.apply();

                obras.remove(position);
                notifyDataSetChanged();
            }
        });

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

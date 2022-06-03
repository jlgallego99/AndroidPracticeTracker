package com.example.androidpracticetracker.ui.practica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.androidpracticetracker.R;

import java.util.ArrayList;

public class ObraArrayAdapter extends ArrayAdapter<Obra> {
    private static final String TAG = "ObraArrayAdapter";
    private ArrayList<Obra> obras = new ArrayList<>();

    static class ObraViewHolder {
        TextView nombre;
        TextView etiquetas;
        //ImageView borrar;
    }

    public ObraArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
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
        viewHolder.nombre.setText(o.getNombre());
        viewHolder.etiquetas.setText(o.getEtiquetas());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

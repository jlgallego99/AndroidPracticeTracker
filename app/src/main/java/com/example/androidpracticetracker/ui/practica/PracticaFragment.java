package com.example.androidpracticetracker.ui.practica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidpracticetracker.databinding.FragmentPracticaBinding;

public class PracticaFragment extends Fragment {

    private FragmentPracticaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PracticaViewModel dashboardViewModel =
                new ViewModelProvider(this).get(PracticaViewModel.class);

        binding = FragmentPracticaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPractica;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.iot_thingspeak;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.iot_thingspeak.databinding.FragmentSecondBinding;

import java.util.Locale;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(view1 -> {

            languageSwitch();

            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);

        });

        binding.buttonSecond2.setOnClickListener(view12 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Method to switch the locale (language) based on the current locale. Currently supports en (english) and da (danish).
     * <p>
     * Slight inspiration: https://stackoverflow.com/questions/41098448/change-language-on-buttonclick-in-an-android-app
     */
    public void languageSwitch() {
        //Locale currentLocale = getResources().getConfiguration().locale; //format like en_US / da_DK
        String loadLocale;

        //Check current language and switch it
        if (Locale.getDefault().getDisplayLanguage().toLowerCase().contains("en")) { //current lang is en
            loadLocale = "da";
        } else if (Locale.getDefault().getDisplayLanguage().toLowerCase().contains("da")) { //current lang is da
            loadLocale = "en";
        } else { //current lang is neither en or da, default to en
            loadLocale = "en";
            Log.d("Language error", "None selected. Default to english");
        }

        Locale locale = new Locale(loadLocale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
    }

}
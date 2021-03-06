package com.example.iot_thingspeak;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.iot_thingspeak.databinding.FragmentFirstBinding;


/**
 * FirstFragment class. Has been built upon an Android Studio (IDE) template
 */
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //OnClick settings icon. Navigate to second fragment
        binding.imageView1.setOnClickListener(view1 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FirstFragment log: ", "onResume has run successfully");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
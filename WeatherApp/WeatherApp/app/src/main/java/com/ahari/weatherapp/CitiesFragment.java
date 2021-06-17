package com.ahari.weatherapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/*
    HW05
    CitiesFragment
    Full Name of Student: Anoosh Hari, Dayakar Ravuri.
 */

public class CitiesFragment extends Fragment {

    ArrayList<String> cities = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CitiesRecyclerViewAdapter adapter;

    ICitiesFragmentListener listener;

    public CitiesFragment() {

    }

    public static CitiesFragment newInstance() {
        CitiesFragment fragment = new CitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        getActivity().setTitle(getString(R.string.cities_title));

        recyclerView = view.findViewById(R.id.citiesView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CitiesRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        //progress bar here
        Data.cities.stream().forEach(city -> cities.add(city.getCity()+", "+city.getCountry()));
        adapter.notifyDataSetChanged();

        return view;
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        String cityName;
        Data.City city;

        public CitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.callForCurrentWeather(city);
                }
            });
        }
    }

    class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesViewHolder> {

        @NonNull
        @Override
        public CitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            CitiesViewHolder holder = new CitiesViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CitiesViewHolder holder, int position) {
            Data.City text = Data.cities.get(position);
            holder.textView.setText(text.getCity()+", "+text.getCountry());
            holder.cityName = text.getCity();
            holder.city = text;
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ICitiesFragmentListener){
            listener = (ICitiesFragmentListener) context;
        }
    }

    interface ICitiesFragmentListener {
        void callForCurrentWeather(Data.City cityName);
    }
}
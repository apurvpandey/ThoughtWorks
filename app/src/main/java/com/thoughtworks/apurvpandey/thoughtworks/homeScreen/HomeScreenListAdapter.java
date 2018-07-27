package com.thoughtworks.apurvpandey.thoughtworks.homeScreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtworks.apurvpandey.thoughtworks.R;
import com.thoughtworks.apurvpandey.thoughtworks.model.Beer;

import java.util.ArrayList;

public class HomeScreenListAdapter extends RecyclerView.Adapter<HomeScreenListAdapter.VH> {

    private Context context;
    private ArrayList<Beer> listOfBeer;

    HomeScreenListAdapter(Context context, ArrayList<Beer> beerList) {
        this.context = context;
        listOfBeer = beerList;
    }

    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_beer_list, parent, false);
        return new VH(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.tvBeerName.setText(listOfBeer.get(position).getName());
        holder.tvBeerStyle.setText(String.format("%s : %s ", context.getResources().getString(R.string.beer_style), listOfBeer.get(position).getStyle()));
        holder.tvAbv.setText(String.format("%s : %s ", context.getResources().getString(R.string.abv_content), listOfBeer.get(position).getAbv()));
        holder.tvOunces.setText(String.format("%s : %.2f ", context.getResources().getString(R.string.ounces), listOfBeer.get(position).getOunces()));
        holder.tvIbu.setText(String.format("%s : %s ", context.getResources().getString(R.string.ibu), listOfBeer.get(position).getIbu()));

    }

    @Override
    public int getItemCount() {
        return listOfBeer.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tvBeerName;
        TextView tvBeerStyle;
        TextView tvAbv;
        TextView tvOunces;
        TextView tvIbu;

        VH(View itemView) {
            super(itemView);

            tvBeerName = itemView.findViewById(R.id.beerName);
            tvBeerStyle = itemView.findViewById(R.id.beerStyle);
            tvAbv = itemView.findViewById(R.id.abv);
            tvOunces = itemView.findViewById(R.id.ounces);
            tvIbu = itemView.findViewById(R.id.ibu);

        }
    }

}

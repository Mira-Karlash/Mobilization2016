package com.nicecode.mobilization2016;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Mira on 23.04.2016.
 */
public class BoxAdapter extends BaseAdapter implements Filterable {

    public static ArrayList<Artist> list;

    private LayoutInflater lInflater;
    private View view;
    private Context context;

    public BoxAdapter(ArrayList<Artist> list, Context context) {
        this.list = list;
        this.context = context;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Artist getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, viewGroup, false);
        }

        loadInfo(position);

        return view;
    }

    // загрузка данных об артисте по позиции в ArrayList

    private void loadInfo(int position) {

        Artist artist = list.get(position);

        Picasso.with(context).load(artist.getImage_small()).into((ImageView) view.findViewById(R.id.image_view));

        TextView textViewName = (TextView) view.findViewById(R.id.name);
        textViewName.setText(artist.getName());
        TextView textViewGenres = (TextView) view.findViewById(R.id.genres);

        //преобразование списка жанров в одну строку
        StringBuilder sb = new StringBuilder();
        for (String genre : artist.getGenres()) {
            sb.append(genre+" ");
        }
        try {
            sb.deleteCharAt(sb.lastIndexOf(", "));
        } catch (Exception e) {
            e.printStackTrace();
        }
        textViewGenres.setText(sb.toString());
        TextView textViewAlbumsAndTracks = (TextView) view.findViewById(R.id.albums_tracks);
        textViewAlbumsAndTracks.setText(artist.getAlbums()+" альбомов, "+artist.getTracks()+" песен");


    }

    public ArrayList<Artist> listFiltered;

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Artist> results = new ArrayList<Artist>();
                if (listFiltered == null)
                    listFiltered = list;
                if (constraint != null) {
                    if (listFiltered != null && listFiltered.size() > 0) {
                        for (final Artist g : listFiltered) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                list = (ArrayList<Artist>) results.values;
                notifyDataSetChanged();
            }
        };

    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

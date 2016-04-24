package com.nicecode.mobilization2016;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Mira on 23.04.2016.
 */
public class BoxAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    String[][] objects;
    String[][] genres;

    BoxAdapter(Context _context, String[][] products, String[][] _genres) {
        context = _context;
        //objects = products;
        objects = new String[products.length][8];
        System.arraycopy( products, 0, objects, 0, products.length );
        //System.arraycopy( objects, 0, products, 0, objects.length );
        genres = new String[_genres.length][2];
        System.arraycopy( _genres, 0, genres, 0, _genres.length );

        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.length;
    }

    // элемент по позиции
    @Override
    public String getItem(int position) {
        return objects[position][0];
    }
    //tv_id.setText(objects[position][0]

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        //Product p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        /*((TextView) view.findViewById(R.id.tvDescr)).setText(p.name);
        ((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
        ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);*/
        ((TextView) view.findViewById(R.id.name)).setText(objects[position][1]);
        ((TextView) view.findViewById(R.id.albums_tracks)).setText(objects[position][3]+" альбомов, "+objects[position][2]+" песен");
        String GENRES ="";
        for(int i =0; i<genres.length; i++){
            if(objects[position][0].equals(genres[i][0])){
                GENRES+=genres[i][1]+" ";
            }
        }
        Log.d("Log", GENRES);
        ((TextView) view.findViewById(R.id.genres)).setText(GENRES);

        Picasso.with(context).load(objects[position][5]).into((ImageView) view.findViewById(R.id.image_view));
        return view;
    }

    // товар по позиции
    /*Product getProduct(int position) {
        return ((Product) getItem(position));
    }*/
}

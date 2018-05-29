package com.example.jonan.ironplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter  extends ArrayAdapter<String> {

    private final Activity context;
    private  String[] itemname;
    private  Bitmap[] imgid;

    public CustomListAdapter(Activity context, String[] itemname, Bitmap[] imgid){
        super(context, R.layout.youtubersearchlist, itemname);
        // TODO Auto-generated constructor stub
        this.context=context;

    }

    public void setImgid( Bitmap[] imgid){
        this.imgid=imgid;
    }

    public void setItemname(  String[] itemname){
        this.itemname=itemname;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.youtubersearchlist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname[position]);
        imageView.setImageBitmap(imgid[position]);
        return rowView;

    };
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
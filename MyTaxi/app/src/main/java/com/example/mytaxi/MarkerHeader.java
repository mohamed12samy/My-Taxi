package com.example.mytaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerHeader implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater=null;
    private View popup;

    public MarkerHeader(LayoutInflater layoutInflater) {
        this.inflater = layoutInflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }

        TextView tv=(TextView)popup.findViewById(R.id.type);
        ImageView img = popup.findViewById(R.id.header_image);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.id);
        tv.setText(marker.getSnippet());

        if(marker.getTitle().equals("TAXI")) img.setImageResource(R.drawable.taxi);
        if(marker.getTitle().equals("POOLING")) img.setImageResource(R.drawable.car_pooling);

        return(popup);
    }
}

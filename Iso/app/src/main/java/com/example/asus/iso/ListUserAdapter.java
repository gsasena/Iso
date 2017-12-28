package com.example.asus.iso;

/**
 * Created by Asus on 19/12/2017.
 */

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListUserAdapter extends BaseAdapter{
    private Activity context;
    private LayoutInflater inflater;
    private List   <ModelUser > NasItem;



    public ListUserAdapter(Activity activity, List   <ModelUser > NasItem) {

        ListUserAdapter.this.context = activity;
        ListUserAdapter.this.NasItem = NasItem;
    }

    @Override
    public int getCount() {
        return NasItem.size();
    }

    @Override
    public Object getItem(int location) {
        return NasItem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) view = inflater.inflate(R.layout.list_row_user, null);

        View rowView= inflater.inflate(R.layout.list_row_user, null, true);

        TextView tvNomor = (TextView) rowView.findViewById(R.id.tvNomor);
        TextView tvNamaUser = (TextView) rowView.findViewById(R.id.tvNamaUser);
        TextView tvEmail = (TextView) rowView.findViewById(R.id.tvEmail);
        TextView tvTipeUser = (TextView) rowView.findViewById(R.id.tvTipeUser);
        ModelUser m = NasItem.get(position);

        tvNomor.setText(m.getNo());
        tvNamaUser.setText(m.getNama());
        tvEmail.setText(m.getEmail());
        tvTipeUser.setText(m.getTipeUser());
        return rowView;
    }
}

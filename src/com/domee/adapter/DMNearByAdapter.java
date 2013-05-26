package com.domee.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.domee.R;
import com.domee.model.DMPois;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by duyuan on 13-5-22.
 */
public class DMNearByAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private LinkedList<DMPois> poisList;

    public DMNearByAdapter(Activity activity) {
        this.mInflater = LayoutInflater.from(activity);
    }

    public final class ViewHolder {
        private TextView nbTitle;
        private TextView nbAddress;
    }

    @Override
    public int getCount() {
        if (poisList != null) {
            return poisList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_near_by, null);
            holder.nbTitle = (TextView) view.findViewById(R.id.nb_title);
            holder.nbAddress = (TextView) view.findViewById(R.id.nb_address);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        DMPois pois = poisList.get(i);
        holder.nbTitle.setText(pois.getTitle());
        holder.nbAddress.setText(pois.getAddress());

        return view;
    }

    public LinkedList<DMPois> getPoisList() {
        return poisList;
    }

    public void setPoisList(LinkedList<DMPois> poisList) {
        this.poisList = poisList;
    }

}

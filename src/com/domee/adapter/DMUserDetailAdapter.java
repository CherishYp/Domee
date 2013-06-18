package com.domee.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.domee.R;

import java.util.List;
import java.util.Map;

/**
 * Created by duyuan on 13-5-29.
 */
public class DMUserDetailAdapter extends BaseAdapter {

    private List<Map<String, String>> mDetailList;
    private Activity mActivity;
    private LayoutInflater mInflater;

    public DMUserDetailAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        this.mInflater = LayoutInflater.from(mActivity);
    }
    public final class ViewHolder {
        private TextView mTextTitle;
        private TextView mText;
    }

    @Override
    public int getCount() {
        if (mDetailList != null) {
            return mDetailList.size();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_user_detail, null);

            holder.mTextTitle = (TextView) convertView.findViewById(R.id.ud_text_tile);
            holder.mText = (TextView) convertView.findViewById(R.id.ud_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> map = mDetailList.get(i);
        holder.mTextTitle.setText(map.get("title"));
        holder.mText.setText(map.get("text"));
        return convertView;
    }

    public List<Map<String, String>> getmDetailList() {
        return mDetailList;
    }

    public void setmDetailList(List<Map<String, String>> mDetailList) {
        this.mDetailList = mDetailList;
    }
}

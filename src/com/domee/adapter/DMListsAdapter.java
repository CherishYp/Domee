package com.domee.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.domee.R;
import com.domee.manager.DMAccountsManager;
import com.domee.manager.DMUIManager;
import com.domee.model.DMLists;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by duyuan on 13-6-16.
 */
public class DMListsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private LinkedList<DMLists> mLists;
    private ListView mListView;

    public DMListsAdapter(Activity mActivity, ListView mListView) {
        this.mInflater = LayoutInflater.from(mActivity);
        this.mListView = mListView;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                DMAccountsManager.curAccount.setGroupType(mLists.get(i).getId() + "");
                DMUIManager.getInstance().getFriendsTimelineActivity().refresh();
            }
        });
    }

    public class ViewHolder {
        private TextView mGroupName;
    }

    @Override
    public int getCount() {
        if (mLists != null) {
            return mLists.size();
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
            convertView = mInflater.inflate(R.layout.item_group, null);
            holder.mGroupName = (TextView) convertView.findViewById(R.id.group_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DMLists lists = mLists.get(i);
        holder.mGroupName.setText(lists.getName());
        return convertView;
    }

    public LinkedList<DMLists> getmLists() {
        return mLists;
    }

    public void setmLists(LinkedList<DMLists> mLists) {
        this.mLists = mLists;
    }



}

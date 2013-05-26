package com.domee.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class DMPagerAdapter extends PagerAdapter {

	private List<View> dmViews;
	public DMPagerAdapter(List<View> dmViews) {
		// TODO Auto-generated constructor stub
		this.dmViews = dmViews;//构造方法，参数是我们的页卡，这样比较方便。
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(dmViews.get(position));//删除页卡
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dmViews.size();
	}

	@Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
         container.addView(dmViews.get(position), 0);//添加页卡
         return dmViews.get(position);
    }

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}

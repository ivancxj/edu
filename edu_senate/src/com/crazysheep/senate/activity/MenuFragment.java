package com.crazysheep.senate.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.crazysheep.senate.R;
import com.crazysheep.senate.fragment.AlbumFragment;
import com.crazysheep.senate.fragment.NotifyFragment;

public class MenuFragment extends ListFragment {

    private ItemListBaseAdapter mAdapter;
    private Menu[] menus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] names = getResources().getStringArray(R.array.menu_names);
        TypedArray imgs = getResources().obtainTypedArray(R.array.menu_imgs);
        menus = new Menu[names.length];
        for (int i = 0; i < imgs.length(); i++) {
            int resId = imgs.getResourceId(i, -1);
            Menu menu = new Menu();
            menu.title = names[i];
            menu.icon = resId;
            menus[i] = menu;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ItemListBaseAdapter(getActivity(), menus, 1);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        for (int i = 0; i < lv.getCount(); i++) {
            lv.getChildAt(i).setBackgroundResource(R.drawable.menu_item_normal);
        }
        Fragment newContent = null;
        switch (position) {
            case 0:// 相册
                newContent = new AlbumFragment();
                v.setBackgroundResource(R.drawable.menu_item_selected1);
                break;
            case 1:// 园所通知
                newContent = new NotifyFragment();
                v.setBackgroundResource(R.drawable.menu_item_selected1);
                break;
            case 2:// 出勤点名
                newContent = new NamedFragment();
                v.setBackgroundResource(R.drawable.menu_item_selected1);
                break;
        }
        if (newContent != null) {
            switchFragment(newContent, (Menu) mAdapter.getItem(position));
        }
    }

    private void switchFragment(Fragment fragment, Menu menu) {
        if (getActivity() == null)
            return;
        if (getActivity() instanceof FragmentChangeActivity) {
            FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
            fca.switchContent(fragment, menu);
        }
    }

    public class ItemListBaseAdapter extends BaseAdapter {

        private Menu[] menus;
        private LayoutInflater mInflater;
        private int dft = 1;

        public ItemListBaseAdapter(Context context, Menu[] menus, int dft) {
            this.menus = menus;
            mInflater = LayoutInflater.from(context);
            this.dft = dft;
        }

        @Override
        public int getCount() {
            if (menus == null)
                return 0;
            return menus.length;
        }

        @Override
        public Object getItem(int position) {
            return menus[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.menu_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(menus[position].title);
            holder.icon.setImageResource(menus[position].icon);
            if (position == dft) {
                convertView.setBackgroundResource(R.drawable.menu_item_selected1);
            } else {
                convertView.setBackgroundResource(R.drawable.menu_item_normal);
            }
            return convertView;
        }

    }

    public static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public class Menu {
        public String title;
        public Integer icon;
    }


}

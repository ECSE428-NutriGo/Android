package ca.mcgill.ecse428.nutrigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapterFoodItem extends BaseAdapter {
    private static ArrayList<ListFoodItem> searchArrayList;

    private LayoutInflater mInflater;

    public CustomBaseAdapterFoodItem(Context context, ArrayList<ListFoodItem> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.multi_lines, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtCityState = (TextView) convertView.findViewById(R.id.items);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getItem());
        String[] macros= searchArrayList.get(position).getMacros();
        String display = "Carbs: "+ macros[0] + " Protein: " + macros[1] + " Fat: " + macros[2];
        holder.txtCityState.setText(display);

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
    }
}
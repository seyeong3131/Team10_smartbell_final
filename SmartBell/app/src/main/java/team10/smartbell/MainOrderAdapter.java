package team10.smartbell;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class MainOrderAdapter extends BaseAdapter {
    public static class ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView    priceTextView;
        TextView    descriptionTextView;
        Button button;
        TextView countTextView;

        ViewHolder(View view) {
            iconImageView       = view.findViewById(R.id.adapter_order_icon_imageview);
            nameTextView        = view.findViewById(R.id.adapter_order_name_textview);
            priceTextView       = view.findViewById(R.id.adapter_order_price_textview);
            descriptionTextView = view.findViewById(R.id.adapter_order_description_textview);
            button              = view.findViewById(R.id.adapter_order_button);
            countTextView      = view.findViewById(R.id.adapter_order_count_textview);
        }
    }


    private Context context;
    private ArrayList<OrderedMenu> orderList;
//    private int count;

    private BiConsumer<Integer, OrderedMenu> listener;
    private String                      buttonText;


    MainOrderAdapter(Context context) {
        this.context = context;
        this.orderList = new ArrayList<OrderedMenu>();
    }

    void setListener(String buttonText, BiConsumer<Integer, OrderedMenu> listener) {
        this.buttonText = buttonText;
        this.listener   = listener;
    }


    void setItems(ArrayList<OrderedMenu> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public OrderedMenu getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adpater_main_order, parent, false);
            convertView.setTag(new MainOrderAdapter.ViewHolder(convertView));
        }

        OrderedMenu menu = getItem(position);

        MainOrderAdapter.ViewHolder viewHolder = (MainOrderAdapter.ViewHolder) convertView.getTag();
        viewHolder.nameTextView.setText(menu.getName());
        viewHolder.priceTextView.setText(context.getString(R.string.price, menu.getPrice()));
        viewHolder.descriptionTextView.setText(menu.getDescription());
        viewHolder.countTextView.setText(String.valueOf(menu.getCount()));

        switch (menu.getType()) {
            case Coffee:
                viewHolder.iconImageView.setBackground(context.getDrawable(R.drawable.coffee));
                viewHolder.nameTextView.setTextColor(Color.parseColor("#00BFFF"));
                break;

            case Tea:
                viewHolder.iconImageView.setBackground(context.getDrawable(R.drawable.tea));
                viewHolder.nameTextView.setTextColor(Color.parseColor("#04B45F"));
                break;

            case Dessert:
                viewHolder.iconImageView.setBackground(context.getDrawable(R.drawable.dessert));
                viewHolder.nameTextView.setTextColor(Color.parseColor("#2EFEF7"));
                break;

            case Ice:
                viewHolder.iconImageView.setBackground(context.getDrawable(R.drawable.ice));
                viewHolder.nameTextView.setTextColor(Color.parseColor("#00FF00"));
                break;
        }

        viewHolder.button.setText(buttonText);
        viewHolder.button.setOnClickListener(v -> listener.accept(position, menu));
        return convertView;
    }
}

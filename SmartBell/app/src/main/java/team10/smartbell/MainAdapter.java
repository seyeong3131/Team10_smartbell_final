package team10.smartbell;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MainAdapter extends android.widget.BaseAdapter {
    public static class ViewHolder {
        ImageView   iconImageView;
        TextView    nameTextView;
        TextView    priceTextView;
        TextView    descriptionTextView;
        Button      button;

        ViewHolder(View view) {
            iconImageView       = view.findViewById(R.id.adapter_main_icon_imageview);
            nameTextView        = view.findViewById(R.id.adapter_main_name_textview);
            priceTextView       = view.findViewById(R.id.adapter_main_price_textview);
            descriptionTextView = view.findViewById(R.id.adapter_main_description_textview);
            button              = view.findViewById(R.id.adapter_main_button);
        }
    }


    private Context     context;
    private List<Menu>  items;

    private BiConsumer<Integer, Menu>   listener;
    private String                      buttonText;


    MainAdapter(Context context) {
        this.context = context;
    }


    void setItems(List<Menu> items) {
        items.sort((item1, item2) -> item1.getName().compareTo(item2.getName()));

        this.items = items;
        notifyDataSetChanged();
    }

    void setListener(String buttonText, BiConsumer<Integer, Menu> listener) {
        this.buttonText = buttonText;
        this.listener   = listener;
    }


    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Menu getItem(int position) {
        return items == null ? null : items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_main, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Menu menu = getItem(position);

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.nameTextView.setText(menu.getName());
        viewHolder.priceTextView.setText(context.getString(R.string.price, menu.getPrice()));
        viewHolder.descriptionTextView.setText(menu.getDescription());

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

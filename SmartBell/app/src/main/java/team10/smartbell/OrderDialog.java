package team10.smartbell;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class OrderDialog extends Dialog {
    OrderDialog(Context context, List<Menu> orders, Runnable listener) {
        super(context);
        setContentView(R.layout.order_dialog);
        setCancelable(false);

        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        findViewById(R.id.dialog_order_button1).setOnClickListener(v -> {
            dismiss();
            listener.run();
        });
        findViewById(R.id.dialog_order_button2).setOnClickListener(v -> dismiss());

        initOrder(orders);
    }

    private void initOrder(List<Menu> orders) {
        ViewGroup layout = findViewById(R.id.dialog_order_content_layout);

        float total = 0.f;

        for (Menu menu : orders) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_order, layout, false);
            layout.addView(view);

            TextView startView  = view.findViewById(R.id.view_order_content_start_textview);
            TextView endView    = view.findViewById(R.id.view_order_content_end_textview);

            startView.setText(menu.getName());
            endView.setText(getContext().getString(R.string.price, menu.getPrice()));

            total += menu.getPrice();
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_order, layout, false);
        layout.addView(view);

        TextView startView  = view.findViewById(R.id.view_order_content_start_textview);
        TextView endView    = view.findViewById(R.id.view_order_content_end_textview);
        endView.setTextColor(Color.parseColor("#FF0000"));
        endView.setTypeface(null, Typeface.BOLD);
        startView.setTypeface(null, Typeface.BOLD_ITALIC);

        startView.setText(R.string.total);
        endView.setText(getContext().getString(R.string.price, total));
    }
}

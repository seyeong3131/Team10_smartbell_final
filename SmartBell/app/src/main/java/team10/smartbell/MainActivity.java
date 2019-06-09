package team10.smartbell;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MainAdapter adapter1, adapter2;

    private List<Menu> orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuFirebaseMessagingService.init(this);

        ListView listView1 = findViewById(R.id.activity_main_listview1);
        ListView listView2 = findViewById(R.id.activity_main_listview2);

        adapter1 = new MainAdapter(this);
        listView1.setAdapter(adapter1);

        adapter2 = new MainAdapter(this);
        listView2.setAdapter(adapter2);

        adapter1.setListener("点单", this::setListener1);
        adapter2.setListener("取消", this::setListener2);

        findViewById(R.id.menu1_button).setOnClickListener(v -> {
            listView1.setVisibility(View.VISIBLE);
            listView2.setVisibility(View.GONE);
        });

        findViewById(R.id.menu2_button).setOnClickListener(v -> {
            listView2.setVisibility(View.VISIBLE);
            listView1.setVisibility(View.GONE);

            adapter2.setItems(orders);
        });

        findViewById(R.id.order_button).setOnClickListener(v -> {
            if (orders.size() == 0) return;

            new OrderDialog(this, orders, () ->
                    ApiManager.shared().order(orders, (error, response) -> {
                        if (error == null) {
                            orders.clear();
                            adapter2.setItems(orders);
                            adapter2.notifyDataSetChanged();
                        }

                        alert(error == null);
                    })
            ).show();
        });

        init();
    }

    private void init() {
        orders = new ArrayList<>();
        adapter1.setItems(Menu.sample(this));
    }

    @SuppressWarnings("unused")
    private void setListener1(int position, Menu menu) {
        orders.add(menu);
    }

    @SuppressWarnings("unused")
    private void setListener2(int position, Menu menu) {
        orders.remove(position);
        adapter2.setItems(orders);
        adapter2.notifyDataSetChanged();
    }

    private void alert(boolean result) {
        String message = getString(result ? R.string.alert_order_success : R.string.alert_order_failure);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}

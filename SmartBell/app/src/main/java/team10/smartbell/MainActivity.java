package team10.smartbell;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MainAdapter adapter1;
    private MainOrderAdapter adapter2;

    private ArrayList<OrderedMenu> orderList;
    private ArrayList<Integer> orderOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuFirebaseMessagingService.init(this);

        ListView listView1 = findViewById(R.id.activity_main_listview1);
        ListView listView2 = findViewById(R.id.activity_main_listview2);

        adapter1 = new MainAdapter(this);
        init();
        listView1.setAdapter(adapter1);

        adapter2 = new MainOrderAdapter(this);
        listView2.setAdapter(adapter2);

        adapter1.setListener("주문(点单)", this::setListener1);
        adapter2.setListener("취소(取消)", this::setListener2);

        findViewById(R.id.menu1_button).setOnClickListener(v -> {
            listView1.setVisibility(View.VISIBLE);
            listView2.setVisibility(View.GONE);
        });

        findViewById(R.id.menu2_button).setOnClickListener(v -> {
            listView2.setVisibility(View.VISIBLE);
            listView1.setVisibility(View.GONE);

            Log.d("OrderList", orderOrder.toString());
            adapter2.setItems(orderList);
        });

        findViewById(R.id.order_button).setOnClickListener(v -> {
            if (orderList.size() == 0) return;

            new OrderDialog(this, orderList, () ->
                    ApiManager.shared().order(orderList, (error, response) -> {
                        if (error == null) {
//                            orders.clear();
//                            adapter2.setItems(orders);
                            adapter2.setItems(orderList);
                            adapter2.notifyDataSetChanged();
                        }

                        alert(error == null);
                    })
            ).show();
        });

        init();
    }

    private void init() {
        adapter1.setItems(Menu.sample(this));

        orderList = new ArrayList<OrderedMenu>();
        orderOrder = new ArrayList<Integer>();
    }

    @SuppressWarnings("unused")
    private void setListener1(int position, Menu menu) {
//        orders.add(menu);
        Log.d("Order", String.valueOf(position));
        if (!orderOrder.contains(position)) {
            OrderedMenu orderedMenu = new OrderedMenu(menu.getTypeAsInt(), menu.getName(), menu.getPrice(), menu.getDescription());
            orderList.add(orderedMenu);
            orderOrder.add(position);
        }
        else {
            int index = orderOrder.indexOf(position);
            Log.d("Ordercheck", String.valueOf(index));
            orderList.get(index).addCount();
        }
    }

    @SuppressWarnings("unused")
    private void setListener2(int position, OrderedMenu menu) {

        orderOrder.remove(position);
        orderList.remove(position);

        adapter2.setItems(orderList);
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

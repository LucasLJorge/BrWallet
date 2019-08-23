package com.jorge.brwallet.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jorge.brwallet.exceptions.LoadWalletErrorException;
import com.jorge.brwallet.model.Wallet;
import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;

import java.util.ArrayList;

public class LoadWalletView extends AppCompatActivity {

    ListView loadListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_wallet);

        loadListView = findViewById(R.id.load_wallet_listview);
        View emptyView = findViewById(R.id.empty_load_list);
        loadListView.setEmptyView(emptyView);

        ArrayList<Wallet> wallets = WalletController.getInstance(getBaseContext()).getWalletList();
        String[] list = new String[wallets.size()];

        for (int i = 0; i < list.length; i++){
            list[i] = wallets.get(i).getName();
        }



        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };
        loadListView.setAdapter(adapter);

        loadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String item = (String) loadListView.getItemAtPosition(position);

                try {
                    if (WalletController.getInstance(getBaseContext()).load(item)){
                        Toast.makeText(getApplicationContext(), "Carregando carteira '" + item + "'...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), ManageWalletView.class);
                        intent.putExtra("RESULTADO", item);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Carteira '" + item + "' não pode ser carregada...", Toast.LENGTH_SHORT).show();
                    }
                } catch (LoadWalletErrorException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}

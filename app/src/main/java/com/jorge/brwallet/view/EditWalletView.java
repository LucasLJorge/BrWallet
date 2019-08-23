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

import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;
import com.jorge.brwallet.model.Asset;
import com.jorge.brwallet.model.Liability;

import java.util.ArrayList;

public class EditWalletView extends AppCompatActivity {

    ListView listAssets;
    ListView listLiabilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet);

        listAssets = findViewById(R.id.select_to_ass_edit_listview);
        listLiabilities = findViewById(R.id.select_to_lia_edit_listview);

        /*View emptyListAssets = findViewById(R.id.empty_asset_list);
        listAssets.setEmptyView(emptyListAssets);

        View emptyListLiabilities = findViewById(R.id.empty_liabilities_list);
        listAssets.setEmptyView(emptyListLiabilities);*/

        ArrayList<Asset> assets = WalletController.getInstance(getBaseContext()).getWalletAssets();
        ArrayList<Liability> liabilities = WalletController.getInstance(getBaseContext()).getWalletLiabilities();

        String[] assetList = new String[assets.size()];
        for (int i = 0; i < assetList.length; i++){
            assetList[i] = assets.get(i).getName();
        }

        String[] liabilitiesList = new String[liabilities.size()];
        for (int i = 0; i < liabilitiesList.length; i++){
            liabilitiesList[i] = liabilities.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, assetList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);

                return view;
            }
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, liabilitiesList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);

                tv.setTextColor(Color.WHITE);

                return view;
            }
        };

        listAssets.setAdapter(adapter);

        listAssets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(assets != null){
                    String item = (String) listAssets.getItemAtPosition(position);

                    Object result = new Asset(
                            assets.get(position).getId(),
                            assets.get(position).getName(),
                            assets.get(position).getValue(),
                            assets.get(position).getDate(),
                            assets.get(position).getCurrency()
                    );
                    Toast.makeText(getApplicationContext(), "Carregando item '" + item + "' para edição...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), EditItemView.class);
                    intent.putExtra("RESULTADO", (Asset)result);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "NÃO DEU CERTO", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listLiabilities.setAdapter(adapter2);

        listLiabilities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(liabilities != null){
                    String item = (String) listAssets.getItemAtPosition(position);

                    Object result = new Liability(
                            liabilities.get(position).getId(),
                            liabilities.get(position).getName(),
                            liabilities.get(position).getValue(),
                            liabilities.get(position).getDate(),
                            liabilities.get(position).getCurrency()
                    );
                    Toast.makeText(getApplicationContext(), "Carregando item '" + item + "' para edição...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), EditItemView.class);
                    intent.putExtra("RESULTADO", (Liability)result);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "NÃO DEU CERTO", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.jorge.brwallet.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jorge.brwallet.model.Wallet;
import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;

import java.util.ArrayList;

public class ExportWalletView extends AppCompatActivity {

    ListView exportListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_wallet);

        exportListView = findViewById(R.id.export_wallet_listview);
        View emptyView = findViewById(R.id.empty_export_list);
        exportListView.setEmptyView(emptyView);

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
        exportListView.setAdapter(adapter);

        exportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String item = (String) exportListView.getItemAtPosition(position);
                String path = getFilesDir().toString() + "/"+ item +".txt";
                Toast.makeText(getApplicationContext(), "Exportando carteira '" + item + "'...", Toast.LENGTH_SHORT).show();

                Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.jorge.brwallet", WalletController.getInstance(getBaseContext()).exportWallet(getBaseContext(), path));
                Intent intent = new Intent(Intent.ACTION_SEND);

                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Compartilhar com..."));
                setResult(Activity.RESULT_OK);
                //WalletController.getInstance(getBaseContext()).deleteWalletFile(getBaseContext(), path);
                finish();
            }
        });
    }
}

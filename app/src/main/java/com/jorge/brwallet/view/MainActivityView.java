package com.jorge.brwallet.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.jorge.brwallet.R;

public class MainActivityView extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    Toast.makeText(context, "O zap zap tá pegando, mas antes, faça suas contas no BRWallet!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        final Button createWallet = findViewById(R.id.b_create_wallet);
        createWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateWalletView.class);
                startActivity(intent);
            }
        });

        final Button loadWallet = findViewById(R.id.b_load_wallet);
        loadWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoadWalletView.class);
                startActivity(intent);
            }
        });

        final Button exportWallet = findViewById(R.id.b_export_wallet);
        exportWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExportWalletView.class);
                startActivityForResult(intent, 1);
            }
        });

        final ImageView appInfo = findViewById(R.id.b_about);
        appInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Mapa carregando...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), MapsActivityView.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Carteira exportada com sucesso!", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(this, "Exportação abortada! :(", Toast.LENGTH_LONG).show();
            }
        }
    @Override
    protected void onStart(){
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

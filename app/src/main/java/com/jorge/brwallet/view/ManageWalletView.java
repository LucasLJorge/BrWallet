package com.jorge.brwallet.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.jorge.brwallet.exceptions.DeleteWalletExceptionError;
import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;

import java.io.File;
import java.math.BigDecimal;

import static com.jorge.brwallet.view.CreateWalletView.fileName;

public class ManageWalletView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static  int SIMPLE_NOTIFICATION_ID = 10001;
    private NotificationManager mNotificationManager;
    private static final String IS_BIG_NOTIFICATION = "isBigNotification";
    private static final String CHANNEL_ID = "com.jorge.brwallet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_wallet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textTotalGeral = findViewById(R.id.value_total_geral);
        TextView textTotalAssets = findViewById(R.id.value_total_assets);
        TextView textTotalLiabilities = findViewById(R.id.value_total_liabilities);

        //WalletController.getInstance(getBaseContext()).getOperations();

        textTotalAssets.setText("R$" + WalletController.getInstance(getBaseContext()).getTotalAssets(getBaseContext()).toString());
        textTotalLiabilities.setText("R$" + WalletController.getInstance(getBaseContext()).getTotalLiabilities(getBaseContext()).toString());

        if(WalletController.getInstance(getBaseContext()).getWalletBalance(getBaseContext()).compareTo(new BigDecimal(0)) == -1){
            textTotalGeral.setText("-R$" + WalletController.getInstance(getBaseContext()).getWalletBalance(getBaseContext()).multiply(new BigDecimal(-1)).toString());
        }else
            textTotalGeral.setText("R$" + WalletController.getInstance(getBaseContext()).getWalletBalance(getBaseContext()).toString());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        createSimpleNotification();

    }

    private void createSimpleNotification() {
        //String actualWallet = getIntent().getExtras().getString("RESULTADO") + " está aberta!";
        String actualWallet = "Uma carteira está aberta, clique aqui para acessá-la.";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.baseline_how_to_vote_black_48)
                        .setContentTitle("BRWallet: Carteira aberta")
                        .setOngoing(false)
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_ID)
                        .setContentText(actualWallet);

        Intent resultIntent = new Intent(this, ManageWalletView.class);
        resultIntent.putExtra(IS_BIG_NOTIFICATION,  false);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Simple Notification channed";
            String description = "Simples Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(SIMPLE_NOTIFICATION_ID++, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_wallet, menu);
        return true;
    }


    //action_about
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(ManageWalletView.this, MapsActivityView.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_my_walltet) {

        } else if (id == R.id.nav_add_asset) {
            Intent intent = new Intent(ManageWalletView.this, AddAssetView.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_liability) {
            Intent intent = new Intent(ManageWalletView.this, AddLiabilityView.class);
            startActivity(intent);
        } else if (id == R.id.nav_edit_wallet) {
            Intent intent = new Intent(ManageWalletView.this, EditWalletView.class);
            startActivity(intent);
        } else if (id == R.id.nav_export_wallet) {
            Toast.makeText(getApplicationContext(), "Exportando carteira '" + WalletController.getInstance(getBaseContext()).getWalletName() + "'...", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.jorge.brwallet", WalletController.getInstance(getBaseContext()).exportWallet(getBaseContext(),
                    getFilesDir().toString() + "/"+ WalletController.getInstance(getBaseContext()).getWalletName() +".txt"));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Compartilhar com..."));
        } else if (id == R.id.nav_delete_wallet) {
            try {
                WalletController.getInstance(getApplicationContext()).deleteWallet();
            } catch (DeleteWalletExceptionError d) {
                d.printStackTrace();
            }
            finish();
            Intent intent = new Intent(ManageWalletView.this, MainActivityView.class);
            startActivity(intent);
        } else if (id == R.id.nav_close_wallet) {
            Intent intent = new Intent(ManageWalletView.this, MainActivityView.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
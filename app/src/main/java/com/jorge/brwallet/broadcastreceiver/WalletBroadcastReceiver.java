package com.jorge.brwallet.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WalletBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if(Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())){
            Toast.makeText(context, "Mudou para o modo avião? Aproveite o tempo e faça suas contas!", Toast.LENGTH_SHORT).show();
        }

        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Toast.makeText(context, "Quer acessar internet? Antes, faça suas contas no BRWallet!", Toast.LENGTH_SHORT).show();

        }*/
    }
}

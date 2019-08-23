package com.jorge.brwallet.view;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.jorge.brwallet.exceptions.LoadWalletErrorException;
import com.jorge.brwallet.exceptions.SaveWalletErrorException;
import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;


public class CreateWalletView extends AppCompatActivity {
    private TextInputEditText editText;
    private Button button;

    public static String fileName = "";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_create_wallet);

            editText = findViewById(R.id.wallet_name);
            button = findViewById(R.id.confirm_creation);

            button.setOnClickListener(confirmCreationOnClickListener());
        }

        private View.OnClickListener confirmCreationOnClickListener() {
            return new View.OnClickListener() {
                public void onClick(View v) {
                    String name = editText.getText().toString();

                    try {
                        if(!WalletController.getInstance(getBaseContext()).saveWallet(name)) {
                            Toast.makeText(v.getContext(), "Carteira com o mesmo nome já criada.",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(v.getContext(), "Carteira" + name + " criada com sucesso!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ManageWalletView.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("RESULTADO", name);
                            startActivity(intent);
                            finish();
                        }
                    } catch (SaveWalletErrorException e) {
                        e.printStackTrace();
                    } catch (LoadWalletErrorException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
}
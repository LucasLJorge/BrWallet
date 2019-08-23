package com.jorge.brwallet.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;

import java.util.ArrayList;
import java.util.Locale;

public class AddLiabilityView extends AppCompatActivity {

    private TextInputEditText nomePassivo;
    private TextInputEditText custoPassivo;
    private TextInputEditText dataPassivo;
    private Calendar calendar;
    private DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_liability);

        checkPermission();

        nomePassivo = findViewById(R.id.liability_name);
        custoPassivo = findViewById(R.id.liability_cost);
        dataPassivo = findViewById(R.id.liability_date);
        dataPassivo.setEnabled(false);

        FloatingActionButton addPassivo = findViewById(R.id.addLiability);

        addPassivo.setOnClickListener(confirmCreationOnClickListener());

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final SpeechRecognizer nSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        final Intent nSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        nSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        nSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    nomePassivo.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.set_nomePassivo).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        nomePassivo.setText("");
                        nomePassivo.setHint("Ouvindo...");
                        break;
                }
                return false;
            }
        });

        nSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    custoPassivo.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.set_custoPassivo).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        nSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        nSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        custoPassivo.setText("");
                        custoPassivo.setHint("Ouvindo...");
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.set_dataPassivo).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int mon = calendar.get(Calendar.MONTH);
                int yea = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(AddLiabilityView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String string = dayOfMonth + "/" + (month+1) + "/" + year;
                        dataPassivo.setText(string);
                    }
                }, day, mon, yea);
                dpd.updateDate(2019, 0, 1);
                dpd.show();
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private View.OnClickListener confirmCreationOnClickListener() {
        return new View.OnClickListener() {
            public void onClick(View v) {
                //Realiza a máscara do BigDecimal para entrada no arquivo
                if(WalletController.getInstance(getBaseContext()).addLiability(getApplicationContext(), nomePassivo, custoPassivo, dataPassivo, "R$")){
                    Toast.makeText(getApplicationContext(), "Item " + nomePassivo.getText().toString() + " adicionado!",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), ManageWalletView.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Campo " + custoPassivo.getText().toString() + " em formato não válido. Preencha novamente.",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}

package com.jorge.brwallet.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jorge.brwallet.exceptions.DeleteWalletExceptionError;
import com.jorge.brwallet.exceptions.SaveWalletErrorException;
import com.jorge.brwallet.R;
import com.jorge.brwallet.controller.WalletController;
import com.jorge.brwallet.model.Asset;
import com.jorge.brwallet.model.Operation;

import java.util.ArrayList;
import java.util.Locale;

public class EditItemView extends AppCompatActivity {

    private TextInputEditText nomeItem;
    private TextInputEditText custoItem;
    private TextInputEditText dateItem;
    private ImageButton deleteItem;
    private TextView isPassivo;
    private Calendar calendar;
    private DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Operation item = new Operation(
                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getId(),
                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getName(),
                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getValue(),
                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getDate(),
                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getCurrency()
        );

        nomeItem = findViewById(R.id.item_name);
        custoItem = findViewById(R.id.item_cost);
        dateItem = findViewById(R.id.item_date);
        deleteItem = findViewById(R.id.deleteItem);

        isPassivo = findViewById(R.id.isPassivo);

        FloatingActionButton confirmEdit = findViewById(R.id.addItem);

        confirmEdit.setOnClickListener(confirmEditOnClickListener());
        deleteItem.setOnClickListener(confirmDeleteOnClickListener());

        nomeItem.setText(item.getName());
        custoItem.setText(item.getValue().toString());
        dateItem.setText(item.getDate());
        dateItem.setEnabled(false);

        isPassivo.setText("Passivo");
        if(getIntent().getSerializableExtra("RESULTADO") instanceof Asset){
            isPassivo.setText("Ativo");
        }

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
            public void onReadyForSpeech(Bundle bundle) {            }

            @Override
            public void onBeginningOfSpeech() {            }

            @Override
            public void onRmsChanged(float v) {            }

            @Override
            public void onBufferReceived(byte[] bytes) {           }

            @Override
            public void onEndOfSpeech() {            }

            @Override
            public void onError(int i) {            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    nomeItem.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {            }

            @Override
            public void onEvent(int i, Bundle bundle) {            }
        });

        findViewById(R.id.set_nomeItem).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        nomeItem.setText("");
                        nomeItem.setHint("Ouvindo...");
                        break;
                }
                return false;
            }
        });

        nSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {        }

            @Override
            public void onBeginningOfSpeech() {      }

            @Override
            public void onRmsChanged(float v) {       }

            @Override
            public void onBufferReceived(byte[] bytes) {       }

            @Override
            public void onEndOfSpeech() {         }

            @Override
            public void onError(int i) {         }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null) {
                    custoItem.setText(matches.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {         }

            @Override
            public void onEvent(int i, Bundle bundle) {         }
        });

        findViewById(R.id.set_custoItem).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        nSpeechRecognizer.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        nSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        custoItem.setText("");
                        custoItem.setHint("Ouvindo...");
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.set_dataItem).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int mon = calendar.get(Calendar.MONTH);
                int yea = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(EditItemView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String string = dayOfMonth + "/" + (month+1) + "/" + year;
                        dateItem.setText(string);
                    }
                }, day, mon, yea);
                dpd.updateDate(2019, 0, 1);
                dpd.show();
            }
        });
    }



    private View.OnClickListener confirmEditOnClickListener() {

        return new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if(getIntent().getSerializableExtra("RESULTADO") instanceof Asset){
                        WalletController.getInstance(getBaseContext()).editAsset(getBaseContext(),
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getId(),
                                nomeItem.getText().toString(),
                                custoItem,
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getDate(),
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getCurrency()
                        );
                    } else{
                        WalletController.getInstance(getBaseContext()).editLiability(getBaseContext(),
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getId(),
                                nomeItem.getText().toString(),
                                custoItem,
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getDate(),
                                ((Operation)getIntent().getSerializableExtra("RESULTADO")).getCurrency()
                        );

                    }

                    Intent intent = new Intent(v.getContext(), ManageWalletView.class);
                    startActivity(intent);
                    finish();

                }catch (NumberFormatException | SaveWalletErrorException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Campo " + custoItem.getText().toString() + " em formato não válido. Preencha novamente.",
                            Toast.LENGTH_LONG).show();
                    custoItem.setText("");
                }
            }
        };
    }

    private View.OnClickListener confirmDeleteOnClickListener() {

        return new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (getIntent().getSerializableExtra("RESULTADO") instanceof Asset) {
                        WalletController.getInstance(getBaseContext()).deleteAsset((int) (((Operation) getIntent().getSerializableExtra("RESULTADO")).getId()));
                    } else {
                        WalletController.getInstance(getBaseContext()).deleteLiability((int) (((Operation) getIntent().getSerializableExtra("RESULTADO")).getId()));
                    }

                    Intent intent = new Intent(v.getContext(), ManageWalletView.class);
                    startActivity(intent);
                    finish();
                }catch(DeleteWalletExceptionError e){
                    e.printStackTrace();
                }
            }
        };
    }
}

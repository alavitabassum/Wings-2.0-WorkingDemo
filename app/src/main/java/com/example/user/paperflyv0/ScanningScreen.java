package com.example.user.paperflyv0;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_ID;
import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_NAME;

public class ScanningScreen extends AppCompatActivity {

    BarcodeDbHelper db;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button done;
    private TextView merchant_name_textview;
    private TextView scan_count1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        // Pass value through intent
        Intent intent = getIntent();
        String merchant_name = intent.getStringExtra(MERCHANT_NAME);
        scan_count1 = (TextView) findViewById(R.id.scan_count);

        merchant_name_textview = (TextView) findViewById(R.id.merchant_name);
        merchant_name_textview.setText("Scan started for: " +merchant_name);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
        }

    private BarcodeCallback callback = new BarcodeCallback() {


        @Override
        public void barcodeResult(BarcodeResult result) {
            //Fetching email from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
            String user = username.toString();

            // current date and time
            final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            db = new BarcodeDbHelper(ScanningScreen.this);
            if(result.getText() == null || result.getText().equals(lastText)) {
                Toast.makeText(ScanningScreen.this, "Entry is null or already entered", Toast.LENGTH_SHORT).show();
                return;
            }

            lastText = result.getText();

            // get merchant id
            Intent intentID = getIntent();
            final String merchant_id = intentID.getStringExtra(MERCHANT_ID);

            barcodeView.setStatusText("Barcode"+result.getText());

            // TODO: add added-by, current-date , vaiia says to add flag in this table
            boolean state = true;
            String updated_by = user;
            String updated_at = currentDateTimeString;
            db.add(merchant_id, lastText, state, updated_by, updated_at);

            final int barcode_per_merchant_counts = db.getRowsCount(merchant_id);

            final String strI = String.valueOf(barcode_per_merchant_counts);
            Toast.makeText(ScanningScreen.this, "Merchant Id" +merchant_id + " Count:" + strI + " Successfull",  Toast.LENGTH_LONG).show();
            scan_count1.setText("Scan count: " +strI);

//          builder.setTitle(strI);
            db.close();

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            done = (Button) findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener(){

                @Override
                //On click function
                public void onClick(View view) {
                    // TODO: Merchant id, scan count, created-by, creation-date, flag
                    db.update_row(strI, merchant_id);
                    Intent intent = new Intent(view.getContext(), MyPickupList_Executive.class);
                    startActivity(intent);

                }
            });
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}

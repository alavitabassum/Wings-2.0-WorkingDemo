package com.example.user.paperflyv0;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_ID;
import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_NAME;

public class ScanningScreen extends AppCompatActivity {
    BarcodeDbHelper db;
    private static final String TAG = ScanningScreen.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button done;
    private TextView merchant_name1;
    private TextView scan_count1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        Intent intent = getIntent();

        String merchant_name = intent.getStringExtra(MERCHANT_NAME);
        scan_count1 = (TextView) findViewById(R.id.scan_count);

        done = (Button) findViewById(R.id.done);
        merchant_name1 = (TextView) findViewById(R.id.merchant_name);

        merchant_name1.setText("Scan started for: " +merchant_name);
//      merchant_name1.setText("Scan started for: " +merchant_id);

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

            db = new BarcodeDbHelper(ScanningScreen.this);
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            Intent intentID = getIntent();;
            String merchant_id = intentID.getStringExtra(MERCHANT_ID);

            db.add(merchant_id, lastText);

            barcodeView.setStatusText("Barcode"+result.getText());
//            barcodeView.setStatusText("Barcode" +merchant_id);
            int barcode_per_merchant_counts = db.getRowsCount(merchant_id);

            String strI = String.valueOf(barcode_per_merchant_counts);

            scan_count1.setText("Scan count: " +strI);
//          Toast.makeText(ScanningScreen.this, "Merchant Id" +merchant_id + " Count:" + strI + " Successfull",  Toast.LENGTH_LONG).show();

//          builder.setTitle(strI);
            db.close();

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
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

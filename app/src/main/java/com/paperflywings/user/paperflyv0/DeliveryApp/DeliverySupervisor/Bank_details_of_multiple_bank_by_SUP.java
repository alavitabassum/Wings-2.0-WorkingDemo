package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP.MultipleBankDepositeBySUP;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisorModel;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP.MultipleBankDepositeBySUP.PRIMARY_KEY;
import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP.MultipleBankDepositeBySUP.SERIAL_NO;
import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP.MultipleBankDepositeBySUP.TOTAL_C_AMT;


public class Bank_details_of_multiple_bank_by_SUP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Button UploadBn, selectBankId,selectDate1,selectDate2,selectDate3,selectDate4,selectDate5;
    private ImageView imgView1,imgView2,imgView3,imgView4,imgView5;
    private ProgressDialog progress;
    private Bitmap bitmap1,bitmap2,bitmap3,bitmap4,bitmap5;
    int totalSumission = 0;
    private final int IMG_REQUEST = 10;
    private final int IMG_REQUEST_MULTIPLE = 20;
    private final int IMG_REQUEST_TRIPLE = 30;
    private final int IMG_REQUEST_FOUR = 40;
    private final int IMG_REQUEST_FIVE = 50;
    private long mLastClickTime = 0;

    String item = "";
    String sqlPrimaryIds = "";
    private RequestQueue requestQueue;
    int count=0;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView total_Cash_collection;
    EditText deposite_slip_number1, deposite_slip_number2, deposite_slip_number3, deposite_slip_number4, deposite_slip_number5;
    EditText bank_deposite_comment1, bank_deposite_comment2, bank_deposite_comment3, bank_deposite_comment4, bank_deposite_comment5;

    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";


    private List<DeliveryCashReceiveSupervisorModel> bankList1;
    private List<DeliveryCashReceiveSupervisorModel> bankList2;
    private List<DeliveryCashReceiveSupervisorModel> bankList3;
    private List<DeliveryCashReceiveSupervisorModel> bankList4;
    private List<DeliveryCashReceiveSupervisorModel> bankList5;
    private List<DeliveryCashReceiveSupervisorModel> pointCodeList;

    BarcodeDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_multiple_bank_details_by_do);
        total_Cash_collection = (TextView) findViewById(R.id.total_cash_to_bank);

        Intent intentTotalCashAmt = getIntent();
        final String total_cash_collecction = intentTotalCashAmt.getStringExtra(TOTAL_C_AMT);
        final String serial_no = intentTotalCashAmt.getStringExtra(SERIAL_NO);
        final String primary_key = intentTotalCashAmt.getStringExtra(PRIMARY_KEY);
        total_Cash_collection.setText("Total Cash: " +total_cash_collecction+" Taka");

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        bankList1 = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList2 = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList3 = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList4 = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList5 = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        pointCodeList = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        bankList1.clear();
        bankList2.clear();
        bankList3.clear();
        bankList4.clear();
        bankList5.clear();
        pointCodeList.clear();

        getBankDetails();
        getPointCodes();

        UploadBn = (Button) findViewById(R.id.btn_upload);

        deposite_slip_number1 = (EditText) findViewById(R.id.deposite_slip_number1);
        deposite_slip_number2 = (EditText) findViewById(R.id.deposite_slip_number2);
        deposite_slip_number3 = (EditText) findViewById(R.id.deposite_slip_number3);
        deposite_slip_number4 = (EditText) findViewById(R.id.deposite_slip_number4);
        deposite_slip_number5 = (EditText) findViewById(R.id.deposite_slip_number5);

        bank_deposite_comment1 = (EditText) findViewById(R.id.bank_deposite_comment1);
        bank_deposite_comment2 = (EditText) findViewById(R.id.bank_deposite_comment2);
        bank_deposite_comment3 = (EditText) findViewById(R.id.bank_deposite_comment3);
        bank_deposite_comment4 = (EditText) findViewById(R.id.bank_deposite_comment4);
        bank_deposite_comment5 = (EditText) findViewById(R.id.bank_deposite_comment5);

        // image view
        imgView1 = (ImageView) findViewById(R.id.imageView1);
        imgView2 = (ImageView) findViewById(R.id.imageView2);
        imgView3 = (ImageView) findViewById(R.id.imageView3);
        imgView4 = (ImageView) findViewById(R.id.imageView4);
        imgView5 = (ImageView) findViewById(R.id.imageView5);

        // Bank List
        final Spinner mBankNameSpinner1 = (Spinner) findViewById(R.id.bank_name1);
        List<String> bankLists1 = new ArrayList<String>();
        bankLists1.add(0,"Select Bank...");
        for (int x1 = 0; x1 < bankList1.size(); x1++) {
            bankLists1.add(bankList1.get(x1).getBankName());
        }

        ArrayAdapter<String> adapterBankListR1 = new ArrayAdapter<String>(Bank_details_of_multiple_bank_by_SUP.this,
                android.R.layout.simple_spinner_item,
                bankLists1);
        adapterBankListR1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner1.setAdapter(adapterBankListR1);



        final Spinner mBankNameSpinner2 = (Spinner) findViewById(R.id.bank_name2);
        List<String> bankLists2 = new ArrayList<String>();
        bankLists2.add(0,"Select Bank...");
        for (int x2 = 0; x2 < bankList2.size(); x2++) {
            bankLists2.add(bankList2.get(x2).getBankName());
        }

        ArrayAdapter<String> adapterBankListR2 = new ArrayAdapter<String>(Bank_details_of_multiple_bank_by_SUP.this,
                android.R.layout.simple_spinner_item,
                bankLists2);
        adapterBankListR2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner2.setAdapter(adapterBankListR2);



        final Spinner mBankNameSpinner3 = (Spinner) findViewById(R.id.bank_name3);
        List<String> bankLists3 = new ArrayList<String>();
        bankLists3.add(0,"Select Bank...");
        for (int x3 = 0; x3 < bankList3.size(); x3++) {
            bankLists3.add(bankList3.get(x3).getBankName());
        }
        ArrayAdapter<String> adapterBankListR3 = new ArrayAdapter<String>(Bank_details_of_multiple_bank_by_SUP.this,
                android.R.layout.simple_spinner_item,
                bankLists3);
        adapterBankListR3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner3.setAdapter(adapterBankListR3);



        final Spinner mBankNameSpinner4 = (Spinner) findViewById(R.id.bank_name4);
        List<String> bankLists4 = new ArrayList<String>();
        bankLists4.add(0,"Select Bank...");
        for (int x4 = 0; x4 < bankList4.size(); x4++) {
            bankLists4.add(bankList4.get(x4).getBankName());
        }
        ArrayAdapter<String> adapterBankListR4 = new ArrayAdapter<String>(Bank_details_of_multiple_bank_by_SUP.this,
                android.R.layout.simple_spinner_item,
                bankLists4);
        adapterBankListR4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner4.setAdapter(adapterBankListR4);



        final Spinner mBankNameSpinner5 = (Spinner) findViewById(R.id.bank_name5);
        List<String> bankLists5 = new ArrayList<String>();
        bankLists5.add(0,"Select Bank...");
        for (int x5 = 0; x5 < bankList5.size(); x5++) {
            bankLists5.add(bankList5.get(x5).getBankName());
        }

        ArrayAdapter<String> adapterBankListR5 = new ArrayAdapter<String>(Bank_details_of_multiple_bank_by_SUP.this,
                android.R.layout.simple_spinner_item,
                bankLists5);
        adapterBankListR5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner5.setAdapter(adapterBankListR5);


        selectDate1 = findViewById(R.id.select_deposite_date1);
        selectDate2 = findViewById(R.id.select_deposite_date2);
        selectDate3 = findViewById(R.id.select_deposite_date3);
        selectDate4 = findViewById(R.id.select_deposite_date4);
        selectDate5 = findViewById(R.id.select_deposite_date5);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        final String currentDateTimeString = df.format(c);

        selectDate1.setText(currentDateTimeString);
        selectDate2.setText(currentDateTimeString);
        selectDate3.setText(currentDateTimeString);
        selectDate4.setText(currentDateTimeString);
        selectDate5.setText(currentDateTimeString);

        // select date
        selectDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Bank_details_of_multiple_bank_by_SUP.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate1.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        // select date
        selectDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Bank_details_of_multiple_bank_by_SUP.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate2.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        // select date
        selectDate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Bank_details_of_multiple_bank_by_SUP.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate3.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        // select date
        selectDate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Bank_details_of_multiple_bank_by_SUP.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate4.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        // select date
        selectDate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Bank_details_of_multiple_bank_by_SUP.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate5.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage2();
            }
        });
        imgView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage3();
            }
        });
        imgView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage4();
            }
        });
        imgView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage5();
            }
        });

        UploadBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Bank_details_of_multiple_bank_by_SUP.this);
                final View mView = getLayoutInflater().inflate(R.layout.bank_details_summary_multiple, null);

                final RelativeLayout view_holder1 = mView.findViewById(R.id.text_holder1);
                final RelativeLayout view_holder2 = mView.findViewById(R.id.text_holder2);
                final RelativeLayout view_holder3 = mView.findViewById(R.id.text_holder3);
                final RelativeLayout view_holder4 = mView.findViewById(R.id.text_holder4);
                final RelativeLayout view_holder5 = mView.findViewById(R.id.text_holder5);

                final TextView text1 = mView.findViewById(R.id.text1);
                final TextView text2 = mView.findViewById(R.id.text2);
                final TextView text3 = mView.findViewById(R.id.text3);
                final TextView text4 = mView.findViewById(R.id.text4);
                final TextView text5 = mView.findViewById(R.id.text5);

                final TextView date1 = mView.findViewById(R.id.date1);
                final TextView bankName1 = mView.findViewById(R.id.bankName1);
                final TextView depositeSlip1 = mView.findViewById(R.id.depositeSlip1);
                final TextView depositedAmount1 = mView.findViewById(R.id.depositedAmount1);

                final TextView date2 = mView.findViewById(R.id.date2);
                final TextView bankName2 = mView.findViewById(R.id.bankName2);
                final TextView depositeSlip2 = mView.findViewById(R.id.depositeSlip2);
                final TextView depositedAmount2 = mView.findViewById(R.id.depositedAmount2);

                final TextView date3 = mView.findViewById(R.id.date3);
                final TextView bankName3 = mView.findViewById(R.id.bankName3);
                final TextView depositeSlip3 = mView.findViewById(R.id.depositeSlip3);
                final TextView depositedAmount3 = mView.findViewById(R.id.depositedAmount3);

                final TextView date4 = mView.findViewById(R.id.date4);
                final TextView bankName4 = mView.findViewById(R.id.bankName4);
                final TextView depositeSlip4 = mView.findViewById(R.id.depositeSlip4);
                final TextView depositedAmount4 = mView.findViewById(R.id.depositedAmount4);

                final TextView date5 = mView.findViewById(R.id.date5);
                final TextView bankName5 = mView.findViewById(R.id.bankName5);
                final TextView depositeSlip5 = mView.findViewById(R.id.depositeSlip5);
                final TextView depositedAmount5 = mView.findViewById(R.id.depositedAmount5);

                final TextView total_cashAmt1 = mView.findViewById(R.id.total_cashAmt1);
                final TextView total_cashAmt2 = mView.findViewById(R.id.total_cashAmt2);
                final TextView error_message_display = mView.findViewById(R.id.error_msg_in_deposite);

                final String deposite_date1 = selectDate1.getText().toString();
                final String bankItems1 = mBankNameSpinner1.getSelectedItem().toString();
                final String slipNumber1 = deposite_slip_number1.getText().toString();
                final String comment1 = bank_deposite_comment1.getText().toString();

                final String deposite_date2 = selectDate2.getText().toString();
                final String bankItems2 = mBankNameSpinner2.getSelectedItem().toString();
                final String slipNumber2 = deposite_slip_number2.getText().toString();
                final String comment2 = bank_deposite_comment2.getText().toString();

                final String deposite_date3 = selectDate3.getText().toString();
                final String bankItems3 = mBankNameSpinner3.getSelectedItem().toString();
                final String slipNumber3 = deposite_slip_number3.getText().toString();
                final String comment3 = bank_deposite_comment3.getText().toString();

                final String deposite_date4 = selectDate4.getText().toString();
                final String bankItems4 = mBankNameSpinner4.getSelectedItem().toString();
                final String slipNumber4 = deposite_slip_number4.getText().toString();
                final String comment4 = bank_deposite_comment4.getText().toString();

                final String deposite_date5 = selectDate5.getText().toString();
                final String bankItems5 = mBankNameSpinner5.getSelectedItem().toString();
                final String slipNumber5 = deposite_slip_number5.getText().toString();
                final String comment5 = bank_deposite_comment5.getText().toString();

                int taka1, taka2, taka3, taka4,taka5;
                if(comment1.equals("")){
                    taka1 = 0;
                } else {
                    taka1 = Integer.parseInt(comment1);
                }

                if(comment2.equals("")){
                    taka2 = 0;
                } else {
                    taka2 = Integer.parseInt(comment2);
                }

                if(comment3.equals("")){
                    taka3 = 0;
                } else {
                    taka3 = Integer.parseInt(comment3);
                }

                if(comment4.equals("")){
                    taka4 = 0;
                } else {
                    taka4 = Integer.parseInt(comment4);
                }

                if(comment5.equals("")){
                    taka5 = 0;
                } else {
                    taka5 = Integer.parseInt(comment5);
                }

                totalSumission = taka1+taka2+taka3+taka4+taka5;

                if(!comment1.equals("") || !comment2.equals("") || !comment3.equals("") || !comment4.equals("") || !comment5.equals("")){
                    total_cashAmt1.setVisibility(View.VISIBLE);
                    total_cashAmt1.setText("Total Cash:"+total_cash_collecction+" Taka");
                    total_cashAmt2.setVisibility(View.VISIBLE);
                    total_cashAmt2.setText("Deposite Cash Amount: "+totalSumission+" Taka");
                } else {
                    total_cashAmt1.setVisibility(View.GONE);
                    total_cashAmt2.setVisibility(View.GONE);
                }

                if(!bankItems1.equals("Select Bank...") || !slipNumber1.equals("") || !comment1.equals("")){
                    view_holder1.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    date1.setText("Date: "+deposite_date1);
                    bankName1.setText("Bank Name: "+bankItems1);
                    depositeSlip1.setText("Slip No: "+slipNumber1);
                    depositedAmount1.setText("Deposite: "+comment1+ " Taka");
                }

                if(!bankItems2.equals("Select Bank...") || !slipNumber2.equals("") || !comment2.equals("")){
                    view_holder2.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    date2.setText("Date: "+deposite_date2);
                    bankName2.setText("Bank Name: "+bankItems2);
                    depositeSlip2.setText("Slip No: "+slipNumber2);
                    depositedAmount2.setText("Deposite: "+comment2+ "Taka");

                }

                if(!bankItems3.equals("Select Bank...") || !slipNumber3.equals("") || !comment3.equals("")){
                    view_holder3.setVisibility(View.VISIBLE);
                    text3.setVisibility(View.VISIBLE);
                    date3.setText("Date: "+deposite_date3);
                    bankName3.setText("Bank Name: "+bankItems3);
                    depositeSlip3.setText("Slip No: "+slipNumber3);
                    depositedAmount3.setText("Deposite: "+comment3+ "Taka");

                }

                if(!slipNumber4.equals("") || !comment4.equals("")){
                    view_holder4.setVisibility(View.VISIBLE);
                    text4.setVisibility(View.VISIBLE);
                    date4.setText("Date: "+deposite_date4);
                    bankName4.setText("Bank Name: "+bankItems4);
                    depositeSlip4.setText("Slip No: "+slipNumber4);
                    depositedAmount4.setText("Deposite: "+comment4+ "Taka");
                }

                if(!bankItems5.equals("Select Bank...") || !slipNumber5.equals("") || !comment5.equals("")){
                    view_holder5.setVisibility(View.VISIBLE);
                    text5.setVisibility(View.VISIBLE);
                    date5.setText("Date: "+deposite_date5);
                    bankName5.setText("Bank Name: "+bankItems5);
                    depositeSlip5.setText("Slip No: "+slipNumber5);
                    depositedAmount5.setText("Deposite: "+comment5+ "Taka");

                }

                alertDialogBuilder.setMessage("This is the Summary of your deposite Slip");
                alertDialogBuilder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                alertDialogBuilder.setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setView(mView);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // mis-clicking prevention, using threshold of 500 ms
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();

                        if(Integer.parseInt(total_cash_collecction) != totalSumission){
                            error_message_display.setText("Total Cash and Deposit Cash Amount Mismatch! Please Check correctly!!!");
                        } else {

                            insertBankDetails(primary_key,username,serial_no,deposite_date1,bankItems1,slipNumber1,comment1,
                                    deposite_date2,bankItems2,slipNumber2,comment2,deposite_date3,bankItems3,
                                    slipNumber3,comment3,deposite_date4,bankItems4,slipNumber4,comment4
                                    ,deposite_date5,bankItems5,slipNumber5,comment5);

                            totalSumission = 0;
                            //Toast.makeText(Bank_details_of_multiple_bank_by_DO.this, "Submit", Toast.LENGTH_SHORT).show();
                        }

                        /*String totalCashs = tv1.getText().toString().trim();
                        String CashComments = cashComment.getText().toString().trim();
                        String CashCollected = totalcashes.getText().toString().trim();

                        if(tv.getText().equals("0 Orders have been selected for cash.") || tv.getText().equals("Please select orders first") ){
                            orderIds.setText("Please Select Orders First!!");
                        }
                        else if(totalCashs.isEmpty()){
                            orderIds.setText("Please enter required fields First!!");
                        }
                        else if(CashComments.isEmpty()){
                            orderIds.setText("Please enter required fields First!!");
                        }
                        else if(CashCollected.isEmpty()){
                            orderIds.setText("Please enter required fields First!!");
                        }
                        else {
                            UpdateCashInfo(username,itemPrimaryIds,itemOrders,totalCashs,CashCollected,CashComments, "C");
                            alertDialog.dismiss();
                            startActivity(intent);
                            //loadRecyclerView(username);
                            itemOrders = "";
                            itemPrimaryIds = "";
                        }*/
                    }
                });
            }
        });

    }

    private void selectImage1(){
        startActivityForResult(Intent.createChooser(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), "Select one image"),
                IMG_REQUEST);
    }

    private void selectImage2(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 2nd image"),  IMG_REQUEST_MULTIPLE);
    }

    private void selectImage3(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 3rd image"),  IMG_REQUEST_TRIPLE);
    }

    private void selectImage4(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 4th image"),  IMG_REQUEST_FOUR);
    }

    private void selectImage5(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 5th image"),  IMG_REQUEST_FIVE);

    }


    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 400) ? (originalSize / 400) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = Bank_details_of_multiple_bank_by_SUP.this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path1 = data.getData();



            if(path1 != null){
//                try {
//                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),path1);
//                    //getResizedBitmap(bitmap, 100,100);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    bitmap1 = getThumbnail(path1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgView1.setImageBitmap(bitmap1);

            } else {
                bitmap1 = null;
            }
        } else if(requestCode == IMG_REQUEST_MULTIPLE && resultCode == RESULT_OK){

            Uri path2 = data.getData();

            if(path2 != null){
//                try {
//                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),path2);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                try {
                    bitmap2 = getThumbnail(path2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView2.setImageBitmap(bitmap2);
            } else {
                bitmap2 = null;
            }
        } else if(requestCode == IMG_REQUEST_TRIPLE && resultCode == RESULT_OK) {

            Uri path3 = data.getData();

            if(path3 != null){
//                try {
//                    bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(),path3);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                try {
                    bitmap3 = getThumbnail(path3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView3.setImageBitmap(bitmap3);

            } else {
                bitmap3 = null;
            }
        } else if(requestCode == IMG_REQUEST_FOUR && resultCode == RESULT_OK){

            Uri path4 = data.getData();

            if(path4 != null){
//                try {
//                    bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(),path4);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                try {
                    bitmap4 = getThumbnail(path4);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView4.setImageBitmap(bitmap4);
            } else {
                bitmap4 = null;
            }
        } else if(requestCode == IMG_REQUEST_FIVE && resultCode == RESULT_OK) {

            Uri path5 = data.getData();

            if(path5 != null){
//                try {
//                    bitmap5 = MediaStore.Images.Media.getBitmap(getContentResolver(),path5);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                try {
                    bitmap5 = getThumbnail(path5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView5.setImageBitmap(bitmap5);

            } else {
                bitmap5 = null;
            }
        }

    }

    private String imageToString(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return "";
        }
    }

    private void getBankDetails() {
        try {
            bankList1.clear();
            bankList2.clear();
            bankList3.clear();
            bankList4.clear();
            bankList5.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_bank_details(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer bankId = c.getInt(0);
                String bankName = c.getString(1);
                DeliveryCashReceiveSupervisorModel bankDetails = new DeliveryCashReceiveSupervisorModel(bankId,bankName);
                bankList1.add(bankDetails);
                bankList2.add(bankDetails);
                bankList3.add(bankDetails);
                bankList4.add(bankDetails);
                bankList5.add(bankDetails);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPointCodes() {
        try {
            pointCodeList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_pointCodes(sqLiteDatabase);
            while (c.moveToNext()) {
                String pointCode = c.getString(0);
                DeliveryCashReceiveSupervisorModel pointCodes = new DeliveryCashReceiveSupervisorModel(pointCode);
                pointCodeList.add(pointCodes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(Bank_details_of_multiple_bank_by_SUP.this, MultipleBankDepositeBySUP.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bank_details_upload_supervisor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent homeIntent = new Intent(Bank_details_of_multiple_bank_by_SUP.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                         /* SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);
*/
                            //Getting out sharedpreferences
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Config.EMAIL_SHARED_PREF, "");

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(Bank_details_of_multiple_bank_by_SUP.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            //Showing the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void insertBankDetails(final String sqlPrimaryId,final String username, final String serial_no,
                                   final String depositeDate1, final String bankName1, final String slipNumber1, final String depositeAmt1,
                                   final String depositeDate2, final String bankName2, final String slipNumber2, final String depositeAmt2,
                                   final String depositeDate3, final String bankName3, final String slipNumber3, final String depositeAmt3,
                                   final String depositeDate4, final String bankName4, final String slipNumber4, final String depositeAmt4,
                                   final String depositeDate5, final String bankName5, final String slipNumber5, final String depositeAmt5) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                imgView1.setImageResource(0);
                                imgView2.setImageResource(0);
                                imgView3.setImageResource(0);
                                imgView4.setImageResource(0);
                                imgView5.setImageResource(0);
                                imgView1.setVisibility(View.GONE);
                                imgView2.setVisibility(View.GONE);
                                imgView3.setVisibility(View.GONE);
                                imgView4.setVisibility(View.GONE);
                                imgView5.setVisibility(View.GONE);

                                total_Cash_collection.setText("0 Taka");

                                Intent intent = new Intent(Bank_details_of_multiple_bank_by_SUP.this, MultipleBankDepositeBySUP.class);
                                Toast.makeText(Bank_details_of_multiple_bank_by_SUP.this, "Successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(Bank_details_of_multiple_bank_by_SUP.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Bank_details_of_multiple_bank_by_SUP.this, "Server disconnected!"+error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                String img1 = imageToString(bitmap1);
                String img2 = imageToString(bitmap2);
                String img3 = imageToString(bitmap3);
                String img4 = imageToString(bitmap4);
                String img5 = imageToString(bitmap5);

                params.put("primary_key", sqlPrimaryId);
                params.put("serial_no", serial_no);
                params.put("username",username);

                params.put("depositDate1", depositeDate1);
                params.put("bankID1",bankName1);
                params.put("depositSlip1",slipNumber1);
                params.put("depositAmt1", depositeAmt1);
                params.put("image1",img1);

                params.put("depositDate2", depositeDate2);
                params.put("bankID2",bankName2);
                params.put("depositSlip2",slipNumber2);
                params.put("depositAmt2", depositeAmt2);
                params.put("image2",img2);

                params.put("depositDate3", depositeDate3);
                params.put("bankID3",bankName3);
                params.put("depositSlip3",slipNumber3);
                params.put("depositAmt3", depositeAmt3);
                params.put("image3",img3);

                params.put("depositDate4", depositeDate4);
                params.put("bankID4",bankName4);
                params.put("depositSlip4",slipNumber4);
                params.put("depositAmt4", depositeAmt4);
                params.put("image4",img4);

                params.put("depositDate5", depositeDate5);
                params.put("bankID5",bankName5);
                params.put("depositSlip5",slipNumber5);
                params.put("depositAmt5", depositeAmt5);
                params.put("image5",img5);

                params.put("flagreq", "Delivery_complete_bank_deposite_slip_by_SUP");

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {

            Toast.makeText(Bank_details_of_multiple_bank_by_SUP.this, "Server Error", Toast.LENGTH_LONG).show();
        }
    }

}



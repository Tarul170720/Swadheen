package com.example.swadheen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.Map;

public class Payment extends AppCompatActivity {


    private static String Workerid="",upid="";
    EditText amountEt, noteEt, nameEt, upiIdEt;
        Button send;



        final int UPI_PAYMENT = 0;
    private static Map<String, Object> map;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_payment);

            initializeViews();
            Intent intent=getIntent();
            Workerid=intent.getStringExtra("WorkerId");
        getWorkerDetatils();
        getpaymntDetatils();
        int amout=getPay();







        send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Getting the values from the EditTexts

                    String amount = ""+amout;

                    String note = noteEt.getText().toString();
                    String name = nameEt.getText().toString();
                    String upiId = upiIdEt.getText().toString();
                    payUsingUpi(amount, upiId, name, note);
                }
            });
        }

        void initializeViews() {
            send = findViewById(R.id.send);
            amountEt = findViewById(R.id.amount_et);
            noteEt = findViewById(R.id.note);
            nameEt = findViewById(R.id.name);
            upiIdEt = findViewById(R.id.upi_id);
        }

        void payUsingUpi(String amount, String upiId, String name, String note) {

            Uri uri = Uri.parse("upi://pay").buildUpon()
                    .appendQueryParameter("pa", upiId)
                    .appendQueryParameter("pn", name)
                    .appendQueryParameter("tn", note)
                    .appendQueryParameter("am", amount)
                    .appendQueryParameter("cu", "INR")
                    .build();


            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);

            // will always show a dialog to user to choose an app
            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

            // check if intent resolves
            if(null != chooser.resolveActivity(getPackageManager())) {
                startActivityForResult(chooser, UPI_PAYMENT);
            } else {
                Toast.makeText(Payment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case UPI_PAYMENT:
                    if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                        if (data != null) {
                            String trxt = data.getStringExtra("response");
                            Log.d("UPI", "onActivityResult: " + trxt);
                            ArrayList<String> dataList = new ArrayList<>();
                            dataList.add(trxt);
                            upiPaymentDataOperation(dataList);
                        } else {
                            Log.d("UPI", "onActivityResult: " + "Return data is null");
                            ArrayList<String> dataList = new ArrayList<>();
                            dataList.add("nothing");
                            upiPaymentDataOperation(dataList);
                        }
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                    break;
            }
        }

        private void upiPaymentDataOperation(ArrayList<String> data) {
            if (isConnectionAvailable(Payment.this)) {
                String str = data.get(0);
                Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
                String paymentCancel = "";
                if(str == null) str = "discard";
                String status = "";
                String approvalRefNo = "";
                String response[] = str.split("&");
                for (int i = 0; i < response.length; i++) {
                    String equalStr[] = response[i].split("=");
                    if(equalStr.length >= 2) {
                        if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                            status = equalStr[1].toLowerCase();
                        }
                        else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                            approvalRefNo = equalStr[1];
                        }
                    }
                    else {
                        paymentCancel = "Payment cancelled by user.";
                    }
                }

                if (status.equals("success")) {
                    //Code to handle successful transaction here.

                    Toast.makeText(Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                    Log.d("UPI", "responseStr: "+approvalRefNo);
                    Intent intent=new Intent(Payment.this,UserMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if("Payment cancelled by user.".equals(paymentCancel)) {
                    Toast.makeText(Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
            }
        }

        public static boolean isConnectionAvailable(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnected()
                        && netInfo.isConnectedOrConnecting()
                        && netInfo.isAvailable()) {
                    return true;
                }
            }
            return false;
        }
    
        public static void getWorkerDetatils(){
            DatabaseReference mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(Workerid).child("Sub Category");
            mWorkerDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        map=(Map<String, Object>)snapshot.getValue();

                        for (Map.Entry<String,Object> entry : map.entrySet()){
                            System.out.println("Key = " + entry.getKey() +
                                    ", Value = " + entry.getValue());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        ArrayList<String> s=new ArrayList<>();
    public void getpaymntDetatils(){
        DatabaseReference mWorkerDatabase= FirebaseDatabase.getInstance().getReference().child("Users_Info").child("Workers").child(Workerid).child("costumerRequest");
        mWorkerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        Map<String, Object> map1=(Map<String, Object>)ds.getValue();
                        if(map1.get("subCategoryName")!=null){
                            s.add(map1.get("subCategoryName").toString());

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private int getPay(){
        int sum=0;
        for(int i=0;i<s.size();i++){
            int rate=(Integer)map.get(s.get(i));

            sum=sum+rate;
        }
        return sum;
    }


}
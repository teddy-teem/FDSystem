package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.itangqi.waveloadingview.WaveLoadingView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class MainTask extends AppCompatActivity {
    TextView info1, info2, info3, info4, info5, info6, info7, info8, info9, info10, info11;
    DatabaseReference myRef,ref;
    String currentDateTimeString;
    public String dbLol,dbunitT,dbunitH,dbSub;
    double dbwater_level,dbhum,dbtemp,mxlevel,mxhumi,mxtemp,mnhumi,mntemp;
    int dbRainA,dbRainD ;
    String temp, dbtime, dbRiver;
    WaveLoadingView mWaveLoadingView;
    TextView t1,t2,t3,t4,Debug;
    SpannableString HRain = new SpannableString("Heavvy Raining");
    SpannableString NRain = new SpannableString("Normal Raining");
    SpannableString Rain = new SpannableString("Raining");
    SpannableString LRain = new SpannableString("Low Raining");
    SpannableString NoRain = new SpannableString("Not Raining");
    ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
    ForegroundColorSpan fcsGray = new ForegroundColorSpan(Color.DKGRAY);
    ForegroundColorSpan fcsYello = new ForegroundColorSpan(Color.YELLOW);
    ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.BLUE);
    ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout linearLayoutBSheet;
    private ToggleButton tbUpDown;
    private TextView txtCantante, txtCancion;
    long backpretime;
    RecyclerView rv;
    FirebaseDatabase database;
    ArrayList<bData> list;
    SearchView srView;
    bAdapterClass BAdapterClass;
    private bAdapterClass.BAdapterClickListner listner;
    String amI="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);

        amI = getIntent().getExtras().getString("amIadmin","0");

        init();
        //SwipLayout();
        tbUpDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    tbUpDown.setChecked(true);
                }else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    tbUpDown.setChecked(false);
                }
            }

            @Override
            public void onSlide(View view, float v) {

            }
        });

        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        if (amI.equals("1")){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    dbLol = dataSnapshot.child("admin").child("area").getValue(String.class).toString();
                    dbunitT = dataSnapshot.child("admin").child("unitT").getValue(String.class).toString();
                    dbunitH = dataSnapshot.child("admin").child("unitH").getValue(String.class).toString();
                    dbSub = dataSnapshot.child("admin").child("subArea").getValue(String.class).toString();
                    txtCantante.setText(dbLol);
                    txtCancion.setText(dbSub);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dbLol = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("area").getValue(String.class).toString();
                    dbunitT = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("unitT").getValue(String.class).toString();
                    dbunitH = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("unitH").getValue(String.class).toString();
                    dbSub = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subArea").getValue(String.class).toString();
                    txtCantante.setText(dbLol);
                    txtCancion.setText(dbSub);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        myRef = FirebaseDatabase.getInstance().getReference().child("Readings");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            try {
                Double dwl, dh, dt, dmxl, dmxh, dmxt, dmnh, dmnt;
                Integer drainA, drainD;
                dwl =  dataSnapshot.child(dbSub).child("Level").getValue(Double.class);
                drainA = dataSnapshot.child(dbSub).child("RainA").getValue(Integer.class);
                drainD = dataSnapshot.child(dbSub).child("RainD").getValue(Integer.class);
                dh = dataSnapshot.child(dbSub).child("Humidity").getValue(Double.class);
                dt = dataSnapshot.child(dbSub).child("Temp").getValue(Double.class);
                dmxl = dataSnapshot.child(dbSub).child("MaxHeight").getValue(Double.class);
                dmxh = dataSnapshot.child(dbSub).child("MaxHumidity").getValue(Double.class);
                dmxt = dataSnapshot.child(dbSub).child("MaxTemp").getValue(Double.class);
                dmnh = dataSnapshot.child(dbSub).child("MinHumidity").getValue(Double.class);
                dmnt = dataSnapshot.child(dbSub).child("MinTemp").getValue(Double.class);
                dbtime= dataSnapshot.child(dbSub).child("time").getValue(String.class);
                dbRiver= dataSnapshot.child(dbSub).child("river").getValue(String.class);

                dbwater_level = dwl.doubleValue();
                dbhum = dh.doubleValue();
                dbtemp = dt.doubleValue();
                mxlevel = dmxl.doubleValue();
                mxhumi = dmxh.doubleValue();
                mxtemp = dmxt.doubleValue();
                mnhumi = dmnh.doubleValue();
                mntemp = dmnt.doubleValue();
                dbRainA = drainA.intValue();
                dbRainD = drainD.intValue();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
            }

              //  String s = String.valueOf(currentDateTimeString.charAt(17));

                SetRiverLevel();
                SetHumiTemp();
                SetRainMessage();
                SetReadings();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void SetRiverLevel(){
        double mxH = mxlevel;
        String prntLevel;

        double progress = (dbwater_level/mxH)*100;
        int x = (int)(progress);
        if(x>100)
            x=100;
        mWaveLoadingView.setProgressValue(x);

        if (dbunitH.equals("Inch")){
            dbwater_level = dbwater_level/ 2.54;
            dbwater_level= Math.round(dbwater_level*100.0)/100.0;
            prntLevel=String.valueOf(dbwater_level)+" Inch";
        }
        else if(dbunitH.equals("Foot")){
            dbwater_level = dbwater_level/30.48;
            dbwater_level= Math.round(dbwater_level*100.0)/100.0;
            prntLevel=String.valueOf(dbwater_level)+" Foot";
        }
        else if(dbunitH.equals("M")){
            dbwater_level = dbwater_level/100;
            dbwater_level = Math.round(dbwater_level*100.0)/100.0;
            prntLevel=String.valueOf(dbwater_level)+" Meter";
        }
        else{
            dbwater_level = Math.round(dbwater_level*100.0)/100.0;
            prntLevel=String.valueOf(dbwater_level)+" Cm";
        }
        mWaveLoadingView.setCenterTitle(prntLevel);
        mWaveLoadingView.setTopTitle("");
        if (x>=100)
            mWaveLoadingView.setBottomTitle("OverFlow");
        else
            mWaveLoadingView.setBottomTitle("");
        mWaveLoadingView.setWaveColor(Color.parseColor("#FF0D8FD5"));
    }
    public  void SetHumiTemp(){
        if (dbunitT.equals("°F")){
            double Temp = dbtemp;
            Temp=(Temp*(9.00/5.00))+32;
            Temp = Math.round(Temp*100.0)/100.0;
            temp = String.valueOf(Temp)+" °F";
        }
        else if(dbunitT.equals("°K")){
            double Temp = dbtemp;
            Temp=Temp + 273.15;
            Temp = Math.round(Temp*100.0)/100.0;
            temp = String.valueOf(Temp)+" °K";
        }
        else {
            temp = dbtemp+" °C";
        }
        t1.setText(temp);
        t2.setText(String.valueOf(dbhum) +" %");
    }
    public void SetRainMessage(){
        int  a = dbRainA;
        if(a<=20){
            t3.setText(NoRain);
        }
        else if(a>20 && a<=30){
            t3.setText(LRain);
        }
        else if(a>30 && a<=50){
            t3.setText(NRain);
        }
        else if(a>50 && a<=60){
            t3.setText(Rain);
        }
        else if(a>60){
            t3.setText(HRain);
        }
    }
    public void SetReadings(){

        if(dbunitH.equals("M")){
            dbwater_level = dbwater_level/100;
            mxlevel = mxlevel/100;
            dbwater_level = Math.round(dbwater_level*100.0)/100.0;
            mxlevel= Math.round(mxlevel*100.0)/100.0;
            info3.setText(String.valueOf(mxlevel)+"M");
            info4.setText(String.valueOf(dbwater_level)+"M");
        }
        else if(dbunitH.equals("Foot")){
            dbwater_level = dbwater_level/30.48;
            mxlevel = mxlevel/30.48;
            dbwater_level= Math.round(dbwater_level*100.0)/100.0;
            mxlevel= Math.round(mxlevel*100.0)/100.0;
            info3.setText(String.valueOf(mxlevel)+"Feet");
            info4.setText(String.valueOf(dbwater_level)+"Feet");
        }
        else if(dbunitH.equals("Inch")){
            dbwater_level = dbwater_level/ 2.54;
            mxlevel = mxlevel/ 2.54;
            dbwater_level= Math.round(dbwater_level*100.0)/100.0;
            mxlevel= Math.round(mxlevel*100.0)/100.0;
            info3.setText(String.valueOf(mxlevel)+"Inchi");
            info4.setText(String.valueOf(dbwater_level)+"Inchi");
        }
        else{
            info3.setText(String.valueOf(mxlevel)+"CM");
            info4.setText(String.valueOf(dbwater_level)+"CM");
        }


        if(dbunitT.equals("°K")) {
            mxtemp = (mxtemp + 273.15);
            mxtemp = Math.round(mxtemp * 100.0) / 100.0;
            mntemp = (mntemp + 273.15);
            mntemp = Math.round(mntemp * 100.0) / 100.0;
            info7.setText(String.valueOf(mxtemp)+"°K");
            info8.setText(String.valueOf(mntemp)+"°K");
        }
        else if(dbunitT.equals("°F")){
            mxtemp = (mxtemp * (9.00 / 5.00)) + 32;
            mxtemp = Math.round(mxtemp * 100.0) / 100.0;
            mntemp = (mntemp * (9.00 / 5.00)) + 32;
            mntemp = Math.round(mntemp * 100.0) / 100.0;
            info7.setText(String.valueOf(mxtemp)+"°F");
            info8.setText(String.valueOf(mntemp)+"°F");
        }
        else {
            info7.setText(String.valueOf(mxtemp)+"°C");
            info8.setText(String.valueOf(mntemp)+"°C");
        }
        info1.setText(dbSub);
        info2.setText(dbRiver);
        info5.setText(dbLol);
        info6.setText(String.valueOf(dbRainA) + "%");

        info9.setText(String.valueOf(mxhumi));
        info10.setText(String.valueOf(mnhumi));
        info11.setText(String.valueOf(dbtime));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_1,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.preference){

            if (amI.equals("1")){
                Intent intent = new Intent(MainTask.this,Preference.class);
                intent.putExtra("iAmAdmin", "1");
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(MainTask.this,Preference.class);
                intent.putExtra("iAmAdmin", "0");
                startActivity(intent);
            }

        }
        if (item.getItemId() == R.id.dlog){
            if (amI.equals("1")){
                Intent intent = new Intent(MainTask.this,LogTable.class);
                intent.putExtra("amIadmin", "1");
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(MainTask.this,LogTable.class);
                intent.putExtra("amIadmin", "0");
                startActivity(intent);
            }

        }
        if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainTask.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.help){
            Intent intent = new Intent(MainTask.this,Help.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.about){
            Intent intent = new Intent(MainTask.this,About.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.user_menu){

            if (amI.equals("1")){
                Toast.makeText(getApplicationContext(),"You are Admin", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(MainTask.this,WelcomeProfile.class);
                startActivity(intent);
            }

        }

        return super.onOptionsItemSelected(item);
    }
    private void init() {
        info1 = findViewById(R.id.info1);
        info2 = findViewById(R.id.info2);
        info3 = findViewById(R.id.info3);
        info4 = findViewById(R.id.info4);
        info5 = findViewById(R.id.info5);
        info6 = findViewById(R.id.info6);
        info7 = findViewById(R.id.info7);
        info8 = findViewById(R.id.info8);
        info9 = findViewById(R.id.info9);
        info10 = findViewById(R.id.info10);
        info11 = findViewById(R.id.info11);

        this.linearLayoutBSheet = findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBSheet);
        this.tbUpDown = findViewById(R.id.toggleButton);
        this.txtCantante = findViewById(R.id.txtCantante);
        this.txtCancion = findViewById(R.id.txtCancion);
        mWaveLoadingView = (WaveLoadingView)findViewById(R.id.waveload);
        t1 = (TextView)findViewById(R.id.textView1);
        t2 = (TextView)findViewById(R.id.textView2);
        t3 = (TextView)findViewById(R.id.textView3);
        // searchArea = (EditText)findViewById(R.id.editSearch);
        HRain.setSpan(fcsRed, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Rain.setSpan(fcsYello, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        NRain.setSpan(fcsBlue, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        LRain.setSpan(fcsGray, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        NoRain.setSpan(fcsGreen, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        rv = (RecyclerView)findViewById(R.id.brecyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Readings");
        srView = (SearchView) findViewById(R.id.srchv);
        setOnClickListner();
        //Debug = findViewById(R.id.debug);
    }
    private void setOnClickListner() {
        listner = new bAdapterClass.BAdapterClickListner() {
            @Override
            public void onClick(View v, int position) {
                myRef = FirebaseDatabase.getInstance().getReference().child("Users");
                if (amI.equals("1")){
                    myRef.child("admin").child("area").setValue(list.get(position).getDeviceArea());
                    myRef.child("admin").child("subArea").setValue(list.get(position).getDeviceID());
                }
                else{

                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("area").setValue(list.get(position).getDeviceArea());
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("subArea").setValue(list.get(position).getDeviceID());
                }
                Intent intent = getIntent();
                intent.putExtra("amIadmin", amI);
                finish();
                startActivity(intent);
            }
        };
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        bData B=new bData();
                        list = new ArrayList<>();
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            B = ds.getValue(bData.class);
                            if (dbunitH.equals("Inch")){
                                Double x = Double.valueOf(B.Level);
                                x=x/2.54;
                                x = Math.round(x*100.0)/100.0;
                                B.Level=x;
                                B.Unit_h ="I";
                                list.add(B);
                            }


                            else if (dbunitH.equals("Foot")){
                                Double x = Double.valueOf(B.Level);
                                x=x/30.48;
                                x = Math.round(x*100.0)/100.0;
                                B.Level=x;
                                B.Unit_h ="F";
                                list.add(B);
                            }
                            else if (dbunitH.equals("M")){
                                Double x = Double.valueOf(B.Level);
                                x=x/100.00;
                                x = Math.round(x*100.0)/100.0;
                                B.Level=x;
                                B.Unit_h ="M";
                                list.add(B);
                            }
                            else{
                               B.Unit_h="CM";
                                B.Level = Math.round(B.Level*100.0)/100.0;
                                list.add(B);
                            }
                        }
                        BAdapterClass = new bAdapterClass(list, listner);
                        rv.setAdapter(BAdapterClass);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainTask.this, "databaseError", Toast.LENGTH_SHORT).show();
                }
            });
        }

        srView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                BAdapterClass.getFilter().filter(s);
                return true;
            }
        });
    }
   /* @Override
    public void onBackPressed() {
        if (backpretime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpretime = System.currentTimeMillis();

    }*/
    /*public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }*/
    /*@Override
    public void onBackPressed() {
        if (backpretime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit",
                    Toast.LENGTH_SHORT).show();
            backpretime = System.currentTimeMillis();
        }
    }*/

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                }).create().show();

    }
}
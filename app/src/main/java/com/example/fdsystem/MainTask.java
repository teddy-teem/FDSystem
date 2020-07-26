package com.example.fdsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.itangqi.waveloadingview.WaveLoadingView;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainTask extends AppCompatActivity {
    DatabaseReference myRef,ref;
    String dbwater_level,dbRainD, dbRainA,dbhum,dbtemp,dbLol,dbunitT,dbunitH,dbSub, mxlevel;
    String water_level,temp;
    String FiArea,SubArea;
    String[] Location;
    ArrayList<String> lista = new ArrayList<>();
    ArrayAdapter<String> adapter;
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
    private ListView listView;
    private TextView txtCantante, txtCancion;
    private ContentLoadingProgressBar progbar;
    long backpretime;
    EditText searchArea;
    SwipeRefreshLayout refreshLayout;
    RecyclerView rv;
    FirebaseDatabase database;
    ArrayList<bData> list;
    SearchView srView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task);
        findViewById(R.id.buttondata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTask.this, LogTable.class);
                startActivity(intent);
                finish();
            }
        });
        init();
        SwipLayout();
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

        myRef = FirebaseDatabase.getInstance().getReference().child("Select");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dbLol = dataSnapshot.child("Area").getValue(String.class).toString();
                dbunitT = dataSnapshot.child("unitTemp").getValue(String.class).toString();
                dbunitH = dataSnapshot.child("unitHeight").getValue(String.class).toString();
                dbSub = dataSnapshot.child("SubArea").getValue(String.class).toString();
                txtCantante.setText(dbLol);
                txtCancion.setText(dbSub);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Toast.makeText(getApplicationContext(), "Database Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference().child("Readings");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                dbwater_level = dataSnapshot.child(dbLol).child("Level").getValue(String.class).toString();
                dbRainA = dataSnapshot.child(dbLol).child("RainA").getValue(String.class).toString();
                dbRainD = dataSnapshot.child(dbLol).child("RainD").getValue(String.class).toString();
                dbhum = dataSnapshot.child(dbLol).child("Humidity").getValue(String.class).toString();
                dbtemp = dataSnapshot.child(dbLol).child("Temp").getValue(String.class).toString();
                mxlevel = dataSnapshot.child(dbLol).child("maxHeight").getValue(String.class).toString();
                //Debug.setText(mxlevel);
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
        double maxH=Double.parseDouble(mxlevel);
        double level = Double.parseDouble(dbwater_level);
        double waterLevel = maxH-level;
        if (waterLevel<0)
            waterLevel=0;
        double progress = (waterLevel/maxH)*100;
        int x = (int)progress;
        if(x>100)
            x=100;
        mWaveLoadingView.setProgressValue(x);

        if (dbunitH.equals("Inch")){
            waterLevel = waterLevel/2.54;
            waterLevel = Math.round(waterLevel*100.0)/100.0;
            water_level=String.valueOf(waterLevel)+" Inch";
        }
        else if(dbunitH.equals("foot")){
            waterLevel = waterLevel/30.48;
            waterLevel = Math.round(waterLevel*100.0)/100.0;
            water_level=String.valueOf(waterLevel)+" Foot";
        }
        else if(dbunitH.equals("M")){
            waterLevel = waterLevel/100;
            waterLevel = Math.round(waterLevel*100.0)/100.0;
            water_level=String.valueOf(waterLevel)+" Metre";
        }
        else{
            waterLevel = Math.round(waterLevel*100.0)/100.0;
            water_level=String.valueOf(waterLevel)+" Cm";
        }
        mWaveLoadingView.setCenterTitle(water_level);
        mWaveLoadingView.setTopTitle("");
        if (x>=100)
            mWaveLoadingView.setBottomTitle("OverFlow");
        else
            mWaveLoadingView.setBottomTitle("");
        mWaveLoadingView.setWaveColor(Color.parseColor("#FF0D8FD5"));
    }
    public  void SetHumiTemp(){
        if (dbunitT.equals("°F")){
            double Temp = Double.parseDouble(dbtemp);
            Temp=(Temp*(9.00/5.00))+32;
            Temp = Math.round(Temp*100.0)/100.0;
            temp = String.valueOf(Temp)+" °F";
        }
        else if(dbunitT.equals("°K")){
            double Temp = Double.parseDouble(dbtemp);
            Temp=Temp + 273.15;
            Temp = Math.round(Temp*100.0)/100.0;
            temp = String.valueOf(Temp)+" °K";
        }
        else {
            temp = dbtemp+" °C";
        }
        t1.setText(temp);
        t2.setText(dbhum+" %");
    }
    public void SetRainMessage(){
        int a = Integer.parseInt(dbRainA);
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
        String Details = "\n\n\n Device Info                 : Flood Detecting System\n HC-SR04 Sensor        : "+water_level+"\n Rain Sensor(Analog) : " + dbRainA +
                "\n Rain Sensor(Digital)  : "+dbRainD+"\n Max Temperature      : 50°C\n Min Temperature       : 10°C\n Max Humidity             : 95%\n Min Humidity              : 45%\n Location                       :"+dbLol ;
        t4.setText(Details);
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
            Intent intent = new Intent(MainTask.this,Preference.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        this.linearLayoutBSheet = findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBSheet);
        this.tbUpDown = findViewById(R.id.toggleButton);
        this.txtCantante = findViewById(R.id.txtCantante);
        this.txtCancion = findViewById(R.id.txtCancion);
        this.progbar = findViewById(R.id.progbar);
        mWaveLoadingView = (WaveLoadingView)findViewById(R.id.waveload);
        t1 = (TextView)findViewById(R.id.textView1);
        t2 = (TextView)findViewById(R.id.textView2);
        t3 = (TextView)findViewById(R.id.textView3);
        t4 = (TextView)findViewById(R.id.textView);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.swip);
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
        //Debug = findViewById(R.id.debug);
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
                        list = new ArrayList<>();
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            list.add(ds.getValue(bData.class));
                        }
                        bAdapterClass BAdapterClass = new bAdapterClass(list);
                        rv.setAdapter(BAdapterClass);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainTask.this, "databaseError.getMessage().toString()", Toast.LENGTH_SHORT).show();
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
                ArrayList<bData> mylist = new ArrayList<>();
                for (bData ld: list){
                    if (ld.getDeviceID().toLowerCase().contains(s.toLowerCase())){
                        mylist.add(ld);
                    }
                }
                bAdapterClass BAdapter = new bAdapterClass(mylist);
                rv.setAdapter(BAdapter);
                return true;
            }
        });

    }

    private void SwipLayout(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backpretime+2000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpretime = System.currentTimeMillis();

    }

}
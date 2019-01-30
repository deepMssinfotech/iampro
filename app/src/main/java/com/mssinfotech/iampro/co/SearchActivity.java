package com.mssinfotech.iampro.co;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spnrSearchType;
    private Spinner spnrCategory;
    List<String> list = new ArrayList<String>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private LinearLayout llRvContent;
    private Button btnSearch;
    private Group groupForm;
    private EditText etSearchData;
    private TextView tvTitle;
    private ImageButton ibtnBack;
    private ImageButton ibtnFilter;
    private RecyclerView rvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_search));
        llRvContent = findViewById(R.id.llRvContent);
        btnSearch = findViewById(R.id.btnSearch);
        etSearchData = findViewById(R.id.etSearchData);
        tvTitle = findViewById(R.id.tvTitle);
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnFilter = findViewById(R.id.ibtnFilter);
        rvContent = findViewById(R.id.rvContent);
        list.add("IMAGE");
        list.add("VIDEO");
        list.add("FRIEND");
        list.add("PRODUCT");
        list.add("PROVIDE");
        list.add("DEMAND");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spnrSearchType = findViewById(R.id.spnrSearchType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSearchType.setAdapter(dataAdapter);
        spnrCategory = findViewById(R.id.spnrCategory);
        spnrSearchType.setOnItemSelectedListener(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String SearchType = spnrSearchType.getSelectedItem().toString();
                String SearchCat = spnrCategory.getSelectedItem().toString();
                String SearchData = etSearchData.getText().toString();
                Toast.makeText(getApplicationContext(), "Clicked on: " + SearchType + " " + SearchCat + " " +SearchData, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        function.getData(SearchActivity.this, this, spnrCategory, list.get(position));
        //Toast.makeText(getApplicationContext(), "Clicked on: " + list.get(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}

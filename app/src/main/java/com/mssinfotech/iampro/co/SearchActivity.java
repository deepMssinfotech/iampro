package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SearchActivity extends Fragment implements AdapterView.OnItemSelectedListener {
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
    String myType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_search, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //llRvContent = view.findViewById(R.id.llRvContent);
        btnSearch = view.findViewById(R.id.btnSearch);
        etSearchData = view.findViewById(R.id.etSearchData);
        tvTitle = view.findViewById(R.id.tvTitle);
        //ibtnBack = view.findViewById(R.id.ibtnBack);
        //ibtnFilter = view.findViewById(R.id.ibtnFilter);
        //rvContent = view.findViewById(R.id.rvContent);
        list.add("IMAGE");
        list.add("VIDEO");
        list.add("FRIEND");
        list.add("PRODUCT");
        list.add("PROVIDE");
        list.add("DEMAND");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        spnrSearchType = view.findViewById(R.id.spnrSearchType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSearchType.setAdapter(dataAdapter);
        spnrCategory = view.findViewById(R.id.spnrCategory);
        spnrSearchType.setOnItemSelectedListener(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String SearchType = spnrSearchType.getSelectedItem().toString();
                String SearchCat = spnrCategory.getSelectedItem().toString();
                String SearchData = etSearchData.getText().toString();
                //Toast.makeText(getContext(), "Clicked on: " + SearchType + " " + SearchCat + " " +SearchData, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getContext(),SearchedActivity.class);
                intent.putExtra("SearchType",SearchType);
                intent.putExtra("SearchCat",SearchCat);
                intent.putExtra("SearchData",SearchData);
                  startActivity(intent);
            }
        });
        try {
            Bundle bundle = getArguments();
            //here is your list array
            myType = bundle.getString("type");
        }
        catch (Exception e){
            myType="IMAGE";
        }

        for (int i = 0; i < list.size() ; i++){
            if(list.get(i).equalsIgnoreCase(myType)){
                int spinnerPosition = dataAdapter.getPosition(list.get(i));
                spnrSearchType.setSelection(spinnerPosition);
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        function.getData(getActivity(), getContext(), spnrCategory, list.get(position));
        //Toast.makeText(getApplicationContext(), "Clicked on: " + list.get(position), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

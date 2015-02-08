package com.example.rxguide;

import java.io.BufferedReader;
import android.widget.Toast;
import java.io.InputStreamReader;
import java.net.URL;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.view.View;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch(position) {
			case 0: 
				return "View Prescription";
			case 1: 
				return "Add New Patient";
			case 2: 
				return "Write Prescription";
			default:
				return "";
			}
		}
	}

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			Bundle args = getArguments();
			int num = args.getInt(ARG_SECTION_NUMBER);
			View rootView;
			switch(num) {
			case 0:
				rootView = inflater.inflate(R.layout.activity_patient_id, container, false);
				return rootView;
			case 1:
				rootView = inflater.inflate(R.layout.activity_clinic__admin, container, false);
				return rootView;
			case 2:
				rootView = inflater.inflate(R.layout.activity_doctordetails, container, false);
				return rootView;
			default:
				rootView = inflater.inflate(R.layout.activity_patient_id, container, false);
				return rootView;
			}
		}
	}

	private class new_entry extends AsyncTask<Void, Void, Void>{

		EditText editText1 = (EditText) findViewById(R.id.name);
		EditText editText2 = (EditText) findViewById(R.id.dobdd);
		EditText editText3 = (EditText) findViewById(R.id.dobmm);
		EditText editText4 = (EditText) findViewById(R.id.dobyy);
		EditText editText5 = (EditText) findViewById(R.id.sex);
		EditText editText6 = (EditText) findViewById(R.id.phone);
		EditText editText7 = (EditText) findViewById(R.id.allergy);
		ProgressDialog pd = new ProgressDialog(MainActivity.this);
		String name,dd,mm,yy,sex,allergy,phone,htmlpage;
		String SERVER_URL;
		@Override
		protected Void doInBackground(Void... arg0) {
			try {

				name = editText1.getText().toString();
				dd = editText2.getText().toString();
				mm = editText3.getText().toString();
				yy = editText4.getText().toString();
				sex = editText5.getText().toString();
				phone = editText6.getText().toString();
				allergy = editText7.getText().toString();
				name=name.replace(" ","_");
				sex=sex.replace(" ","_");
				allergy=allergy.replace(" ","_");
				htmlpage="";
				if(name!="" && dd!="" && mm!="" && yy!="" && sex!="" && phone!="" && allergy!="" && phone.length()>=10 && (sex.toLowerCase().equals("male") || sex.toLowerCase().equals("female") || sex.toLowerCase().equals("male_") || sex.toLowerCase().equals("female_"))){
					SERVER_URL ="http://rxguide.pythonanywhere.com/RXGUIDE/default/add_new?name="+name+"&sex="+sex+"&contact="+phone+"&yy="+yy+"&mm="+mm+"&dd="+dd+"&allergies="+allergy;			
					URL server_url = new URL(SERVER_URL);
					BufferedReader in = new BufferedReader(new InputStreamReader(server_url.openStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						htmlpage = htmlpage.concat(inputLine);
					in.close();
				}
			} catch (Exception S) {
				S.printStackTrace();
			}
			return null;
		}

		@Override
		public void onPostExecute(Void values) {   
			pd.dismiss();
			if(htmlpage!=""){
				editText1.setText("");
				editText2.setText("");
				editText3.setText("");
				editText4.setText("");
				editText5.setText("");
				editText6.setText("");
				editText7.setText("");
				Toast savedToast = Toast.makeText(getApplicationContext(),"Pateint with ID "+htmlpage+" created", Toast.LENGTH_SHORT);
				savedToast.show();
			}
			else {
				Toast savedToast = Toast.makeText(getApplicationContext(),"Please make sure you have entered the values properly", Toast.LENGTH_SHORT);
				savedToast.show();
			}

		}

		@Override
		protected void onPreExecute() {
			pd.setMessage("LOADING...");
			pd.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	public void updateDatabase(View view) {		
		new new_entry().execute();
	}

	public void sender(View view) {
		String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
		Intent intent = new Intent(this, Prescriptiondisplay.class);
		EditText editText = (EditText) findViewById(R.id.editText1);
		String message = editText.getText().toString();
		if(message.length()==0) {
			Toast savedToast = Toast.makeText(getApplicationContext(),"Please enter an ID", Toast.LENGTH_SHORT);
			savedToast.show();
		}
		else{
			intent.putExtra(EXTRA_MESSAGE, message);
			startActivity(intent);
		}
	}

	public final static String doctor_name = "com.example.rxguide.name";
	public final static String hospital_name = "com.example.rxguide.hname";
	
	public void sendname(View view)
	{
		Intent intent = new Intent(this, Doctor.class);
	    EditText editText1 = (EditText) findViewById(R.id.DOCTOR_NAME);
	    EditText editText2 = (EditText) findViewById(R.id.HOSPITAL_NAME);
	    String message1 = editText1.getText().toString();
	    String message2 = editText2.getText().toString();
	    intent.putExtra(doctor_name, message1);
	    intent.putExtra(hospital_name, message2);
	    startActivity(intent);
	}
}

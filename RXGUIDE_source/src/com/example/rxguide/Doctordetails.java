package com.example.rxguide;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class Doctordetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctordetails);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.doctordetails, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
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

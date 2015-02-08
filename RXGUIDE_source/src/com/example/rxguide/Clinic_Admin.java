package com.example.rxguide;

import java.io.*;
import java.net.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Clinic_Admin extends Activity {

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clinic__admin);        
	}
	
	private class new_entry extends AsyncTask<Void, Void, Void>{

		EditText editText1 = (EditText) findViewById(R.id.name);
		EditText editText2 = (EditText) findViewById(R.id.dobdd);
		EditText editText3 = (EditText) findViewById(R.id.dobmm);
		EditText editText4 = (EditText) findViewById(R.id.dobyy);
		EditText editText5 = (EditText) findViewById(R.id.sex);
		EditText editText6 = (EditText) findViewById(R.id.phone);
		EditText editText7 = (EditText) findViewById(R.id.allergy);
		ProgressDialog pd = new ProgressDialog(Clinic_Admin.this);
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
				SERVER_URL="http://10.1.39.99:8000/RXGUIDE/default/add_new?name="+name+"&sex="+sex+"&contact="+phone+"&yy="+yy+"&mm="+mm+"&dd="+dd+"&allergies="+allergy;
				htmlpage="";
			    URL server_url = new URL(SERVER_URL);
				BufferedReader in = new BufferedReader(new InputStreamReader(server_url.openStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null)
					htmlpage = htmlpage.concat(inputLine);
				in.close();				
			} catch (Exception S) {
				S.printStackTrace();
			}
			return null;
		}

		@Override
		public void onPostExecute(Void values) {   
			editText1.setText("");
			editText2.setText("");
			editText3.setText("");
			editText4.setText("");
			editText5.setText("");
			editText6.setText("");
			editText7.setText("");
			pd.dismiss();
			Toast savedToast = Toast.makeText(getApplicationContext(),"Pateint with ID "+htmlpage+" created", Toast.LENGTH_SHORT);
			savedToast.show();

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
}
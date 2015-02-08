package com.example.rxguide;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


@SuppressLint("NewApi")
public class Doctor extends Activity implements OnClickListener {
	private DrawingView drawView;
	private float smallBrush, mediumBrush, largeBrush;
	private ImageButton drawBtn, eraseBtn, saveBtn;
	String hname;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor);
		drawView = (DrawingView)findViewById(R.id.drawing);
		drawBtn = (ImageButton)findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);
		drawView.setBrushSize(smallBrush);
		eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);
		saveBtn = (ImageButton)findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);
		Intent intent = getIntent();
		EditText e = (EditText) findViewById(R.id.dname);
		e.setText(intent.getStringExtra(MainActivity.doctor_name));
		hname=intent.getStringExtra(MainActivity.hospital_name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String yourDate = dateFormat.format(date);
		EditText dat = (EditText) findViewById(R.id.date);
		dat.setText(yourDate);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view){
		if(view.getId()==R.id.draw_btn){
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.eraser_chooser);
			brushDialog.show();

			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
				}
			});

			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
				}
			});

			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					drawView.setErase(false);
					brushDialog.dismiss();
				}
			});

		}

		else if(view.getId()==R.id.erase_btn){
			//switch to erase - choose size
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Eraser size:");
			brushDialog.setContentView(R.layout.eraser_chooser);
			brushDialog.show();

			ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});

			ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					drawView.setErase(true);
					drawView.setBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});
		}

		else if(view.getId()==R.id.save_btn){
			final String Name,id;
			final EditText e1=(EditText)findViewById(R.id.editText1);
			final EditText e5=(EditText)findViewById(R.id.editText5);
			Name=e1.getText().toString();
			id=e5.getText().toString();
			final String img_name=id+"__"+Name+"_"+UUID.randomUUID().toString();
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing?");
			saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){

					drawView.setDrawingCacheEnabled(true);
					final String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(),drawView.getDrawingCache(),img_name, "drawing");
					String[] proj = { MediaStore.Images.Media.DATA };
					Uri x=Uri.parse(imgSaved);
					CursorLoader loader = new CursorLoader(getBaseContext(), x, proj, null, null, null);
					Cursor cursor = loader.loadInBackground();
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					final String filepath=cursor.getString(column_index);

					Thread t=new Thread(new Runnable()
					{
						public void run()
						{

							String SERVER_URL="http://rxguide.pythonanywhere.com/RXGUIDE/default/add_img";
							try	{

								HttpPost post = new HttpPost(SERVER_URL);
								HttpClient client = new DefaultHttpClient();
								MultipartEntityBuilder builder = MultipartEntityBuilder.create();

								builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
								builder.addTextBody("patient_id",id ); 
								builder.addTextBody("_formname","prescription/create" );
								File f= new File(filepath);
								builder.addPart("prescription_image",new FileBody(f));

								post.setEntity(builder.build());
								client.execute(post);
								
								URL x = new URL(SERVER_URL);
								BufferedReader in = new BufferedReader(new InputStreamReader(x.openStream()));
								String y = "";
								String inputLine;
								while ((inputLine = in.readLine()) != null)
									y = y.concat(inputLine);
								in.close();
								f.delete();
							} catch (Exception e) {
								e.printStackTrace();						
							}
						}
					});
					t.start();
					try {
						t.join();
						Toast savedToast = Toast.makeText(getApplicationContext(),
								"Drawing saved to Server!", Toast.LENGTH_SHORT);
						savedToast.show();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					drawView.destroyDrawingCache();
				}
			});
			saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
					dialog.cancel();
				}
			});
			saveDialog.show();
		}


	}
	String[] params; 
	public void showtext(View view)	{

		final EditText e4=(EditText)findViewById(R.id.editText4);
		final EditText e3=(EditText)findViewById(R.id.editText3);
		final EditText e2=(EditText)findViewById(R.id.age);
		final EditText e1=(EditText)findViewById(R.id.editText1);
		final EditText e5=(EditText)findViewById(R.id.editText5);


		Thread t=new Thread() {
			public void run() {

				String id = e5.getText().toString();

				if(id!="") {	
					String SERVER_URL="http://rxguide.pythonanywhere.com/RXGUIDE/default/basic?num="+id;
					try {

						HttpPost post = new HttpPost(SERVER_URL);
						HttpClient client = new DefaultHttpClient();
						client.execute(post);
						URL x = new URL(SERVER_URL);
						BufferedReader in = new BufferedReader(new InputStreamReader(x.openStream()));
						String y = "";
						String inputLine;
						while ((inputLine = in.readLine()) != null)
							y = y.concat(inputLine);
						in.close();
						params=y.split("!");
						params[3]=params[3].replace("\\n", "\n");
						params[3]=params[3].replace("_", " ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		t.start();
		try {
			t.join();
			params[0]=params[0].replace("_"," ");
			params[3]=params[3].replace("_"," ");
			e1.setText(params[0]);
			e2.setText(params[2]);
			e3.setText(params[1]);
			e4.setText(params[3]);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public final static String title="title";

	public void printer(View view) {

		Thread t=new Thread() {
			public void run() {
				try {
					final EditText e4=(EditText)findViewById(R.id.editText4);
					final EditText e3=(EditText)findViewById(R.id.editText3);
					final EditText e2=(EditText)findViewById(R.id.age);
					final EditText e1=(EditText)findViewById(R.id.editText1);
					final EditText e5=(EditText)findViewById(R.id.editText5);
					final EditText d=(EditText)findViewById(R.id.dname);
					String id = e5.getText().toString();
					String name= e1.getText().toString();
					String age= e2.getText().toString();
					String sex= e3.getText().toString();
					String allergies= e4.getText().toString();
					String doc = d.getText().toString();
					doc=doc.replace(" ","_");
					allergies=allergies.replace(" ", "_");
					name=name.replace(" ", "_");
					URL url = new URL("http://rxguide.pythonanywhere.com/RXGUIDE/default/prescription_PDF?uid="+id+"&name="+name+"&age="+age+"&sex="+sex+"&allergy="+allergies+"&doctor="+doc+"&hname="+hname);
					HttpGet httpRequest = new HttpGet((url).toURI());
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
					HttpEntity entity = response.getEntity();
					BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
					InputStream input = b_entity.getContent();
					OutputStream outputStream = null;
					File file = new File(Environment.getExternalStorageDirectory()+"/prescription.pdf");
					if (!file.exists()) {
						file.createNewFile();
					}
					outputStream = new FileOutputStream(file);
					int read = 0;
					byte[] bytes = new byte[1024];
					while ((read = input.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					if (input != null) {
						input.close();
					}
					if (outputStream != null) {
						outputStream.close();
					}
					
				} catch (Exception S) {
					S.printStackTrace();
				}
			}
			
		};

		t.start();
		try {
			t.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		prin();
	}
	
	public void prin(){
		if (isNetworkAvailable() == false) {
			Toast.makeText(this,
					"Network connection not available, Please try later",
					Toast.LENGTH_SHORT).show();
		} else {
			File file = new File(Environment.getExternalStorageDirectory()+"/prescription.pdf");
			Intent printIntent = new Intent(Doctor.this, PrintDialogActivity.class);
			printIntent.setDataAndType (Uri.fromFile(file), "application/pdf");
			printIntent.putExtra("title", "Prescription");
			startActivity(printIntent);
		}
	}

	public boolean isNetworkAvailable() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			Log.e("Network Testing", "***Available***");
			return true;
		}
		Log.e("Network Testing", "***Not Available***");
		return false;
	}

}
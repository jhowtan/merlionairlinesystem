package sg.mas.gatecontrol;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GateCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_check);
    }

    public void scanBoardingPass(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra("SCAN_WIDTH", 900);
        integrator.addExtra("SCAN_HEIGHT", 300);
        integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 0L);
        integrator.addExtra("PROMPT_MESSAGE", "Scan the passenger's boarding pass");
        ArrayList<String> codeTypes = new ArrayList<>();
        codeTypes.add("PDF_417");
        integrator.initiateScan(codeTypes);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String eTicketNumber = scanResult.getContents();
            ((EditText) findViewById(R.id.eticketNumber)).setText(eTicketNumber);
            new GateCheckTask(eTicketNumber).execute();
        }
    }

    public class GateCheckTask extends AsyncTask<Void, Void, Boolean> {
        private final String mETicketNumber;
        private String mPassengerName;
        private String mSeatNumber;

        GateCheckTask(String eTicketNumber) {
            mETicketNumber = eTicketNumber;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(Utils.getRemoteApi(getApplicationContext()) + "gateCheck.xhtml?eticket=" + URLEncoder.encode(mETicketNumber, "UTF-8"));
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");

                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    inputStream.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    mPassengerName = jsonObject.getString("passengerName");
                    mSeatNumber = jsonObject.getString("seatNumber");
                    return jsonObject.getString("status").equals("success");
                }
            } catch (Exception e) {
                // Unable to gate check
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                ((EditText) findViewById(R.id.passengerName)).setText(mPassengerName);
                ((EditText) findViewById(R.id.passengerSeatNumber)).setText(mSeatNumber);
                ((EditText) findViewById(R.id.gateCheckStatus)).setText("CLEARED FOR BOARDING");
                Toast.makeText(getApplicationContext(), mPassengerName + " cleared for boarding.", Toast.LENGTH_LONG).show();
                //Snackbar.make(getWindow().getDecorView().getRootView(), mPassengerName + " cleared for boarding.", Snackbar.LENGTH_LONG).show();
            } else {
                ((EditText) findViewById(R.id.passengerName)).setText("");
                ((EditText) findViewById(R.id.passengerSeatNumber)).setText(" ");
                ((EditText) findViewById(R.id.gateCheckStatus)).setText("NOT CLEARED FOR BOARDING");
                Toast.makeText(getApplicationContext(), "Passenger not cleared for boarding!", Toast.LENGTH_LONG).show();
                //Snackbar.make(getWindow().getDecorView().getRootView(), "Passenger not cleared for boarding!", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}

package com.example.muhem.currencyprocesses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    float TRY;
    float USD;
    float EUR;
    float GBP;
    float given_count;
    float balance_TRY;
    float balance_USD;
    float balance_EUR;
    float balance_GBP;
    String selection;

    private RadioGroup radioGroup;

    private Button b_purchase, b_exchange;
    private EditText et_givenCount;
    private TextView tv_TRY, tv_USD, tv_EUR, tv_GBP;
    private TextView tv_equ1, tv_equ2, tv_equ3;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Background().execute("https://api.exchangeratesapi.io/latest?base=TRY");

        b_purchase = findViewById(R.id.b_purchase);
        b_exchange = findViewById(R.id.b_exchange);
        radioGroup = findViewById(R.id.rg);
        et_givenCount = findViewById(R.id.et_givenCount);
        tv_TRY = findViewById(R.id.tv_TRY);
        tv_USD = findViewById(R.id.tv_USD);
        tv_EUR = findViewById(R.id.tv_EUR);
        tv_GBP = findViewById(R.id.tv_GBP);
        tv_equ1 = findViewById(R.id.tv_equ1);
        tv_equ2 = findViewById(R.id.tv_equ2);
        tv_equ3 = findViewById(R.id.tv_equ3);

        given_count = Float.parseFloat(et_givenCount.getText().toString());

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        balance_TRY = sharedPref.getFloat("balance_TRY", 1000);
        balance_USD = sharedPref.getFloat("balance_USD", 0);
        balance_EUR = sharedPref.getFloat("balance_EUR", 0);
        balance_GBP = sharedPref.getFloat("balance_GBP", 0);
        selection = sharedPref.getString("selection", "null");
        TRY = sharedPref.getFloat("TRY", 0);
        USD = sharedPref.getFloat("USD", 0);
        EUR = sharedPref.getFloat("EUR", 0);
        GBP = sharedPref.getFloat("GBP", 0);

        tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
        tv_USD.setText("    $ : " + Float.toString(balance_USD));
        tv_EUR.setText("    € : " + Float.toString(balance_EUR));
        tv_GBP.setText("    £ : " + Float.toString(balance_GBP));


        b_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_givenCount.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "You must write a value!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selection == "USD") {
                    if (given_count > balance_USD) {
                        Toast.makeText(MainActivity.this, "Not enough USD balance for exchange!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    float usd_try = (TRY / USD) * given_count;
                    balance_USD -= given_count;
                    balance_TRY += usd_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_USD", balance_USD);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_USD.setText("    $ : " + Float.toString(balance_USD));
                } else if (selection == "EUR") {
                    if (given_count > balance_EUR) {
                        Toast.makeText(MainActivity.this, "Not enough EUR balance for exchange!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    float eur_try = (TRY / EUR) * given_count;
                    balance_EUR -= given_count;
                    balance_TRY += eur_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_EUR", balance_EUR);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_EUR.setText("    € : " + Float.toString(balance_EUR));
                } else if (selection == "GBP") {
                    if (given_count > balance_GBP) {
                        Toast.makeText(MainActivity.this, "Not enough GBP balance for exchange!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    float gbp_try = (TRY / GBP) * given_count;
                    balance_GBP -= given_count;
                    balance_TRY += gbp_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_GBP", balance_GBP);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_GBP.setText("    £ : " + Float.toString(balance_GBP));
                }
            }
        });

        b_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_givenCount.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "You must write a value!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (given_count > balance_TRY) {
                    Toast.makeText(MainActivity.this, "Not enough TRY balance for purchase!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selection == "USD") {
                    float usd_try = (TRY / USD) * given_count;
                    balance_USD += given_count;
                    balance_TRY -= usd_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_USD", balance_USD);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_USD.setText("    $ : " + Float.toString(balance_USD));
                } else if (selection == "EUR") {
                    float eur_try = (TRY / EUR) * given_count;
                    balance_EUR += given_count;
                    balance_TRY -= eur_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_EUR", balance_EUR);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_EUR.setText("    € : " + Float.toString(balance_EUR));
                } else if (selection == "GBP") {
                    float gbp_try = (TRY / GBP) * given_count;
                    balance_GBP += given_count;
                    balance_TRY -= gbp_try;
                    editor.putFloat("balance_TRY", balance_TRY);
                    editor.putFloat("balance_GBP", balance_GBP);
                    editor.commit();
                    tv_TRY.setText("    ₺ : " + Float.toString(balance_TRY));
                    tv_GBP.setText("    £ : " + Float.toString(balance_GBP));
                }

            }
        });

        et_givenCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_givenCount.getText().toString().equals("")) {
                    tv_equ1.setText("");
                    tv_equ2.setText("");
                    tv_equ3.setText("");
                    return;
                }
                given_count = Float.parseFloat(et_givenCount.getText().toString());
                if (selection == "USD") {
                    selectionUSD();
                } else if (selection == "EUR") {
                    selectionEUR();
                } else if (selection == "GBP") {
                    selectionGBP();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (et_givenCount.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "You must write a value!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (checkedId == R.id.rb_USD) {
                    selection = "USD";
                    selectionUSD();
                } else if (checkedId == R.id.rb_EUR) {
                    selection = "EUR";
                    selectionEUR();
                } else if (checkedId == R.id.rb_GBP) {
                    selection = "GBP";
                    selectionGBP();
                }
                editor.putString("selection", "GBP");
                editor.commit();
            }
        });
    }

    class Background extends AsyncTask<String, String, String> {

        protected String doInBackground(String ...params){
            params[0] = "https://api.exchangeratesapi.io/latest?base=TRY";
            HttpURLConnection connection;
            BufferedReader br;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String satir;
                String dosya = "";

                while ((satir = br.readLine()) != null) {
                    dosya += satir;
                }

                br.close();
                return dosya;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "hata";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jo = jsonObject.getJSONObject("rates");
                TRY = 1;
                USD = Float.parseFloat(jo.getString("USD"));
                EUR = Float.parseFloat(jo.getString("EUR"));
                GBP = Float.parseFloat(jo.getString("GBP"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("TRY", TRY);
        editor.putFloat("USD", USD);
        editor.putFloat("EUR", EUR);
        editor.putFloat("GBP", GBP);
        editor.commit();
    }

    protected void selectionUSD() {
        float usd_try = (TRY / USD) * given_count;
        float usd_eur = (EUR / USD) * given_count;
        float usd_gbp = (GBP / USD) * given_count;

        tv_equ1.setText("        " + given_count + " USD = " + Float.toString(usd_try) + " TRY");
        tv_equ2.setText("        " + given_count + " USD = " + Float.toString(usd_eur) + " EUR");
        tv_equ3.setText("        " + given_count + " USD = " + Float.toString(usd_gbp) + " GBP");
    }

    protected void selectionEUR() {
        float eur_try = (TRY / EUR) * given_count;
        float eur_usd = (USD / EUR) * given_count;
        float eur_gbp = (GBP / EUR) * given_count;

        tv_equ1.setText("        " + given_count + " EUR = " + Float.toString(eur_try) + " TRY");
        tv_equ2.setText("        " + given_count + " EUR = " + Float.toString(eur_usd) + " USD");
        tv_equ3.setText("        " + given_count + " EUR = " + Float.toString(eur_gbp) + " GBP");
    }

    protected void selectionGBP() {
        float gbp_try = (TRY / GBP) * given_count;
        float gbp_usd = (USD / GBP) * given_count;
        float gbp_eur = (EUR / GBP) * given_count;

        tv_equ1.setText("        " + given_count + " GBP = " + Float.toString(gbp_try) + " TRY");
        tv_equ2.setText("        " + given_count + " GBP = " + Float.toString(gbp_usd) + " USD");
        tv_equ3.setText("        " + given_count + " GBP = " + Float.toString(gbp_eur) + " EUR");
    }
}
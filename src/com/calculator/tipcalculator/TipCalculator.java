package com.calculator.tipcalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class TipCalculator extends Activity {
    /**
     * Called when the activity is first created.
     */
    private static final String APP_URL = "http://market.android.com/details?id=com.calculator.tipcalculator";
    private EditText billEditText;
    private Double billTotal = 0.0;
    private Double customAmount = 0.0;

    private EditText tip10EditText;
    private EditText tip15EditText;
    private EditText tip20EditText;
    private EditText total10EditText;
    private EditText total15EditText;
    private EditText total20EditText;
    private EditText totalRounded10EditText;
    private EditText totalRounded15EditText;
    private EditText totalRounded20EditText;


    private SeekBar seekBar;
    private TextView customTipTextView;
    private int customTipValue = 18;

    private EditText finalTip;
    private EditText finalTotal;
    private EditText finalRoundTotal;

    private EditText noOfPeople;
    private EditText perPersonAmount;
    private EditText customAmountEditText;
    private EditText customTipPercentEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        billEditText = (EditText) findViewById(R.id.billEditText);
        tip10EditText = (EditText) findViewById(R.id.tip10EditText);
        tip10EditText.setInputType(InputType.TYPE_NULL);
        tip15EditText = (EditText) findViewById(R.id.tip15EditText);
        tip20EditText = (EditText) findViewById(R.id.tip20EditText);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        customTipTextView = (TextView) findViewById(R.id.customTipTextView);
        total10EditText = (EditText) findViewById(R.id.total10EditText);
        total15EditText = (EditText) findViewById(R.id.total15EditText);
        total20EditText = (EditText) findViewById(R.id.total20EditText);

        totalRounded10EditText = (EditText) findViewById(R.id.totalRounded10EditText);
        totalRounded15EditText = (EditText) findViewById(R.id.totalRounded15EditText);
        totalRounded20EditText = (EditText) findViewById(R.id.totalRounded20EditText);

        finalTip = (EditText) findViewById(R.id.finalTipEditText);
        finalTotal = (EditText) findViewById(R.id.finalTotalEditText);
        finalRoundTotal = (EditText) findViewById(R.id.finalRoundTotalEditText);

        noOfPeople = (EditText) findViewById(R.id.noOfPeopleEditText);
        perPersonAmount = (EditText) findViewById(R.id.perPersonEditText);

        customAmountEditText = (EditText) findViewById(R.id.customAmountEditText);
        customTipPercentEditText = (EditText) findViewById(R.id.customTipPercentEditText);

        billEditText.addTextChangedListener(new BillTotalTextWatchListener());
        seekBar.setOnSeekBarChangeListener(new MySeekChangeListener());
        customAmountEditText.addTextChangedListener(new CustomAmountTextWatchListener());
    }

    private void updateTipValues() {
        double tenPercentTip = (billTotal * 0.1);
        double totalTenPercent = tenPercentTip + billTotal;
        double fifteenPercentTip = (billTotal * 0.15);
        double totalFifteenPercent = fifteenPercentTip + billTotal;
        double twentyPercentTip = (billTotal * 0.20);
        double totalTwentyPercent = twentyPercentTip + billTotal;
        tip10EditText.setText(getFormattedString(tenPercentTip));
        tip15EditText.setText(getFormattedString(fifteenPercentTip));
        tip20EditText.setText(getFormattedString(twentyPercentTip));

        total10EditText.setText(getFormattedString(totalTenPercent));
        total15EditText.setText(getFormattedString(totalFifteenPercent));
        total20EditText.setText(getFormattedString(totalTwentyPercent));

        totalRounded10EditText.setText(getRoundedFormattedString(totalTenPercent));
        totalRounded15EditText.setText(getRoundedFormattedString(totalFifteenPercent));
        totalRounded20EditText.setText(getRoundedFormattedString(totalTwentyPercent));
        updateCustom();
    }

    private String getFormattedString(double value) {
        return String.format("%.2f", value);
    }

    private String getRoundedFormattedString(double value) {
        long round = Math.round(value);
        return String.format("%d", round);
    }

    private void updateCustomTipValue() {
        customTipTextView.setText(customTipValue + "%");
        if (billTotal > 0) {
            updateCustom();

        }
    }

    private void updateCustom() {
        double customTip = (billTotal * customTipValue * 0.01);
        double totalCustomPercent = customTip + billTotal;

        finalTip.setText(getFormattedString(customTip));
        finalTotal.setText(getFormattedString(totalCustomPercent));
        finalRoundTotal.setText(getRoundedFormattedString(totalCustomPercent));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.emailApp:
                shareAppViaEmail();
                return true;
            case R.id.rateApp:
                rateApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareAppViaEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Tip Calculator App");
        intent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app " + APP_URL);

        Intent chooser = Intent.createChooser(intent, "Tell a friend about Tip Calculator");
        startActivity(chooser);
    }

    private void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(APP_URL));
        startActivity(intent);
    }

    class BillTotalTextWatchListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                billTotal = Double.parseDouble(charSequence.toString());
            } catch (NumberFormatException e) {
                billTotal = 0.0;
            }
            updateTipValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class CustomAmountTextWatchListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                customAmount = Double.parseDouble(charSequence.toString());
                if (billTotal < customAmount) {
                    double customTipPercentValue = (customAmount - billTotal) * 100 / billTotal;
                    customTipPercentEditText.setText(getFormattedString(customTipPercentValue));
                } else {
                    customTipPercentEditText.setText("");
                }
            } catch (NumberFormatException e) {
                customAmount = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    class MySeekChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            customTipValue = seekBar.getProgress();
            updateCustomTipValue();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}

package net.mcdermotsoft.postagecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends ActionBarActivity
{
	MainActivity context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText weightInput = (EditText)findViewById(R.id.weightInput);
		final Spinner spinner = (Spinner) findViewById(R.id.packageTypeSpinner);
		final Button calculateButton = (Button)findViewById(R.id.calculateButton);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.package_type_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				weightInput.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});

		ImageView questionMark = (ImageView)findViewById(R.id.questionMark);
		questionMark.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context,HelpActivity.class);
				Bundle bundle = new Bundle();
				if(spinner.getSelectedItem().toString().equals("Letter"))
					bundle.putString("url","http://postcalc.usps.com/PopUps/Letter.htm");
				if(spinner.getSelectedItem().toString().equals("Large Envelope"))
					bundle.putString("url", "http://postcalc.usps.com/PopUps/LargeEnvelope.htm");
				if(spinner.getSelectedItem().toString().equals("Package"))
					bundle.putString("url","http://postcalc.usps.com/PopUps/Package.htm");
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		calculateButton.setEnabled(false);

		weightInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				doCalculate(spinner,weightInput);
				return true;
			}
		});
		weightInput.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				((TextView)findViewById(R.id.total)).setText("");

				if(weightInput.getText().length() > 0)
					calculateButton.setEnabled(true);
				else
					calculateButton.setEnabled(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			@Override
			public void afterTextChanged(Editable s){}
		});

		calculateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				doCalculate(spinner,weightInput);
			}
		});
	}

	private void doCalculate(Spinner spinner, EditText weightInput)
	{
		String type = spinner.getSelectedItem().toString();
		double weight = 0;
		try
		{
			weight = Double.parseDouble(weightInput.getText().toString());
		}
		catch(NumberFormatException ex)
		{
			weightInput.setError("The weight must be a number");
			return;
		}
		if(weight <= 0)
		{
			weightInput.setError("The weight must be > 0oz");
			return;
		}
		if(weight > 13)
		{
			weightInput.setError("The weight must be < 13oz");
			return;
		}

		((TextView)findViewById(R.id.total)).setText(calculate(type, weight));
	}

	String calculate(String type, double weight)
	{
		double total = 0;
		DecimalFormat formatter = new DecimalFormat("$0.00");

		if(type.equals("Letter"))
		{
			if(weight <= 3.5)
				total = .46+((Math.ceil(weight)-1)*.2);
			else
			{
				total = .92+((Math.ceil(weight)-1)*.2);
			}
		}
		if(type.equals("Large Envelope"))
			total = .92+((Math.ceil(weight)-1)*.2);
		if(type.equals("Package"))
			total = 2.07+((Math.ceil(weight)-1)*.17);

		return formatter.format(total);
	}
}

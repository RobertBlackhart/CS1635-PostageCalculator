package net.mcdermotsoft.postagecalculator;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TableRow;

public class HelpActivity extends ActionBarActivity
{
	HelpActivity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString("url");

		WebView webView = (WebView)findViewById(R.id.webView);
		webView.loadUrl(url);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);

				WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

				DisplayMetrics metrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(metrics);

				metrics.widthPixels /= metrics.density;

				view.loadUrl("javascript:var scale = " + metrics.widthPixels + " / (document.body.scrollWidth + 10); document.body.style.zoom = scale;");
			}
		});
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
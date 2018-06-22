package philippmatthes.com.manni.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import java.util.List;

import philippmatthes.com.manni.R;
import philippmatthes.com.manni.RouteChangeManager;

public class RouteChangeActivity extends SpinnerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_changes);
        List<String> changes = getIntent().getStringArrayListExtra("changes");
        setTitle("Route changes");
        String html = RouteChangeManager.getShared().changesHTML(changes);
        WebView webView = (WebView) findViewById(R.id.route_changes_webview);
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
   }

}

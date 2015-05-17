package net.inforge.z0rapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity {

    EditText customText;
    Button shrink;
    TextView textView;
    EditText linkText;

    String longLink;
    String customLink;
    String url;
    String customUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customText = (EditText) this.findViewById(R.id.customText);
        shrink = (Button) this.findViewById(R.id.shrink);
        textView = (TextView) this.findViewById(R.id.textView);
        linkText = (EditText) this.findViewById(R.id.linkText);
        linkText.setText(check_clipboard(get_clipboard()));
        shrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longLink = linkText.getText().toString().toLowerCase();
                customLink = customText.getText().toString().toLowerCase().replaceAll("\\s+", "-");
                url = "http://z0r.it/yourls-api.php?signature=4e4b657a91&action=shorturl&format=simply&url=" + longLink + "&title=upload_wth_z0r_android";
                customUrl = "http://z0r.it/yourls-api.php?signature=4e4b657a91&action=shorturl&keyword=" + customLink + "&format=simply&url=" + longLink + "&title=upload_wth_z0r_android";
                if (check_clipboard(get_clipboard()) != "") {
                    show_toast("Shrinking...");
                    if (customLink == "") {
                        getShrink task = new getShrink();
                        task.execute(new String[]{url});
                    } else {
                        getShrink task = new getShrink();
                        task.execute(new String[]{customUrl});
                    }
                } else {
                    show_toast("Invalid URL");
                }
            }
        });

        ClipboardManager clipBoard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipBoard.addPrimaryClipChangedListener( new ClipboardListener() );

    }

    public void show_toast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public String get_clipboard() {
        if (is_clipboard()) {
            ClipData cd = ((ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE)).getPrimaryClip();
            ClipData.Item cdi = cd.getItemAt(cd.getItemCount() - 1);
            return cdi.getText().toString();
        } else {
            return "";
        }
    }

    public void set_clipboard(String link) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", link);
        clipboard.setPrimaryClip(clip);
    }

    public boolean is_clipboard() {
        return ((ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE)).hasPrimaryClip();
    }

    public String check_clipboard(String cb) {
        if (is_clipboard()) { // Se la clipboard è vuota restituisce "errore"
            Pattern url_regex = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9\\+&@#/%?=~_|!:,.;]*\\.[-A-Za-z0-9\\+&@#/%=~_|():?]+");
            Matcher match = url_regex.matcher(cb);
            if (match.matches()) {
                return cb;
            } else {
                cb = "http://" + cb;
                match = url_regex.matcher(cb);
                if (match.matches()) {
                    return cb;
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    private class getShrink extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String response = "";

            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
                HttpResponse execute = client.execute(httpGet);
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (Exception e) {
                show_toast(e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == "" && customLink.length() > 0) {
                show_toast("Custom link already exists in database or is reserved");
            } else if (result == "") {
                show_toast("Error during URL shrinkig");
            } else {
                set_clipboard(result);
                customText.setText("");
                show_toast("URL shrinked and copied in the clipboard!");
            }
        }
    }

    class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {
        public void onPrimaryClipChanged()
        {
            linkText.setText(check_clipboard(get_clipboard()));
        }
    }
}
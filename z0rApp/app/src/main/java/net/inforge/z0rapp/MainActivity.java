package net.inforge.z0rapp;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.*;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;



public class MainActivity extends ActionBarActivity {

    EditText customLink;
    Button shrink;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // vengono assegnati gli ID
        customLink= (EditText) this.findViewById(R.id.customLink);
        shrink = (Button) this.findViewById(R.id.shrink);
        textView = (TextView) this.findViewById(R.id.textView);
        // click event del bottone
        shrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_toast(get_clipboard()); // test del contenuto della clipboard
                /*
                String link_toshrink = check_clipboard(get_clipboard()); // Controlla la clipboard e salva il contenuto
                if (link_toshrink != "errore") { // Se il contenuto va bene, si prosegue
                    Editable custom_link = customLink.getText(); // Prendo il valore nel campo testo facoltativo
                    if (custom_link.length() < 1) { // se il link va bene si prosegue allo shrinking
                        shrinking(link_toshrink); // se il campo custom è vuoto non c'è personalizzazione
                    } else {
                        custom_shrinking(link_toshrink, custom_link); // altrimenti si procede con lo shrink personalizzato
                    }
                } else { // Se il contenuto da errore (non c'è contenuto o non è un URL valido) mostra un avviso
                    show_toast("La clipboard non contiene un URL valido");
                }
                */

            }
        });
    }

    // Funzione che mostra un toast
    public void show_toast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // Mi creo una funzione per prendere il contenuto della clipboard per comodità
    public String get_clipboard(){
        if (is_clipboard()) {
            return ((ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE)).getPrimaryClip().toString();
        } else {
            return "";
        }
    }

    // Mi credo una funzione per impostare il contenuto della clipboard per comodità
    public void set_clipboard(String link) {
        ClipData clip = ClipData.newPlainText("label",link);
        ((ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE)).setPrimaryClip(clip);
    }

    // Funzione che dice se la clipboard ha contenuto o no
    public boolean is_clipboard(){
        return ((ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE)).hasPrimaryClip();
    }

    // Funzione che fa la richiesta GET a z0r.it
    public void shrinking(java.lang.CharSequence link){
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL("http://z0r.it/yourls-api.php?signature=4e4b657a91&action=shorturl&format=simply&url=" + link + "&title=upload_wth_z0r_andr0id");
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {

        }
        set_clipboard(sb.toString());
    }

    // Funzione che fa la richiesta GET a z0r.it con personalizzazione
    public void custom_shrinking(java.lang.CharSequence link, Editable custom_link){
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL("http://z0r.it/yourls-api.php?signature=4e4b657a91&action=shorturl&keyword=" + custom_link + "&format=simply&url=" + link + "&title=upload_wth_z0r_andr0id");
            urlConn = url.openConnection();
            if (urlConn != null)
                urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {

        }
        set_clipboard(sb.toString());
    }

    // Controlla il contenuto della clipboard
    public String check_clipboard(String cb){
        if (is_clipboard()) { // Se la clipboard è vuota restituisce "errore"
            Pattern url_regex = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9\\+&@#/%?=~_|!:,.;]*\\.[-A-Za-z0-9\\+&@#/%=~_|():?]+");
            Matcher match = url_regex.matcher(cb);
            if (match.matches()) {
                return cb;
            } else {
                cb = "http://"+cb;
                match = url_regex.matcher(cb);
                if (match.matches()) {
                    return cb;
                } else {
                    return "errore";
                }
            }
        } else {
            return "errore";
        }
    }

    /* Crea un menu, per ora non serve
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}

package net.inforge.z0rapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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
                // viene preso il valore nel campo testo facoltativo
                Editable link = customLink.getText();
                // viene fatto il controllo dell'url in clipboard
                if (check_clipboard() != "errore") {
                    // se il link va bene si prosegue all shirnking
                    if (link.length()<1) {
                        // se il campo custom è vuoto non c'è personalizzazione
                        shrinking();
                    } else {
                        // altrimenti si procede con lo shrink personalizzato
                        custom_shrinking(link);
                    }
                } else {
                    // mostra errore clipboard
                }
            }
        });
    }

    public void shrinking(){

    }

    public void custom_shrinking(Editable link){

    }

    public java.lang.String check_clipboard(){
        return "ciao";
    }

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
}

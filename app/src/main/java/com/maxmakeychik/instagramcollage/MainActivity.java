package com.maxmakeychik.instagramcollage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private boolean isAuthorizing = false;
    EditText nickEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nickEditText = (EditText) findViewById(R.id.nickEditText);
        nickEditText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if(!isAuthorizing) {
                    isAuthorizing = true;
                    getMostLikedPhotos();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    void getMostLikedPhotos(){
        final String username = nickEditText.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

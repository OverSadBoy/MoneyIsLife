package Kazuki.moneyislife;

import Kazuki.moneyislife.api.Api;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.Nullable;


public class AddAct extends AppCompatActivity {

    public static final String TYPE_KEY = "type";

    private TextInputEditText name;
    private TextInputEditText  price;
    private Button addBtn;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.nameS);
        price = findViewById(R.id.numS);
        type = getIntent().getStringExtra(TYPE_KEY);
        TextListener listener = new TextListener();
        name.addTextChangedListener(listener);
        price.addTextChangedListener(listener);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setTextColor(getResources().getColor(R.color.white));
        addBtn.setOnClickListener((View v) -> {
            String nameValue = name.getText().toString();
            String priceValue = price.getText().toString();
            Item item = new Item(nameValue, priceValue, type);
            Intent intent = new Intent();
            intent.putExtra("item", item);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private class TextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            CharSequence nameText = name.getText();
            CharSequence priceText = price.getText();
            if(!TextUtils.isEmpty(nameText) && !TextUtils.isEmpty(priceText)) {
                addBtn.setVisibility(View.VISIBLE);
            }
            else {
                addBtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


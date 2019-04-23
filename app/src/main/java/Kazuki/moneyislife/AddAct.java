package Kazuki.moneyislife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddAct extends AppCompatActivity {

    public static final String TYPE_KEY = "type";

    EditText name;
    EditText num;
    Button plus;
    boolean checkName;
    boolean checkNum;

    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.add_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.nameS);
        num = findViewById(R.id.numS);
        plus = findViewById(R.id.plus);
        checkName = false;
        checkNum = false;

        type = getIntent().getStringExtra(TYPE_KEY);

        num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkName)
                    plus.setEnabled(!name.getText().toString().isEmpty() && !num.getText().toString().isEmpty());
                if (!num.getText().toString().isEmpty())
                    checkNum = true;
                else
                    checkNum = false;
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (checkNum)
                    plus.setEnabled(!name.getText().toString().isEmpty() && !num.getText().toString().isEmpty());
                if (!name.getText().toString().isEmpty())
                    checkName = true;
                else
                    checkName = false;
            }
        });

        plus.setOnClickListener((View v) -> {

            Item item = new Item(name.getText().toString(), num.getText().toString(), type);
            Intent intent = new Intent();
            intent.putExtra("item", item);
            setResult(RESULT_OK,intent);
            finish();
        });
    }
}

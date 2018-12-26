package com.bugting.www.numericalproofgenerator;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public Button search;
    public Switch auto_stop;
    public EditText textbox;
    public Button options;
    private boolean strong = true, weak = true, sum = true, pozhen = false;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        options = findViewById(R.id.options);
        auto_stop = findViewById(R.id.auto_stop);
        textbox = findViewById(R.id.textbox);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double content = Double.parseDouble(textbox.getText().toString());
                Intent result = new Intent(MainActivity.this, result.class);
                result.putExtra("key", content);
                result.putExtra("strong", strong);
                result.putExtra("weak", weak);
                result.putExtra("sum", sum);
                result.putExtra("pozhen", pozhen);
                result.putExtra("stop", auto_stop.isChecked());
                startActivity(result);
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_options, null);
                final CheckBox strong_ = mView.findViewById(R.id.strong);
                final CheckBox weak_ = mView.findViewById(R.id.weak);
                final CheckBox sum_ = mView.findViewById(R.id.sum);
                final CheckBox pozhen_ = mView.findViewById(R.id.pozhen);
                strong_.setChecked(strong);
                weak_.setChecked(weak);
                sum_.setChecked(sum);
                pozhen_.setChecked(pozhen);
                Button confirm = mView.findViewById(R.id.confirm);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!strong_.isChecked() && !weak_.isChecked()
                                && !sum_.isChecked() && !pozhen_.isChecked()) {
                            Toast.makeText(MainActivity.this,"请至少选择一种论证", Toast.LENGTH_LONG).show();
                        } else {
                            strong = strong_.isChecked();
                            weak = weak_.isChecked();
                            sum = sum_.isChecked();
                            pozhen = pozhen_.isChecked();
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });
    }

}

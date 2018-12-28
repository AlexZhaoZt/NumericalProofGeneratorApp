package com.bugting.www.numericalproofgenerator;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.content.ClipboardManager;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class result extends AppCompatActivity {
    private double target;
    private CheckBox strong_display, weak_display, sum_display, pozhen_display;
    private boolean stop, strong, weak, sum, pozhen;
    private ListView list;
    private String[] strong_arr, weak_arr, sum_arr;
    private String pozhen_str;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native String[] strong(double key);

    public native String[] weak(double key);

    public native String[] sum(double key);

    public native String pozhen(double key);

    public native void changeStop(boolean st);

    public native void reset();

    private AlertDialog.Builder builder;

    private void setOnItemClickListenerHelper(ListView list, final ArrayAdapter<String> adapter) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String label = "Proof #" + Integer.toString(position);
                ClipData clip = ClipData.newPlainText(label, adapter.getItem(position));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(result.this, "复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnCheckedChangeListenerHelper(CheckBox ck) {
        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                adapter.clear();
                if (strong_display.isChecked()) {
                    adapter.addAll(strong_arr);
                }
                if (weak_display.isChecked()) {
                    adapter.addAll(weak_arr);
                }
                if (sum_display.isChecked()) {
                    adapter.addAll(sum_arr);
                }
                if (pozhen_display.isChecked()) {
                    adapter.add(pozhen_str);
                }
            }
        });
    }


    private class Strong extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        @Override
        protected Void doInBackground(Void... voids) {
            strong_arr = strong(target);
            publishProgress(strong_arr);

//        ArrayList<String> l = Arrays.asList(sar);
//        System.out.println(aList.get(0));

            return null;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) list.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(result.this, "强算术论证完成", Toast.LENGTH_SHORT).show();
            if (!weak && !sum && !pozhen && adapter.isEmpty()) {
                builder.show();
            }
            if (!weak && !sum && !pozhen) {
                reset();
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.addAll(values);
        }
    }

    private class Weak extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        @Override
        protected Void doInBackground(Void... voids) {
            weak_arr = weak(target);
            publishProgress(weak_arr);
//        ArrayList<String> l = Arrays.asList(sar);
//        System.out.println(aList.get(0));

            return null;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) list.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(result.this, "弱算术论证完成", Toast.LENGTH_SHORT).show();
            if (!sum && !pozhen && adapter.isEmpty()) {
                builder.show();
            }
            if (!sum && !pozhen) {
                reset();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.addAll(values);
        }
    }

    private class Sum extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        public String[] array;
        @Override
        protected Void doInBackground(Void... voids) {
            sum_arr = sum(target);
            publishProgress(sum_arr);
//        ArrayList<String> l = Arrays.asList(sar);
//        System.out.println(aList.get(0));

            return null;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) list.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(result.this, "求和论证完成", Toast.LENGTH_SHORT).show();
            if (!pozhen && adapter.isEmpty()) {
                builder.show();
            }
            if (!pozhen) {
                reset();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.addAll(values);
        }
    }

    private class Pozhen extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        @Override
        protected Void doInBackground(Void... voids) {
            pozhen_str = pozhen(target);
            if (pozhen_str != null)
            publishProgress(pozhen_str);
//        ArrayList<String> l = Arrays.asList(sar);
//        System.out.println(aList.get(0));

            return null;
        }

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) list.getAdapter();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(result.this, "迫真论证完成", Toast.LENGTH_SHORT).show();
            if (adapter.isEmpty()) {
                builder.show();
            }
            reset();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        list = findViewById(R.id.list);
        strong_display = findViewById(R.id.strong_display);
        weak_display = findViewById(R.id.weak_display);
        sum_display = findViewById(R.id.sum_display);
        pozhen_display = findViewById(R.id.pozhen_display);
        Bundle ex = intent.getExtras();
        target = ex.getDouble("key");
        stop = ex.getBoolean("stop");
        strong = ex.getBoolean("strong");
        weak = ex.getBoolean("weak");
        sum = ex.getBoolean("sum");
        pozhen = ex.getBoolean("pozhen");
        strong_display.setChecked(strong);
        strong_display.setEnabled(strong);
        weak_display.setChecked(weak);
        weak_display.setEnabled(weak);
        sum_display.setChecked(sum);
        sum_display.setEnabled(sum);
        pozhen_display.setChecked(pozhen);
        pozhen_display.setEnabled(pozhen);
        if (stop) {
            strong_display.setEnabled(false);
            weak_display.setEnabled(false);
            sum_display.setEnabled(false);
            pozhen_display.setEnabled(false);
        }
        builder = new AlertDialog.Builder(result.this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("论证失败")
                .setMessage("没有找到结果，建议尝试其他论证选项")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                        finish();
                    }
                });
        //final ArrayAdapter<ArrayAdapter<String>> all_adapter = new ArrayAdapter<>(result.this, android.R.layout.simple_list_item_1, new ArrayList<ArrayAdapter<String>>());
        list.setAdapter(new ArrayAdapter<String>(result.this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
        changeStop(stop);
        if (strong) {
            new Strong().execute();
        }
        if (weak) {
            new Weak().execute();
        }
        if (sum) {
            new Sum().execute();
        }
        if (pozhen) {
            new Pozhen().execute();
        }
        setOnItemClickListenerHelper(list, (ArrayAdapter<String>) list.getAdapter());
        setOnCheckedChangeListenerHelper(strong_display);
        setOnCheckedChangeListenerHelper(weak_display);
        setOnCheckedChangeListenerHelper(sum_display);
        setOnCheckedChangeListenerHelper(pozhen_display);
    }
}

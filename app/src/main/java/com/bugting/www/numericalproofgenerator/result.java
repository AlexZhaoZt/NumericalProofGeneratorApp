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
import android.widget.ListView;
import android.content.ClipboardManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class result extends AppCompatActivity {
    public ListView list;
    private double target;
    private boolean stop, strong, weak, sum, pozhen;
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native String[] strong(double key);

    public native String[] weak(double key);

    public native String[] sum(double key);

    public native void changeStop(boolean st);

    private AlertDialog.Builder builder;



    private class Strong extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        public String[] array;
        @Override
        protected Void doInBackground(Void... voids) {
            array = strong(target);
            for (String str : array) {
                publishProgress(str);
            }
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
            if (!weak && !sum && list.getAdapter().isEmpty()) {
                builder.show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }
    }

    private class Weak extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        public String[] array;
        @Override
        protected Void doInBackground(Void... voids) {
            array = weak(target);
            for (String str : array) {
                publishProgress(str);
            }
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
            if (!sum && list.getAdapter().isEmpty()) {
                builder.show();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }
    }

    private class Sum extends AsyncTask<Void, String, Void> {
        private ArrayAdapter<String> adapter;
        public String[] array;
        @Override
        protected Void doInBackground(Void... voids) {
            array = sum(target);
            for (String str : array) {
                publishProgress(str);
            }
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
            if (list.getAdapter().isEmpty()) {
                builder.show();
            }
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
        Bundle ex = intent.getExtras();
//        boolean stop = false, cont = true;
        target = ex.getDouble("key");
        stop = ex.getBoolean("stop");
        strong = ex.getBoolean("strong");
        weak = ex.getBoolean("weak");
        sum = ex.getBoolean("sum");
        pozhen = ex.getBoolean("pozhen");
        builder = new AlertDialog.Builder(result.this, android.R.style.Theme_Material_Dialog_Alert)
                .setTitle("论证失败")
                .setMessage("没有找到结果，建议尝试其他论证选项")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(result.this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        list.setAdapter(adapter);
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
}

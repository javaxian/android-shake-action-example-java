package com.javaxian.cleanarchitecture.shakeactionexample.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javaxian.cleanarchitecture.shakeactionexample.R;
import com.javaxian.cleanarchitecture.shakeactionexample.presentation.view.manager.IShakeActionListener;
import com.javaxian.cleanarchitecture.shakeactionexample.presentation.view.manager.ShakeActionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IShakeActionListener {

    EditText name;
    EditText lastName;
    Button button;
    TextView helloText;

    private ShakeActionManager shakeActionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.editText);
        lastName = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        helloText = (TextView) findViewById(R.id.textView2);

        initResources();
    }

    private void initResources() {
        button.setOnClickListener(this);

        shakeActionManager = new ShakeActionManager(this, this);
        shakeActionManager.startShakeAction();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.button:

                helloText.setText("HELLO " + name.getText() + " " + lastName.getText() + " from Mexico");

                break;
        }
    }

    @Override
    public void onShakeAction() {

        name.setText("");
        lastName.setText("");
        helloText.setText("Say Hello!!!");

        Toast.makeText(this, "The fields was cleared by action shake", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeActionManager.registerShakeAction();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeActionManager.unregisterShakeAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shakeActionManager.unregisterShakeAction();
    }
}

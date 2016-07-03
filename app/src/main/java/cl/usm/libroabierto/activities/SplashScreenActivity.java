package cl.usm.libroabierto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cl.usm.libroabierto.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //TODO conexion base de datos

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        openSigninActivity();
                    }
                },
                2000);
    }

    private void openSigninActivity(){
        //Intent intent= new Intent().setClass(SplashScreenActivity.this, SigninActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

package cl.usm.libroabierto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import cl.usm.libroabierto.R;
import cl.usm.libroabierto.models.ApiResponse;
import cl.usm.libroabierto.models.Usuario;
import cl.usm.libroabierto.network.LibroAbiertoAPI;
import cl.usm.libroabierto.network.LibroAbiertoClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SigninActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private Context mContext;
    private static final String TAG = "SignInActivity";

    private static DatabaseHelper db;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    private Usuario usuario;
    private TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mContext = this;
        db = new DatabaseHelper(this);

        mStatusTextView = (TextView) findViewById(R.id.status);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        //Controlando Sesion con Google Sign In Api
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            usuario = db.getUsuario();
            Log.d(TAG, usuario.toString());
            if (usuario.getId() == 0){
                String nombre = (acct.getDisplayName() != null ? acct.getDisplayName() : "");
                String telefono = "";
                String email = (acct.getEmail() != null ? acct.getEmail() : "");
                String foto = (acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "");

                usuario = new Usuario(0,nombre,telefono,email,foto);

                addUsuario();
                mStatusTextView.setText("Te has registrado exitosamente " + nombre + "!");
            }
            else{
                mStatusTextView.setText("Bienvenido nuevamente " + usuario.getNombre() + "!");
            }

            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        openMainActivity();
                    }
                }, 1000);
        } else {
            mStatusTextView.setText("No se ha podido logear, intente nuevamente");
        }
    }

    public void addUsuario(){
        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        Call<ApiResponse> call = api.addUsuario(usuario.getNombre(), usuario.getTelefono(), usuario.getEmail(), usuario.getFoto());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,response.body().toString());
                    Log.d(TAG, ""+db.updateUsuario(usuario.getId(), response.body().getState(), usuario.getNombre(), usuario.getTelefono(), usuario.getEmail(), usuario.getFoto()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("RetrofitFailure", t.toString());
            }
        });
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            /*
            case R.id.sign_out_button:
                //signOut();
                break;
            case R.id.disconnect_button:
                //revokeAccess();
                break;
            */
        }
    }

    private void openMainActivity(){
        Intent intent= new Intent().setClass(SigninActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

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
        View.OnClickListener,
        Callback<ApiResponse>{

    private Context mContext;
    private static final String TAG = "SignInActivity";

    private static DatabaseHelper db;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

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
            Usuario usuario = db.getUsuario();
            if (usuario.getId() == 0 && usuario.getEmail().equals("")){
                String nombre = (acct.getDisplayName() != null ? acct.getDisplayName() : "");
                String telefono = "";
                String email = (acct.getEmail() != null ? acct.getEmail() : "");
                String foto = (acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "");

                db.updateUsuario(0, 0,nombre, telefono, email, foto);
                addUsuarioServer(nombre, telefono, email, foto);
                mStatusTextView.setText("Te has registrado exitosamente " + nombre + "!");
            }
            else{
                getUsuarioServer(acct.getEmail());
                mStatusTextView.setText("Bienvenido nuevamente " + acct.getEmail() + "!");
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

    public void addUsuarioServer(String nombre, String telefono, String email, String foto){
        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        Call<ApiResponse> call = api.addUsuario(nombre, telefono, email, foto);
        call.enqueue(this);
    }

    public void getUsuarioServer(String email){
        Retrofit retrofit = LibroAbiertoClient.getClient();
        LibroAbiertoAPI api = retrofit.create(LibroAbiertoAPI.class);
        Call<Usuario> call = api.getUsuario(email);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    Usuario usuario = response.body();
                    db.updateUsuario(0,usuario.getId(), usuario.getNombre(), usuario.getTelefono(), usuario.getEmail(), usuario.getFoto());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.d("RetrofitFailure", t.toString());
            }
        });
    }

    @Override
    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
        if(response.isSuccessful()){
            Log.d(TAG,response.body().getMsg());
        }
    }

    @Override
    public void onFailure(Call<ApiResponse> call, Throwable t) {
        Log.d("RetrofitFailure", t.toString());
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

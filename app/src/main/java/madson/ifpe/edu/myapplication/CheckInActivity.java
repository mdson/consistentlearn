package madson.ifpe.edu.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.util.Locale;

public class CheckInActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button checkInButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isButtonEnabled = true;

    //24 horas em milliseconds
    private static final long TWENTY_FOUR_HOURS_MILLIS = 24 * 60 * 60 * 1000;
    private ProgressBar loadingProgressBar;
    private TextView statusTextView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private static final int CHECK_IN_DELAY = 2000; // 2 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkInButton = findViewById(R.id.checkInButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        statusTextView = findViewById(R.id.statusTextView);

        checkInButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler = new Handler();
            private Runnable runnable;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Inicia o carregamento após segurar o botão por 2 segundos
                        handler.postDelayed(runnable = () -> {
                            startCheckIn();
                        }, CHECK_IN_DELAY);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Cancela o carregamento caso o botão seja solto antes dos 2 segundos
                        handler.removeCallbacks(runnable);
                        loadingProgressBar.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        // Adicionando o clique do botão usando setOnClickListener
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia o carregamento ao clicar no botão
                startCheckIn();
            }
        });


        isButtonEnabled = getButtonState();
        checkInButton.setEnabled(isButtonEnabled);

        timeLeftInMillis = getRemainingTime();
        if (!isButtonEnabled && timeLeftInMillis > 0) {
            startCountDownTimer();
        } else {
            updateCountdownText();
        }

    }


    private void saveRemainingTime(long timeLeftInMillis) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("remaining_time", timeLeftInMillis);
        editor.apply();
    }


    private long getRemainingTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getLong("remaining_time", TWENTY_FOUR_HOURS_MILLIS); // Default value is 24 hours if not found
    }

    private void saveButtonState(boolean isButtonEnabled) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_button_enabled", isButtonEnabled);
        editor.apply();
    }


    private boolean getButtonState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("is_button_enabled", true);
    }


    private void startCheckIn() {
        if (!isButtonEnabled) {
            return;
        }

        loadingProgressBar.setVisibility(View.VISIBLE);
        statusTextView.setText("Realizando o check-in...");

        loadingProgressBar.setVisibility(View.VISIBLE);
        statusTextView.setText("Realizando o check-in...");

        // Simulando uma ação de check-in com atraso de 2 segundos
        new Handler().postDelayed(() -> {
            loadingProgressBar.setVisibility(View.GONE);
            statusTextView.setText("Check-in realizado com sucesso!");

            // desabilita o botão e so libera dps de 24h
            checkInButton.setEnabled(false);
            isButtonEnabled = false;
            timeLeftInMillis = TWENTY_FOUR_HOURS_MILLIS;
            startCountDownTimer();

            saveButtonState(isButtonEnabled);
        }, CHECK_IN_DELAY);
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                // qnd o tempo termina o botão é reabilitado
                checkInButton.setEnabled(true);
                isButtonEnabled = true;
                statusTextView.setText(""); //
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();

            saveRemainingTime(timeLeftInMillis);
            saveButtonState(isButtonEnabled);
        }
    }

    private void updateCountdownText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String countdownText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        statusTextView.setText("Próximo Check-in em: " + countdownText);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item1:
                Toast.makeText(this, "Item 1 selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCheckIn:
                Intent intent = new Intent(CheckInActivity.this, CheckInActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item3:
                Toast.makeText(this, "Item 3 selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnDeslogar:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(CheckInActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rotinasList:
                Intent intent2 = new Intent(CheckInActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickExpandirMenu(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
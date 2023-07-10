package madson.ifpe.edu.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdicionarRotinaActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<String> selectedDays;
    private int hourOfDay;
    private int minute;

    private CheckBox checkboxMonday;
    private CheckBox checkboxTuesday;
    private CheckBox checkboxWednesday;
    private CheckBox checkboxThursday;
    private CheckBox checkboxFriday;
    private CheckBox checkboxSaturday;
    private CheckBox checkboxSunday;
    private Button btnTimePicker;
    private TextView txtSelectedTime;
    private Button btnScheduleNotifications;
    private EditText edit_name;
    public List<NotificationList> notificationsList = new ArrayList<>();
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_rotina);

        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

        selectedDays = new ArrayList<>();
        edit_name = findViewById(R.id.edit_name);
        checkboxMonday = findViewById(R.id.checkbox_monday);
        checkboxTuesday = findViewById(R.id.checkbox_tuesday);
        checkboxWednesday = findViewById(R.id.checkbox_wednesday);
        checkboxThursday = findViewById(R.id.checkbox_thursday);
        checkboxFriday = findViewById(R.id.checkbox_friday);
        checkboxSaturday = findViewById(R.id.checkbox_saturday);
        checkboxSunday = findViewById(R.id.checkbox_sunday);
        btnTimePicker = findViewById(R.id.btn_time_picker);
        txtSelectedTime = findViewById(R.id.txt_selected_time);
        btnScheduleNotifications = findViewById(R.id.btn_schedule_notifications);

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnScheduleNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotifications();
            }
        });
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AdicionarRotinaActivity.this.hourOfDay = hourOfDay;
                        AdicionarRotinaActivity.this.minute = minute;
                        txtSelectedTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    private void scheduleNotifications() {

        if (checkboxMonday.isChecked()) {
            selectedDays.add("Segunda-feira");
        }

        if (checkboxTuesday.isChecked()) {
            selectedDays.add("Terça-feira");
        }

        if (checkboxWednesday.isChecked()) {
            selectedDays.add("Quarta-feira");
        }

        if (checkboxThursday.isChecked()) {
            selectedDays.add("Quinta-feira");
        }

        if (checkboxFriday.isChecked()) {
            selectedDays.add("Sexta-feira");
        }

        if (checkboxSaturday.isChecked()) {
            selectedDays.add("Sábado");
        }

        if (checkboxSunday.isChecked()) {
            selectedDays.add("Domingo");
        }

        if (selectedDays.isEmpty()) {
            Toast.makeText(this, "Selecione pelo menos um dia da semana.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (txtSelectedTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Selecione um horário.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (edit_name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Selecione um nome.", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Cria o gerenciador de notificações
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria o canal de notificação (apenas se estiver no Android Oreo ou posterior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Nome do Canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Cria um intent para abrir a MainActivity quando a notificação for clicada
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Cria a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                //.setSmallIcon(R.drawable.ic_notification)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Título da Notificação")
                .setContentText("Conteúdo da Notificação")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        // Obtém um identificador único para a notificação (pode ser usado para atualizar ou cancelar a notificação posteriormente)
        int notificationId = (int) System.currentTimeMillis();

        for (String day : selectedDays) {
            int dayOfWeek = getDayIndex(day);

            if (dayOfWeek != -1) {
                Calendar notificationTime = getNextNotificationTime(dayOfWeek);

                // Agenda a notificação para o horário selecionado e repetição semanal
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int requestCode = dayOfWeek * 10000 + hourOfDay * 100 + minute;
                PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            }
        }

        notificationManager.notify(notificationId, builder.build());
        addNotificationList(selectedDays, hourOfDay, minute, edit_name.getText().toString());

        Toast.makeText(this, "Atividade agendada com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Calendar getNextNotificationTime(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Se a hora atual for depois do horário selecionado para a notificação,
        // avança para o próximo dia da semana selecionado
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        return calendar;
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "Segunda-feira":
                return Calendar.MONDAY;
            case "Terça-feira":
                return Calendar.TUESDAY;
            case "Quarta-feira":
                return Calendar.WEDNESDAY;
            case "Quinta-feira":
                return Calendar.THURSDAY;
            case "Sexta-feira":
                return Calendar.FRIDAY;
            case "Sábado":
                return Calendar.SATURDAY;
            case "Domingo":
                return Calendar.SUNDAY;
            default:
                return -1; // Valor inválido
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }

    private void addNotificationList(List<String> selectedDays, int hourOfDay, int minute, String titulo) {
        NotificationList notificacao = new NotificationList(selectedDays, hourOfDay, minute, titulo);
        NotificationData.addNotification(notificacao);
    }

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }
}
package madson.ifpe.edu.myapplication;

import java.util.List;

public class NotificationList {
    private List<String> selectedDays;
    private int hourOfDay;
    private int minute;
    private String titulo;

    public NotificationList(List<String> selectedDays, int hourOfDay, int minute, String titulo) {
        this.selectedDays = selectedDays;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.titulo = titulo;
    }

    public List<String> getSelectedDays() {
        return selectedDays;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitulo(){return titulo;}

    @Override public String toString() {
        return "Atividade: " + titulo + "\nHorário da Atividade: " + hourOfDay + ":" + minute + "\nDias da Atividade: " + selectedDays;
    }
}

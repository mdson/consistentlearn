package madson.ifpe.edu.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterRotinasPersonalizado extends ArrayAdapter<NotificationList> {

    public AdapterRotinasPersonalizado(Context context, List<NotificationList> notifications) {
        super(context, 0, notifications);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationList notification = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_list_item, parent, false);
        }

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewContent = convertView.findViewById(R.id.textViewContent);
        TextView textViewDays = convertView.findViewById(R.id.textViewDays);

        textViewTitle.setText(notification.getTitulo());
        textViewContent.setText("Horário da Atividade: " + notification.getHourOfDay() + ":" + notification.getMinute());
        textViewDays.setText("Dias da Atividade: " + notification.getSelectedDays());

        return convertView;
    }
}

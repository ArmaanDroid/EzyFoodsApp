package sanguinebits.com.ezyfoods.home.fragments.notifications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Notification;
import sanguinebits.com.ezyfoods.R;
import utils.DateTimeUtil;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VHolder> {
    private List<Notification> notifications;
    private AdapterCallBack callBack;
    public NotificationAdapter(List<Notification> notifications, AdapterCallBack callBack) {
        this.notifications = notifications;
        this.callBack=callBack;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        try {
            vHolder.setData(notifications.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        private final TextView textViewMessage;
        private final TextView textViewDate;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.openDishDetails(getAdapterPosition());
                }
            });
        }

        public void setData(Notification notification) throws Exception {
            textViewMessage.setText(notification.getMessage());

            Date date = new Date();
            date.setTime(Long.parseLong(notification.getCreatedAt()));
            textViewDate.setText(DateTimeUtil.FormatDateMonthNamwYear.format(date));
        }
    }

    public interface AdapterCallBack {

        void openDishDetails(int adapterPosition);
    }
}

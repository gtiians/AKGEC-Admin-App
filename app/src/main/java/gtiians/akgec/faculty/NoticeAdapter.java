package gtiians.akgec.faculty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewAdapter> {

    private final Context context;
    private final ArrayList<NoticeData> list;


    public NoticeAdapter(Context context, ArrayList<NoticeData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item_layout, parent, false);
        return new NoticeViewAdapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter holder, @SuppressLint("RecyclerView") int position) {

        NoticeData currentItem = list.get(position);

        holder.deleteNoticeTitle.setText(currentItem.getTitle());

        try {
            if (currentItem.getImage() != null)
                Picasso.get().load(currentItem.getImage()).into(holder.deleteNoticeImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.deleteNotice.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are You Sure Want to Delete This Notice?");
            builder.setCancelable(true);
            builder.setPositiveButton("YES", (dialogInterface, i) -> {

                DatabaseReference reference = FirebaseDatabase.getInstance("https://akgec-ghaziabad-2115402d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("News Feed");
                reference.child(currentItem.getKey()).removeValue()
                        .addOnCompleteListener(task -> Toast.makeText(context, "Notice Deleted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show());
                notifyItemRemoved(position);

            });
            builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());

            AlertDialog dialog = null;
            try {
                dialog = builder.create();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dialog != null)
                dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NoticeViewAdapter extends RecyclerView.ViewHolder {

        private final Button deleteNotice;
        private final TextView deleteNoticeTitle;
        private final ImageView deleteNoticeImage;

        public NoticeViewAdapter(@NonNull View itemView) {
            super(itemView);

            deleteNotice = itemView.findViewById(R.id.deleteNotice);
            deleteNoticeTitle = itemView.findViewById(R.id.deleteNoticeTitle);
            deleteNoticeImage = itemView.findViewById(R.id.deleteNoticeImage);

        }
    }

}

package omt.aduc8386.loginmodule.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import omt.aduc8386.loginmodule.R;
import omt.aduc8386.loginmodule.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> users;
    private OnUserClickListener onUserClickListener;

    public UserAdapter(List<User> users, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View userView = inflater.inflate(R.layout.item_view_user, parent, false);

        return new UserAdapter.MyViewHolder(userView, onUserClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);

        Glide.with(holder.itemView.getContext())
                .load(user.getAvatar())
                .centerCrop()
                .into(holder.ivAvatar);

        holder.tvName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.tvEmail.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvEmail;


        public MyViewHolder(@NonNull View itemView, OnUserClickListener onUserClickListener) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);

            itemView.setOnClickListener(v -> {

                int userId = (int) getItemId(getAdapterPosition());
                getUser(userId);

                onUserClickListener.onUserClick(userId);

            });
        }

        public long getItemId(int position) {
            return users != null ? users.get(position).getId() : -1;
        }
    }

    private void getUser(int userId) {

    }

    public interface OnUserClickListener {
        void onUserClick(int userId);
    }
}

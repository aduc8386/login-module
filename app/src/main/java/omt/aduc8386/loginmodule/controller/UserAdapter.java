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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> users;
    private final OnUserListener onUserListener;

    public UserAdapter(List<User> users, OnUserListener onUserListener) {
        this.users = users;
        this.onUserListener = onUserListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View userView = inflater.inflate(R.layout.item_view_user, parent, false);
        return new UserViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        Glide.with(holder.ivAvatar.getContext())
                .load(user.getAvatar())
                .centerCrop()
                .error(R.drawable.avatar)
                .into(holder.ivAvatar);

        holder.tvName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        holder.tvEmail.setText(user.getEmail());

        holder.itemView.setOnClickListener(v -> {
            onUserListener.onUserClick(user.getId());
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvEmail;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
        }
    }

    public interface OnUserListener {
        void onUserClick(int userId);
    }
}

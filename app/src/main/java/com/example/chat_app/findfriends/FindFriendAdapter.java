package com.example.chat_app.findfriends;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chat_app.Common.Constants;
import com.example.chat_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.FindFriendViewHolder>{
    private Context context;
    private List<FindFriendsModel> findFriendsModelList;

    public FindFriendAdapter(Context context, List<FindFriendsModel> findFriendsModelList) {
        this.context = context;
        this.findFriendsModelList = findFriendsModelList;
    }

    @NonNull
    @Override
    public FindFriendAdapter.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_friends_layout,parent,false);
        return new FindFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendAdapter.FindFriendViewHolder holder, int position) {
        FindFriendsModel friendsModel=findFriendsModelList.get(position);
        holder.tvFullName.setText(friendsModel.getUserName());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef= storage.getInstance().getReferenceFromUrl("gs://chat-app-b5989.appspot.com");
        StorageReference mountainsRef= fileRef.child(friendsModel.getPhotoName());
        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG",String.valueOf(uri));
                Glide.with(context)
                        .load(uri)
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(holder.ivProfile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return findFriendsModelList.size();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProfile;
        private TextView tvFullName;
        private Button btnSendRequest, btnCancelRequest;
        private ProgressBar pbRequest;
        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile=itemView.findViewById(R.id.ivProfile);
            tvFullName=itemView.findViewById(R.id.tvFullName);
            btnSendRequest=itemView.findViewById(R.id.btnSendRequest);
            btnCancelRequest=itemView.findViewById(R.id.btnCancelRequest);
            pbRequest=itemView.findViewById(R.id.pbRequest);
        }
    }
}

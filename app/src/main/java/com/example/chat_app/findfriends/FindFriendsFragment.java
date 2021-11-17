package com.example.chat_app.findfriends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat_app.Common.Constants;
import com.example.chat_app.Common.NodeNames;
import com.example.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFriendsFragment extends Fragment {

    private RecyclerView rvFindFriends;
    private FindFriendAdapter findFriendAdapter;
    private List<FindFriendsModel> findFriendsModelList;
    private TextView tvEmptyFriendsList;
    private DatabaseReference databaseReference,databaseReferenceFriendRequests;
    private FirebaseUser currentUser;
    private View progressBar;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FindFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFriendsFragment newInstance(String param1, String param2) {
        FindFriendsFragment fragment = new FindFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        rvFindFriends=view.findViewById(R.id.rvFindFriends);
        progressBar=view.findViewById(R.id.progressBar);
        tvEmptyFriendsList=view.findViewById(R.id.tvEmptyFriendsList);
        rvFindFriends.setLayoutManager(new LinearLayoutManager(getActivity()));
        findFriendsModelList=new ArrayList<>();
        findFriendAdapter=new FindFriendAdapter(getActivity(),findFriendsModelList);
        rvFindFriends.setAdapter(findFriendAdapter);
        databaseReference= FirebaseDatabase.getInstance().getReference().child(NodeNames.USERS);
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceFriendRequests=FirebaseDatabase.getInstance().getReference().child(NodeNames.FRIEND_REQUESTS)
                .child(currentUser.getUid());
        tvEmptyFriendsList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Query query=databaseReference.orderByChild(NodeNames.NAME);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                findFriendsModelList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String userId=ds.getKey();
                    if(userId.equals(currentUser.getUid())){
                        return;
                    }
                    if(ds.child(NodeNames.NAME).getValue()!=null){
                        String fullName=ds.child(NodeNames.NAME).getValue().toString();
                        String photoName="images/"+userId+".jpg";
                        databaseReferenceFriendRequests.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String requestType=snapshot.child(NodeNames.REQUEST_TYPE).getValue().toString();
                                    if(requestType.equals(Constants.REQUEST_STATUS_SENT)){
                                        findFriendsModelList.add(new FindFriendsModel(fullName,photoName,userId,true));
                                        findFriendAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    findFriendsModelList.add(new FindFriendsModel(fullName,photoName,userId,false));

                                    findFriendAdapter.notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                        tvEmptyFriendsList.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), getContext().getString(R.string.failed_to_fetch_friends,error.getMessage()),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
package com.thuydev.saydream.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.BillAdapter;
import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.databinding.FragmentChoxacnhanBinding;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Bill extends Fragment {
    FragmentChoxacnhanBinding viewLayout;
    BillAdapter adapter;
    List<Bill> lstBill;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewLayout = FragmentChoxacnhanBinding.inflate(getLayoutInflater());
        return viewLayout.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstBill  = new ArrayList<>();

        loadBillData();
        adapter = new BillAdapter(getContext(),lstBill);
        viewLayout.rcvListDoncho.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewLayout.rcvListDoncho.setAdapter(adapter);
    }
    private void loadBillData() {
        getListBill(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                lstBill.clear();
                lstBill.addAll((List<Bill>) obj[0]);
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void getListBill(ICallBackAction action) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idUser = FirebaseAuth.getInstance().getUid();
        db.collection(Tag.DTO_BILL).whereEqualTo("idUser", idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    action.CallBack(task.getResult().toObjects(Bill.class));
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}

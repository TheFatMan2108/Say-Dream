package com.thuydev.saydream.Fragment;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.BillAdapter;
import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.TabKhoanchiBinding;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_BillToMonth extends Fragment {
    TabKhoanchiBinding viewbinding;
    BillAdapter billAdapter;
    List<Bill> list;
    FirebaseFirestore db;
    LocalDate dateStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    LocalDate dateEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String ngayStart = formatter.format(dateStart);
    String ngayEnd = formatter.format(dateEnd);
    FirebaseUser user;
    Long tong = 0l;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewbinding = TabKhoanchiBinding.inflate(getLayoutInflater());
        return viewbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        list = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        billAdapter = new BillAdapter(getContext(), list, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {

            }
        });
        viewbinding.rcvListKhoanchi.setAdapter(billAdapter);
        viewbinding.rcvListKhoanchi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getData();
    }

    private void getData() {
        db.collection(Tag.DTO_BILL)
                .whereGreaterThanOrEqualTo("date", ngayStart)
                .whereLessThanOrEqualTo("date", ngayEnd).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()) {
                            return;
                        }
                        list.clear();
                        for (QueryDocumentSnapshot dc : task.getResult()) {
                            if (user.getUid().equals(dc.toObject(Bill.class).getIdUser())) {
                                list.add(dc.toObject(Bill.class));
                                tong += dc.toObject(Bill.class).getTotalPrice();
                                viewbinding.tvTonggiaKhoanchi.setText(String.format("%s: %s VND",getString(R.string.priceProduct), NumberFormat.getNumberInstance(Locale.getDefault()).format(tong)));
                                billAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}

package com.thuydev.saydream.Fragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thuydev.saydream.Adapter.BillAdapter;
import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Fragment_KhoanChi extends Fragment {

    RecyclerView rcv_list;
    TextView tongGia;
    BillAdapter billAdapter;
    List<Bill> list;
    FirebaseFirestore db;
    LocalDate dateStart = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    LocalDate dateEnd= LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    String ngayStart = formatter.format(dateStart);
    String ngayEnd=formatter.format(dateEnd);
    FirebaseUser user ;
    Long tong=0l;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_khoanchi,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        anhXa(view);
    }

    private void anhXa(View view) {
        rcv_list = view.findViewById(R.id.rcv_list_khoanchi);
        tongGia = view.findViewById(R.id.tv_tonggia_khoanchi);
        list = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        getData();
        billAdapter = new BillAdapter(getContext(), list, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                // Không làm gì cả, hoặc thực hiện một hành động mặc định
            }
        });

        rcv_list.setAdapter(billAdapter);
        rcv_list.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void getData() {
        db.collection("donHangDaDuyet")
                .whereGreaterThanOrEqualTo("ngayMua", ngayStart)
                .whereLessThanOrEqualTo("ngayMua", ngayEnd).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isComplete()){
                            return;
                        }
                        list.clear();
                        for (QueryDocumentSnapshot dc : task.getResult()){
                            if (user.getUid().equals(dc.toObject(Bill.class).getIdUser())){
                                list.add(dc.toObject(Bill.class));
                                tong+=dc.toObject(Bill.class).getTotalPrice();
                                tongGia.setText("Giá: "+ NumberFormat.getNumberInstance(Locale.getDefault()).format(tong)+" VND");
                                billAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
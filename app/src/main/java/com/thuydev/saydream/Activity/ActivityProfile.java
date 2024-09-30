package com.thuydev.saydream.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thuydev.saydream.Adapter.AdapterProfile;
import com.thuydev.saydream.Adapter.AddressAdapter;
import com.thuydev.saydream.Adapter.BillAdapter;
import com.thuydev.saydream.Adapter.DepositMoneyAdapter;
import com.thuydev.saydream.DTO.Bill;
import com.thuydev.saydream.DTO.User;
import com.thuydev.saydream.Extentions.ActivityExtentions;
import com.thuydev.saydream.Extentions.FomatExtention;
import com.thuydev.saydream.Extentions.Regex;
import com.thuydev.saydream.Extentions.Tag;
import com.thuydev.saydream.Interface.ICallBackAction;
import com.thuydev.saydream.R;
import com.thuydev.saydream.databinding.ActivityThongtintaikhoanBinding;
import com.thuydev.saydream.databinding.DialogDoiMatKhauBinding;
import com.thuydev.saydream.databinding.DialogLichsuBinding;
import com.thuydev.saydream.databinding.DialogNaptienBinding;
import com.thuydev.saydream.databinding.DialogThemHangBinding;
import com.thuydev.saydream.databinding.DialogUpdateprofileBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ActivityProfile extends AppCompat {
    private static final int CODE_IMAGE = 1;
    public static ActivityProfile instance;
    ActivityThongtintaikhoanBinding view;
    TabLayoutMediator mediator;
    AdapterProfile adapterProfile;
    FirebaseUser user;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    ICallBackAction getImage;
    User n_User;
    private SharedPreferences sharedPreferences;
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Intent intent = o.getData();
                        if (intent == null) {
                            return;
                        }
                        Uri uri = intent.getData();
                        PutImageOnDataBase(uri);
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = ActivityThongtintaikhoanBinding.inflate(getLayoutInflater());
        if (instance == null) instance = this;
        setContentView(view.getRoot());
        OnInit(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                // do somethings
            }
        });
        SetData(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                n_User = (User) obj[0];
                RefreshData(n_User);
            }
        });
        TopMenu();
        mediator.attach();
    }

    private void SetData(ICallBackAction action) {
        db.collection(Tag.DTO_User).document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        action.CallBack(task.getResult().toObject(User.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ", e);
                    }
                });
    }

    private void OnInit(ICallBackAction action) {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        adapterProfile = new AdapterProfile(this, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                action.CallBack(obj);
            }
        });
        view.viewPage2ThongtinKhach.setAdapter(adapterProfile);
        mediator = new TabLayoutMediator(view.tabLayoutThongtinkhach, view.viewPage2ThongtinKhach, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.InfoProfile);
                } else {
                    tab.setText(R.string.buyInMonth);
                }
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        n_User = new User();
    }

    private void TopMenu() {
        view.imvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view.llNaptien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepositMoney();
            }
        });
        view.imvUpdatethongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
        view.imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });
    }

    public void DepositMoney() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogNaptienBinding viewDialog = DialogNaptienBinding.inflate(getLayoutInflater(), null, false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();

        viewDialog.btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                ShowProgressDialog();
                if (viewDialog.edtSotien.getText().toString().isEmpty() ||
                        Regex.CheckRegex(Regex.CheckNumber, viewDialog.edtSotien.getText().toString())) {
                    Toast.makeText(ActivityProfile.this, R.string.isNumber, Toast.LENGTH_SHORT).show();
                    return;
                }
                long money = Long.parseLong(viewDialog.edtSotien.getText().toString());
                if (money < 100000L) {
                    Toast.makeText(ActivityProfile.this, R.string.min100K, Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, Object> data = new HashMap<>();
                data.put("id", id);
                data.put("idUser", user.getUid());
                data.put("money", money);
                data.put("time", ActivityExtentions.GetTime());
                data.put("status", 0);
                db.collection(Tag.DTO_Money).document(id).set(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ActivityProfile.this, R.string.waitMinute, Toast.LENGTH_SHORT).show();
                                String urlTemplate = "https://img.vietqr.io/image/vietinbank-101870446659-compact2.jpg?amount=%d&addInfo=%s";
                                @SuppressLint("DefaultLocale") String formattedUrl = String.format(urlTemplate, money, id);
                                ActivityExtentions.ShowQR(formattedUrl, ActivityProfile.this);
                                dialog.cancel();
                                HideProgressDialog();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(Tag.TAG_LOG, "onFailure: ", e);
                                HideProgressDialog();
                            }
                        });
            }
        });
    }

    public void ShowDepositMoneyBill(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogLichsuBinding viewDialog = DialogLichsuBinding.inflate(getLayoutInflater(), null, false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();

        List<HashMap<String,Object>> list = new ArrayList<>();
        DepositMoneyAdapter depositMoneyAdapter = new DepositMoneyAdapter(this,list);
        viewDialog.rcvListLichsu.setAdapter(depositMoneyAdapter);
        viewDialog.rcvListLichsu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        GetDepositMoneyBill(new ICallBackAction() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void CallBack(Object... obj) {
                QuerySnapshot data = (QuerySnapshot)obj[0];
                list.clear();
               if (data!=null){
                   for (QueryDocumentSnapshot dc :data)
                   {
                       list.add((HashMap<String, Object>) dc.getData());
                       depositMoneyAdapter.notifyDataSetChanged();
                   }
               }

            }
        });

    }

    private void GetDepositMoneyBill(ICallBackAction action) {
        db.collection(Tag.DTO_Money)
                .whereEqualTo("idUser",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(ActivityProfile.this, R.string.Nothing, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        action.CallBack(task.getResult());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ",e );
                    }
                });
    }

    public void UpdateProfile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogUpdateprofileBinding viewDialog = DialogUpdateprofileBinding.inflate(getLayoutInflater(), null, false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();
        final Uri[] link = {null};;
        SetData(new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {
                n_User = (User) obj[0];
                if (n_User.getImage()!=null && !n_User.getImage().isEmpty()) {
                    link[0] = Uri.parse(n_User.getImage());
                }
                Glide.with(ActivityProfile.this).load(n_User.getImage()).error(R.drawable.baseline_crop_original_24).into(viewDialog.imvAddAnhEdit);
                viewDialog.edtEmailEdit.setText(n_User.getEmail());
                viewDialog.edtHotenEdit.setText(n_User.getFullName());
                viewDialog.edtSdtEdit.setText(n_User.getNumberPhone());
            }
        });
        viewDialog.imvAddAnhEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPermission(ActivityProfile.this);
                ShowProgressDialog();
                getImage = new ICallBackAction() {
                    @Override
                    public void CallBack(Object... obj) {
                        link[0] = (Uri) obj[0];
                        HideProgressDialog();
                        Glide.with(ActivityProfile.this).load(link[0])
                                .error(R.drawable.baseline_crop_original_24)
                                .into(viewDialog.imvAddAnhEdit);
                    }
                };

            }
        });
        viewDialog.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgressDialog();
                String email = viewDialog.edtEmailEdit.getText().toString();
                String numberPhone = viewDialog.edtSdtEdit.getText().toString();
                String fullName = viewDialog.edtHotenEdit.getText().toString();
                String linkImage = link[0].toString();
                if (fullName.isEmpty() || email.isEmpty() || numberPhone.isEmpty() || linkImage.isEmpty()) {
                    Toast.makeText(ActivityProfile.this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                if (Regex.CheckRegex(Regex.CheckEmail, email)) {
                    Toast.makeText(ActivityProfile.this, R.string.WrongEmailFomat, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                if (Regex.CheckRegex(Regex.CheckNumber, numberPhone)) {
                    Toast.makeText(ActivityProfile.this, R.string.WrongNumberPhone, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                n_User.setImage(linkImage);
                n_User.setFullName(fullName);
                n_User.setEmail(email);
                n_User.setNumberPhone(numberPhone);
                db.collection(Tag.DTO_User)
                        .document(user.getUid())
                        .set(n_User)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ActivityProfile.this, R.string.UpdateProfileComplate, Toast.LENGTH_SHORT).show();
                                HideProgressDialog();
                                dialog.cancel();
                                RefreshData(n_User);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(Tag.TAG_LOG, "onFailure: ", e);
                            }
                        });

            }
        });
    }

    public void RefreshData(User user) {
        Glide.with(this).load(user.getImage()).error(R.drawable.user1).into(view.imvAvatar);
        view.tvSoduKhach.setText(String.format("%s VND", FomatExtention.MakeStyleMoney(user.getBalance())));
        if (user.getFullName().isEmpty()) view.tvUsernameKhach.setText(user.getEmail());
        else view.tvUsernameKhach.setText(user.getFullName());
    }

    public void AddAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogThemHangBinding viewDialog = DialogThemHangBinding.inflate(getLayoutInflater(),null,false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final int[] change = {0};
        viewDialog.edtThemhang.setHint(R.string.FomatAddess);
        viewDialog.tvTittle2.setText(R.string.address);
        viewDialog.edtThemhang.setVisibility(View.GONE);

        if(n_User.getLocations()==null){
            n_User.setLocations(new ArrayList<>());
        }
        AddressAdapter addressAdapter = new AddressAdapter(n_User.getLocations(),this);
        viewDialog.listHang.setAdapter(addressAdapter);
        viewDialog.listHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ActivityProfile.this);
                builder1.setTitle(R.string.Notifi).setIcon(R.drawable.cancel).setMessage(R.string.deleteEver);
                builder1.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // xóa phần tử list ở đây
                        n_User.getLocations().remove(position);
                        db.collection(Tag.DTO_User).document(user.getUid()).set(n_User).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()) {
                                    Toast.makeText(ActivityProfile.this, R.string.deleteComplate, Toast.LENGTH_SHORT).show();
                                    addressAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ActivityProfile.this, R.string.error, Toast.LENGTH_SHORT).show();
                                }
                                HideProgressDialog();
                            }
                        });
                    }
                });
                builder1.create().show();
                return true;
            }
        });
        viewDialog.listHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                n_User.setAddress(n_User.getLocations().get(position));
                db.collection(Tag.DTO_User).document(user.getUid()).set(n_User).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                            Toast.makeText(ActivityProfile.this, R.string.choiceAddress, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ActivityProfile.this, R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        viewDialog.ibtnAddhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change[0] ==0){
                    viewDialog.edtThemhang.setVisibility(View.VISIBLE);
                    change[0] =1;
                }else {
                    String address = viewDialog.edtThemhang.getText().toString();
                    if (address.isEmpty()){
                        Toast.makeText(ActivityProfile.this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
                        HideProgressDialog();
                        return;
                    }
                    viewDialog.edtThemhang.setVisibility(View.GONE);
                    change[0] = 0;
                    n_User.getLocations().add(address);
                    db.collection(Tag.DTO_User).document(user.getUid()).set(n_User).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()){
                                Toast.makeText(ActivityProfile.this, R.string.choiceAddress, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ActivityProfile.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public  void ChoiceLanguage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogThemHangBinding viewDialog = DialogThemHangBinding.inflate(getLayoutInflater(),null,false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewDialog.tvTittle2.setText(R.string.language);
        viewDialog.edtThemhang.setVisibility(View.GONE);
        viewDialog.ibtnAddhang.setVisibility(View.GONE);

        List<String> languages = new ArrayList<>();
        languages.add(getString(R.string.lang_vn));
        languages.add(getString(R.string.lang_en));

        AddressAdapter languegeadapter = new AddressAdapter(languages,this);
        viewDialog.listHang.setAdapter(languegeadapter);

        viewDialog.listHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String langCode = "";
                if (languages.get(position).equals(getString(R.string.lang_vn))){
                    langCode = "vi";
                } else if (languages.get(position).equals(getString(R.string.lang_en))) {
                   langCode = "en";
                }else {
                    Log.e(Tag.TAG_LOG, "onItemClick: "+"nothing" );
                }
                ActivityExtentions.SetLocal(ActivityProfile.this,langCode);
                recreate();
                SaveLanguege(langCode);
                dialog.cancel();
            }
        });

    }


    private void SaveLanguege(String langCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang", langCode);
        editor.apply();
    }
    public void HistoryBuy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogLichsuBinding viewDialog = DialogLichsuBinding.inflate(getLayoutInflater(),null,false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        List<Bill> billList = new ArrayList<>();
        BillAdapter billAdapter = new BillAdapter(this, billList, new ICallBackAction() {
            @Override
            public void CallBack(Object... obj) {

            }
        });
        viewDialog.rcvListLichsu.setAdapter(billAdapter);
        viewDialog.rcvListLichsu.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        GetBillList(new ICallBackAction() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void CallBack(Object... obj) {
                billList.clear();
                billList.addAll((List<Bill>)obj[0]);
                billAdapter.notifyDataSetChanged();
            }
        });
    }

    private void GetBillList(ICallBackAction action) {
        db.collection(Tag.DTO_BILL)
                .whereEqualTo("idUser",n_User.getId())
                .whereEqualTo("status",1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()){
                            Toast.makeText(ActivityProfile.this, R.string.Nothing, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        action.CallBack(task.getResult().toObjects(Bill.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ",e );
                    }
                });
    }

    public void ChangePassWord() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogDoiMatKhauBinding viewDialog = DialogDoiMatKhauBinding.inflate(getLayoutInflater(), null, false);
        builder.setView(viewDialog.getRoot());
        Dialog dialog = builder.create();
        dialog.show();

        viewDialog.btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgressDialog();
                String newPassword = viewDialog.edtNhapmkmoi.getText().toString();
                String comfirmPassword = viewDialog.edtXacnhanmk.getText().toString();

                if (newPassword.isEmpty() || comfirmPassword.isEmpty()) {
                    Toast.makeText(ActivityProfile.this, R.string.IsEmpty, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                if (!newPassword.equals(comfirmPassword)) {
                    Toast.makeText(ActivityProfile.this, R.string.WrongPasswordConfirm, Toast.LENGTH_SHORT).show();
                    HideProgressDialog();
                    return;
                }
                user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ActivityProfile.this, R.string.UpdatePassCompalete, Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        HideProgressDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Tag.TAG_LOG, "onFailure: ", e);
                        HideProgressDialog();
                    }
                });
            }
        });
    }

    public void SignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Notifi);
        builder.setIcon(R.drawable.user1);
        builder.setMessage(R.string.signOutUser);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ActivityProfile.this, ActivityLogin.class);
                finishAffinity();
                if (!isFinishing()) {
                    return;
                }
                startActivity(intent);
            }
        });
        builder.create().show();

    }

    public void ShowProgressDialog() {
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(getString(R.string.MessageLoading));
        progressDialog.show();
    }

    public void HideProgressDialog() {
        progressDialog.cancel();
    }

    public void GetImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
        progressDialog.show();
    }

    @SuppressLint("ObsoleteSdkInt")
    public void GetPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            GetImage();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permission = new String[]{android.Manifest.permission.READ_MEDIA_IMAGES};
            requestPermissions(permission, CODE_IMAGE);
            return;
        }
        if (context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // xử lý sau
            GetImage();
        } else {
            String[] permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, CODE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetImage();
            } else {
                Toast.makeText(this, R.string.GetPermission, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void PutImageOnDataBase(Uri imageUri) {
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("Avatar").child(user.getUid());
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.getResult() != null) {
                                    Uri uri = task.getResult();
                                    getImage.CallBack(uri);
                                } else {
                                    Toast.makeText(ActivityProfile.this, R.string.errorGetLink, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityProfile.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
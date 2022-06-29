package com.example.projectmoveek.fragment_main;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectmoveek.controller.DatabaseHelper;
import com.example.projectmoveek.MainActivity;
import com.example.projectmoveek.R;
import com.example.projectmoveek.SignInActivity;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseHelper mDbHelper;
    private Context mContext;

    private ImageView imgSignOut, imgAvatar, imgEditProfile, imgChangePassword;
    private Button btnSignIn;
    private TextView tvFullname, tvEmail, tvPhoneNum, tvNumFavor, tvNumDownload;
    private LinearLayout uiSignIn, uiSignOut;

    private View dialogViewEditInfo, dialogViewChangePassword;

    private String fid;

    // IP tro
//    private String ip = "192.168.0.111";
    // IP nha
    private String ip = "192.168.0.109";

    private String url_get_user = "http://" + ip + "/android_moveek/get_data_user.php";
    private String url_get_favor = "http://" + ip + "/android_moveek/get_data_movie_favor.php";
    private String url_update_avatar = "http://" + ip + "/android_moveek/update_user_avatar.php";
    private String url_update_info = "http://" + ip + "/android_moveek/update_user_info.php";
    private String url_update_password = "http://" + ip + "/android_moveek/update_user_password.php";
    private String path = "Avatars/" + fid + "/avatar_new.jpg";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        dialogViewEditInfo = inflater.inflate(R.layout.edit_info_dialog, container, false);
        dialogViewChangePassword = inflater.inflate(R.layout.change_password_dialog, container, false);

        uiSignIn = root.findViewById(R.id.ui_signed_in);
        uiSignOut = root.findViewById(R.id.ui_signed_out);

        // UI when haven't signed in
        btnSignIn = root.findViewById(R.id.btn_sign_in);

        // UI when have signed in
        imgSignOut = root.findViewById(R.id.img_logout_change);
        imgAvatar = root.findViewById(R.id.img_avatar);
        imgChangePassword = root.findViewById(R.id.img_password_change);
        imgEditProfile = root.findViewById(R.id.img_edit_profile);
        tvFullname = root.findViewById(R.id.tv_name);
        tvEmail = root.findViewById(R.id.tv_mail);
        tvPhoneNum = root.findViewById(R.id.tv_phone_num_change);
        tvNumFavor = root.findViewById(R.id.tv_favorite_num);
        tvNumDownload = root.findViewById(R.id.tv_download_num);

        mAuth = FirebaseAuth.getInstance();
        mDbHelper = new DatabaseHelper(getContext());

        // Get context
        mContext = getContext();


        // Check have signed in to set view
        setView();

        // Catch event click sign in if haven't signed in
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });


        // Catch event click sign out
        imgSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutUser();
            }
        });


        // Catch event click avatar
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        // Catch event click edit profile
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        // Catch event click change password
        imgChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        return root;
    }

    private void signOutUser() {
        mAuth.signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void changePassword() {
        // Get response to check password is correct?
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_get_user, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AlertDialog.Builder editProfileDialog = new AlertDialog.Builder(getContext());
                        if (dialogViewChangePassword.getParent() != null) {
                            ((ViewGroup) dialogViewChangePassword.getParent()).removeView(dialogViewChangePassword);
                        }
                        EditText edtPasswordOld = dialogViewChangePassword.findViewById(R.id.edt_password_old);
                        EditText edtPasswordNew = dialogViewChangePassword.findViewById(R.id.edt_password_new);
                        EditText edtPasswordConfirm = dialogViewChangePassword.findViewById(R.id.edt_password_confirm);

                        edtPasswordOld.setText("");
                        edtPasswordNew.setText("");
                        edtPasswordConfirm.setText("");

                        editProfileDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                                Boolean check = true;
                                // Check password old
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        JSONObject object = response.getJSONObject(i);
                                        if (object.getString("FID").equals(mAuth.getCurrentUser().getUid())) {
                                            String strPasswordDB = object.getString("Password");
                                            // Check if password is true?
                                            if (edtPasswordOld.getText().toString().equals(strPasswordDB)) {
                                                Toast.makeText(mContext, "Correct Password", Toast.LENGTH_SHORT).show();
                                                check = true;
                                                break;
                                            } else {
                                                check = false;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getContext(), "Loi json" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                // CHECK NEW PASSWORD
                                if (edtPasswordNew.getText().toString().isEmpty() == true) {
                                    check = false;
                                }
                                if (edtPasswordNew.getText().toString().length() < 6) {
                                    check = false;
                                }

                                // CHECK CONFIRM PASSWORD
                                if (edtPasswordConfirm.getText().toString().isEmpty() == true) {
                                    check = false;
                                }
                                if (!edtPasswordConfirm.getText().toString().equals(edtPasswordNew.getText().toString())) {
                                    check = false;
                                }

                                // Update to database
                                if (check) {
                                    RequestQueue requestQueuePassword = Volley.newRequestQueue(mContext);
                                    StringRequest stringRequestPassword = new StringRequest(Request.Method.POST, url_update_password,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if (response.trim().equals("success")) {
                                                        Toast.makeText(mContext, "Cap nhat len database thanh cong", Toast.LENGTH_SHORT).show();
                                                        setView();
                                                    } else {
                                                        Toast.makeText(mContext, "Cap nhat len database that bai", Toast.LENGTH_SHORT).show();
                                                        Log.e("BBB", response);
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(mContext, "Phan hoi that bai", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                    ) {
                                        @Nullable
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("fidUser", fid);
                                            params.put("passwordUser", edtPasswordNew.getText().toString());

                                            return params;
                                        }
                                    };
                                    requestQueuePassword.add(stringRequestPassword);

                                    // Re-authentication
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(user.getEmail(), edtPasswordOld.getText().toString());

                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        user.updatePassword(edtPasswordNew.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "Password updated");
                                                                } else {
                                                                    Log.d(TAG, "Error password not updated");
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.d(TAG, "Error auth failed");
                                                    }
                                                }
                                            });

                                    signOutUser();
                                } else
                                    Toast.makeText(mContext, "Yeu cau dien dung cu phap!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        editProfileDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Close the dialog

                            }
                        });
                        editProfileDialog.setTitle("Change password");
                        editProfileDialog.setView(dialogViewChangePassword);
                        editProfileDialog.create().show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "Loi phan hoi" + error);
                        Toast.makeText(getContext(), "Loi phan hoi " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void setView() {
        if (mAuth.getCurrentUser() != null) {
            uiSignOut.setVisibility(View.GONE);
            uiSignIn.setVisibility(View.VISIBLE);

            // Get fid user
            fid = mAuth.getCurrentUser().getUid();

            // Get number of favor and download
            setNumFavorDownload();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url_get_user, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    if (object.getString("FID").equals(mAuth.getCurrentUser().getUid())) {
                                        UserModel model = new UserModel(object.getInt("ID"), object.getString("FID")
                                                , object.getString("Email")
                                                , object.getString("Password")
                                                , object.getString("Fullname")
                                                , object.getString("Phonenum")
                                                , object.getString("Avatar")
                                                , object.getString("Bio"));
                                        // Set avatar
                                        Glide.with(getContext()).load(model.getAvatar()).into(imgAvatar);
                                        // Set fullname
                                        tvFullname.setText(model.getFullname());
                                        // Set email
                                        tvEmail.setText(model.getEmail());
                                        // Set phonenum
                                        tvPhoneNum.setText(model.getPhoneNum());
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Loi json" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("AAA", "Loi phan hoi" + error);
                            Toast.makeText(getContext(), "Loi phan hoi " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(jsonArrayRequest);

        } else {
            uiSignIn.setVisibility(View.GONE);
            uiSignOut.setVisibility(View.VISIBLE);
        }
    }

    private void setNumFavorDownload() {
        List<ItemModel> mList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Set num favor
        StringRequest requestLike = new StringRequest(Request.Method.POST, url_get_favor,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseLike) {
                        // Parse JSON
                        try {
                            JSONArray jsonArray = new JSONArray(responseLike);
                            tvNumFavor.setText(jsonArray.length() + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Loi JSON", error.getMessage());
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("u_fid", fid);
                return params;
            }
        };
        requestQueue.add(requestLike);

        // Set num download
        // Path storage
        File fileDownload = new File("/storage/emulated/0/Android/data/com.example.projectmoveek/files/Download");
        File[] listFileMovie = fileDownload.listFiles();
        int countDownload = 0;
        for (File f : listFileMovie) {
            countDownload++;
        }
        tvNumDownload.setText(countDownload + "");
    }

    private void editProfile() {
        AlertDialog.Builder editProfileDialog = new AlertDialog.Builder(getContext());

        if (dialogViewEditInfo.getParent() != null) {
            ((ViewGroup) dialogViewEditInfo.getParent()).removeView(dialogViewEditInfo);
        }
        EditText editName = dialogViewEditInfo.findViewById(R.id.edt_new_name);
        EditText editPhoneNum = dialogViewEditInfo.findViewById(R.id.edt_new_phonenum);

        editName.setText(tvFullname.getText().toString());
        editPhoneNum.setText(tvPhoneNum.getText().toString());

        editProfileDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Update to database
                String newName = editName.getText().toString();
                String newPhoneNum = editPhoneNum.getText().toString();

                // CHECK FULLNAME
                if (newName.isEmpty() == true) {
                    editName.setError("Fullname is required!");
                    return;
                }

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_info,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.trim().equals("success")) {
                                    Toast.makeText(mContext, "Cap nhat len database thanh cong", Toast.LENGTH_SHORT).show();
                                    setView();
                                } else {
                                    Toast.makeText(mContext, "Cap nhat len database that bai", Toast.LENGTH_SHORT).show();
                                    Log.e("BBB", response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, "Phan hoi that bai", Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("fidUser", fid);
                        params.put("nameUser", newName);
                        params.put("phonenumUser", newPhoneNum);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });

        editProfileDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Close the dialog

            }
        });
        editProfileDialog.setTitle("Edit Profile");
        editProfileDialog.setView(dialogViewEditInfo);
        editProfileDialog.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                Glide.with(getContext()).load(imageUri).into(imgAvatar);

                // Upload image to firebase
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference fileRef = mStorageRef.child(path);
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(mContext, "Cap nhat len firebase thanh cong", Toast.LENGTH_SHORT).show();

                        // Get image to update to database
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(mContext, "Lay image thanh cong", Toast.LENGTH_SHORT).show();

                                // New avatar's url
                                String urlNewAvatar = uri.toString();

                                // Update to database
                                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_avatar,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.trim().equals("success")) {
                                                    Toast.makeText(mContext, "Cap nhat len database thanh cong", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mContext, "Cap nhat len database that bai", Toast.LENGTH_SHORT).show();
                                                    Log.e("BBB", response);
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(mContext, "Phan hoi that bai", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                ) {
                                    @Nullable
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("fidUser", fid);
                                        params.put("avatarUser", urlNewAvatar);

                                        return params;
                                    }
                                };
                                requestQueue.add(stringRequest);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "Lay image that bai", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Cap nhat len firebase that bai", Toast.LENGTH_SHORT).show();
                        Log.e("AAA", e.getMessage());
                    }
                });
            }
        }

    }
}
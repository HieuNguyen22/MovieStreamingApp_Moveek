package com.example.projectmoveek.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmoveek.adapter.ItemAdapterLong;
import com.example.projectmoveek.adapter.ItemAdapter;
import com.example.projectmoveek.adapter.SliderAdapter;
import com.example.projectmoveek.model.CastJoinMovieModel;
import com.example.projectmoveek.model.CastModel;
import com.example.projectmoveek.model.HireModel;
import com.example.projectmoveek.model.ItemModel;
import com.example.projectmoveek.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mcontext;

    public static final String MOVIE_TABLE = "MOVIE_TABLE";
    public static final String M_ID = "mID";
    public static final String M_TITLE = "mITLE";
    public static final String M_POSTER = "mPOSTER";
    public static final String M_YEAR = "mYEAR";
    public static final String M_LENGTH = "mLENGTH";
    public static final String M_TYPE = "mTYPE";
    public static final String M_GENRE = "mGENRE";
    public static final String M_DESCRIPTION = "mDESCRIPTION";
    public static final String M_TRAILER = "mTrailer";
    public static final String M_IMDB = "mIMDB";
    public static final String M_IS_FAVOR = "mIS_FAVOR";
    public static final String mDescription = "Set during the Cold War era, orphaned chess prodigy Beth Harmon struggles with addiction in a quest to become the greatest chess player in the world.";


    public static final String CAST_TABLE = "CAST_TABLE";
    public static final String C_ID = "cID";
    public static final String C_NAME = "cNAME";
    public static final String C_IMAGE = "cIMAGE";


    public static final String USER_TABLE = "USER_TABLE";
    public static final String U_ID = "uID";
    public static final String U_FID = "fID";
    public static final String U_EMAIL = "uEmail";
    public static final String U_PASSWORD = "uPassword";
    public static final String U_FULLNAME = "uFullname";
    public static final String U_PHONENUM = "uPhoneNum";
    public static final String U_AVATAR = "uAvatar";
    public static final String U_BIO = "uBio";


    public static final String HIRE_TABLE = "HIRE_TABLE";
    public static final String H_ID = "hID";
    public static final String H_CHAR_NAME = "h_CHAR_NAME";


    public static final String LIKE_TABLE = "LIKE_TABLE";
    public static final String L_ID = "lID";
    public static final String L_ISLIKED = "lIsLiked";

    // IP tro
//    public static final String ip = "192.168.0.102";
    // IP nha
//    public static final String ip = "192.168.0.109";
    // IP dt
    private String ip = "192.168.43.134";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "moveek_app.db", null, 1);
        this.mcontext = context;
    }


    // This is called the first time a database is accessed. There should be code in here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Delete old table
//        String deleteTableStatement = "DROP TABLE IF EXISTS " + MOVIE_TABLE;
//        db.execSQL(deleteTableStatement);

        // Create table MOVIE
        String createMovieTableStatement = "CREATE TABLE " + MOVIE_TABLE + " (" + M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + M_TITLE + " TEXT, " + M_POSTER + " TEXT, " +
                M_YEAR + " TEXT, " + M_LENGTH + " TEXT, " + M_TYPE + " TEXT, " + M_GENRE + " TEXT, " + M_DESCRIPTION + " TEXT, " +
                M_TRAILER + " TEXT, " + M_IMDB + " FLOAT, " + M_IS_FAVOR + " BOOLEAN)";

        // Create table CAST
        String createCastTableStatement = "CREATE TABLE " + CAST_TABLE + " (" + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_NAME + " TEXT, " + C_IMAGE + " TEXT)";

        // Create table USER
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + " (" + U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + U_FID + " TEXT, " + U_EMAIL + " TEXT, " +
                U_PASSWORD + " TEXT, " + U_FULLNAME + " TEXT, " + U_PHONENUM + " TEXT, " +
                U_AVATAR + " TEXT, " + U_BIO + " TEXT)";

        // Create table HIRE
        String createHireTableStatement = "CREATE TABLE " + HIRE_TABLE + " (" + H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + M_ID + " INTEGER, " + C_ID + " INTEGER, " + H_CHAR_NAME + " TEXT)";
        // Create table LIKE
        String createLikeTableStatement = "CREATE TABLE " + LIKE_TABLE + " (" + L_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + M_ID + " INTEGER, " + U_FID + " TEXT, " + L_ISLIKED + " BOOLEAN)";

        db.execSQL(createMovieTableStatement);
        db.execSQL(createCastTableStatement);
        db.execSQL(createUserTableStatement);
        db.execSQL(createHireTableStatement);
        db.execSQL(createLikeTableStatement);

        // Add movie data
//        addMovieData(new ItemModel(-1, "Enemy", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_poster%2FEnemy_poster.jpg?alt=media&token=d0304c87-1f37-452c-aa4c-8cb54c3295c6"
//                , "2013(USA)", "1h31m", "Movie", "Drama/Comedy", mDescription
//                , "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_trailer%2Fenemy_trailer.mp4?alt=media&token=d7afcdfe-0508-4bc6-b349-ea1916cd9599"
//                , 6.9, false), db);
//
//        addMovieData(new ItemModel(-1, "Dune", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_poster%2Fdune_poster.jpg?alt=media&token=d305539f-6d24-46d1-ada2-a06fab2fe00c"
//                , "2021(USA)", "2h31m", "Movie", "Fantasy", mDescription
//                , "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_trailer%2Fdune_trailer.mp4?alt=media&token=4a5fa771-9bdf-4448-8934-69f7a19346c2"
//                , 8.1, false), db);
//
//        addMovieData(new ItemModel(-1, "The Batman", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_poster%2Fthe_batman_poster.jpg?alt=media&token=98c9c73f-ffce-4fdd-b12c-0e834e66fe52"
//                , "2022(USA)", "2h40m", "Movie", "Drama/Comedy", mDescription
//                , "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_trailer%2Fthe_batman_trailer.mp4?alt=media&token=bfd739a3-7459-4755-816d-5c648596e8e2"
//                , 8.5, false), db);
//
//        addMovieData(new ItemModel(-1, "Doctor Strange", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_poster%2Fdoctor_strange_poster.jpg?alt=media&token=9622bd8c-9db2-4cab-961f-51e02c0a9932"
//                , "2022(USA)", "2h15m", "Movie", "Sci-fi", mDescription
//                , "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/movie_trailer%2Fdoctor_strange_trailer.mp4?alt=media&token=40a23e53-aada-4407-8c09-147d5dc25f00"
//                , 7.2, false), db);

        // Add cast data
//        addCastData(new CastModel(-1, "Jake Gyllenhaal", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fjake_gyllenhaal.jpg?alt=media&token=c4371b7f-e606-454a-b4fd-2fd0dbc03240"), db);
//        addCastData(new CastModel(-1, "Melanie Laurent", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fmelanie_laurent.jpg?alt=media&token=b6aad224-a806-4a1b-8703-e29cc8c613bc"), db);
//        addCastData(new CastModel(-1, "Sarah Gadon", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fsarah_gadon.jpg?alt=media&token=05efe454-1308-4fe9-bfe4-fe1026c5fceb"), db);
//
//        addCastData(new CastModel(-1, "Timothee Chalamet", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Ftimothee_chalamet.jpg?alt=media&token=b3212c6f-1b67-49b8-9854-784187d0d5d6"), db);
//        addCastData(new CastModel(-1, "Zendaya", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fzendaya.jpg?alt=media&token=a17e5701-8169-44d5-8d82-741c8bbd8f22"), db);
//        addCastData(new CastModel(-1, "Rebecca Ferguson", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Frebecca_ferguson.jpg?alt=media&token=0bac5564-c925-4836-97da-4cb36d0866d0"), db);
//        addCastData(new CastModel(-1, "Oscar Isaac", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Foscar_isaac.jpg?alt=media&token=07d012f3-97a5-419a-999e-8d60ca24e0e3"), db);
//        addCastData(new CastModel(-1, "Jason Momoa", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fjason_momoa.jpg?alt=media&token=2bb33309-ae1f-493c-b133-fb9671ecf886"), db);
//        addCastData(new CastModel(-1, "Stellan Skarsgard", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fstellan_skarsgard.jpg?alt=media&token=d1aa2cdf-a438-445c-bdc8-90fccd1e44d6"), db);
//
//        addCastData(new CastModel(-1, "Robert Pattinson", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Frobert_pattinson.jpg?alt=media&token=a8f14491-2fae-459f-b4bf-65032e14cf07"), db);
//        addCastData(new CastModel(-1, "Zoe Kravitz", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fzoe_kravitz.jpg?alt=media&token=f80246f8-a84f-4277-91f5-ba11c3d6d0e2"), db);
//        addCastData(new CastModel(-1, "Paul Dano", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fpaul_dano.jpg?alt=media&token=ee951870-5144-447b-a5a0-7aa6eba04261"), db);
//        addCastData(new CastModel(-1, "Colin Farrell", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fcolin_farrell.jpg?alt=media&token=81857b7d-6d3a-4150-a1a2-055fd37b4107"), db);
//
//        addCastData(new CastModel(-1, "Benedict Cumberbatch", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fbenedict_cumberbatch.jpg?alt=media&token=9f8c72dd-772d-4d25-831b-374f2104129b"), db);
//        addCastData(new CastModel(-1, "Elizabeth Olsen", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Felizabeth_olsen.jpg?alt=media&token=87e727f8-8032-4d63-93d9-5b04f1f27fd9"), db);
//        addCastData(new CastModel(-1, "Patrick Steward", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fpatrick_steward.jpg?alt=media&token=d2a8d3a1-7263-48cb-bbac-1b44f6a30f1e"), db);
//        addCastData(new CastModel(-1, "Xochitl Gomez", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fxochitl_gomez.jpg?alt=media&token=faabd28e-bf0f-4329-9bf9-2f5ae71b123d"), db);
//        addCastData(new CastModel(-1, "Bruce Campbell", "https://firebasestorage.googleapis.com/v0/b/projectmoveekapp.appspot.com/o/cast_image%2Fbruce_campbell.jpg?alt=media&token=e33bf6e1-0f29-42b0-b753-dcf08d428078"), db);
//

        // Add hire data
        addHireData(new HireModel(-1, 1, 1, "Adam Bell"), db);
        addHireData(new HireModel(-1, 1, 2, "Mary"), db);
        addHireData(new HireModel(-1, 1, 3, "Helen Bell"), db);

        addHireData(new HireModel(-1, 2, 4, "Paul Atreides"), db);
        addHireData(new HireModel(-1, 2, 5, "Chani"), db);
        addHireData(new HireModel(-1, 2, 6, "Lady Jessica"), db);
        addHireData(new HireModel(-1, 2, 7, "Leto Atreides"), db);
        addHireData(new HireModel(-1, 2, 8, "Duncan Idaho"), db);
        addHireData(new HireModel(-1, 2, 9, "Vladimir Harkonnen"), db);

        addHireData(new HireModel(-1, 3, 10, "Bruce Wayne"), db);
        addHireData(new HireModel(-1, 3, 11, "Selina Kyle"), db);
        addHireData(new HireModel(-1, 3, 12, "Riddler"), db);
        addHireData(new HireModel(-1, 3, 13, "Penguin"), db);

        addHireData(new HireModel(-1, 4, 14, "Doctor Strange"), db);
        addHireData(new HireModel(-1, 4, 15, "Wanda Maximoff"), db);
        addHireData(new HireModel(-1, 4, 16, "Charles Xavier"), db);
        addHireData(new HireModel(-1, 4, 17, "America Chavez"), db);
        addHireData(new HireModel(-1, 4, 18, "Vladimir Harkonnen"), db);

    }

    // This is called if the database version number changes. It prevents previous users apps from breaking when  you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private void addCastData(CastModel castModel, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(C_NAME, castModel.getName());
        cv.put(C_IMAGE, castModel.getImage());

        db.insert(CAST_TABLE, null, cv);
    }

    public void addUserData(UserModel userModel) {
        String url = "http://" + ip + "/android_moveek/insert_user.php";
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(mcontext, "Tao tai khoan thanh cong", Toast.LENGTH_SHORT).show();
                            Log.e("AAA", response.trim());
                        } else {
                            Toast.makeText(mcontext, "Loi khong tao duoc tai khoan", Toast.LENGTH_SHORT).show();
                            Log.e("BBB", response.trim());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mcontext, "Xay ra loi phan hoi! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fid", userModel.getfID().trim());
                params.put("email", userModel.getEmail().trim());
                params.put("password", userModel.getPassword().trim());
                params.put("fullname", userModel.getFullname().trim());
                params.put("phonenum", userModel.getPhoneNum().trim());
                params.put("avatar", userModel.getAvatar().trim());
                params.put("bio", userModel.getBio().trim());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void addHireData(HireModel hireModel, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(M_ID, hireModel.getmID());
        cv.put(C_ID, hireModel.getcID());
        cv.put(H_CHAR_NAME, hireModel.getCharName());

        db.insert(HIRE_TABLE, null, cv);
    }

    public void addLikeData(int mID, String uFID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(M_ID, mID);
        cv.put(U_FID, uFID);
        cv.put(L_ISLIKED, true);

        db.insert(LIKE_TABLE, null, cv);
    }

    public void addUnlikeData(int mID, String uFID) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "DELETE FROM " + MOVIE_TABLE + " WHERE " + M_ID + " = " + "\"" + mID + "\"" + " AND " + U_FID + " = " + "\"" + uFID + "\"";
        db.execSQL(queryString);
    }

    private boolean checkIsLiked(int mID, String uFID) {
        boolean check = false;

        // Get data from database
        String queryString = "SELECT " + L_ISLIKED + " FROM " + LIKE_TABLE + " WHERE " + M_ID + " = " + "\"" + mID + "\"" + " AND " + U_FID + " = " + "\"" + uFID + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                check = cursor.getInt(0) == 1 ? true : false;

            } while (cursor.moveToNext());
        } else {
            // failure, do not add anything
        }

        cursor.close();
        db.close();
        return check;
    }

    public List<ItemModel> getAllMoviesItem(ItemAdapter adapter) {
        String url = "http://" + ip + "/android_moveek/get_data_movie.php";
        List<ItemModel> mList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                mList.add(new ItemModel(object.getInt("ID"), object.getString("Title")
                                        , object.getString("Poster")
                                        , object.getString("Year")
                                        , object.getString("Length")
                                        , object.getString("Type")
                                        , object.getString("Genre")
                                        , object.getString("Description")
                                        , object.getString("Trailer")
                                        , object.getDouble("Imdb")));
                            } catch (JSONException e) {
                                Toast.makeText(mcontext, "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "Loi phan hoi" + error);
                        Toast.makeText(mcontext, "Loi phan hoi! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return mList;
    }

    public List<ItemModel> getAllMoviesItemSlider(SliderAdapter adapter) {
        String url = "http://" + ip + "/android_moveek/get_data_movie.php";
        List<ItemModel> mList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                mList.add(new ItemModel(object.getInt("ID"), object.getString("Title")
                                        , object.getString("Poster")
                                        , object.getString("Year")
                                        , object.getString("Length")
                                        , object.getString("Type")
                                        , object.getString("Genre")
                                        , object.getString("Description")
                                        , object.getString("Trailer")
                                        , object.getDouble("Imdb")));
                            } catch (JSONException e) {
                                Toast.makeText(mcontext, "Loi JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "Loi phan hoi" + error);
                        Toast.makeText(mcontext, "Loi phan hoi! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return mList;
    }

    public List<ItemModel> getAllMoviesItemLong(ItemAdapterLong adapter) {
        String url = "http://" + ip + "/android_moveek/get_data_movie.php";

        List<ItemModel> mList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);

                                mList.add(new ItemModel(object.getInt("ID"), object.getString("Title"), object.getString("Poster"), object.getString("Year")
                                        , object.getString("Length")
                                        , object.getString("Type")
                                        , object.getString("Genre")
                                        , object.getString("Description")
                                        , object.getString("Trailer")
                                        , object.getDouble("Imdb")));
                            } catch (JSONException e) {
                                Toast.makeText(mcontext, "cmm" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "dcmm" + error);
                        Toast.makeText(mcontext, "DIT CON ME MAY! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return mList;
    }

    public List<ItemModel> getMoviesFavor(ItemAdapterLong adapter, String fidUser) {
        String url = "http://" + ip + "/android_moveek/get_data_movie_favor.php";
        List<ItemModel> mList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        // Check favor
        StringRequest requestLike = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseLike) {
                        // Parse JSON
                        try {
                            JSONArray jsonArray = new JSONArray(responseLike);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                mList.add(new ItemModel(object.getInt("ID"), object.getString("Title"), object.getString("Poster"), object.getString("Year")
                                        , object.getString("Length")
                                        , object.getString("Type")
                                        , object.getString("Genre")
                                        , object.getString("Description")
                                        , object.getString("Trailer")
                                        , object.getDouble("Imdb")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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
                params.put("u_fid", fidUser);
                return params;
            }
        };
        requestQueue.add(requestLike);

        return mList;
    }

    // WHEN USE DATABASE IN THE APP
//    public List<ItemModel> getAllMovies(String uFID) {
//        List<ItemModel> mList = new ArrayList<>();
//        // get data from database
//        String queryString = "SELECT * FROM " + MOVIE_TABLE;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(queryString, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                int movieID = cursor.getInt(0);
//                String movieTitle = cursor.getString(1);
//                String moviePoster = cursor.getString(2);
//                String movieYear = cursor.getString(3);
//                String movieLength = cursor.getString(4);
//                String movieType = cursor.getString(5);
//                String movieGenre = cursor.getString(6);
//                String movieDescription = cursor.getString(7);
//                String trailerURL = cursor.getString(8);
//                Double movieImdb = cursor.getDouble(9);
//                boolean movieIsFavor = checkIsLiked(movieID, uFID);
//
//                ItemModel itemModel = new ItemModel(movieID, movieTitle, moviePoster, movieYear, movieLength, movieType, movieGenre, movieDescription, trailerURL, movieImdb, movieIsFavor);
//                mList.add(itemModel);
//
//            } while (cursor.moveToNext());
//        } else {
//            // failure, do not add anything
//        }
//        cursor.close();
//        db.close();
//        return mList;
//    }

    public UserModel getUserData(String fID) {
        final UserModel[] model = new UserModel[1];
        // IP tro
        String url = "http://" + ip + "/android_moveek/get_data_user.php";

        RequestQueue requestQueue = Volley.newRequestQueue(mcontext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                if (object.getString("FID").equals(fID)) {
                                    model[0] = new UserModel(object.getInt("ID"), object.getString("FID"), object.getString("Email"), object.getString("Password")
                                            , object.getString("Fullanme")
                                            , object.getString("Phonenum")
                                            , object.getString("Avatar")
                                            , object.getString("Bio"));
                                }
                            } catch (JSONException e) {
                                Toast.makeText(mcontext, "cmm" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AAA", "dcmm" + error);
                        Toast.makeText(mcontext, "DIT CON ME MAY! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
        return model[0];
    }


    public List<CastJoinMovieModel> getCastJoinMovie(int mId) {
        List<CastJoinMovieModel> mList = new ArrayList<>();
        String queryString =
                "SELECT " + HIRE_TABLE + "." + H_ID + ", " + CAST_TABLE + "." + C_NAME + ", " + CAST_TABLE + "." + C_IMAGE + ", " + HIRE_TABLE + "." + H_CHAR_NAME
                        + " FROM " + CAST_TABLE
                        + " INNER JOIN " + HIRE_TABLE
                        + " ON " + "(" + mId + " = " + HIRE_TABLE + "." + M_ID + " AND " + CAST_TABLE + "." + C_ID + " = " + HIRE_TABLE + "." + C_ID + ")";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int hireID = cursor.getInt(0);
                String castName = cursor.getString(1);
                String castImage = cursor.getString(2);
                String charName = cursor.getString(3);

                CastJoinMovieModel model = new CastJoinMovieModel(hireID, castName, castImage, charName);
                mList.add(model);

            } while (cursor.moveToNext());
        } else {
            // failure, do not add anything
        }
        cursor.close();
        db.close();
        return mList;
    }

    public void updateFavoriteToTrue(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "UPDATE " + MOVIE_TABLE + " SET " + M_IS_FAVOR + " = " + 1 + " WHERE " + M_ID + " = " + id;
        db.execSQL(queryString);
    }

    public void updateFavoriteToFalse(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String queryString = "UPDATE " + MOVIE_TABLE + " SET " + M_IS_FAVOR + " = " + 0 + " WHERE " + M_ID + " = " + id;
        db.execSQL(queryString);
    }
}

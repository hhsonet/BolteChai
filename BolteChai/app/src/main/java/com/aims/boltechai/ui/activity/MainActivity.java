package com.aims.boltechai.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Select;
import com.aims.boltechai.R;
import com.aims.boltechai.model.Category;
import com.aims.boltechai.model.Help;
import com.aims.boltechai.ui.adapter.HelpAdapter;
import com.aims.boltechai.ui.fragment.ActivityListFragment;
import com.aims.boltechai.util.AppUtils;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Main Activity of the Application
 * Setup the data and other initial items.
 */
public class MainActivity extends AppCompatActivity implements HelpAdapter.HelpListener {

    private ActivityListFragment activeFragment;
    private ImageButton ibAdd;
    private ImageButton ibHb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // configure Database (Active Android)
        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Category.class, Help.class);
        ActiveAndroid.initialize(config.create());

        // Category and items containing fragment
        activeFragment = new ActivityListFragment();

        // check the first time use and setup initial data for Database
        if (isFirstTime()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(AppUtils.BOLTE_CHAI_INITIAL, AppUtils.STR_NO);
            editor.putString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_PARENT);
            editor.commit();
            setInitialData();
        }

        ibAdd = (ImageButton) findViewById(R.id.ib_add_main);
        ibHb = (ImageButton) findViewById(R.id.ib_hb_main);

        // navigation drawer
        Drawer();

        // read from cache and setup language mode
        setLanguage();

        // read from cache and setup user mode
        if (getRoleName().equals(AppUtils.MODE_PARENT)) {
            ibAdd.setVisibility(View.VISIBLE);
        } else {
            ibAdd.setVisibility(View.GONE);
        }

        // tells fragment about button click event
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getRoleName().equals(AppUtils.MODE_PARENT)) {
                    activeFragment.onAddButtonClicked();
                } else {
                    help();
                }
            }
        });

        // starting fragment that contains categories and items
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, activeFragment
                , "main_fragment").commit();
    }

    private void setLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ln = preferences.getString(AppUtils.BOLTE_CHAI_LANGUAGE, "en");
        setLocale(ln);
    }

    // setup initial data (category & items ) : once ( first time)
    private void setInitialData() {
        addsubEnglish();
        addsubBangla();
    }

    // initial data for english
    public void addsubEnglish() {
        Uri uri;
        File f;
        int parentID;
        String strRole;

        strRole = "en";

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.home);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryHome = new Category(0, "Home", f.getPath(), strRole);
        categoryHome.save();
        parentID = Integer.parseInt(categoryHome.getId().toString());
        addSubCategory(parentID, "My Home", R.drawable.my_home, R.raw.home_my_home);
        addSubCategory(parentID, "Visiting Friends", R.drawable.home2, R.raw.friendhome);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.shopping);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        Category categoryOut = new Category(0, "Out", f.getPath(), strRole);
        categoryOut.save();
        parentID = Integer.parseInt(categoryOut.getId().toString());
        addSubCategory(parentID, "Car Ride", R.drawable.car, R.raw.car);
        addSubCategory(parentID, "Shopping", R.drawable.shopping, R.raw.shopping);
        addSubCategory(parentID, "Therapy", R.drawable.therapy, R.raw.trpy);
        addSubCategory(parentID, "Restaurant", R.drawable.restaurant, R.raw.resturant);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hungry);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        Category categoryHungry = new Category(0, "Hungry", f.getPath(), strRole);
        categoryHungry.save();
        parentID = Integer.parseInt(categoryHungry.getId().toString());
        addSubCategory(parentID, "Apple", R.drawable.apple, R.raw.apple);
        addSubCategory(parentID, "Rice", R.drawable.rice, R.raw.rice);
        addSubCategory(parentID, "noodle", R.drawable.noodls, R.raw.noodle);
        addSubCategory(parentID, "Drink", R.drawable.water, R.raw.water);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.emotion);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        Category categoryEmotion = new Category(0, "Emotion", f.getPath(), strRole);
        categoryEmotion.save();
        parentID = Integer.parseInt(categoryEmotion.getId().toString());
        addSubCategory(parentID, "Happy", R.drawable.happy, R.raw.happy);
        addSubCategory(parentID, "Sad", R.drawable.sad, R.raw.sad);
        addSubCategory(parentID, "Hungry", R.drawable.hungry, R.raw.hungry);
        addSubCategory(parentID, "Angry", R.drawable.angry, R.raw.angry);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.shirt);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        Category categoryCloths = new Category(0, "Cloths", f.getPath(), strRole);
        categoryCloths.save();
        parentID = Integer.parseInt(categoryCloths.getId().toString());
        addSubCategory(parentID, "Shirt", R.drawable.shirt, R.raw.shirt);
        addSubCategory(parentID, "Pant", R.drawable.pant, R.raw.pant);
        addSubCategory(parentID, "Shorts", R.drawable.shorts, R.raw.shorts);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ball);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        Category categoryPlay = new Category(0, "Play", f.getPath(), strRole);
        categoryPlay.save();
        parentID = Integer.parseInt(categoryPlay.getId().toString());
        addSubCategory(parentID, "Ball", R.drawable.ball, R.raw.ball);
        addSubCategory(parentID, "Puzzle", R.drawable.puzzle, R.raw.puzzle);
        addSubCategory(parentID, "Computer", R.drawable.computer, R.raw.computer);
    }

    // initial data for bangla
    public void addsubBangla() {
        Uri uri;
        File f;
        int parentID;

        String strRole;
        strRole = "bn";

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.shopping);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryHome = new Category(0, "ঘুরে বেড়ানো", f.getPath(), strRole);
        categoryHome.save();

        parentID = Integer.parseInt(categoryHome.getId().toString());
        addSubCategoryBn(parentID, "গাড়িতে চড়ব ", R.drawable.car, R.raw.gri);
        addSubCategoryBn(parentID, "শপিং ", R.drawable.shopping, R.raw.shoping_voice);
        addSubCategoryBn(parentID, "থেরাপি  ", R.drawable.therapy, R.raw.therapy_bn);
        addSubCategoryBn(parentID, "রেস্টুরেন্ট   ", R.drawable.restaurant, R.raw.resturent_bn);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.hungry);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryHungry = new Category(0, "খওয়াদাওয়া ", f.getPath(), strRole);
        categoryHungry.save();
        parentID = Integer.parseInt(categoryHungry.getId().toString());
        addSubCategoryBn(parentID, "আপেল ", R.drawable.apple, R.raw.applekhabo);
        addSubCategoryBn(parentID, "ভাত ", R.drawable.rice, R.raw.vaat);
        addSubCategoryBn(parentID, "নুডুলস", R.drawable.noodls, R.raw.noodls_bn);
        addSubCategoryBn(parentID, "পানি ", R.drawable.water, R.raw.pani);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.emotion);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryEmotion = new Category(0, "কেমন আছি ?", f.getPath(), strRole);
        categoryEmotion.save();
        parentID = Integer.parseInt(categoryEmotion.getId().toString());
        addSubCategoryBn(parentID, "ভাল আছি ", R.drawable.happy, R.raw.amivaloasi);
        addSubCategoryBn(parentID, "মন খারাপ ", R.drawable.sad, R.raw.monkharap);

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.shirt);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryCloths = new Category(0, "আমার জামা ", f.getPath(), strRole);
        categoryCloths.save();
        parentID = Integer.parseInt(categoryCloths.getId().toString());
        addSubCategoryBn(parentID, "জামা ", R.drawable.shirt, R.raw.jama);
        addSubCategoryBn(parentID, "প্যান্ট", R.drawable.pant, R.raw.pantporbo);
        //addSubCategory(parentID, "Shorts", R.drawable.shorts, R.raw.shorts);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.ball);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryPlay = new Category(0, "খেলাধুলা ", f.getPath(), strRole);
        categoryPlay.save();
        parentID = Integer.parseInt(categoryPlay.getId().toString());
        addSubCategoryBn(parentID, "বল ", R.drawable.ball, R.raw.ball_bn);
        addSubCategoryBn(parentID, "পাজল ", R.drawable.puzzle, R.raw.pazzel_bn);
        addSubCategoryBn(parentID, "গেম ", R.drawable.computer, R.raw.game_bn);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.b4);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryBrush = new Category(0, "দাত মাজা ", f.getPath(), strRole);
        categoryBrush.save();

        parentID = Integer.parseInt(categoryBrush.getId().toString());
        addSubCategoryBn(parentID, "ব্রাশ নেব ", R.drawable.b1, R.raw.brushnibo);
        addSubCategoryBn(parentID, "ব্রাশ ধুব ", R.drawable.b2, R.raw.brush_dhubo);
        addSubCategoryBn(parentID, "পেস্ট লাগাব", R.drawable.b3, R.raw.brush_e_pest_lagabo);
        addSubCategoryBn(parentID, "ব্রাশ করব ", R.drawable.b4, R.raw.brush_korbo);
        addSubCategoryBn(parentID, "কুলি করব ", R.drawable.b5, R.raw.kuli);
        addSubCategoryBn(parentID, "ব্রাশ ধুব ", R.drawable.b6, R.raw.brush_dhubo);
        addSubCategoryBn(parentID, "ট্যাপ বন্ধ করব ", R.drawable.b7, R.raw.tabbondho);
        addSubCategoryBn(parentID, "মুখ মুছব", R.drawable.b8, R.raw.mukhmuchbo);


        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.toilet1);
        f = AppUtils.saveImageFile(uri, "image_" + 0 + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());

        Category categoryToilet = new Category(0, "টয়লেট  ", f.getPath(), strRole);
        categoryToilet.save();
        parentID = Integer.parseInt(categoryToilet.getId().toString());
        addSubCategoryBn(parentID, "টয়লেট যাব", R.drawable.toilet1, R.raw.tjabo);
        addSubCategoryBn(parentID, "প্যান্ট খুলব", R.drawable.toilet2, R.raw.pantkhulbo);
        addSubCategoryBn(parentID, "টয়লেট করব ", R.drawable.toilet3, R.raw.toiletkorbo);
        addSubCategoryBn(parentID, "তিস্যু নিব ", R.drawable.toilet_4, R.raw.tissue);
        addSubCategoryBn(parentID, "পরিষ্কার করব", R.drawable.toilet_5, R.raw.poriskar);
        addSubCategoryBn(parentID, "প্যান্ট পরব", R.drawable.toilet_6, R.raw.pantporbo);
        addSubCategoryBn(parentID, "ফ্লাস করব", R.drawable.toilet_7, R.raw.flash);
        addSubCategoryBn(parentID, "সাবান দিয়ে হাত ধুব", R.drawable.toilet_8, R.raw.sabanhatdubo);
        addSubCategoryBn(parentID, "হাত ধুব", R.drawable.toilet_9, R.raw.hatmuchbo);
        addSubCategoryBn(parentID, "দরজা বন্ধ করব ", R.drawable.toilet_10, R.raw.dorja);


    }


    // add categories / items under parent Category (for English Mode)
    private void addSubCategory(int parent, String name, int image, int audio) {
        Uri uri, uri2;
        File f, f2;

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + image);
        uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + audio);

        f = AppUtils.saveImageFile(uri, "image_" + parent + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        f2 = AppUtils.saveAudioFile(uri2, "audio_" + parent + "_" + SystemClock.uptimeMillis() + ".mp3", getApplicationContext());

        Category category = new Category(parent, name, f.getPath(), f2.getPath(), "en");
        category.save();
    }

    // add categories / items under parent Category (for Bangla Mode)
    private void addSubCategoryBn(int parent, String name, int image, int audio) {
        Uri uri, uri2;
        File f, f2;

        uri = Uri.parse("android.resource://" + getPackageName() + "/" + image);
        uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + audio);
        f = AppUtils.saveImageFile(uri, "image_" + parent + "_" + SystemClock.uptimeMillis() + ".jpg", getApplicationContext());
        f2 = AppUtils.saveAudioFile(uri2, "audio_" + parent + "_" + SystemClock.uptimeMillis() + ".mp3", getApplicationContext());

        Category category = new Category(parent, name, f.getPath(), f2.getPath(), "bn");
        category.save();
    }

    // check the cache
    private boolean isFirstTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferences.getString(AppUtils.BOLTE_CHAI_INITIAL, AppUtils.STR_YES);
        if (name.equals(AppUtils.STR_YES)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (getRoleName().equals("Parent")) {
            menu.getItem(2).setTitle(R.string.child_mode);
        } else {
            menu.getItem(2).setTitle(R.string.parent_mode);
        }

        if (getRoleName().equals("Parent")) {
            menu.getItem(3).setTitle(R.string.string_set_help);
        } else {
            menu.getItem(3).setTitle(R.string.string_help);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_language) {
            languageSelection();
            return true;
        } else if (id == R.id.action_column) {
            showDialog();
            return true;
        } else if (id == R.id.action_user_mode) {
        } else if (id == R.id.action_help) {
            help();
        } else if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }


    // controlling multiple layer
    public void moveToFragment(long id) {
        activeFragment = new ActivityListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ActivityListFragment.PARENT_ID, id);
        activeFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, activeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityListFragment myFragment = (ActivityListFragment) getSupportFragmentManager().findFragmentByTag("main_fragment");
        if (myFragment != null && myFragment.isVisible()) {
            activeFragment = myFragment;
        }
    }

    // setting language  Mode
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppUtils.BOLTE_CHAI_LANGUAGE, lang);
        editor.commit();

    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_select_columns);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText etColumnsNumber = (EditText) dialog.findViewById(R.id.et_dialog_number_of_columns);
        Button popBtnCancel = (Button) dialog.findViewById(R.id.btn_cancel_dialoug_column);
        Button popBtnSave = (Button) dialog.findViewById(R.id.btn_save_dialoug_column);

        etColumnsNumber.setText(getSpanNumb(AppUtils.BOLTE_CHAI_IMAGE) + "");
        popBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        popBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidColumn(etColumnsNumber)) {
                    saveToSharePref(AppUtils.BOLTE_CHAI_IMAGE, Integer.parseInt(etColumnsNumber.getText().toString()));
                    dialog.dismiss();
                    restartActivity();
                } else {
                    Toast.makeText(MainActivity.this, R.string.column_error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });


        if (dialog != null)
            dialog.show();
    }


    // for help option
    public void help() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_help);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        List<Help> helpItems = new ArrayList<Help>();
        RecyclerView rvHelp = (RecyclerView) dialog.findViewById(R.id.rv_help);
        GridLayoutManager gridLayout;

        List<Help> item = new Select().from(Help.class).execute();
        helpItems.addAll(item);

        HelpAdapter adapter = new HelpAdapter(helpItems, dialog.getContext(), this);

        gridLayout = new GridLayoutManager(dialog.getContext(), 2);
        rvHelp.setLayoutManager(gridLayout);
        rvHelp.setItemAnimator(new DefaultItemAnimator());
        rvHelp.setAdapter(adapter);

        Button popBtnCancel = (Button) dialog.findViewById(R.id.btn_help_cancel);
        if (getRoleName().equals("Child")) {
            popBtnCancel.setVisibility(View.GONE);
        }
        popBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newHelp();
                dialog.dismiss();
            }
        });


        if (dialog != null)
            dialog.show();
    }

    // for switching language
    private void languageSelection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_language_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        View viewEnglish = dialog.findViewById(R.id.layout_language_english);
        View viewBangla = dialog.findViewById(R.id.layout_language_bangla);

        final ImageView ivBangla = (ImageView) dialog.findViewById(R.id.iv_cb_bangla);
        final ImageView ivEnglish = (ImageView) dialog.findViewById(R.id.iv_cb_english);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ln = preferences.getString("BolteChaiLanguage", "en");

        if (ln.equals("en")) {

            ivEnglish.setVisibility(View.VISIBLE);
        } else if (ln.equals("bn")) {

            ivBangla.setVisibility(View.VISIBLE);
        }

        viewBangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivEnglish.setVisibility(View.GONE);
                ivBangla.setVisibility(View.VISIBLE);
                setLocale("bn");
                dialog.dismiss();
                restartActivity();
            }
        });

        viewEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBangla.setVisibility(View.GONE);
                ivEnglish.setVisibility(View.VISIBLE);
                setLocale("en");
                dialog.dismiss();
                restartActivity();
            }
        });

        dialog.show();
    }

    // for User Mode  option
    private void modeSelection() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_mode_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        View viewEnglish = dialog.findViewById(R.id.layout_mode_english);
        View viewBangla = dialog.findViewById(R.id.layout_mode_bangla);

        final ImageView ivBangla = (ImageView) dialog.findViewById(R.id.iv_cbmode_bangla);
        final ImageView ivEnglish = (ImageView) dialog.findViewById(R.id.iv_cbmode_english);

        if (getRoleName().equals(AppUtils.MODE_PARENT)) {
            ivBangla.setVisibility(View.VISIBLE);

        } else {
            ivEnglish.setVisibility(View.VISIBLE);
        }


        viewBangla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivEnglish.setVisibility(View.GONE);
                ivBangla.setVisibility(View.VISIBLE);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_PARENT);
                editor.commit();
                restartActivity();
            }
        });

        viewEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBangla.setVisibility(View.GONE);
                ivEnglish.setVisibility(View.VISIBLE);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_CHILD);
                editor.commit();
                restartActivity();
            }
        });

        dialog.show();
    }

    private int getSpanNumb(String name) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getInt(name, 2);
    }

    private static boolean isValidColumn(EditText etText) {
        // check EditText empty or not
        if (etText.getText().toString().trim().length() > 0) {
            if (Integer.parseInt(etText.getText().toString()) > 0 && Integer.parseInt(etText.getText().toString()) <= 4) {
                return true;
            }
        }


        return false;
    }

    public void saveToSharePref(String name, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public String getRoleName() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return preferences.getString(AppUtils.BOLTE_CHAI_ROLE, AppUtils.MODE_PARENT);
    }

    @Override
    public void onDeleteClicked(final String name) {
        restartActivity();
    }

    public String getLanguage() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ln = preferences.getString(AppUtils.BOLTE_CHAI_LANGUAGE, "en");
        return ln;
    }

    // navigation drawer
    public void Drawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .build();

        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withActivity(MainActivity.this)
                .withFullscreen(false)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Change Language").withSetSelected(true).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Control Mode").withSetSelected(false).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Help Contacts").withSetSelected(false).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Set Column").withSetSelected(false).withIdentifier(4),
                        new PrimaryDrawerItem().withName("About").withSetSelected(false).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {


                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(1)) {

                            languageSelection();
                        }
                        if (drawerItem.equals(2)) {
                            modeSelection();
                        }
                        if (drawerItem.equals(3)) {
                            startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        }
                        if (drawerItem.equals(4)) {
                            showDialog();
                        }
                        if (drawerItem.equals(5)) {
                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        }
                        return true;
                    }
                }).withDrawerGravity(Gravity.LEFT)
                .build();
        result.openDrawer();
        result.closeDrawer();
        result.isDrawerOpen();

        // hamburger icon click
        ibHb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });
    }
}

package studio.goldenapp.quiz_ocean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ethanhua.skeleton.Skeleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Drawer result;
    Boolean darkModeEnabled, questionSoundEnabled, answerSoundEnabled, quizSoundEnabled;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar Setup
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefManager = new PrefManager(this);

        // Create the AccountHeader
        @SuppressLint("UseCompatLoadingForDrawables") AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(Color.WHITE)
                .withHeaderBackground(R.drawable.ocean_background)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Gunjan Arora")
                                .withEmail("Gunjanarora20@gmail.com")
                                .withIcon(getResources().getDrawable(R.drawable.profile))
                                .withTextColor(Color.WHITE)
                )
                .withOnAccountHeaderListener((view, profile, currentProfile) -> false)
                .build();

        //create the drawer and remember the `Drawer` result object
        Toolbar toolbar = new Toolbar(this);
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Profile").withIcon(FontAwesome.Icon.faw_user),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Results").withIcon(FontAwesome.Icon.faw_list),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Search Quiz").withIcon(FontAwesome.Icon.faw_search),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Top Rated Quiz").withIcon(FontAwesome.Icon.faw_star),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Latest Quiz").withIcon(FontAwesome.Icon.faw_poll),
                        new PrimaryDrawerItem().withIdentifier(1).withName("Notifications").withIcon(FontAwesome.Icon.faw_bell).withBadge("4").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),

                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(2).withName("Rate This App").withIcon(FontAwesome.Icon.faw_star1),
                        new SecondaryDrawerItem().withIdentifier(2).withName("Share This App").withIcon(FontAwesome.Icon.faw_share),
                        new SecondaryDrawerItem().withIdentifier(2).withName("More Apps").withIcon(FontAwesome.Icon.faw_android),
                        new SecondaryDrawerItem().withName("Developers").withIcon(FontAwesome.Icon.faw_code)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    // do something with the clicked item :D
                    return false;
                })
                .build();

        DatabaseReference databaseReference;
        List<QuizGrid> quizGrids = new ArrayList<>();
        RecyclerView recyclerView;
        final QuizGridRecycler[] adapter = new QuizGridRecycler[1];

        recyclerView = findViewById(R.id.quiz_grid_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager
                (new GridLayoutManager(MainActivity.this, 3));

        Skeleton.bind(recyclerView)
                .adapter(adapter[0]).load(R.layout.quiz_grid_recycler).show();

        databaseReference = FirebaseDatabase.getInstance().getReference("QuizGridData/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuizGrid quizGrid = dataSnapshot.getValue(QuizGrid.class);
                    quizGrids.add(quizGrid);
                }

                adapter[0] = new QuizGridRecycler(MainActivity.this, quizGrids);
                recyclerView.setAdapter(adapter[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("QuizGridDataError", databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        menu.getItem(2).setChecked(prefManager.isDarkModeOn());
        menu.getItem(3).setChecked(prefManager.isQuizSoundOn());
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                result.openDrawer();
            case R.id.action_search:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;
            case R.id.action_quiz_sound:
                quizSoundEnabled = !item.isChecked();
                prefManager.setQuizSound(quizSoundEnabled);
                item.setChecked(quizSoundEnabled);
                return true;
           /* case R.id.action_question_sound:
                questionSoundEnabled = !item.isChecked();
                prefManager.setQuestionSound(questionSoundEnabled);
                item.setChecked(questionSoundEnabled);
                return true;
            case R.id.action_answer_sound:
                answerSoundEnabled = !item.isChecked();
                prefManager.setAnswerSound(answerSoundEnabled);
                item.setChecked(answerSoundEnabled);
                return true;*/
            case R.id.action_dark_mode:
                darkModeEnabled = !item.isChecked();
                prefManager.setDarkMode(darkModeEnabled);
                item.setChecked(darkModeEnabled);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}

class QuizGridRecycler extends RecyclerView.Adapter<QuizGridRecycler.ViewHolder> {

    View view;
    Context context;
    List<QuizGrid> quizGrids;

    public QuizGridRecycler(Context context, List<QuizGrid> quizGrids) {
        this.context = context;
        this.quizGrids = quizGrids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.quiz_grid_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizGrid quizGrid = quizGrids.get(position);
        holder.quiz_card_name.setText(quizGrid.getName());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("QuizGridImage/" + quizGrid.getId() + ".jpg")
                .getDownloadUrl()
                .addOnSuccessListener(uri -> Glide.with(context)
                        .load(uri.toString()).into(holder.quiz_card_image));

        holder.itemView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, QuizListHolder.class);
            intent.putExtra("QUIZ_GRID_ID", quizGrid.getId());
            intent.putExtra("QUIZ_GRID_NAME", quizGrid.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return quizGrids.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView quiz_card_name;
        ImageView quiz_card_image;

        public ViewHolder(View itemView) {
            super(itemView);
            quiz_card_name = itemView.findViewById(R.id.quiz_card_text);
            quiz_card_image = itemView.findViewById(R.id.quiz_card_image);
        }
    }
}

class QuizGrid {
    private String name, id;

    public QuizGrid() {
        // Empty constructor required for firebase
    }

    public QuizGrid(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
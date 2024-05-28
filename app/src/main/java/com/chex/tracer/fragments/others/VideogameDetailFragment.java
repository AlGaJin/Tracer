package com.chex.tracer.fragments.others;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.adapters.recyclerview.AvatarAdapter;
import com.chex.tracer.api.APICallBack;
import com.chex.tracer.api.managers.ReviewManager;
import com.chex.tracer.api.managers.UserManager;
import com.chex.tracer.api.managers.VideogameManager;
import com.chex.tracer.api.models.Platform;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.utils.UserViewModel;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import io.noties.markwon.Markwon;

public class VideogameDetailFragment extends Fragment {

    private final int MAX_LINES = 5;
    private final String[] NINTENDO = {"nintendo switch", "wii u", "nintendo 3ds"};
    private final String[] PLAYSTATION = {"playstation 5", "playstation 4", "playstation 3", "playstation 2", "ps vita"};
    private final VideogameManager videogameManager = new VideogameManager();
    private final UserManager userManager = new UserManager();
    private final ReviewManager reviewManager = new ReviewManager();

    private Videogame game;
    private boolean isCollapsed = true;
    private TextView titleTxtV, descrTxtV, releasedTxtV, metacriticTxtV, ratingTxtV, totalRatingsTxtV;
    private ImageView gameImgBackground, dropImgV, totalRatingsImgV;
    private MaterialDivider totalRatingsDivider;
    private LinearLayout platformLayout;
    private CircleProgress metacriticScore;
    private RatingBar ratingBar;
    private float averageRating;
    private FloatingActionButton optionsFab;
    private BottomSheetDialog bottomSheetDialog;
    private CheckBox playedCheckBox, playingCheckBox, backlogCheckBox;
    private View sheetView;
    private BarChart ratingBarChart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_videogame_detail, container, false);

        Bundle bundle = getArguments();
        if(bundle != null){
            game = bundle.getParcelable("videogame");
        }else{
            Toast.makeText(requireContext(), "Game details cannot be loaded", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).accionBack(null);
        }

        gameImgBackground = v.findViewById(R.id.gameImgBackground);
        dropImgV = v.findViewById(R.id.dropImgV);
        platformLayout = v.findViewById(R.id.platformLayout);
        metacriticScore = v.findViewById(R.id.metacriticScoreCircleProgress);
        descrTxtV = v.findViewById(R.id.gameDescrMKTxtV);
        titleTxtV = v.findViewById(R.id.gameTitleTxtV);
        releasedTxtV = v.findViewById(R.id.releasedTxtV);
        metacriticTxtV = v.findViewById(R.id.metacriticTxtV);
        optionsFab = v.findViewById(R.id.optionsFab);
        ratingTxtV = v.findViewById(R.id.ratingTxtV);
        ratingBarChart = v.findViewById(R.id.ratingBarChart);

        totalRatingsImgV = v.findViewById(R.id.totalRatingsImgV);
        totalRatingsTxtV = v.findViewById(R.id.totalRatingsTxtV);
        totalRatingsDivider = v.findViewById(R.id.totalRatingsDivider);

        descrTxtV.setOnClickListener(view -> collapsaitor());
        dropImgV.setOnClickListener(view -> collapsaitor());
        optionsFab.setOnClickListener(view -> bottomSheetDialog.show());

        bottomSheetDialog = new BottomSheetDialog(requireContext());
        sheetView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_game_options, null);
        bottomSheetDialog.setContentView(sheetView);


        setGameData();
        setGameOptionData();

        videogameManager.getPlatforms(game.getId(), new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if(isAdded()){
                    setPlatforms((List<Platform>) obj);
                }
            }
            @Override
            public void onError() {}
        });
        videogameManager.getRatings(game.getId(), new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if(obj != null){
                    processRatings((List<Float>) obj);
                }
            }

            @Override
            public void onError() {
            }
        });

        applyLayoutTransition();
        return v;
    }

    private void processRatings(List<Float> ratings) {
        try {
            Map<Float, Integer> ratingCounts = new HashMap<>();
            float totalRating = 0;

            for (float rating : ratings) {
                totalRating += rating;
                if(!ratingCounts.containsKey(rating)){
                    ratingCounts.put(rating, 0);
                }
                ratingCounts.replace(rating, ratingCounts.get(rating) + 1);
            }

            if(!ratings.isEmpty()){
                averageRating = totalRating / ratings.size();
            }

            ratingTxtV.setText(String.valueOf(totalRating));

            List<BarEntry> entries = new ArrayList<>();
            for (float i = 0.5f; i <= 5; i = i+0.5f) {
                if(ratingCounts.containsKey(i)){
                    entries.add(new BarEntry(i, ratingCounts.get(i)));
                }else{
                    entries.add(new BarEntry(i, 0.01f));
                }
            }
            BarDataSet dataSet = new BarDataSet(entries, "");
            dataSet.setDrawValues(false);

            BarData data = new BarData(dataSet);
            data.setBarWidth(0.48f); // Ancho de las barras para que estén ligeramente separadas
            ratingBarChart.setData(data);

            // Otros ajustes
            ratingBarChart.setPinchZoom(false);
            ratingBarChart.setDoubleTapToZoomEnabled(false);
            ratingBarChart.setScaleEnabled(false);
            ratingBarChart.setDrawValueAboveBar(false);
            ratingBarChart.getDescription().setEnabled(false);
            ratingBarChart.getLegend().setEnabled(false);
            ratingBarChart.setDrawGridBackground(false);
            ratingBarChart.setDrawBarShadow(false);
            ratingBarChart.getAxisLeft().setDrawGridLines(false);
            ratingBarChart.getXAxis().setDrawGridLines(false);
            ratingBarChart.getAxisLeft().setDrawAxisLine(false);
            ratingBarChart.getXAxis().setDrawAxisLine(false);
            ratingBarChart.getAxisRight().setEnabled(false);
            ratingBarChart.getAxisLeft().setEnabled(false);
            ratingBarChart.getXAxis().setEnabled(false);
            ratingBarChart.setDrawBorders(false);
            ratingBarChart.setFitBars(true);
            ratingBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    myOnSelected(e.getY(), e.getX());
                }
                @Override
                public void onNothingSelected() {
                    myOnNothingSelected();
                }
            });
            ratingBarChart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    if (ratingBarChart.getData() != null) {
                        Highlight highlight = ratingBarChart.getHighlightByTouchPoint(me.getX(), me.getY());
                        ratingBarChart.highlightValue(highlight);
                        if (highlight != null) {
                            Entry entry = ratingBarChart.getData().getEntryForHighlight(highlight);
                            if (entry != null) {
                                myOnSelected(entry.getY(), entry.getX());
                            }
                        }
                    }
                }
                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    myOnNothingSelected();
                }
                @Override
                public void onChartLongPressed(MotionEvent me) {}
                @Override
                public void onChartDoubleTapped(MotionEvent me) {}
                @Override
                public void onChartSingleTapped(MotionEvent me) {}
                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}
                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {}
                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {}
            });

            ratingBarChart.invalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void myOnSelected(float count, float rate){
        if (count >= 1) {
            ratingTxtV.setText(String.valueOf(rate));
            totalRatingsTxtV.setText(String.valueOf(count).split("\\.")[0]);
            totalRatingsImgV.setVisibility(View.VISIBLE);
            totalRatingsTxtV.setVisibility(View.VISIBLE);
            totalRatingsDivider.setVisibility(View.VISIBLE);
        }else{
            myOnNothingSelected();
        }
    }

    private void myOnNothingSelected(){
        ratingBarChart.highlightValue(null);
        totalRatingsImgV.setVisibility(View.GONE);
        totalRatingsTxtV.setVisibility(View.GONE);
        totalRatingsDivider.setVisibility(View.GONE);
        ratingTxtV.setText(String.valueOf(averageRating));
    }

    private void setGameData() {
        if(game.getImage() != null){
            ImageLoader imageLoader = Coil.imageLoader(requireContext());
            ImageRequest request = new ImageRequest.Builder(requireContext()).data(game.getImage()).crossfade(true).target(gameImgBackground).build();
            imageLoader.enqueue(request);
        }

        titleTxtV.setText(game.getTitle());

        if(game.getDescr() != null){
            Spanned markdownText = Markwon.create(requireContext()).toMarkdown(game.getDescr());
            descrTxtV.setText(game.getDescr());
            if(descrTxtV.getLineHeight() < MAX_LINES){
                dropImgV.setVisibility(View.GONE);
            }
            descrTxtV.setText(markdownText);
        }else{
            dropImgV.setVisibility(View.GONE);
        }

        if(game.getReleased() != null){

            releasedTxtV.setText(getYear(game.getReleased()));
        }

        if(game.getMetacritic_rate() != null){
            metacriticScore.setProgress(game.getMetacritic_rate());
            chooseColor();
        }else{
            metacriticTxtV.setVisibility(View.GONE);
            metacriticScore.setVisibility(View.GONE);
        }
    }
    private String getYear(Timestamp date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }
    private void chooseColor() {
        int score = game.getMetacritic_rate();

        if(score < 50){
            metacriticScore.setFinishedColor(getResources().getColor(R.color.lowScore, null));
        }else if(score < 75){
            metacriticScore.setFinishedColor(getResources().getColor(R.color.midScore, null));
        }else{
            metacriticScore.setFinishedColor(getResources().getColor(R.color.highScore, null));
        }
    }

    private void setGameOptionData() {
        int userId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();
        ((TextView) sheetView.findViewById(R.id.gop_titleGameTxtV)).setText(game.getTitle());
        ((TextView) sheetView.findViewById(R.id.gop_releasedGameTxtV)).setText(getYear(game.getReleased()));
        ratingBar = sheetView.findViewById(R.id.ratingBar);
        playedCheckBox = sheetView.findViewById(R.id.playedCheckBox);
        playingCheckBox = sheetView.findViewById(R.id.playingCheckBox);
        backlogCheckBox = sheetView.findViewById(R.id.backlogCheckBox);
        sheetView.findViewById(R.id.addReview).setOnClickListener(view ->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("videogame", game);
            bundle.putFloat("rate", ratingBar.getRating());
            ((MainActivity)requireActivity()).changeFragmentWithBundle(2, bundle);
            bottomSheetDialog.dismiss();
        });

        playedCheckBox.setOnClickListener(view ->{
            if(ratingBar.getRating() > 0 && !playedCheckBox.isChecked()){
                playedCheckBox.setChecked(true);
                Toast.makeText(requireContext(), "Unrated the game if you don't have played", Toast.LENGTH_SHORT).show();
            }
            if(playingCheckBox.isChecked() && playedCheckBox.isChecked()){
                playingCheckBox.setChecked(false);
            }
        });

        playingCheckBox.setOnClickListener(view ->{
            if(backlogCheckBox.isChecked() && playingCheckBox.isChecked()){
                backlogCheckBox.setChecked(false);
            }
        });

        ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            if(v > 0){
                playedCheckBox.setChecked(true);
            }
        });

        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            userManager.updateGameData(userId, game.getId(),
                playedCheckBox.isChecked(), playingCheckBox.isChecked(), backlogCheckBox.isChecked(), ratingBar.getRating());
        });

        userManager.getUserGameData(userId, game.getId(), new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if(obj != null){
                    List<Boolean> states = (List<Boolean>) obj;
                    playedCheckBox.setChecked(states.get(0));
                    playingCheckBox.setChecked(states.get(1));
                    backlogCheckBox.setChecked(states.get(2));
                }
            }

            @Override
            public void onError() {

            }
        });
        reviewManager.getReview(game.getId(), userId, new APICallBack() {
            @Override
            public void onSuccess(Object obj) {
                if(obj != null){
                    ratingBar.setRating(((Review)obj).getRate());
                }
            }

            @Override
            public void onError() {}
        });
    }

    private void collapsaitor(){
        if(isCollapsed){
            descrTxtV.setMaxLines(Integer.MAX_VALUE);
            dropImgV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ui_drop_up, null));
        }else{
            descrTxtV.setMaxLines(MAX_LINES);
            dropImgV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ui_drop_down, null));
        }
        isCollapsed = !isCollapsed;
    }
    private void applyLayoutTransition() {
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(325);
        transition.enableTransitionType(LayoutTransition.CHANGING);
        ((LinearLayout) descrTxtV.getParent()).setLayoutTransition(transition);
    }

    private void setPlatforms(List<Platform> platforms){
        final ArrayList<String> nintendoPlatform = new ArrayList<>(Arrays.asList(NINTENDO));
        final ArrayList<String> sonyPlatform = new ArrayList<>(Arrays.asList(PLAYSTATION));
        boolean isNintendoAdded = false, isPSAdded = false, isXboxAdded = false;

        for (Platform platform : platforms) {
            String platformName = platform.getName().toLowerCase();
            if(nintendoPlatform.contains(platformName) && !isNintendoAdded) {
                platformLayout.addView(getPlatformImgV(R.drawable.platform_nintendo));
                isNintendoAdded =true;
            } else if (sonyPlatform.contains(platformName)&& !isPSAdded){
                platformLayout.addView(getPlatformImgV(R.drawable.platform_playstation));
                isPSAdded = true;
            } else if(platformName.contains("xbox") && !isXboxAdded){
                platformLayout.addView(getPlatformImgV(R.drawable.platform_xbox));
                isXboxAdded = true;
            } else {
                switch (platformName){
                    case "pc":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_pc));
                        break;
                    case "macos":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_macos));
                        break;
                    case "linux":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_linux));
                        break;
                    case "android":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_android));
                        break;
                    case "ios":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_ios));
                        break;
                    case "web":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_web));
                        break;
                    case "dreamcast":
                        platformLayout.addView(getPlatformImgV(R.drawable.platform_dreamcast));
                        break;
                }
            }
        }
    }
    private View getPlatformImgV(int drawableId) {
        ImageView imgV = new ImageView(requireContext());
        imgV.setImageDrawable(ResourcesCompat.getDrawable(getResources(), drawableId, null));

        int widthInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(widthInPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgV.setLayoutParams(layoutParams);

        return imgV;
    }
}
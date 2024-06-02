package com.chex.tracer.fragments.others;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.managers.ReviewManager;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.Videogame;
import com.chex.tracer.utils.UserViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ReviewFragment extends Fragment {

    private final ReviewManager reviewManager = new ReviewManager();

    private Bundle bundle;
    private Videogame game;
    private Review review;

    private ImageView gameImgV;
    private TextView titleTxtV, releasedTxtV;
    private RatingBar ratingBar;
    private EditText reviewEditTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container, false);

        gameImgV = v.findViewById(R.id.review_gameImgV);
        titleTxtV = v.findViewById(R.id.review_gameTitleTxtV);
        releasedTxtV = v.findViewById(R.id.review_releasedTxtV);
        ratingBar = v.findViewById(R.id.review_ratingBar);
        reviewEditTxt = v.findViewById(R.id.reviewEditTxt);

        bundle = getArguments();
        if(bundle != null){
            game = bundle.getParcelable("videogame");
            review = bundle.getParcelable("review");
        }else{
            Toast.makeText(requireContext(), "Game details cannot be loaded", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).actionBack(null);
        }

        setData();

        v.findViewById(R.id.submitBtn).setOnClickListener(view -> checkSubmit());

        getActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext(), R.style.MyAlertDialog)
                        .setTitle(R.string.discard)
                        .setMessage(R.string.alert_discard_msg)
                        .setPositiveButton(R.string.discard, (dialogInterface, i) -> {
                            ((MainActivity)getActivity()).actionBack(bundle);
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
        return v;
    }

    private void checkSubmit() {
        float rate = ratingBar.getRating();
        String reviewText = reviewEditTxt.getText().toString().trim();

        if(rate == 0){
            if(review.getReview() != null && !review.getReview().isEmpty()){
                new AlertDialog.Builder(requireContext(), R.style.MyAlertDialog)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.alert_delete_msg)
                        .setPositiveButton(R.string.delete, (dialogInterface, i) -> {
                            submit(rate, reviewText);
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }else{
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        }else{
            submit(rate, reviewText);
        }
    }

    private void submit(float rate, String reviewTxt) {
        int userId = new ViewModelProvider(requireActivity()).get(UserViewModel.class).getLoggedUser().getId();
        int gameId = game.getId();

        reviewManager.editReview(userId, gameId, rate, reviewTxt);

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        review.setRate(rate);
        review.setReview(reviewTxt);
        bundle.putParcelable("review", review);
        ((MainActivity)getActivity()).actionBack(bundle);
    }

    private void setData() {
        //Se carga la imagen
        ImageLoader imageLoader = Coil.imageLoader(requireContext());
        ImageRequest request = new ImageRequest.Builder(requireContext()).data(game.getImage()).crossfade(true).target(gameImgV).build();
        imageLoader.enqueue(request);

        //Se carga el título del juego
        titleTxtV.setText(game.getTitle());

        //Se carga el año de lanzamiento
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(game.getReleased());
        releasedTxtV.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        //Se cargan los datos de la review si la hubiera anteriormente
        ratingBar.setRating(review.getRate());
        if(review.getReview() != null) reviewEditTxt.setText(review.getReview());
    }
}
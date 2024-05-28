package com.chex.tracer.fragments.others;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chex.tracer.R;
import com.chex.tracer.activities.MainActivity;
import com.chex.tracer.api.models.Review;
import com.chex.tracer.api.models.Videogame;

import java.util.Calendar;
import java.util.GregorianCalendar;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ReviewFragment extends Fragment {
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

        Bundle bundle = getArguments();
        if(bundle != null){
            game = bundle.getParcelable("videogame");
            review = bundle.getParcelable("review");
        }else{
            Toast.makeText(requireContext(), "Game details cannot be loaded", Toast.LENGTH_SHORT).show();
            ((MainActivity) requireActivity()).accionBack(null);
        }

        setData();

        return v;
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
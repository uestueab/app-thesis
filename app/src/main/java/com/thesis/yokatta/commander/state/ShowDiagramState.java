package com.thesis.yokatta.commander.state;

import android.content.Context;
import android.content.ContextWrapper;

import com.thesis.yokatta.databinding.ActivityStartingScreenBinding;
import com.thesis.yokatta.model.entity.FlashCard;
import com.thesis.yokatta.model.entity.PastReview;

import java.security.IdentityScope;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ShowDiagramState {
    private ActivityStartingScreenBinding binding;
    private Context context;
    private ArrayList<PastReview> pastReviews;
}
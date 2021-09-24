package com.test.viewpagerfun.listeners.onClick;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.test.viewpagerfun.constants.ConstantsHolder.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddNoteListener implements View.OnClickListener {

    private Context currentActivity;
    private Class targetActivity;
    private int requestCode;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getCurrentActivity(), getTargetActivity());
        intent.putExtra(REQUEST_CODE, requestCode);
        resultLauncher.launch(intent);
    }
}

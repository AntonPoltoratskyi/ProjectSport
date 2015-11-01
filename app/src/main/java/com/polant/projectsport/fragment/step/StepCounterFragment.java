package com.polant.projectsport.fragment.step;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.glomadrian.dashedcircularprogress.DashedCircularProgress;
import com.polant.projectsport.Constants;
import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityOtherCalculators;
import com.polant.projectsport.preferences.PreferencesNewActivity;

import java.util.Formatter;

/**
 * Created by ����� on 18.10.2015.
 */
public class StepCounterFragment extends Fragment{

    //���������, ������� ��������� ��������, ������� ���������� ��������.
    public interface StepCounterManagerListener{
        void registerCounter();
        void unregisterCounter();
        void setStepCount(int stepCount);//��� ������������ �������� ��������, ������� ������ ������������ �� ������.
        void setBeforeResetCount(int resetCount);//��� ������������ ���������� ���������� �����.
    }


    private static final int LAYOUT = R.layout.fragment_step_counter;

    private Activity activity;

    private View view;
    private DashedCircularProgress circularProgress;

    //�������� ProgressView.
    private int progressValue;
    private int maxProgressValue;

    private static final float STEP_CALORIES_KOEFF = 0.07f;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initProgressView();
        initButtons();
        updateProgress(getMaxProgressValue());
        //Snackbar.make(view, R.string.hint_start_step_counter, Snackbar.LENGTH_SHORT).show();
    }

    //������� �� SharedPreferences.
    private int getMaxProgressValue(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        return sp.getInt(PreferencesNewActivity.PREF_TARGET_STEP_COUNT, 10000);
    }

    private void initProgressView(){
        circularProgress = (DashedCircularProgress) view.findViewById(R.id.progressSteps);

        //� ���� ������ ������� ������� �������� �����.
        progressValue = ((ActivityOtherCalculators) activity).getStepCountValue();

        //������������� ��������, � �� ��������.
        circularProgress.setValue(progressValue);
    }

    //���������� ����� �������������� ���������� �����-������� ������ ���� ���-�� �����,
    //��� � onCreate().
    private void updateProgress(int max){
        maxProgressValue = max;
        circularProgress.setMax(max);
        //��� ���������� ���������� �������� ������!
        circularProgress.setValue(progressValue);

        //������������ ��������.
        TextView targetValue = (TextView) view.findViewById(R.id.textViewStepCounterTargetValue);
        targetValue.setText(String.valueOf(max));

        //�������� � ���������.
        TextView textViewCurrentProgress = (TextView) view.findViewById(R.id.textCurrentProgress);
        float percent = progressValue * 100 / circularProgress.getMax();
        try(Formatter formatter = new Formatter()) {
            formatter.format("%.1f", percent);
            String resultStr = formatter.toString().replaceAll(",", ".");
            percent = Float.valueOf(resultStr);
        }
        textViewCurrentProgress.setText(String.valueOf(percent));

        //������� �������� (� �����, � �� � ���������).
        TextView currentValue = (TextView) view.findViewById(R.id.textViewStepCounterCurrentValue);
        currentValue.setText(String.valueOf(progressValue));

        //������������ ���������� ���������� ��-�� ������ �������.
        TextView deltaCalories = (TextView) view.findViewById(R.id.textViewDeltaCaloriesStepCounter);
        String deltaText = "- " + String.valueOf((int) (progressValue * STEP_CALORIES_KOEFF)) +
                                    getString(R.string.text_ccal);
        deltaCalories.setText(deltaText);
    }

    //������������� ���� ������ (���� � start, stop, reset) ���������.
    private void initButtons(){
        //������ ������ ���� ���������� �����.
        Button targetBt = (Button) view.findViewById(R.id.buttonSetStepCounterTarget);
        targetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialogChangeStepCountTarget();
            }
        });

        //������ ���������� ���������.
        Button startBt = (Button) view.findViewById(R.id.buttonStartStepCounter);
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityOtherCalculators)activity).registerCounter();
            }
        });
        Button stopBt = (Button) view.findViewById(R.id.buttonStopStepCounter);
        stopBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityOtherCalculators) activity).unregisterCounter();
            }
        });
        Button resetBt = (Button) view.findViewById(R.id.buttonResetStepCounter);
        resetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressValue != 0) {
                    buildAlertReset();
                }
            }
        });
    }

    ///-----------------------------------------------------------------/

    //���������� AlertDialog ��� ��������� ���� ���������� �����.
    private void buildAlertDialogChangeStepCountTarget() {

        //���������� �������, � ������� ������������ ������ ���������� ��������� ���.
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //������� �� id �������, � ������ View, ����� ����� �������� ������ � ����.
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_step_count_target, null);

        builder.setTitle(R.string.alertTargetStepCountTitle)
                .setMessage(R.string.alertTargetStepCountMessage)
                .setCancelable(true)
                .setIcon(R.drawable.alert_add_food_icon)
                .setView(alertView)
                .setPositiveButton(getString(R.string.alertTargetStepCountPositiveBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPositiveClickAlertResult(alertView);
                    }
                })
                .setNegativeButton(getString(R.string.alertTargetStepCountNegativeBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //������������� ��������� �������.
    private void setPositiveClickAlertResult(View alertView) {
        EditText targetText = (EditText) alertView.findViewById(R.id.editTextTargetStepCount);
        if (!TextUtils.isEmpty(targetText.getText())){
            int targetCount = Integer.valueOf(targetText.getText().toString());
            if (targetCount >= Constants.MIN_STEP_COUNT_TARGET) {
                //�������� ������� ���������� ����� � ���������.
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(PreferencesNewActivity.PREF_TARGET_STEP_COUNT, targetCount);
                editor.apply();

                //������������ ������������ �������� � ProgressView.
                updateProgress(targetCount);
            }
            else{
                Toast.makeText(getActivity(), R.string.toast_mistake_min_target_step_count, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getActivity(), R.string.toast_err_target_step_count, Toast.LENGTH_SHORT).show();
        }
    }


    //���� ����� ���������� �������� ��� �������� ������ �� ��������.
    public void stepDetected(int value){
        progressValue = value;
        updateProgress(maxProgressValue);
    }

    //���� ����������� ������� �� ������������� ������ reset buildAlertReset-�������.
    private void resetBtClick(){
        ((ActivityOtherCalculators) activity).setBeforeResetCount(progressValue);
        progressValue = 0;
        ((ActivityOtherCalculators) activity).setStepCount(progressValue);
        updateProgress(maxProgressValue);
    }

    //������ ������������� ������ ��������.
    private void buildAlertReset() {

        //���������� �������, � ������� ������������ ������ ���������� ��������� ���.
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.alertResetTitle)
                .setMessage(R.string.alertResetMessage)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.alertResetPositiveBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetBtClick();
                    }
                })
                .setNegativeButton(getString(R.string.alertResetNegativeBt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.polant.projectsport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityCalculateFood;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.SpecificFood;

/**
 * Created by ����� on 05.10.2015.
 */
public class AdapterCalculateDetailsFood extends CursorAdapter{

    private LayoutInflater layoutInflater;
    private final Context mContext;

    public AdapterCalculateDetailsFood(Context _context, Cursor c, int flags) {
        super(_context, c, flags);
        mContext = _context;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.list_adapter_details_food, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String nameFood = cursor.getString(cursor.getColumnIndex(Database.FOOD_NAME));
        //������� ����� INTEGER ��������, �� ����� �������� � ����� ����� cursor.getString(). � �������� - �������� � ���.
        final String caloriesCount = String.valueOf(cursor.getInt(cursor.getColumnIndex(Database.CAL_COUNT)));

        //���������������� ���������� �������� � ������.
        TextView nameTextView = (TextView) view.findViewById(R.id.textViewDetailFood);
        TextView caloriesTextView = (TextView) view.findViewById(R.id.textViewCalInFood);
        nameTextView.setText(nameFood);
        caloriesTextView.setText(caloriesCount);


        //����� ���, ��� ����� ��� ����������� ����� �� ImageView ��� ����������.
        final int idSpecificFood = cursor.getInt(cursor.getColumnIndex(Database.ID_SPECIFIC_FOOD));
        final String foodCategory = cursor.getString(cursor.getColumnIndex(Database.FOOD_CATEGORY));

        ImageView imageClickable = (ImageView) view.findViewById(R.id.imageViewDetailFood);
        imageClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecificFood sf = new SpecificFood(idSpecificFood, foodCategory, nameFood, Integer.valueOf(caloriesCount));
                //������� ���������, ������� ����� �������������� ��� � ����� ��������.
                ((ActivityCalculateFood) mContext).changeSelectedCaloriesCount(sf, true);
            }
        });
    }

}

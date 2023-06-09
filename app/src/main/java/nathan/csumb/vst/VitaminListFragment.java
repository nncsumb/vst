package nathan.csumb.vst;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import nathan.csumb.vst.db.AppDatabase;
import nathan.csumb.vst.db.vstDAO;

public class VitaminListFragment extends Fragment {

    private RecyclerView recyclerView;
    private vstDAO mvstDAO;
    private int timeCurrentState = 0;

    private int nameQtyCurrentState = 0;
    private SharedPreferences mSharedPreferences;
    private List<Vitamin> vitamins;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_vitamin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QuantityNotification mQuantityNotification = new QuantityNotification(getActivity());
        mQuantityNotification.start();

        mSharedPreferences = requireActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt("userId", -1);
        Button backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().onBackPressed();

            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);


        getDatabase();

        render(0, userId, 0);
        MaterialButton timeFilterbutton = view.findViewById(R.id.timeFilterButton);

        timeFilterbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                timeCurrentState++;
                if (timeCurrentState > 2) {
                    timeCurrentState = 0;
                }
                switch (timeCurrentState) {
                    case 0:
                        timeFilterbutton.setText("Morning");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                    case 1:
                        timeFilterbutton.setText("Afternoon");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                    case 2:
                        timeFilterbutton.setText("Night");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                }
            }
        });
        MaterialButton nameQtyFilterbutton = view.findViewById(R.id.nameQtyFilterButton);
        nameQtyFilterbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                nameQtyCurrentState++;
                if (nameQtyCurrentState > 3) {
                    nameQtyCurrentState = 0;
                }
                switch (nameQtyCurrentState) {
                    case 0:
                        nameQtyFilterbutton.setText("Name ⬇️");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                    case 1:
                        nameQtyFilterbutton.setText("Name ⬆️");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                    case 2:
                        nameQtyFilterbutton.setText("Quantity ⬇️");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                    case 3:
                        nameQtyFilterbutton.setText("Quantity ⬆️");
                        render(timeCurrentState, userId, nameQtyCurrentState);
                        break;
                }
            }
        });

    }

    public void render(int timeCurrentState, int userId, int nameQtyState) {
        if (timeCurrentState == 0 && nameQtyState == 0) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameDesc(userId, "Morning");
        }
        if (timeCurrentState == 0 && nameQtyState == 1) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameAsc(userId, "Morning");
        }
        if (timeCurrentState == 0 && nameQtyState == 2) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyDesc(userId, "Morning");
        }
        if (timeCurrentState == 0 && nameQtyState == 3) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyAsc(userId, "Morning");
        }

        if (timeCurrentState == 1 && nameQtyState == 0) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameDesc(userId, "Afternoon");
        }
        if (timeCurrentState == 1 && nameQtyState == 1) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameAsc(userId, "Afternoon");
        }
        if (timeCurrentState == 1 && nameQtyState == 2) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyDesc(userId, "Afternoon");
        }
        if (timeCurrentState == 1 && nameQtyState == 3) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyAsc(userId, "Afternoon");
        }

        if (timeCurrentState == 2 && nameQtyState == 0) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameDesc(userId, "Night");
        }
        if (timeCurrentState == 2 && nameQtyState == 1) {
            vitamins = mvstDAO.getVitaminsByUserIdByNameAsc(userId, "Night");
        }
        if (timeCurrentState == 2 && nameQtyState == 2) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyDesc(userId, "Night");
        }
        if (timeCurrentState == 2 && nameQtyState == 3) {
            vitamins = mvstDAO.getVitaminsByUserIdByQtyAsc(userId, "Night");
        }

        VitaminAdapter.OnVitaminClickListener onVitaminClickListener = position -> {

            Vitamin vitamin = vitamins.get(position);
            Intent intent = new Intent(requireActivity(), EditVitaminActivity.class);
            intent.putExtra("vitaminId", vitamin.getVitaminId());
            startActivity(intent);
        };


        VitaminAdapter adapter = new VitaminAdapter(requireActivity(), vitamins, onVitaminClickListener);
        recyclerView.setAdapter(adapter);
    }

    private void getDatabase() {
        mvstDAO = Room.databaseBuilder(requireActivity(), AppDatabase.class, AppDatabase.DB_NAME).allowMainThreadQueries().build().getvstDAO();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Show buttons
        showButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        hideButtons();
    }

    private void showButtons() {
        TextView welcomeTextView = requireActivity().findViewById(R.id.welcomeTextView);
        welcomeTextView.setVisibility(View.VISIBLE);

        TextView waterTextView = requireActivity().findViewById(R.id.waterTextView);
        waterTextView.setVisibility(View.VISIBLE);

        Button drinkButton = requireActivity().findViewById(R.id.drinkWater);
        drinkButton.setVisibility(View.VISIBLE);

        Button resetButton = requireActivity().findViewById(R.id.resetWater);
        resetButton.setVisibility(View.VISIBLE);

        Button addVitaminButton = requireActivity().findViewById(R.id.addVitaminButton);
        addVitaminButton.setVisibility(View.VISIBLE);

        Button takeVitaminButton = requireActivity().findViewById(R.id.takeVitaminButton);
        takeVitaminButton.setVisibility(View.VISIBLE);

        Button editProfileButton = requireActivity().findViewById(R.id.settingsButton);
        editProfileButton.setVisibility(View.VISIBLE);

        Button vitaminStackButton = requireActivity().findViewById(R.id.vitaminStackButton);
        vitaminStackButton.setVisibility(View.VISIBLE);

        Button logoutButton = requireActivity().findViewById(R.id.logoutButton);
        logoutButton.setVisibility(View.VISIBLE);
        if (mSharedPreferences.getBoolean("admin", false)) {
            Button adminAreaButton = requireActivity().findViewById(R.id.adminAreaButton);
            adminAreaButton.setVisibility(View.VISIBLE);
        } else {
            Button adminAreaButton = requireActivity().findViewById(R.id.adminAreaButton);
            adminAreaButton.setVisibility(View.GONE);
        }
    }

    private void hideButtons() {
        TextView welcomeTextView = requireActivity().findViewById(R.id.welcomeTextView);
        welcomeTextView.setVisibility(View.INVISIBLE);

        TextView waterTextView = requireActivity().findViewById(R.id.waterTextView);
        waterTextView.setVisibility(View.INVISIBLE);

        Button drinkButton = requireActivity().findViewById(R.id.drinkWater);
        drinkButton.setVisibility(View.INVISIBLE);

        Button resetButton = requireActivity().findViewById(R.id.resetWater);
        resetButton.setVisibility(View.INVISIBLE);

        Button addVitaminButton = requireActivity().findViewById(R.id.addVitaminButton);
        addVitaminButton.setVisibility(View.INVISIBLE);

        Button takeVitaminButton = requireActivity().findViewById(R.id.takeVitaminButton);
        takeVitaminButton.setVisibility(View.INVISIBLE);

        Button editProfileButton = requireActivity().findViewById(R.id.settingsButton);
        editProfileButton.setVisibility(View.INVISIBLE);

        Button vitaminStackButton = requireActivity().findViewById(R.id.vitaminStackButton);
        vitaminStackButton.setVisibility(View.INVISIBLE);

        Button logoutButton = requireActivity().findViewById(R.id.logoutButton);
        logoutButton.setVisibility(View.INVISIBLE);
        if (mSharedPreferences.getBoolean("admin", false)) {
            Button adminAreaButton = requireActivity().findViewById(R.id.adminAreaButton);
            adminAreaButton.setVisibility(View.INVISIBLE);
        } else {
            Button adminAreaButton = requireActivity().findViewById(R.id.adminAreaButton);
            adminAreaButton.setVisibility(View.INVISIBLE);
        }
    }

}





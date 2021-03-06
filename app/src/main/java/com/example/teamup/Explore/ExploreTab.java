package com.example.teamup.Explore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teamup.CreateBroadcast.CreateBroadcast;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.User;
import com.example.teamup.model.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExploreTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final String TAG = ExploreTab.this.getClass().getSimpleName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth currentUser;
    ListView lvBroadcast;
    Broadcast broadcast, savingBroadcast;
    User user;
    private ArrayList<Broadcast> broadcastList = new ArrayList<Broadcast>();
    private BroadcastAdapter adapter;
    Button createBroadcast;
    Dialog dialog, completedDialog;
    int butttonCounter = 0;
    boolean applicantExists = false, workerExists = false, sameCreator = false;

    EditText prsearch;

    public ExploreTab() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ExploreTab newInstance(String param1, String param2) {
        ExploreTab fragment = new ExploreTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideKeyboard(getActivity());
        user = SessionStorage.getUser(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.activity_explore, container, false);

        lvBroadcast = view.findViewById(R.id.listview_explore_projects);
        currentUser = FirebaseAuth.getInstance();
        createBroadcast = view.findViewById(R.id.addproject);
        dialog = new Dialog(getActivity());
        completedDialog = new Dialog(getActivity());

        //search ET
        prsearch = view.findViewById(R.id.editText);
        prsearch.clearFocus();

        prsearch.setShowSoftInputOnFocus(true);
// To set the the cursor visible when the search bar is clicked
        prsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prsearch.setCursorVisible(true);
            }
        });


        createBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateBroadcast.class));
            }
        });

        broadcast = new Broadcast();
        loadBroadcastList();

        return view;
    }

    public void showApplyDialogue(final int pos) {
        dialog.setContentView(R.layout.explore_project_display_popup);

        final TextView projectName = dialog.findViewById(R.id.popup_project_name);
        final TextView creatorName = dialog.findViewById(R.id.popup_project_creator);
        final TextView projectShortDescription = dialog.findViewById(R.id.popup_project_short_description);
        final TextView projectLongDescription = dialog.findViewById(R.id.popup_project_long_description);
        final TextView shortPlaceholder = dialog.findViewById(R.id.placeholderDescription);
        final Button cancelButton = dialog.findViewById(R.id.popup_cancel_button);
        final Button acceptButton = dialog.findViewById(R.id.popup_accept_button);
        final TextView entryDisplay = dialog.findViewById(R.id.applicationdisplaypopup);
        final EditText entryEdit = dialog.findViewById(R.id.applicationentrypopup);
        final ScrollView scrollView = dialog.findViewById(R.id.SkillDisplayScrollView);

        Chip chip1 = dialog.findViewById(R.id.chip1);
        Chip chip2 = dialog.findViewById(R.id.chip2);
        Chip chip3 = dialog.findViewById(R.id.chip3);
        Chip chip4 = dialog.findViewById(R.id.chip4);
        Chip chip5 = dialog.findViewById(R.id.chip5);
        Chip chip6 = dialog.findViewById(R.id.chip6);
        Chip chip7 = dialog.findViewById(R.id.chip7);
        Chip chip8 = dialog.findViewById(R.id.chip8);
        Chip chip9 = dialog.findViewById(R.id.chip9);
        Chip chip10 = dialog.findViewById(R.id.chip10);
        Chip chip11 = dialog.findViewById(R.id.chip11);
        Chip chip12 = dialog.findViewById(R.id.chip12);
        Chip chip13 = dialog.findViewById(R.id.chip13);
        Chip chip14 = dialog.findViewById(R.id.chip14);
        Chip chip15 = dialog.findViewById(R.id.chip15);

        projectName.setText(broadcastList.get(pos).getBroadcastName());
        creatorName.setText(broadcastList.get(pos).getCreatorName());
        projectShortDescription.setText(broadcastList.get(pos).getBroadcastDescription());

        Log.d(TAG, "ACCEPTANCE TYPE: " + broadcastList.get(pos).getAcceptanceType());

        if (broadcastList.get(pos).getAcceptanceType().equals("Automatic")) {
            acceptButton.setText("Join");
            acceptButton.setTextColor(Color.parseColor("#35C80B"));
            acceptButton.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
        }

        if (sameCreator == true) {
            acceptButton.setText("This is your project");
            acceptButton.setTextColor(Color.parseColor("#D1D1D1"));
            acceptButton.setBackground(getResources().getDrawable(R.drawable.unpressable_button));
        } else if (applicantExists == true) {
            acceptButton.setText("Request send already");
            acceptButton.setTextColor(Color.parseColor("#D1D1D1"));
            acceptButton.setBackground(getResources().getDrawable(R.drawable.unpressable_button));
        } else if (workerExists == true) {
            acceptButton.setText("Already Accepted");
            acceptButton.setTextColor(Color.parseColor("#D1D1D1"));
            acceptButton.setBackground(getResources().getDrawable(R.drawable.unpressable_button));
        }

        List<Chip> allChips = new ArrayList<>();
        allChips.add(chip1);
        allChips.add(chip2);
        allChips.add(chip3);
        allChips.add(chip4);
        allChips.add(chip5);
        allChips.add(chip6);
        allChips.add(chip7);
        allChips.add(chip8);
        allChips.add(chip9);
        allChips.add(chip10);
        allChips.add(chip11);
        allChips.add(chip12);
        allChips.add(chip13);
        allChips.add(chip14);
        allChips.add(chip15);

        List<String> skillsArray;
        skillsArray = (broadcastList.get(pos).getInterestTags());

        int i = 0;

        if (skillsArray != null) {
            for (String s : skillsArray) {
                allChips.get(i).setText(s);
                allChips.get(i).setVisibility(View.VISIBLE);
                i++;
            }
            for (int x = i; x < allChips.size(); x++) {
                allChips.get(x).setVisibility(View.GONE);
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (butttonCounter % 2 == 0) {
                    dialog.dismiss();
                } else if (butttonCounter % 2 == 1) {
                    Toast.makeText(getContext(), "SAVING APPLICANT", Toast.LENGTH_SHORT);
                    String reason = String.valueOf(entryEdit.getText());
                    if (reason == null || reason.equals("") || reason.replaceAll("\\s", "").equals("")) {
                    } else {
                        saveApplicant(reason, broadcastList.get(pos).getBroadcastId());
                        savingBroadcast = broadcastList.get(pos);
                    }
                }
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (applicantExists == false && sameCreator == false && workerExists == false && butttonCounter % 2 == 0) {

                    if (broadcastList.get(pos).getAcceptanceType().equals("Automatic")) {
                        savingBroadcast = broadcastList.get(pos);
                        saveWorker(broadcastList.get(pos).getBroadcastId());
                    } else {
                        scrollView.setVisibility(View.GONE);
                        projectShortDescription.setVisibility(View.GONE);
                        shortPlaceholder.setVisibility(View.GONE);
                        projectLongDescription.setVisibility(View.GONE);
                        entryDisplay.setVisibility(View.VISIBLE);
                        entryEdit.setVisibility(View.VISIBLE);

                        acceptButton.setBackgroundColor(Color.TRANSPARENT);
                        acceptButton.setTextColor(Color.parseColor("#828282"));
                        acceptButton.setText("Back");
                        cancelButton.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                        cancelButton.setTextColor(Color.parseColor("#35C80B"));
                        cancelButton.setText("Submit Application");
                        butttonCounter++;
                    }
                } else if (applicantExists == false && sameCreator == false && workerExists == false && butttonCounter % 2 == 1) {
                    scrollView.setVisibility(View.VISIBLE);
                    projectShortDescription.setVisibility(View.VISIBLE);
                    shortPlaceholder.setVisibility(View.VISIBLE);
                    projectLongDescription.setVisibility(View.VISIBLE);
                    entryDisplay.setVisibility(View.GONE);
                    entryEdit.setVisibility(View.GONE);
                    acceptButton.setBackground(getResources().getDrawable(R.drawable.create_project_skill_add_button));
                    acceptButton.setTextColor(Color.parseColor("#6CACFF"));
                    acceptButton.setText("Request To Join");
                    cancelButton.setBackgroundColor(Color.TRANSPARENT);
                    cancelButton.setTextColor(Color.parseColor("#828282"));
                    cancelButton.setText("Cancel");
                    butttonCounter++;
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                applicantExists = false;
                workerExists = false;
                sameCreator = false;
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void saveWorker(final String broadcastId) {
        User user = SessionStorage.getUser(getActivity());

        final Worker worker = new Worker();
        worker.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        worker.setWorkerName(currentUser.getCurrentUser().getDisplayName());
        worker.setProjectId(broadcastId);
        worker.setProfilePicURL(user.getProfileImageLink());
        worker.setLocationTags(user.getLocationTags());
        worker.setInterestTags(user.getInterestTags());

        Object[] array = {worker};

        db.collection("Projects").document(broadcastId).update("workersList", FieldValue.arrayUnion(array))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + "Success");
                            List<String> workersID = new ArrayList<>();
                            if (broadcast.getWorkersId() == null) {
                                workersID.add(worker.getUserId());
                                db.collection("Projects").document(broadcastId).update("workersId", FieldValue.arrayUnion(workersID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: " + "Applicant Id update");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + "Applicant Id update");
                                    }
                                });
                            } else {
                                db.collection("Projects").document(broadcastId).update("workersId", FieldValue.arrayUnion(worker.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: " + "Applicant Id update");
                                        dialog.dismiss();
                                        showCompletedDialog();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + "Applicant Id update");
                                    }
                                });
                            }

                        } else {
                            Log.d(TAG, "onComplete: " + "Failure");
                        }
                    }
                });

        List<String> locationTags = savingBroadcast.getLocationTags();
        List<String> interestTags = savingBroadcast.getInterestTags();

        Log.d(TAG, locationTags.toString() + '\n' + interestTags.toString());

        for (final String location : locationTags) {
            for (final String interest : interestTags) {
                db.collection("MasterProjectCollection")
                        .document(location)
                        .collection(interest)
                        .document(broadcastId)
                        .update("workersList", FieldValue.arrayUnion(array));

                db.collection("MasterProjectCollection")
                        .document(location)
                        .collection(interest)
                        .document(broadcastId)
                        .update("workersId", FieldValue.arrayUnion(user.getUserId()));
            }
        }
    }

    public void saveApplicant(String shortPitch, final String projectId) {

        User user = SessionStorage.getUser(getActivity());

        final Applicant applicant = new Applicant();
        applicant.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        applicant.setApplicantName(currentUser.getCurrentUser().getDisplayName());
        applicant.setProjectId(projectId);
        applicant.setShortPitch(shortPitch);
        applicant.setApplicantPhn(Objects.requireNonNull(currentUser.getCurrentUser()).getEmail());
        applicant.setProfilePicURL(user.getProfileImageLink());
        applicant.setLocationTags(user.getLocationTags().toString());
        applicant.setInterestTags(user.getInterestTags().toString());

        Object[] array = {applicant};


        db.collection("Projects").document(projectId).update("applicantList", FieldValue.arrayUnion(array))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + "Success");
                            List<String> applicantIds = new ArrayList<>();
                            if (broadcast.getApplicantId() == null) {
                                applicantIds.add(applicant.getUserId());
                                db.collection("Projects").document(projectId).update("applicantId", FieldValue.arrayUnion(applicantIds)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: " + "Applicant Id update");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + "Applicant Id update");
                                    }
                                });
                            } else {
                                db.collection("Projects").document(projectId).update("applicantId", FieldValue.arrayUnion(applicant.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: " + "Applicant Id update");
                                        dialog.dismiss();
                                        showCompletedDialog();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + "Applicant Id update");
                                    }
                                });
                            }

                            int newApplicants = broadcast.getNewApplicants() + 1;
                            broadcast.setNewApplicants(newApplicants);
                            SessionStorage.saveProject(getActivity(), broadcast);
                            db.collection("Projects").document(projectId).update("newApplicants", newApplicants).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: " + "Applicant Id update");
                                    dialog.dismiss();
                                    showCompletedDialog();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + "Applicant Id update");
                                }
                            });

                        } else {
                            Log.d(TAG, "onComplete: " + "Failure");
                        }
                    }
                });
    }

    public void showCompletedDialog() {
        completedDialog.setContentView(R.layout.application_confirmation_popup);
        completedDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button button = completedDialog.findViewById(R.id.completedDialogeDoneButton);
        TextView title = completedDialog.findViewById(R.id.applyConfirmationTitle);
        TextView description = completedDialog.findViewById(R.id.applyConfirmationDescription);

        if(savingBroadcast.getAcceptanceType().equals("Automatic")) {
            title.setText("Successfully joined");
            description.setText("Congratulations! You are now part of the " + savingBroadcast.getBroadcastName() + " circle. Head over to workbench to view the groups activity");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completedDialog.dismiss();
            }
        });
        completedDialog.show();
    }

    public void loadBroadcastList() {

        broadcastList = new ArrayList<>();
        if (broadcastList.size() > 0)
            broadcastList.clear();

        List<String> locationTags = user.getLocationTags();
        List<String> interestTags = user.getInterestTags();

        for (final String location : locationTags) {
            for (final String interest : interestTags) {
                db.collection("MasterProjectCollection")
                        .document(location)
                        .collection(interest)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        Broadcast broadcast = documentSnapshot.toObject(Broadcast.class);
                                        boolean contains = false;
                                        for (Broadcast b : broadcastList) {
                                            if (b.getBroadcastId().equals(broadcast.getBroadcastId())) {
                                                contains = true;
                                            }
                                        }

                                        if (!contains)
                                            broadcastList.add(broadcast);
                                    }
                                    adapter = new BroadcastAdapter(getActivity(), broadcastList);
                                    lvBroadcast.setAdapter(adapter);

                                    prsearch.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            String text = prsearch.getText().toString().toLowerCase(Locale.getDefault());
                                            adapter.filter(text);
                                        }
                                    });


                                    lvBroadcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            applicantExists = false;
                                            sameCreator = false;
                                            workerExists = false;

                                            if (broadcastList.get(i).getCreatorId().equals(Objects.requireNonNull(currentUser.getCurrentUser()).getUid())) {
                                                Log.d(TAG, broadcastList.get(i).getCreatorId() + "\n" + currentUser.getCurrentUser().getUid());
                                                sameCreator = true;
                                            }

                                            if (broadcastList.get(i).getApplicantId() != null) {
                                                if (broadcastList.get(i).getApplicantId().contains(Objects.requireNonNull(currentUser.getCurrentUser()).getUid())) {
                                                    applicantExists = true;
                                                }
                                            }

                                            if (broadcastList.get(i).getWorkersId() != null) {
                                                if (broadcastList.get(i).getWorkersId().contains(Objects.requireNonNull(currentUser.getCurrentUser()).getUid())) {
                                                    workerExists = true;
                                                }
                                            }

                                            showApplyDialogue(i);
                                        }
                                    });

                                } else {
                                    Log.d("Logger", "No such Valid Item");
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }

    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

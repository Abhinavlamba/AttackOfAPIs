package com.nsh.pucho.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nsh.pucho.Adapter.CardAdapter;
import com.nsh.pucho.Adapter.LabelAdapter;
import com.nsh.pucho.Database.DatabaseHelper;
import com.nsh.pucho.Extra.Card;
import com.nsh.pucho.Extra.Label;
import com.nsh.pucho.Extra.Sample;
import com.nsh.pucho.Listener.RecyclerTouchListener;
import com.nsh.pucho.R;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class CviFrag extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String rating_value="1";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<Card> cardList1 = new ArrayList<>();
    private List<Card> cardList = new ArrayList<>();
    private RecyclerView cvi_media_rec, use_own_rec1;
    private CardAdapter mCardAdapter, mCardAdapter1;
    private Sample sample = new Sample();

    private List<Label> labelList = new ArrayList<>();
    private List<Label> labelList1 = new ArrayList<>();
    private List<Label> labelList2 = new ArrayList<>();
    private RecyclerView cvi_label, cvi_shot, cvi_explicit;
    private LabelAdapter mLabelAdapter, mLabelAdapter1, mLabelAdapter2;

    public CviFrag() {
    }

    public static CviFrag newInstance(String param1, String param2) {
        CviFrag fragment = new CviFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cvi, container, false);
        recView(view);
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            process = Runtime.getRuntime().exec( "logcat -f " + "/storage/emulated/0/"+"Logging.txt");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        /*System.out.println(sample.cvi11r());
        System.out.println(sample.cvi12r());
        System.out.println(sample.cvi13r());
        System.out.println(sample.cvi21r());
        System.out.println(sample.cvi22r());
        System.out.println(sample.cvi23r());*/
        return view;
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(getContext(), " No Internet Connection available ", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void recView(View view) {

        cvi_media_rec = view.findViewById(R.id.cvi_media_rec);
        use_own_rec1 = view.findViewById(R.id.use_own_rec1);
        final LinearLayout k = view.findViewById(R.id.loading);

        mCardAdapter = new CardAdapter(getContext(), cardList);
        mCardAdapter1 = new CardAdapter(getContext(), cardList1);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        cvi_media_rec.setLayoutManager(mLayoutManager);
        use_own_rec1.setLayoutManager(mLayoutManager1);

        cvi_media_rec.setItemAnimator(new DefaultItemAnimator());
        cvi_media_rec.setAdapter(mCardAdapter);

        use_own_rec1.setItemAnimator(new DefaultItemAnimator());
        use_own_rec1.setAdapter(mCardAdapter1);

        prepareCardData();
        prepareCard1Data();

        cvi_media_rec.addOnItemTouchListener(new RecyclerTouchListener(getContext(), cvi_media_rec, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, int position) {
                final Card card = cardList.get(position);
                final Dialog dialog = new Dialog(getContext());

                k.setVisibility(View.VISIBLE);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.card_cvi);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final Handler handle = new Handler();
                if(checkInternetConnection()){
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            k.setVisibility(View.GONE);
                            dialog.show();
                        }
                    }, 5000);}
                else{
                    k.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                cvi_label = dialog.findViewById(R.id.cvi_label);
                cvi_shot = dialog.findViewById(R.id.cvi_shot);
                cvi_explicit = dialog.findViewById(R.id.cvi_explicit);

                mLabelAdapter = new LabelAdapter(labelList, getContext());
                mLabelAdapter1 = new LabelAdapter(labelList1, getContext());
                mLabelAdapter2 = new LabelAdapter(labelList2, getContext());

                FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
                flowLayoutManager.setAutoMeasureEnabled(true);
                cvi_label.setLayoutManager(flowLayoutManager);
                cvi_label.setItemAnimator(new DefaultItemAnimator());
                cvi_label.setAdapter(mLabelAdapter);


                FlowLayoutManager flowLayoutManager1 = new FlowLayoutManager();
                flowLayoutManager1.setAutoMeasureEnabled(true);
                cvi_shot.setLayoutManager(flowLayoutManager1);
                cvi_shot.setItemAnimator(new DefaultItemAnimator());
                cvi_shot.setAdapter(mLabelAdapter1);

                FlowLayoutManager flowLayoutManager2 = new FlowLayoutManager();
                flowLayoutManager2.setAutoMeasureEnabled(true);
                cvi_explicit.setLayoutManager(flowLayoutManager2);
                cvi_explicit.setItemAnimator(new DefaultItemAnimator());
                cvi_explicit.setAdapter(mLabelAdapter2);

                labelList.clear();
                labelList1.clear();
                labelList2.clear();
                prepareLabelData(position);

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        final Dialog dialog1 = new Dialog(getContext());
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.card_star);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        //dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog1.show();
                        TextView issue = dialog1.findViewById(R.id.issue);
                        issue.setOnClickListener(CviFrag.this);

                        RatingBar ratingBar = dialog1.findViewById(R.id.ratingBar);
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                if (v > 0)
                                    rating_value = String.valueOf(v);
                                else
                                    rating_value = String.valueOf(0);
                            }
                        });

                        dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                Toast.makeText(getContext(), "Thanks for the feedback", Toast.LENGTH_SHORT).show();
                                DatabaseHelper n = new DatabaseHelper(getContext());
                                n.insertRecent(card.getName(), rating_value, card.getFunction());

                            }
                        });

                    }
                });
                //Toast.makeText(getContext(), card.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareLabelData(int a) {

        Label label, label1, label2;
        switch (a) {

            case 0:

                ArrayList<String> x3 = sample.cvi11r();
                for (int i = 0; i < x3.size(); i++) {
                    label = new Label(x3.get(i));
                    Log.i("CVI Label", label.getLabel());
                    labelList.add(label);
                }
                mLabelAdapter.notifyDataSetChanged();

                ArrayList<String> x6 = sample.cvi13r();
                for (int i = 0; i < x6.size(); i++) {
                    label1 = new Label(x6.get(i));

                    Log.i("CVI Shot", label1.getLabel());
                    labelList1.add(label1);
                }
                mLabelAdapter1.notifyDataSetChanged();
                ArrayList<String> x5 = sample.cvi12r();
                for (int i = 0; i < x5.size(); i++) {
                    label2 = new Label(x5.get(i));

                    Log.i("CVI Explicit", label2.getLabel());
                    labelList2.add(label2);
                }
                mLabelAdapter2.notifyDataSetChanged();
                /*System.out.println(labelList);
                System.out.println(labelList1);
                System.out.println(labelList2);

                Log.d("CVI label", labelList.toString());
                Log.d("CVI Shot", labelList1.toString());
                Log.d("CVI Explicit", labelList2.toString());
*/
                break;
            case 1:
                ArrayList<String> x31 = sample.cvi21r();
                for (int i = 0; i < x31.size(); i++) {
                    label = new Label(x31.get(i));

                    Log.i("CVI Label", label.getLabel());
                    labelList.add(label);
                }

                ArrayList<String> x61 = sample.cvi23r();
                for (int i = 0; i < x61.size(); i++) {
                    label1 = new Label(x61.get(i));

                    Log.i("CVI Shot", label1.getLabel());
                    labelList1.add(label1);
                }

                ArrayList<String> x51 = sample.cvi22r();
                for (int i = 0; i < x51.size(); i++) {
                    label2 = new Label(x51.get(i));

                    Log.i("CVI Explicit", label2.getLabel());
                    labelList2.add(label2);
                }

                /*Log.d("CVI label", labelList.toString());
                Log.d("CVI Shot", labelList1.toString());
                Log.d("CVI Explicit", labelList2.toString());
*/
                mLabelAdapter.notifyDataSetChanged();
                mLabelAdapter1.notifyDataSetChanged();
                mLabelAdapter2.notifyDataSetChanged();
                break;
            default:
                break;

        }
    }

    private void prepareCardData() {
        Card card = new Card("GBikes & Dinosaur", "https://cdn.suwalls.com/wallpapers/fantasy/dinosaur-20061-1920x1080.jpg", "Video Intelligence");
        cardList.add(card);


        card = new Card("Cat Video", "https://i.ytimg.com/vi/YCaGYUIfdy4/maxresdefault.jpg", "Video Intelligence");
        cardList.add(card);


        mCardAdapter.notifyDataSetChanged();
    }

    private void prepareCard1Data() {
        Card card = new Card("Not supported", "https://cdn.shopify.com/s/files/1/1367/8297/products/CLOTHES_1024x1024.jpg", "Feature not available");
        cardList1.add(card);
        mCardAdapter1.notifyDataSetChanged();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:hadanis.singh@gmail.com"));
              //  intent.putExtra(Intent.EXTRA_EMAIL, "hadanis1.singh@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Issue in Cloud Video Intelligence Response");
                intent.putExtra(Intent.EXTRA_TEXT, "Explain the discrepancy and mention the media.");
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

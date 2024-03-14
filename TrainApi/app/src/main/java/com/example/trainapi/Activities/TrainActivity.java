package com.example.trainapi.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainapi.Adapter.TrainAdapter;
import com.example.trainapi.EndPoints.TrainApi;
//import com.example.trainapi.Model.TrainData;
import com.example.trainapi.MainActivity;
import com.example.trainapi.Model.TrainResponseModel;
import com.example.trainapi.Network.RetrofitClient;
import com.example.trainapi.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.gson.Gson;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.rvadapter.AdmobNativeAdAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private TrainAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView backBtn;
    Button watchAds;

    private InterstitialAd mInterstitialAd;
    private static final String TAG = "MainActivity";
    private AdView trainAdView;
    private RewardedAd rewardedAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        progressDialog = new ProgressDialog(TrainActivity.this);
        progressDialog.setMessage("Please Wait 🚉...");
        progressDialog.show();



        //======================================BANNER ADS =========================================//
        implementBannerAds();




        // =================================== Interstial Add ===================================== //
        implementInterstialAds();
        backBtn = findViewById(R.id.backBtnId);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd != null) {
                    moveToSearchTrain();
                    mInterstitialAd.show(TrainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
//                moveToSearchTrain();
            }
        });


        //================================================= REWARD Ads =============================//
        watchAds = findViewById(R.id.watchAd);
        loadRewarded();
        watchAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedAd != null) {
                    rewardedAd.show(TrainActivity.this, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Load a new rewarded ad
                        }
                    });

                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.");
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            loadRewarded();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.e(TAG, "Ad failed to show fullscreen content.");
                            rewardedAd = null;
                        }

                        @Override
                        public void onAdImpression() {
                            Log.d(TAG, "Ad recorded an impression.");
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.");
                        }
                    });

                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
            }
        });




        // ====================================DATA FETCHING ================================================//
        Intent intent = getIntent();
        String trainNoName = intent.getStringExtra("Train_no_name");

        // Create request body
        String requestBodyJson = "{\"search\": \"" + trainNoName + "\"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);


        //==================================== RETROFIT CALL ====================================//
        Call<List<TrainResponseModel>> call = new RetrofitClient()
                .getRetrofitInstance()
                .create(TrainApi.class)
                .searchTrain(requestBody);

        call.enqueue(new Callback<List<TrainResponseModel>>() {
            @Override
            public void onResponse(Call<List<TrainResponseModel>> call, Response<List<TrainResponseModel>> response) {
                progressDialog.dismiss();
                List<TrainResponseModel> trainDataList = response.body();
                if (response.isSuccessful() && !trainDataList.isEmpty()) {
                    generateTrainDataList(trainDataList);
                } else {
                    Log.e("ERROR", response.message());
                    Toast.makeText(TrainActivity.this, "Please Enter Correct Train No / Train Name", Toast.LENGTH_SHORT).show();
                    moveToSearchTrain();
                }
            }
            @Override
            public void onFailure(Call<List<TrainResponseModel>> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Toast.makeText(TrainActivity.this, "Server Error :< Try Again after some time! ", Toast.LENGTH_SHORT).show();
                moveToSearchTrain();
            }
        });
    }
    private void generateTrainDataList(List<TrainResponseModel> trainResponseModels){
        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainAdapter(this, trainResponseModels);
//        recyclerView.setAdapter(adapter);

        // ------------------------------------ Implementing Native Ads in Recycler View ----------------------------//


        AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.Companion.
                with("ca-app-pub-3940256099942544/2247696110", adapter, "small")
        .adItemInterval(1).build();
        recyclerView.setAdapter(admobNativeAdAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
    private void moveToSearchTrain()
    {
        Intent intent = new Intent(TrainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // Implementing Banner Ads:
    private void implementBannerAds()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        trainAdView = findViewById(R.id.trainAdView);
        trainAdView.loadAd(adRequest);

        trainAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                trainAdView.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked();
            }
        });
    }

    private void implementInterstialAds()
    {
        // dummy: ca-app-pub-3940256099942544/1033173712
        // real: ca-app-pub-5407774234677570/4046793234
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    public void loadRewarded()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
    }
}
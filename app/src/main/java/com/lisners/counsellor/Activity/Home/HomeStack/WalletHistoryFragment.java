package com.lisners.counsellor.Activity.Home.HomeStack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisners.counsellor.Activity.Home.HomeActivity;
import com.lisners.counsellor.Adapters.TransactionAdaptor;
import com.lisners.counsellor.ApiModal.Transaction;
import com.lisners.counsellor.GlobalData;
import com.lisners.counsellor.R;
import com.lisners.counsellor.utils.GetApiHandler;
import com.lisners.counsellor.utils.URLs;
import com.lisners.counsellor.zWork.adapter.TransactionAdapter;
import com.lisners.counsellor.zWork.commens.EndlessRecyclerViewScrollListener;
import com.lisners.counsellor.zWork.restApi.viewmodel.DataViewModel;
import com.lisners.counsellor.zWork.utils.Utils;
import com.lisners.counsellor.zWork.utils.ViewModelUtils;

import timber.log.Timber;

public class WalletHistoryFragment extends Fragment implements View.OnClickListener {

    DataViewModel dVM;
    TransactionAdapter tAdapter;


    LinearLayout ll_walletHeader, ll_withdrawMoney;
    TextView tvHeader, tvWalletMoney;
    ImageButton btn_header_left;
    LinearLayout lv_add_wallet, lv_withdraw_history;
    RecyclerView rv_history;
    ProgressBar pb_loader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_history, container, false);
        dVM = ViewModelUtils.getViewModel(DataViewModel.class, this);
        init(view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    private void init(View view) {

        ll_withdrawMoney = view.findViewById(R.id.ll_withdrawMoney);
        ll_walletHeader = view.findViewById(R.id.ll_walletHeader);
        ll_walletHeader.setVisibility(View.VISIBLE);
        ll_withdrawMoney.setVisibility(View.GONE);
        tvHeader = view.findViewById(R.id.tvHeader);
        tvWalletMoney = view.findViewById(R.id.tvWalletMoney);

        btn_header_left = view.findViewById(R.id.btn_header_left);
        btn_header_left.setImageResource(R.drawable.ic_svg_header_menu);

        lv_withdraw_history = view.findViewById(R.id.lv_withdraw_history);
        lv_withdraw_history.setOnClickListener(this);
        pb_loader = view.findViewById(R.id.pb_loader);
        btn_header_left.setOnClickListener(this);
        tvHeader.setText("Wallet");
        rv_history = view.findViewById(R.id.rv_history);


        lv_add_wallet = view.findViewById(R.id.lv_add_wallet);
        /*lv_add_wallet.setOnClickListener(this);*/
        lv_add_wallet.setVisibility(View.GONE);

        initView();
    }





    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.btn_header_left:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.lv_add_wallet:
                transaction.replace(R.id.fragment_container, new AddMoneyWalletFragment());
                transaction.addToBackStack("HomeFragment");
                transaction.commit();
                break;
            case R.id.lv_withdraw_history:
                transaction.replace(R.id.fragment_container, new WithdrawMoneyFragment());
                transaction.addToBackStack("HomeFragment");
                transaction.commit();
                break;
        }
    }


    private void initView() {


        //top
        Utils.setNestedScrollingEnabledFalse(rv_history);
        rv_history.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) rv_history.getLayoutManager()) {

            public void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView) {

                Timber.e("Page No %d", page);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNextData(page);
                    }
                }, 0);

            }

            @Override
            public int getStartPage() {
                return 1;
            }
        };

        rv_history.addOnScrollListener(scrollListener);

        getData();

    }


    private void getData() {
        pb_loader.setVisibility(View.VISIBLE);

        dVM
                .getWalletHistory(1)
                .observe(getViewLifecycleOwner(), response -> {
                    pb_loader.setVisibility(View.GONE);

                    if (response.getStatus() && response.getData() != null) {
                        GlobalData.dashboard_balance = String.valueOf(response.getData().getWallet());
                        tvWalletMoney.setText("₹ " + GlobalData.dashboard_balance);


                        if (response.getData().getTransaction() != null && response.getData().getTransaction().getData().size() > 0) {


                            tAdapter = new TransactionAdapter(response.getData().getTransaction().getData(), getActivity());
                            rv_history.setAdapter(tAdapter);

                            tAdapter.isFooterShow = true;
                            tAdapter.notifyItemChanged(tAdapter.getListSize());
                        }
                    }

                });
    }

    private void getNextData(int page) {
        dVM
                .getWalletHistory(page)
                .observe(getViewLifecycleOwner(), response -> {

                    if (response.getStatus() && response.getData() != null) {
                        if (response.getData().getTransaction() != null && response.getData().getTransaction().getData().size() > 0) {
                            tAdapter.addAll(response.getData().getTransaction().getData());
                        }
                    }
                    else {
                        tAdapter.isFooterShow = false;
                        tAdapter.notifyItemChanged(tAdapter.getListSize());
                    }

                });

    }

}
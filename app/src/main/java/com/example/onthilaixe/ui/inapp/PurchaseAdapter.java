package com.example.onthilaixe.ui.inapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.billingclient.api.ProductDetails;
import com.example.onthilaixe.R;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private OnClickListener onClickListener;
    private List<ProductDetails> productDetailsListc = new ArrayList<>();
    private Context context;

    public void setData(Context context, List<ProductDetails> productDetailsList) {
        this.productDetailsListc = productDetailsList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.
                item_subscription, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.display(productDetailsListc.get(position));
    }

    @Override
    public int getItemCount() {
        return productDetailsListc.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubName;
        private ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            item = itemView.findViewById(R.id.item);
        }

        public void display(ProductDetails productDetails) {
            if (productDetails == null) return;
            String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
            tvSubName.setText(setTitleValue(productDetails.getProductId(), price));
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClickItem(productDetails);
                }
            });
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickItem(ProductDetails item);
    }

    private String setTitleValue(String productId, String price) {
        switch (productId) {
            case Constant.KEY_GOLD_1:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/1 gold");
            case Constant.KEY_GOLD_2:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/5 gold");
            case Constant.KEY_GOLD_3:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/30 gold");
            case Constant.KEY_GOLD_4:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/50 gold");
            case Constant.KEY_GOLD_5:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/80 gold");
            case Constant.KEY_GOLD_6:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/120 gold");
            case Constant.KEY_GOLD_7:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/150 gold");
            case Constant.KEY_GOLD_8:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/250 gold");

            default:
                return String.format(context.getResources().getString(R.string.message_purchase_one), "/0 gold");
        }
    }
}

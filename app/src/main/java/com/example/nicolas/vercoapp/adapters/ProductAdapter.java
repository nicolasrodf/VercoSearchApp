package com.example.nicolas.vercoapp.adapters;


import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolas.vercoapp.model.Product;
import com.example.nicolas.vercoapp.R;
import com.example.nicolas.vercoapp.service.ProductService;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Product> productList;
    private OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductList(Product product);
    }

    public void setOnProductClickListener(OnProductClickListener onProductClickListener){
        this.onProductClickListener = onProductClickListener;
    }

    public ProductAdapter(Context context, ArrayList<Product> productList){
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        final Product product = productList.get(position);
        holder.trademarkTextView.setText(product.getTrademark());
        holder.modelTextView.setText(product.getModel());
        holder.imageView.setImageBitmap(product.getPhotoBitmap());
        if(product.getDiscount()!=0){

            //Filtro de acuerdo al género
            ProductService productService = new ProductService();
            Integer discountPercent = productService.getDiscountPercent(product.getDiscount());
            Double discountPrice = productService.getPriceDiscount(product.getDiscount(), product.getPrice());
            holder.discountTextView.setText("-" + discountPercent.toString() + "%");
            holder.discountTextView.setTextSize(10);
            holder.normalPriceTextView.setText("S/." + String.format("%.2f",product.getPrice()));
            holder.normalPriceTextView.setPaintFlags(holder.normalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discountPriceTextView.setText("S/." + String.format("%.2f",discountPrice));
        } else {
            holder.circleImageView.setVisibility(View.GONE);
            holder.discountTextView.setVisibility(View.GONE);
            holder.discountPriceTextView.setText("S/." + String.format("%.2f",product.getPrice()));
            holder.normalPriceTextView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductClickListener.onProductList(product);
            }
            //en vez de hacerlo para el itemView se podria hacer para click en otra parte (petNameTextView, u otro)
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView trademarkTextView;
        TextView modelTextView;
        TextView discountTextView;
        TextView normalPriceTextView;
        TextView discountPriceTextView;
        ImageView circleImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImageView);
            trademarkTextView = itemView.findViewById(R.id.trademarkTextView);
            modelTextView = itemView.findViewById(R.id.modelTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
            normalPriceTextView = itemView.findViewById(R.id.normalPriceTextView);
            discountPriceTextView = itemView.findViewById(R.id.discountPriceTextView);
            circleImageView = itemView.findViewById(R.id.circleImageView);

        }
    }

}

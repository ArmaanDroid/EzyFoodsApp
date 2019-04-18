package sanguinebits.com.ezyfoods.home.fragments.cart;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import model.CartItem;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VHolder> {
   private List<CartItem> cartItems;
    private CartAdapterListner listner;
    public CartAdapter(List<CartItem> cartItems,CartAdapterListner listner) {
        this.cartItems = cartItems;
        this.listner=listner;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_cart_item,viewGroup,false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vHolder, int i) {
        try {
            vHolder.setData(cartItems.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        private final SimpleDraweeView imageView;
        private final TextView textViewCount;
        private final TextView textViewItemName;
        private final TextView textViewPrice;
        private final ImageView imageViewCancel;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            imageView=    itemView.findViewById(R.id.imageView1);
            textViewCount=    itemView.findViewById(R.id.textViewCount);
            textViewItemName=    itemView.findViewById(R.id.textView1);
            textViewPrice=    itemView.findViewById(R.id.textViewPrice);
            imageViewCancel=    itemView.findViewById(R.id.imageView8);
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(itemView.getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                    .setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER)
                    .setProgressBarImage(R.drawable.icon_large_process)
                    .setFailureImage(R.color.gradientLast)
                    .build();
            imageView.setHierarchy(hierarchy);

            imageViewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listner.deleteItem(getAdapterPosition());
                }
            });
        }

        public void setData(CartItem cartItem) throws Exception{
            textViewItemName.setText(cartItem.getDish().getTitle());
            textViewCount.setText(String.valueOf(cartItem.getCount()));

            if (!cartItem.getDish().getDishImage().isEmpty()) {
                String uri = AppConst.DISH_IMAGE_BASE_URL + cartItem.getDish().getDishImage();
                imageView.setImageURI(uri);
            }else{
                imageView.setImageResource(R.color.gradientLast);
            }

            Float price = Float.valueOf(cartItem.getDish().getPrice())*cartItem.getCount();
            textViewPrice.setText(String.valueOf(price));

        }
    }

    public interface CartAdapterListner{
        void deleteItem(int position);
    }
}

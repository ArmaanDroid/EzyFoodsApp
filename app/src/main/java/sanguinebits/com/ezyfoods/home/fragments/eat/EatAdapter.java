package sanguinebits.com.ezyfoods.home.fragments.eat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import model.Dish;
import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class EatAdapter extends RecyclerView.Adapter<EatAdapter.ViewHolder> {
    private EatAdapterInterface anInterface;
    private List<Dish> dishes;

    EatAdapter(List<Dish> dishes, EatAdapterInterface anInterface) {
        this.anInterface = anInterface;
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public EatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_eat, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EatAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(dishes.get(i));
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewItemName;
        private final TextView textViewButton;
        private final SimpleDraweeView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.textView13);
            textViewButton = itemView.findViewById(R.id.textView14);
            imageView = itemView.findViewById(R.id.imageView);

            textViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    anInterface.onClick(getAdapterPosition());
                }
            });

//            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(itemView.getResources())
//                    .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
//                    .setProgressBarImageScaleType(ScalingUtils.ScaleType.CENTER)
//                    .setProgressBarImage(R.drawable.icon_large_process)
//                    .setFailureImage(R.color.gradientLast)
//                    .build();
//            imageView.setHierarchy(hierarchy);
        }


        void setData(Dish dish) {
            textViewItemName.setText(TextUtils.concat(dish.getTitle(), " - ", dish.getCuisineType()," ($",dish.getPrice(),")"));
            if (!dish.getDishImage().isEmpty()) {
                String uri = AppConst.DISH_IMAGE_BASE_URL + dish.getDishImage();
                imageView.setImageURI(uri);
            }else{
                imageView.setImageResource(R.color.gradientLast);
            }
        }
    }

    interface EatAdapterInterface {
        void onClick(int position);
    }
}

package com.example.contactappai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private List<User> list;
    private Context context;
    private List<User> filterUserDataList;

    public CustomAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
        filterUserDataList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sampl_layout,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(filterUserDataList.get(position).getName());
        holder.letter.setText(filterUserDataList.get(position).getLetter());
        if(filterUserDataList.get(position).getPhoto()!=null){
            Picasso.get().load(filterUserDataList.get(position).getPhoto()).into(holder.circleImageView);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserInfoActivity.class);
                //MECARD:N:Owen,Sean;ADR:76 9th Avenue, 4th Floor, New York, NY 10011;TEL:12125551212;EMAIL:srowen@example.com;
                String pass = "MECARD:N:"+filterUserDataList.get(position).getName()+";ADR:"+filterUserDataList.get(position).getAddress()+
                        ";TEL:"+filterUserDataList.get(position).getNumber()+";EMAIL:"+filterUserDataList.get(position).getEmail()+
                        ";FACEBOOK:"+filterUserDataList.get(position).getFacebook()+";LINKEDIN:"+filterUserDataList.get(position).getLinkedin()+
                        ";PHTOTO:"+filterUserDataList.get(position).getPhoto()+";LETTER:"+filterUserDataList.get(position).getLetter()+";";
                intent.putExtra("value",pass);
                intent.putExtra("photo",filterUserDataList.get(position).getPhoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterUserDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,letter;
        private CircleImageView circleImageView;
        private ConstraintLayout layout;
        private ImageView qrCode;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layoutId);
            letter = itemView.findViewById(R.id.nameLetterId);
            name = itemView.findViewById(R.id.contactNameId);
            circleImageView = itemView.findViewById(R.id.imageView);
        }
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key = constraint.toString();
                if(key.isEmpty()){
                    filterUserDataList = list;
                }else{
                    List<User> listFilter = new ArrayList<>();
                    for(User user:list){
                        if(user.getName().toLowerCase().contains(key.toLowerCase())){
                            listFilter.add(user);
                        }
                    }
                    filterUserDataList = listFilter;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterUserDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterUserDataList = (List<User>)results.values;
                notifyDataSetChanged();
            }
        };
    }

}

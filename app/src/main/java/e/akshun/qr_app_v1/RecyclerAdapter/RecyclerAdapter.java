package e.akshun.qr_app_v1.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.akshun.qr_app_v1.R;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.QrcodeViewHolder> {

    private ArrayList<QRcode> qRcodeList;
    private OnqrcodeListener monqrcodeListener;


    public RecyclerAdapter(ArrayList<QRcode> qRcodeList, OnqrcodeListener onqrcodeListener) {
        this.qRcodeList = qRcodeList;
        this.monqrcodeListener = onqrcodeListener;
    }

    public interface OnqrcodeListener{
        void onqrClick(int position);
    }

    @NonNull
    @Override
    public QrcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item,parent,false);

        return new QrcodeViewHolder(view,monqrcodeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QrcodeViewHolder holder, int position) {

        QRcode qRcode = qRcodeList.get(position);
        holder.qrcodename.setText(qRcode.getQrcodename());
        holder.qrcodeimage.setImageResource(qRcode.getQrcodeimage());

    }

    @Override
    public int getItemCount() {
        return qRcodeList.size();
    }

    public  static  class QrcodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView qrcodename;
        public ImageView qrcodeimage;
        OnqrcodeListener onqrcodeListener;

        public QrcodeViewHolder(@NonNull View itemView,OnqrcodeListener onqrcodeListener) {
            super(itemView);
            qrcodename = itemView.findViewById(R.id.profile_name);
            qrcodeimage = itemView.findViewById(R.id.qr_image);
            this.onqrcodeListener = onqrcodeListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onqrcodeListener.onqrClick(getAdapterPosition());


        }
    }

}

package com.example.zeng.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zeng.weather.AppController;
import com.example.zeng.weather.R;
import com.example.zeng.weather.data.CityInfo;

import java.util.List;

public class EditCityActivity extends AppCompatActivity {
    private ImageButton btnBack,btnEdit,btnAdd;
    private RecyclerView recyclerView;
    private List<CityInfo> cityInfoList;
    private RecyclerAdapter adapter;
    private LinearLayoutManager manager;
    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnEdit = (ImageButton)findViewById(R.id.btn_edit);
        btnAdd = (ImageButton)findViewById(R.id.btn_add);
        recyclerView = (RecyclerView)findViewById(R.id.rv_city);

        cityInfoList = AppController.getInstance().getCityInfoList();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                int swipeFlags = 0;//0为不支持，表示任何方向上都不算滑动
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                cityInfoList = AppController.getInstance().swap(fromPosition,toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditCityActivity.this,OrientationActivity.class);
                intent.putExtra("isOrientation",false);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in_animation, R.anim.left_out_animation);
                EditCityActivity.this.finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adapter.isEdit){
                    adapter.setEdit(false);
                    adapter.notifyDataSetChanged();
                }
                else {
                    EditCityActivity.this.finish();
                    overridePendingTransition(R.anim.right_in_animation, R.anim.left_out_animation);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!adapter.isEdit){
                    adapter.setEdit(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
        private boolean isEdit;

        public RecyclerAdapter(){
            isEdit =false;
        }

        public void setEdit(boolean isEdit){
            this.isEdit = isEdit;
        }

        public boolean isEdit(){
            return isEdit;
        }

        @Override
        public int getItemCount() {
            return cityInfoList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    EditCityActivity.this).inflate(R.layout.itm_edit_city_recycler, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(cityInfoList.get(position).getCityName() + cityInfoList.get(position).getCityOrder());
            if(isEdit)holder.btn.setVisibility(View.VISIBLE);
            else holder.btn.setVisibility(View.GONE);
            holder.btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    cityInfoList = AppController.getInstance().delete(position);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView tv;
            public Button btn;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv_city);
                btn= (Button) view.findViewById(R.id.btn_delete);
            }
        }
    }

}

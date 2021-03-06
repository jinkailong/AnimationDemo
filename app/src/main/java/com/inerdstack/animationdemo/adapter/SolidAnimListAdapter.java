package com.inerdstack.animationdemo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inerdstack.animationdemo.R;
import com.inerdstack.animationdemo.anim.CustomAnimation;
import com.inerdstack.animationdemo.anim.TurnProcess;
import com.inerdstack.animationdemo.util.DensityUtil;

/**
 * Created by wangjie on 2016/11/11.
 */

public class SolidAnimListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private static final int LIST_SIZE = 10;

    // 动画转变线
    private float mTurningLine;
    // Recyclerview视图
    private RecyclerView mParentView;

    public SolidAnimListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recyclerview
        mParentView = (RecyclerView) parent;
        // 以recyclerview中等高度水平线作为动画转变的标准
        mTurningLine = 300;
        // 子项
        View view = LayoutInflater.from(mContext).inflate(R.layout.anim_list_item, parent, false);

        // 返回ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).initListener(mParentView, position);
    }

    @Override
    public int getItemCount() {
        return LIST_SIZE;
    }

    private float getItemHeight(View itemView) {

        return DensityUtil.dip2px(mContext, 160);

    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        // 视图容器
        private ViewGroup containerView;
        // 子项顶部所在的位置（像素点）
        private float itemTop = Integer.MIN_VALUE;

        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            containerView = (ViewGroup) itemView.findViewById(R.id.root_view);
            this.itemView = itemView;
        }

        /**
         * 初始化滑动监听
         */
        private void initListener(RecyclerView recyclerView, final int position) {
            // 初始化item高度
            final float itemHeight = DensityUtil.dip2px(mContext, 160);
            if (itemTop == Integer.MIN_VALUE) {
                itemTop = position * itemHeight;
            }
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    // 获取recyclerview的布局管理器
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    float itemTop = layoutManager.getDecoratedTop(itemView);
                    int process = TurnProcess.getProcess(itemTop, mTurningLine, itemHeight);
                    initAnim(containerView, process);

                }
            });
        }
    }


    private void initAnim(ViewGroup viewGroup, int process) {
        CustomAnimation animation = new CustomAnimation(mContext);
        animation.setView(viewGroup);
        animation.setAlphaViewId(R.id.mark);
        animation.setCornersViewId(R.id.img_pic);
        animation.setImageViewId(R.id.img_pic);
        animation.setMarginViewId(R.id.container);
        animation.setMarginHorizontal(DensityUtil.dip2px(mContext, 16));
        animation.setCornerRadius(DensityUtil.dip2px(mContext, 6));

        animation.setAnimByProcess(process);
    }

    /**
     * 获取滚动的距离
     */
    private int getScrollDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibleItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }
}

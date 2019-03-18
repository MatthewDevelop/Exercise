package edu.whpu.matthew.listviewtest;

import java.util.List;

/**
 * Created by matthew on 17-3-11.
 */

public class MyAdapter extends MyBaseAdapter<Student> {

    public MyAdapter(List<Student> data) {
        super(data);
    }

    @Override
    public void setData(ViewHolder holder, Student t) {
        holder.setText(R.id.mTv1, t.getName()).setText(R.id.mTv2, t.getSex());

    }

}

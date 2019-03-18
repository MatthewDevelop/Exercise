package edu.whpu.matthew.memoryLeak;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by matthew on 17-6-26.
 */

public class TextViewHelper {

    private Context mContext;
    private static TextViewHelper mHelper=null;
    private TextView mTextView;


    public static TextViewHelper getInstance(Context context){
        if(mHelper==null){
            mHelper=new TextViewHelper(context);
        }
        return mHelper;
    }

    private TextViewHelper(Context context) {
        mContext=context;
    }

    public void setText(TextView textView){
        mTextView=textView;
        mTextView.setText(mContext.getString(R.string.app_name));
    }

    public void removeTv(){
        mTextView=null;
    }
}

package edu.whpu.matthew.animationmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener{

    private RelativeLayout level1,level2,level3;
    private ImageView iv_home,iv_menu;
    private boolean isLevel2Shown=true;
    private boolean isLevel3Shown=true;
    private boolean isMenuShown=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setListener();
    }

    private void setListener() {
        iv_home.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        level1= (RelativeLayout) findViewById(R.id.level1);
        level2= (RelativeLayout) findViewById(R.id.level2);
        level3= (RelativeLayout) findViewById(R.id.level3);
        iv_home= (ImageView) findViewById(R.id.iv_home);
        iv_menu= (ImageView) findViewById(R.id.iv_menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_MENU){
            if(isMenuShown){
                int offset=0;
                if(isLevel3Shown){
                    AnimationUtil.closeMenu(level3,offset);
                    offset+=200;
                    isLevel3Shown=false;
                }
                if(isLevel2Shown){
                    AnimationUtil.closeMenu(level2,offset);
                    offset+=200;
                    isLevel2Shown=false;
                }

                AnimationUtil.closeMenu(level1,offset);
            }else {
                AnimationUtil.openMenu(level1,0);
                AnimationUtil.openMenu(level2,200);
                isLevel2Shown=true;
                AnimationUtil.openMenu(level3,400);
                isLevel3Shown=true;
            }

            isMenuShown=!isMenuShown;

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_home:
                if(AnimationUtil.count!=0){
                    return;
                }
                int offset=0;
                if(isLevel2Shown){
                    if(isLevel3Shown){
                        AnimationUtil.closeMenu(level3,0);
                        isLevel3Shown=false;
                        offset+=200;
                    }
                    AnimationUtil.closeMenu(level2,offset);
                }else{
                    AnimationUtil.openMenu(level2,offset);
                }
                isLevel2Shown=!isLevel2Shown;
                break;

            case R.id.iv_menu:
                if(AnimationUtil.count!=0){
                    return;
                }
                if(isLevel3Shown){
                    AnimationUtil.closeMenu(level3,0);
                }else{
                    AnimationUtil.openMenu(level3,0);
                }
                isLevel3Shown=!isLevel3Shown;
                break;
            default:
                break;
        }
    }
}

package piyushjohnson.assistdot;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by samuelraj on 04/04/18.
 */

public class SliderAdapter extends PagerAdapter {

    Context context;
    Button button;
    public ViewGroup viewGroup;
    int pages_total = 4;

    public int[] slides = {
        R.layout.slide_1,
        R.layout.slide_2,
        R.layout.slide_3,
        R.layout.slide_4,
    };

    public SliderAdapter(Context context,ViewGroup viewGroup){
        this.context = context;
        this.viewGroup = viewGroup;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        RelativeLayout layout =(RelativeLayout) View.inflate(context,slides[position],null);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public int getCount() {
        return pages_total;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}

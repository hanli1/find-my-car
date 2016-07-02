package example.com.findmycar;


import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionFragment extends Fragment
{
    private TextView distance;
    private ImageView arrow;
    public DirectionFragment()
    {
        // Required empty public constructor
    }

    public static DirectionFragment newInstance(float distance, float rotation)
    {
        DirectionFragment fragment = new DirectionFragment();
        Bundle args = new Bundle();
        args.putFloat("distance", distance);
        args.putFloat("rotation", rotation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle args = getArguments();
        float distance = args.getFloat("distance", 0);
        float rotation = args.getFloat("rotation", 0);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_direction, container, false);
        this.distance = (TextView) v.findViewById(R.id.fragment_direction_distance);
        this.arrow = (ImageView) v.findViewById(R.id.fragment_direction_arrow);

        updateUI(distance, rotation);
        return v;
    }

    public void updateUI(float distance, final float rotation)
    {
        String ending = " m";
        if(distance >= 1000)
        {
            distance /= 1000;
            ending = " km";
        }
        String stringDistance = String.format("%.2f", distance);
        this.distance.setText(stringDistance + ending);

        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(this.arrow ,
                "rotation", this.arrow.getRotation(), rotation);
        imageViewObjectAnimator.setInterpolator(new FastOutSlowInInterpolator());
        imageViewObjectAnimator.setDuration(700); // miliseconds
        imageViewObjectAnimator.start();
    }

}

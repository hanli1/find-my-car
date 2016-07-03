package example.com.findmycar;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.GeofenceStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectionFragment extends Fragment
{
    private LinearLayout saveSpot;
    private FrameLayout findCar;
    private TextView currentStatus;
    private LinearLayout revealView;
    private ImageView bubble;

    private Geocoder geocoder;
    private OnDataPass dataPasser;
    private boolean animatingBubble;

    private float bubbleInitialX;
    private float bubbleInitialY;

    @Override
    public void onAttach(Context c)
    {
        super.onAttach(c);
        dataPasser = (OnDataPass) c;
    }

    public SelectionFragment()
    {
        // Required empty public constructor
    }

    public static SelectionFragment newInstance()
    {
        SelectionFragment fragment = new SelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_selection, container, false);
        saveSpot = (LinearLayout) view.findViewById(R.id.fragment_selection_save_spot);
        findCar = (FrameLayout) view.findViewById(R.id.fragment_selection_find_car);
        currentStatus = (TextView) view.findViewById(R.id.fragment_selection_current_status);
        revealView = (LinearLayout) view.findViewById(R.id.fragment_selection_bottom_reveal);
        bubble = (ImageView) view.findViewById(R.id.fragment_selection_bubble);

        saveSpot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(dataPasser.setParkingLocation() && !animatingBubble)
                {
                    //circleRevealBottomPanel();
                    animateBubbleArc();
                }
                else
                    Toast.makeText(getActivity(), "No parking location set", Toast.LENGTH_LONG).show();
            }
        });


        revealView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dataPasser.findMyCar();
            }
        });

        animatingBubble = false;
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener()
                {
                    @Override
                    public void onGlobalLayout()
                    {
                        // Layout has happened here.
                        bubbleInitialX = bubble.getX();
                        bubbleInitialY = bubble.getY();
                        // Don't forget to remove your listener when you are done with it.
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
        return view;
    }

    public void setCurrentStatus(Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        currentStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        currentStatus.setText(getFormattedAddress(location));
    }

    public String getFormattedAddress(Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try
        {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        String ret = "";
//        if(knownName != null)
//            ret += knownName + "\n";
        return ret + address + "\n" + city + ", " + state + " " + postalCode;
    }

    public interface OnDataPass
    {
        boolean setParkingLocation();

        void findMyCar();
    }

    private void circleRevealBottomPanel()
    {
        if(revealView.getVisibility() == View.VISIBLE)
        {
            Utilities.darkenThenLightenAnimation(getActivity(), revealView).start();
        }
        else
        {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    revealView,
                    revealView.getWidth() / 2,
                    revealView.getHeight() / 2, 0,
                    revealView.getWidth());

            //mRevealBackgroundView.setBackgroundColor(getResources().getColor(fromColor));
            animator.setInterpolator(Utilities.fastOutSlowInInterpolator);
            animator.setDuration(500);
            animator.start();
            revealView.setVisibility(View.VISIBLE);
        }
    }

    private void animateBubbleArc()
    {
        float currX = bubble.getX();
        float currY = bubble.getY();
        Path curve = new Path();
        curve.moveTo(currX, currY);
        curve.cubicTo(currX, currY, currX + 250, currY + 250, currX, currY + 500);
        final PathMeasure pathMeasure = new PathMeasure(curve, false);

        final float length = pathMeasure.getLength();
        final float aCoordinates[] = {currX, currY};

        final ValueAnimator movementAnimation = ValueAnimator.ofObject(new FloatEvaluator(),
                0, 1);
        movementAnimation.setInterpolator(Utilities.fastOutSlowInInterpolator);
        movementAnimation.setDuration(500); // milliseconds

        movementAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(ValueAnimator animator)
            {
                float currValue = (float) animator.getAnimatedValue();
                pathMeasure.getPosTan(length * currValue, aCoordinates, null);
                bubble.setX(aCoordinates[0]);
                bubble.setY(aCoordinates[1]);
                bubble.setScaleX(1 - currValue);
                bubble.setScaleY(1 - currValue);

            }

        });
        movementAnimation.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                bubble.animate().alpha(1).setDuration(100);
                bubble.setVisibility(View.VISIBLE);
                animatingBubble = true;
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                bubble.animate().alpha(0).setDuration(100).withEndAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //why I don't have to reset scale?
                        //bubble.setScaleX(1);
                        bubble.setX(bubbleInitialX);
                        bubble.setY(bubbleInitialY);
                        circleRevealBottomPanel();
                    }
                });

                animatingBubble = false;
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });
        movementAnimation.start();

    }

}

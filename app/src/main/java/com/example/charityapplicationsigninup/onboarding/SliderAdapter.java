package com.example.charityapplicationsigninup.onboarding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.charityapplicationsigninup.R;

public class SliderAdapter extends PagerAdapter {

    private final static String TAG="SliderAdapter";


    LayoutInflater layoutInflater;
    Context context;
    private int length;


     public SliderAdapter(Context context){
         this.context=context;
    }

    //values for slider
    //images
    public int[] slide_images={
             R.drawable.app_logo,
            R.drawable.give_food,
            R.drawable.rain
    };

     //heading
    public String[] slide_heading = {
            //1
             "Welcome to Kindness.",
             //2
             "There is no exercise better for the heart than reaching " +
                     "down and lifting people up. ",
             //3
             "Sign up now",
    };

    //description
    public String [] slide_description = {
            //1
            "We only have what we give",
            //2
            "No one has ever become poor by giving.",
            //3
            ""
    };

    @Override
    public int getCount() {

        //total number of slides
        //add one for video slide
        length= slide_heading.length+1;
        return length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService( context.LAYOUT_INFLATER_SERVICE );
        View view=layoutInflater.inflate( R.layout.slide_layout,container,false );


        TextView slideHeading = (TextView) view.findViewById( R.id.headingView );
        TextView slideText = (TextView) view.findViewById( R.id.descriptionView );
        ImageView slideImage = (ImageView) view.findViewById( R.id.imageView );
        VideoView slideVideo =(VideoView) view.findViewById( R.id.videoView );




        if(position == length-1) {
            slideHeading.setVisibility( View.GONE );
            slideImage.setVisibility( view.GONE );
            slideText.setVisibility( View.GONE );
            playVideo( slideVideo );
        }

            //handle different case
            else {

            slideImage.setImageResource( slide_images[position] );
            slideHeading.setText( slide_heading[position] );
            slideText.setText( slide_description[position] );
            slideVideo.setVisibility( View.GONE );

            if(position==1){
                slideHeading.setBackgroundResource( R.drawable.heading_two );
                slideHeading.setTextColor( context.getResources().getColor( R.color.colorPrimary ) );
                slideHeading.setTextSize(20);

                slideHeading.setTypeface( Typeface.DEFAULT );

                slideText.setBackgroundResource( R.drawable.heading_one );
                slideText.setTextColor( context.getResources().getColor( R.color.colorWhite ) );
                slideText.setTextSize(20);

            }
        }

        container.addView( view );
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (RelativeLayout)object );
    }

    private void playVideo(VideoView videoView) {

        String path = "android.resource://" + context.getPackageName() + "/" + R.raw.vid;
        videoView.setVideoURI(Uri.parse(path));
        MediaController mediaC = new MediaController( this.context);
        videoView.setMediaController( mediaC );
        mediaC.setAnchorView( videoView );

        videoView.start();

    }

}

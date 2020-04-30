package com.example.charityapplicationsigninup.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.charityapplicationsigninup.R;
import com.example.charityapplicationsigninup.accounts.WelcomeActivity;

public class Onboarding extends AppCompatActivity {
    private static final String TAG="Onboarding";


    private ViewPager slideViewPager;
    private LinearLayout dotsLayout;
    private SliderAdapter sliderAdapter;

    private TextView[] dots;

    //Buttons
    private Button skipBtn;
    private Button nextBtn;
    private Button backBtn;

    private int currentPage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_onboarding );

        //Views
        slideViewPager= (ViewPager) findViewById( R.id.slideViewPager );
        dotsLayout=(LinearLayout) findViewById( R.id.dotsLayout );
        skipBtn=(Button) findViewById( R.id.skip_btn );
        backBtn=(Button) findViewById( R.id.back_btn );
        nextBtn=(Button) findViewById( R.id.next_btn );


        sliderAdapter=new SliderAdapter( this );
        slideViewPager.setAdapter( sliderAdapter );
        addDotsIndicator( 0 );

        slideViewPager.addOnPageChangeListener( viewListener );

        skipBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });

        backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem( currentPage-1 );
            }
        } );

        nextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage+1 == dots.length)
                {
                    goToMain();
                }
                slideViewPager.setCurrentItem( currentPage+1 );
            }
        } );

    }


    private void goToMain(){
        Intent intent =new Intent( getBaseContext(),WelcomeActivity.class );
        startActivity( intent );
        finish();
    }

    //count number of items that you need
    public void addDotsIndicator(int position){

        dots=new TextView[4];
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText( Html.fromHtml("&#8226") );
            dots[i].setTextSize(50);
            dots[i].setTextColor( getResources().getColor( R.color.colorPrimaryLight ) );
            dotsLayout.addView( dots[i] );
        }

        if(dots.length>0)
        {
            dots[position].setTextColor(getResources().
                    getColor( R.color.colorPrimary)  );
        }

    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage=position;

            //first page
            if(position==0){

                //disable back button
                backBtn.setVisibility( View.INVISIBLE );
                backBtn.setEnabled( false );

                nextBtn.setEnabled( true );
                nextBtn.setText( getResources().getString( R.string.next ) ) ;

                skipBtn.setEnabled( true );
                skipBtn.setText( getResources().getString( R.string.skip ) );
                skipBtn.setVisibility( View.VISIBLE );

            }

            //last page
            else if(position== dots.length-1){

                backBtn.setText( getResources().getString( R.string.back ) );
                backBtn.setEnabled( true );
                backBtn.setVisibility( View.VISIBLE );

                nextBtn.setEnabled( true );
                nextBtn.setText( getResources().getString( R.string.start_app ) );

                skipBtn.setEnabled( false );
                skipBtn.setVisibility( View.INVISIBLE );

            }

            else{

                backBtn.setText( getResources().getString( R.string.back ) );
                backBtn.setEnabled( true );
                backBtn.setVisibility( View.VISIBLE );

                nextBtn.setEnabled( true );
                nextBtn.setText( getResources().getString( R.string.next ) );

                skipBtn.setEnabled( true );
                skipBtn.setText( getResources().getString( R.string.skip ) );
                skipBtn.setVisibility( View.VISIBLE );

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}

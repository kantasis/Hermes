package com.zerofocus.hermes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class Story implements View.OnClickListener {

    public static final String TAG = MainActivity.TAG;
    public static final int DESCRIPTION_lIMIT = 300;

    private static int[] _previews={
        R.drawable.default_preview,
    };

    private static String[] patternToBold_list={
        "Android",
        "Dev",
    };

    private String _title="";
    private String _description="";
    private Date _pubDate;
    private Uri _link;
    private Uri _image;
    private Uri _source;
    private int _imageResource=-1;

    public Story(){
        // int index = new java.util.Random().nextInt(Story._previews.length);
    }

    public Date getPubDate(){
        return _pubDate;
    }

    public void setPubDate(String string){
        _pubDate = Story.parseDate(string);
    }

    public Uri getSource(){
        return _source;
    }

    public void setSource(Uri uri){
        _source = uri;
    }

    public String getDescription(){
        return _description;
    }

    public void setDescription(String string){
        _truncateDescription();
        for (String pattern: patternToBold_list)
            _description = _description.replaceAll("<b>"+pattern+"</b>",pattern);

    }

    public String getTitle(){
        return _title;
    }

    public void setTitle(String string){
        _title=string;
        for (String pattern: patternToBold_list)
            _title = _title.replaceAll("<b>"+pattern+"</b>",pattern);
    }

    public Uri getLink(){
        return _link;
    }

    public void setLink(Uri uri){
        _link=uri;
    }

    public Uri getImage(){
        return _image;
    }

    public void setImage(Uri uri){
        _image = uri;
    }

    public boolean isValid(){
        return getTitle()!=null && getDescription()!=null && getLink()!=null;
    }

    public int getImageResource(){
        return _imageResource;
    }

    public class StoryComparator implements Comparator<Story> {

        public StoryComparator(){
            super();
        }

        @Override
        public int compare(Story one, Story other){
            if (one.getPubDate().after(other.getPubDate()))
                return -1;
            if (one.getPubDate().before(other.getPubDate()))
                return 1;
            return 0;
        }
    }

    @Override
    public void onClick(View view) {
        try{
            Log.i(MainActivity.TAG,"Visiting "+getLink().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, getLink());
            ((MainActivity)view.getContext()).startActivityForResult(intent,MainActivity.RQ_STORY);
            // TODO: Add an analytics event here
        }catch(ActivityNotFoundException e){
            Toast.makeText(view.getContext(),"No application can handle this action",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // TODO: Add a ViewHolder here

    private static Date parseDate(String str){
        Date result=null;
        DateFormat[] formats = {
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH),
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        };
        //Log.i(TAG,"parsing date: "+str);

        for (DateFormat format : formats) {
            try {
                result = format.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (result!=null)
                break;
        }
        if (result==null) {
            result = new Date();
            //Log.i(TAG,"No format found for "+str+", using (date now)");
        }
        return result;
    }

    private void _truncateDescription(){
        if ( _description.length() < DESCRIPTION_lIMIT )
            return;

        int count=0;
        boolean flag=true;
        int i;
        for (i=0; i<_description.length(); i++){
            if (Character.isLetter(_description.charAt(i)))
                count++;
            if (count>=DESCRIPTION_lIMIT)
                break;
        }

        _description = _description.substring(0,i)+"...";
    }

}

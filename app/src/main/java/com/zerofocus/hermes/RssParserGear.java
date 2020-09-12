package com.zerofocus.hermes;

import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssParserGear {

    public final static String TAG="VvV";
    
    private MainActivity _main;

    public static ArrayList<Story> parseFeed(InputStream inputStream) 
            throws XmlPullParserException, IOException {
        String link_string = null;
        String tagText_string = null;
        boolean isInsideItem_flag = false;
        String pattern_string = "src=\"([^\"]*)\"";
        Pattern imageSource_pattern = Pattern.compile(pattern_string);
        ArrayList<Story> stories = new ArrayList<>();
        
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            int eventType_int = parser.getEventType();
            Story story=null;

            while (eventType_int != XmlPullParser.END_DOCUMENT) {

                String tagname_string = parser.getName();

                if (tagname_string==null){
                    tagText_string = parser.getText();
                    eventType_int = parser.next();
                    continue;
                }

                switch (eventType_int) {
                    case XmlPullParser.START_TAG:
                        if (tagname_string.equalsIgnoreCase("item") || tagname_string.equalsIgnoreCase("entry")) {
                            story = new Story();
                            isInsideItem_flag =true;
                        } else if (!isInsideItem_flag)
                            break;
                        if ( tagname_string.equalsIgnoreCase("link")) {
                            String tagType_string = parser.getAttributeValue(null,"rel");
                            if (tagType_string==null){
                                link_string = parser.getAttributeValue(null, "href");
                            }else if ( tagType_string.equalsIgnoreCase("alternate") ){
                                link_string = parser.getAttributeValue(null, "href");
                            }else if ( tagType_string.equalsIgnoreCase("enclosure") ) {
                                story.setImage( Uri.parse(parser.getAttributeValue(null,"href")) );
                            }
                            if (link_string!=null) {
                                story.setLink(Uri.parse(link_string));
                                link_string=null;
                            }
                        }else if ( tagname_string.equalsIgnoreCase("media:thumbnail")
                                || tagname_string.equalsIgnoreCase("media:content")) {
                            story.setImage( Uri.parse(parser.getAttributeValue(null,"url")) );
                        }
                    break;
                    case XmlPullParser.TEXT:
                        tagText_string = parser.getText();
                    break;
                    case XmlPullParser.END_TAG:
                        if (!isInsideItem_flag)
                            break;
                            // echo warning, unopened closing tag!
                        if (tagname_string.equalsIgnoreCase("item") || tagname_string.equalsIgnoreCase("entry")) {
                            stories.add(story);
                            isInsideItem_flag = false;
                        } else if (tagname_string.equalsIgnoreCase("title")) {
                            story.setTitle(tagText_string);
                        } else if (tagname_string.equalsIgnoreCase("content")
                                || tagname_string.equalsIgnoreCase("description")
                                || tagname_string.equalsIgnoreCase("content:encoded")
                                || tagname_string.equalsIgnoreCase("media:description")
                                || tagname_string.equalsIgnoreCase("summary")) {
                            // TODO: Handle more thoroughly HTML encoding
                            String content_string = tagText_string.replace("&quot;","\"");
                            Matcher imageSource_matcher = imageSource_pattern.matcher(content_string);
                            if (imageSource_matcher.find( )) {
                                Log.w(TAG,"Caught an image in the description tag");
                                story.setImage(Uri.parse(imageSource_matcher.group(1)));
                            }else{
                                story.setDescription(content_string);
                            }

                            tagText_string=tagText_string.replaceAll("</?img[^>]*/?>","");
                            tagText_string=tagText_string.replaceAll("\\s\\s*"," ").trim();
                            story.setDescription(tagText_string);
                        } else if (tagname_string.equalsIgnoreCase("link")) {
                            if (story.getLink()==null) {
                                story.setLink(Uri.parse(tagText_string));
                            }
                        } else if (tagname_string.equalsIgnoreCase("published")
                                || tagname_string.equalsIgnoreCase("pubDate")) {
                            story.setPubDate(tagText_string);
                        }
                    break;
                    default:
                    break;
                }
                //TODO: An exception here
                eventType_int=parser.next();
            }
            return stories;
        } finally {
            inputStream.close();
        }
    }
}

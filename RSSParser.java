package com.example.RssReader;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by 1391758 on 2017/05/15.
 */
public class RSSParser {
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<String> links = new ArrayList<String>();
    ArrayList<String> images = new ArrayList<String>();

    public RSSParser(String feed_url)
    {
        parseRSS_String(feed_url);
    }

    public String getTextForTitle(String title)
    {
        int position = titles.indexOf(title);
        return descriptions.get(position);
    }

    public String getLinkForTitle(String title)
    {
        int position = titles.indexOf(title);
        return links.get(position);
    }


    void parseRSS_String(String RSS)
    {
        String title = "";
        String link = "";
        String description = "";
        String img_url = "";


        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlFactoryObject.newPullParser();
            InputStream feed = new ByteArrayInputStream(RSS.getBytes(Charset.forName("UTF-8")));;

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(feed, null);

            int event;
            String text = "";

            try
            {
                int count = 0;
                event = parser.getEventType();
                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=parser.getName();

                    switch (event)
                    {
                        case XmlPullParser.START_TAG:
                            Log.d("XML", "start tag " );
                            break;

                        case XmlPullParser.TEXT:
                            text = parser.getText();
                            //Log.d("XML", "text");
                            break;

                        case XmlPullParser.END_TAG:

                            switch (name) {
                                case "title":
                                    count++;
                                    Log.d("XML", "end tag "+ Integer.toString(count));
                                    title = text;
                                    titles.add(title);
                                    Log.d("TITLE", title);
                                    break;

                                case "description":
                                    description = text;
                                    descriptions.add(description);
                                    break;

                                case "url":
                                    // try converting the stream from the url to drawable in main program
                                    img_url = text;
                                    images.add(img_url);
                                    break;

                                case "link":
                                    link = text;
                                    links.add(link);
                                    break;
                            }
                    }

                    event = parser.next();
                }
                Log.d("RSS", "end of feed reached");
            }catch (Exception e)
            {
                Log.d("XML ISSUE", " :-{");
            }

        } catch (Exception e)
        {
            Log.d("XML ISSUE", " :-[");
        }

    }
}

/*
    EXAMPLE OF CODE THAT GOES IN MAIN

  AsyncHTTPRequest asyncHttpRequest = new AsyncHTTPRequest(
                "http://rss.nytimes.com/services/xml/rss/nyt/World.xml",params) {
            @Override
            protected void onPostExecute(String output) {
                xml = output;
                RSSParser Parser = new RSSParser(xml);
                Log.d("BEHOLD","object instantiated");
                //list = Parser.titles;
                for(String s : Parser.titles)
                {
                    list.add(s);
                }
                Log.d("length", Integer.toString(list.size()));
                adapter.notifyDataSetChanged();
                Log.d("adapter", "adapter notified");
                //parseRSS_String(xml);
            }
        };
        asyncHttpRequest.execute();
*/
package com.em.news.myfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import database.DBAdapter;
import database.Model;

public class Main extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbAdapter = new DBAdapter(this);

        recyclerView = findViewById(R.id.main_content_recycler_view);
        recyclerAdapter = new RecyclerAdapter(this, dbAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        new FetchFeed().execute();
    }

    private class FetchFeed extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            String title = null;
            String link = null;
            String description = null;
            String guid = null;
            String image_src = null;
            String pub_date = null;
            boolean isItem = false;
            InputStream inputStream;

            try {
                URL url = new URL("http://feeds.feedburner.com/ndtvnews-top-stories");
                inputStream = url.openConnection().getInputStream();

                XmlPullParser xmlPullParser = Xml.newPullParser();
                xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlPullParser.setInput(inputStream, null);

                xmlPullParser.nextTag();
                while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                    int eventType = xmlPullParser.getEventType();

                    String name = xmlPullParser.getName();
                    if(name == null)
                        continue;

                    if(eventType == XmlPullParser.END_TAG) {
                        if(name.equalsIgnoreCase("item")) {
                            isItem = false;
                        }
                        continue;
                    }

                    if (eventType == XmlPullParser.START_TAG) {
                        if(name.equalsIgnoreCase("item")) {
                            isItem = true;
                            continue;
                        }
                    }

                    Log.d("MyXmlParser", "Parsing name ==> " + name);
                    String result = "";
                    if (xmlPullParser.next() == XmlPullParser.TEXT) {
                        result = xmlPullParser.getText();
                        xmlPullParser.nextTag();
                    }

                    if (name.equalsIgnoreCase("title")) {
                        title = result;
                    } else if (name.equalsIgnoreCase("link")) {
                        link = result;
                    } else if (name.equalsIgnoreCase("description")) {
                        description = result;
                    } else if (name.equalsIgnoreCase("pubDate")) {
                        pub_date = result;
                    } else if (name.equalsIgnoreCase("fullimage")) {
                        image_src = result;
                    } else if (name.equalsIgnoreCase("guid")) {
                        guid = result;
                    }
                    if (title != null && link != null && description != null &&
                            pub_date != null && image_src != null && guid != null) {
                        if(isItem) {
                            Model item = new Model();
                            item.setTitle(title);
                            item.setSource(link);
                            item.setDescription(description);
                            item.setImage(image_src);
                            item.setGuid(guid);
                            item.setPub_date(pub_date);
                            if (!dbAdapter.isPresent(item))
                                dbAdapter.insert(item);
                        }

                        title = null;
                        link = null;
                        description = null;
                        pub_date = null;
                        image_src = null;
                        guid = null;
                        isItem = false;
                    }
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            recyclerAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(dbAdapter.getSize()-1);
        }
    }


}

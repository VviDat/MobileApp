package com.example.assigment3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ListItemActivity extends AppCompatActivity {




    int soluong;
    ListView lvTieuDe;
    ArrayList<String> arrayTitle,arrayLink,arrayDescription,arrayImg,arrayMota;
    ArrayAdapter adapter;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        Intent intent = getIntent();
        String link = intent.getStringExtra("linkrss");



        lvTieuDe = (ListView) findViewById(R.id.listviewTieude);
        arrayTitle = new ArrayList<>();
        arrayLink = new ArrayList<>();
        arrayDescription = new ArrayList<>();
        arrayMota = new ArrayList<>();
        arrayImg = new ArrayList<>();

        adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item ,arrayTitle);
        lvTieuDe.setAdapter(adapter);

        new ReadContentWebsite().execute(link);

        lvTieuDe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(arrayImg.get(position)!=null)
                        openDialog(Gravity.CENTER,arrayTitle.get(position),arrayDescription.get(position),arrayImg.get(position),arrayMota.get(position),arrayLink.get(position));
                    else openDialog(Gravity.CENTER,arrayTitle.get(position),arrayDescription.get(position),null,arrayMota.get(position),arrayLink.get(position));
                } catch (IndexOutOfBoundsException e){
                    openDialog(Gravity.CENTER,arrayTitle.get(position),arrayDescription.get(position),null,arrayMota.get(position),arrayLink.get(position));
                }

            }
        });


    }
    private  void openDialog(int gravity,String title,String description,String url,String mota,String link){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_diaglog);

        Window window = dialog.getWindow();
        if(window == null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windoAttributes = window.getAttributes();
            windoAttributes.gravity = gravity;
            window.setAttributes(windoAttributes);
        }
        TextView tvTitle = dialog.findViewById(R.id.tv_title);
        TextView tvDescription = dialog.findViewById(R.id.tv_description);
        Button btnMore= dialog.findViewById(R.id.btn_more);
        Button btnClose= dialog.findViewById(R.id.btn_close);

        imageView = dialog.findViewById(R.id.image);


        tvDescription.setText(mota);
        tvTitle.setText(title);
        if(url != null){
            new LoadImageFromURL().execute(url);
        }else {
            imageView.setImageResource(R.drawable.download);
        }
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private  class ReadContentWebsite extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder= new StringBuilder();
            try {
                URL url = new URL(strings[0]);

                URLConnection urlConnection = url.openConnection();

                InputStream inputStream= urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line= "";

                while((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");


                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser parser = new XMLDOMParser();

            Document document= parser.getDocument(s);

            NodeList nodeList= document.getElementsByTagName("item");
            NodeList nodeListDesrciption= document.getElementsByTagName("description");
            NodeList nodeListMediaContent = document.getElementsByTagName("media:content");
            soluong = nodeListMediaContent.getLength();
//            Toast.makeText(ListItemActivity.this,String.valueOf(nodeListMediaContent.getLength()),Toast.LENGTH_SHORT).show();
            String mota="";
            String tieude= "";
            for(int i=0;i < nodeList.getLength();i++){
                String cdata= nodeListDesrciption.item(i+1).getTextContent();
                Element element = (Element) nodeList.item(i);
                tieude = parser.getValue(element,"title");
                arrayTitle.add(tieude);
                arrayLink.add(parser.getValue(element,"link"));
                arrayDescription.add(parser.getValue(element,"media:description"));
                arrayMota.add(cdata);
            }

            for (int y=0;y< nodeListMediaContent.getLength();y++){
                Element element1 = (Element) nodeListMediaContent.item(y);
                if(element1.hasAttribute("url") ){

                    String u =  element1.getAttribute("url");
                    arrayImg.add(u);
                }


            }

            adapter.notifyDataSetChanged();


        }
    }
    private class LoadImageFromURL extends  AsyncTask<String,Void, Bitmap>{
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
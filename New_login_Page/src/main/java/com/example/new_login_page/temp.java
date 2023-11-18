package com.example.new_login_page;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.events.MouseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.scene.image.Image;
import java.util.ResourceBundle;

public class temp {
    String target;
    public void set(String s)
    {
        this.target = s;
    }

    @FXML
    private ImageView imageView;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label titleLabel;


    public void show(javafx.scene.input.MouseEvent event) throws IOException {
        String target = SharedDataModel.getInstance().getData();

        URL url = new URL("https://api.itbook.store/1.0/new");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            stringBuffer.append(line);
        }
        JSONObject jsonObject = new JSONObject(stringBuffer.toString());
        JSONArray jsonArray = jsonObject.getJSONArray("books");
        for(int i = 0 ; i < jsonArray.length() ; i++)
        {
            JSONObject book = jsonArray.getJSONObject(i);
            String title = book.getString("title");
            if(title.equals(target)) {

                String imageURL = book.getString("image");

                Image image = null;
                try {
                    image = new Image(imageURL);
                } catch (Exception e) {

                    e.printStackTrace();
                }
                imageView.setImage(image);
                titleLabel.setText(book.getString("title"));
                subtitleLabel.setText(book.getString("subtitle"));
                priceLabel.setText(book.getString("price"));
                isbnLabel.setText(book.getString("isbn13"));
            }
        }
    }
}

package com.example.new_login_page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;


public class jsonController implements Initializable {

    @FXML
    ListView<AnchorPane> listView;
    ObservableList<AnchorPane> observableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL Url, ResourceBundle resourceBundle) {
        String link = "https://api.itbook.store/1.0/new";
        try {
            parseJson(getWebResponse(link));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        listView.setOnMouseClicked(event -> {

            AnchorPane selectedItem = listView.getSelectionModel().getSelectedItem();
            Label title = (Label) selectedItem.getChildren().get(1);
            System.out.println("Label : " + title.getText());
            String target = title.getText();
            try {
                itemClicked(target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    private void itemClicked(String target) throws IOException {
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
            if(title.equals(target))
            {
                String subtitle = book.getString("subtitle");
                String isbn = book.getString("isbn13");
                String Url = book.getString("url");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Title : " + target + "\n" + "Subtitle : " + subtitle + "\n" + "ISBN : " + isbn + "\n" + "WEBSITE LINK : " + Url);
                alert.showAndWait();



                Stage stage = (Stage) listView.getScene().getWindow();
//                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("temp.fxml"));
                Scene scene = new Scene(loader.load());
                temp obj = loader.getController();
                obj.set(target);
                SharedDataModel.getInstance().setData(target);
                stage = new Stage();
                stage.setScene(scene);
                stage.show();

            }
        }
    }

    public String getWebResponse(String link) throws IOException
    {
        URL url = new URL(link);
        StringBuffer stringBuffer = new StringBuffer();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
        }

        return stringBuffer.toString();
    }


    private void parseJson(String string) {

        JSONObject jsonObject = new JSONObject(string);
        JSONArray jsonArray = jsonObject.getJSONArray("books");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String title = jsonObject1.getString("title");
            System.out.println(title);
            String price = jsonObject1.getString("price");
            String imageURL = jsonObject1.getString("image");


            javafx.scene.control.Label titleLabel = new javafx.scene.control.Label( title);
            javafx.scene.control.Label priceLabel = new javafx.scene.control.Label( price);

            ImageView imageView = new ImageView();
            Image image = null;
            try {
                image = new Image(imageURL);
                imageView.setImage(image);
                imageView.setFitWidth(70);
                imageView.setFitHeight(70);
            } catch (Exception e) {

                e.printStackTrace();
            }

            AnchorPane.setTopAnchor(imageView, 5.0);
            AnchorPane.setLeftAnchor(imageView, 5.0);
            AnchorPane.setTopAnchor(titleLabel, 5.0);
            AnchorPane.setLeftAnchor(titleLabel, 70.0);
            AnchorPane.setTopAnchor(priceLabel, 30.0);
            AnchorPane.setLeftAnchor(priceLabel, 70.0);
            AnchorPane anchorPane = new AnchorPane();

            anchorPane.getChildren().addAll(imageView, titleLabel, priceLabel);
//            listView.getItems().add(anchorPane);
            observableList.add(anchorPane);
        }
        listView.setItems(observableList);
    }
}

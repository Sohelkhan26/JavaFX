package com.example.new_login_page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {
    String name = "Sohel";
    public void setName(String name)
    {
        this.name = name;
    }
    @FXML
    Label usernameLabel;

    @FXML
    private TextField availableBooks_author;

    @FXML
    private TextField availableBooks_bookID;

    @FXML
    private TextField availableBooks_bookTitle;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private TableColumn<Book, String> availableBooks_col_author;

    @FXML
    private TableColumn<Book , Integer> availableBooks_col_bookID;

    @FXML
    private TableColumn<Book , String> availableBooks_col_bookTItle;

    @FXML
    private TableColumn<Book , String> availableBooks_col_date;

    @FXML
    private TableColumn<Book , String> availableBooks_col_genre;

    @FXML
    private TableColumn<Book , Integer> availableBooks_col_price;

    @FXML
    private DatePicker availableBooks_date;

    @FXML
    private AnchorPane availableBooks_form;

    @FXML
    private TextField availableBooks_genre;

    @FXML
    private TextField availableBooks_price;

    @FXML
    private TextField availableBooks_search;

    @FXML
    private TableView<Book> availableBooks_tableView;

    @FXML
    private Label dashboard_AB;

    @FXML
    private Label dashboard_TC;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_customerChart;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private Button logout;

    @FXML
    private ComboBox<String> purchase_bookID;

    @FXML
    private ComboBox<String> purchase_bookTitle;

    @FXML
    private Button purchase_btn;

    @FXML
    private TableColumn<Book, String> purchase_col_author;

    @FXML
    private TableColumn<Book, Integer> purchase_col_bookID;

    @FXML
    private TableColumn<Book , String> purchase_col_bookTitle;

    @FXML
    private TableColumn<Book , String> purchase_col_genre;

    @FXML
    private TableColumn<Book , Integer> purchase_col_price;

    @FXML
    private TableColumn<Book , Integer> purchase_col_quantity;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private Label purchase_info_author;

    @FXML
    private Label purchase_info_bookID;

    @FXML
    private Label purchase_info_bookTItle;

    @FXML
    private Label purchase_info_date;

    @FXML
    private Label purchase_info_genre;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    TableColumn purchase_col_publish_date;

    @FXML
    private TableView<Book> purchase_tableView;

    @FXML
    private Label purchase_total;


    @FXML
    private Label username;
    @FXML
    TextField availableBooks_quantity;
    Connection connection;
    PreparedStatement statement;

    Alert alert;
    @FXML
    void availableBooksAdd(ActionEvent event) throws SQLException {

        String checkData = "SELECT Book_Id FROM available_books WHERE Book_Id = ?";

        statement = connection.prepareStatement(checkData);
        statement.setString(1 , availableBooks_bookID.getText());

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Book ID: " + availableBooks_bookID.getText() + " already exists!");
            alert.showAndWait();
        }
        else if(availableBooks_author.getText().isEmpty() || availableBooks_genre.getText().isEmpty() || availableBooks_price.getText().isEmpty() ||  availableBooks_date.getValue() == null || availableBooks_bookTitle.getText().isEmpty() || availableBooks_bookID.getText().isEmpty())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        }
        else {
            String command = "INSERT INTO available_books (Book_Id , Book_Title , Author , Genre , Price , publish_date , Quantity) VALUES (? , ? , ? , ? , ? , ? , ?)";
            statement = connection.prepareStatement(command);

            statement.setString(1, availableBooks_bookID.getText());
            statement.setString(2, availableBooks_bookTitle.getText());
            statement.setString(3, availableBooks_author.getText());
            statement.setString(4, availableBooks_genre.getText());
            statement.setString(5, availableBooks_quantity.getText());
            statement.setString(6 , String.valueOf(availableBooks_date.getValue()));
            statement.setString(7 , availableBooks_quantity.getText());

            statement.executeUpdate();

            availableBooksClear(event);
            showBookDateInTable();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added!");
            alert.showAndWait();
            populatePurchase_bookTitle();
            populatePurchase_bookID();
        }
        }

    @FXML
    void availableBooksClear(ActionEvent event) {

        availableBooks_quantity.clear();
        availableBooks_bookID.clear();
        availableBooks_author.clear();
        availableBooks_genre.clear();
        availableBooks_bookTitle.clear();
        availableBooks_date.setValue(null);
        availableBooks_price.clear();
    }

    @FXML
    void availableBooksDelete(ActionEvent event) throws SQLException{
        String command = "DELETE FROM available_books WHERE Book_Id = ?";

        statement = connection.prepareStatement(command);

        statement.setString(1 , availableBooks_bookID.getText());

        try{
            if(availableBooks_bookID.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill the Book ID field!");
                alert.showAndWait();
            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Book with ID: " + availableBooks_bookID.getText() + " ?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement.executeUpdate();
                    showBookDateInTable();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful Delete!");
                    alert.showAndWait();
                    populatePurchase_bookTitle();
                    populatePurchase_bookID();
                    showBookDateInTable();
                    availableBooksClear(event);
                }
            }
        }catch(Exception e){e.printStackTrace();}

    }
    @FXML
    void availableBooksSelect(MouseEvent event) {

        Book bookD = availableBooks_tableView.getSelectionModel().getSelectedItem();
        int num = availableBooks_tableView.getSelectionModel().getSelectedIndex();

        if((num - 1) < -1){ return; }

        availableBooks_bookID.setText(String.valueOf(bookD.getBookId()));
        availableBooks_bookTitle.setText(bookD.getBookTitle());
        availableBooks_author.setText(bookD.getAuthor());
        availableBooks_genre.setText(bookD.getGenre());
        availableBooks_date.setValue(LocalDate.parse(String.valueOf(bookD.getDate())));
        availableBooks_price.setText(String.valueOf(bookD.getPrice()));
    }

    @FXML
    void availableBooksUpdate(ActionEvent event) throws SQLException {

        Connection connection;
        DBconnection dBconnection = new DBconnection();
        PreparedStatement statement , statement1;


        String bookId = availableBooks_bookID.getText();
        String bookTitle = availableBooks_bookTitle.getText();
        String author = availableBooks_author.getText();
        String genre = availableBooks_genre.getText();
        String price = availableBooks_price.getText();
        String date = String.valueOf(availableBooks_date.getValue());
        String quantity = availableBooks_quantity.getText();


        String command = "UPDATE available_books SET Book_Title = ?, Author = ?, Genre = ?, Quantity = ?, Price = ?, publish_date = ? WHERE Book_id = ?";
        String check_if_the_book_is_present = "SELECT *FROM available_books WHERE Book_Id = ?";


        connection = dBconnection.getDBConnection();
        statement = connection.prepareStatement(command);
        statement1 = connection.prepareStatement(check_if_the_book_is_present);

        statement1.setString(1 , bookId);
        ResultSet resultSet = statement1.executeQuery();

        if(!resultSet.next())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("The Book doesn't exist!");
            alert.showAndWait();
        }
        else{
            statement.setString(1 , bookTitle);
            statement.setString(2 , author);
            statement.setString(3 , genre);
            statement.setString(4 , quantity);
            statement.setString(5 , price);
            statement.setString(6 , date);
            statement.setString(7 , bookId);
            try{
                if(availableBooks_bookID.getText().isEmpty()
                        || availableBooks_bookTitle.getText().isEmpty()
                        || availableBooks_author.getText().isEmpty()
                        || availableBooks_genre.getText().isEmpty()
                        || availableBooks_date.getValue() == null
                        || availableBooks_price.getText().isEmpty()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();
                }else{
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to UPDATE Book with ID: " + availableBooks_bookID.getText() + "?");
                    Optional<ButtonType> option = alert.showAndWait();

                    if(ButtonType.OK.equals(option.get())){

                        statement.executeUpdate();
                        showBookDateInTable();
                        populatePurchase_bookID();
                        populatePurchase_bookTitle();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful Updated!");
                        alert.showAndWait();
    //                    availableBooksShowListData();
                        availableBooksClear(event);
                    }
                }
            }catch(Exception e){e.printStackTrace();}
        }

    }

    int Quantity;
    @FXML
    void purchaseAdd(MouseEvent event) throws SQLException{

        System.out.println("PurchaseAdd");
        int quant = purchase_quantity.getValue();
        System.out.println(quant);
        String id = purchase_info_bookID.getText();
        String command = "SELECT * FROM available_books WHERE Book_Id = ?";
        statement = connection.prepareStatement(command);
        statement.setString(1 , id);
        ResultSet resultSet = statement.executeQuery();
        int price;
        if(resultSet.next()) {
            price = resultSet.getInt("Price");
            Quantity = resultSet.getInt("Quantity");
            if(quant > Quantity)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("This Many book is not available!");
                alert.showAndWait();
            }
            else {
                purchase_total.setText(String.valueOf(price*quant));
                Quantity -= quant;
                System.out.println(Quantity);
            }
        }
        else purchase_total.setText("Something went wrong");
    }
    @FXML
    public void payButtonClicked(MouseEvent event) throws SQLException {

        String updateCommand = "UPDATE available_books SET Quantity = ? WHERE Book_Id = ?";
        statement = connection.prepareStatement(updateCommand);

        statement.setString(1 , String.valueOf(Quantity));
        statement.setString(2 , purchase_info_bookID.getText());
        statement.executeUpdate();

        showBookDateInTable();

        System.out.println("DB Updated!");

        String command1 = "INSERT INTO profit (Date , income) VALUES (? , ?)";
        statement = connection.prepareStatement(command1);
        statement.setString(1 , String.valueOf(LocalDate.now()));
        statement.setString(2 , purchase_total.getText());

        int row = statement.executeUpdate();
        if(row > 0)
        {
            System.out.println("Paid");
        }
        UpdateChart();
    }

    @FXML
    void purchaseBookId(ActionEvent event) throws SQLException {

        String value = purchase_bookID.getValue();
        String title  = "", author = "", genre = "" , date = "2039/30/3";
        String command = "SELECT * FROM available_books WHERE Book_Id = ?";

        PreparedStatement statement = connection.prepareStatement(command);
        statement.setString(1 , value);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
        {
            title   = resultSet.getString("Book_Title");
            author  = resultSet.getString("Author");
            genre    = resultSet.getString("Genre");
            date    = resultSet.getString("publish_date");
        }
        purchase_info_bookID.setText(value);
        purchase_info_author.setText(author);
        purchase_info_genre.setText(genre);
        purchase_info_date.setText(date);
        purchase_info_bookTItle.setText(title);
    }

    @FXML
    void purchaseBookTitle(ActionEvent event) throws SQLException {
        String value = purchase_bookTitle.getValue();
        String title  = " ", author = "", genre = "Horror " , date = "2039/30/3" , bookId = "";
        String command = "SELECT * FROM available_books WHERE Book_Title = ?";

        PreparedStatement statement = connection.prepareStatement(command);
        statement.setString(1 , value);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next())
        {
            title   = resultSet.getString("Book_Title");
            author  = resultSet.getString("Author");
            genre    = resultSet.getString("Genre");
            date    = resultSet.getString("publish_date");
            bookId = resultSet.getString("Book_Id");
        }
        purchase_info_bookID.setText(bookId);
        purchase_info_author.setText(author);
        purchase_info_genre.setText(genre);
        purchase_info_date.setText(date);
        purchase_info_bookTItle.setText(title);

    }

    public void switchForm(ActionEvent event){

        if(event.getSource() == dashboard_btn){
            availableBooks_date.setValue(LocalDate.now());

            dashboard_form.setVisible(true);
            availableBooks_form.setVisible(false);
            purchase_form.setVisible(false);

        }else if(event.getSource() == availableBooks_btn){
            dashboard_form.setVisible(false);
            availableBooks_form.setVisible(true);
            purchase_form.setVisible(false);


//            availableBooksSeach();
        }else if(event.getSource() == purchase_btn){
            dashboard_form.setVisible(false);
            availableBooks_form.setVisible(false);
            purchase_form.setVisible(true);
        }
    }

    double x = 0.0 , y = 0.0;
    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){

                logout.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) ->{
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) ->{
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) ->{
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }

        }catch(Exception e){e.printStackTrace();}
    }

    public ObservableList<Book> availableBooksListData(){


        String productViewQuery = "SELECT * FROM available_books";

        ResultSet resultSet;
        ObservableList<Book> listData = FXCollections.observableArrayList();

        try{
            statement = connection.prepareStatement(productViewQuery);
            resultSet = statement.executeQuery();

            Book book;

            while(resultSet.next()){

                book = new Book(resultSet.getString("Book_Title") , resultSet.getString("Author") , resultSet.getString("Genre") ,
                        resultSet.getInt("Price") , resultSet.getInt("Quantity") , resultSet.getInt("Book_Id"), resultSet.getString("publish_date"));

                listData.add(book);
            }

        }catch(Exception e){e.printStackTrace();}
        return listData;
    }

    @FXML
    TableColumn<Book , Integer> availableBooks_col_quantity;
    public void showBookDateInTable()
    {
        ObservableList<Book> listData;
        ObservableList<Book> listData1;

        listData = availableBooksListData();
        listData1 = availableBooksListData();

        availableBooks_tableView.setItems(listData);
        purchase_tableView.setItems(listData1);


        availableBooks_col_bookID.setCellValueFactory(new PropertyValueFactory<>("BookId"));
        availableBooks_col_bookTItle.setCellValueFactory(new PropertyValueFactory<>("BookTitle"));
        availableBooks_col_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        availableBooks_col_genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        availableBooks_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        availableBooks_col_date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        availableBooks_col_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));


        purchase_col_bookID.setCellValueFactory(new PropertyValueFactory<>("BookId"));
        purchase_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("BookTitle"));
        purchase_col_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        purchase_col_genre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        purchase_col_publish_date.setCellValueFactory(new PropertyValueFactory<>("Date"));

    }
    public void availableBooksSearch(Event event)
    {
        ObservableList<Book> availableBooksList = availableBooks_tableView.getItems();
        FilteredList<Book> filter = new FilteredList<>(availableBooksList, e -> true);

        availableBooks_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateBookData -> {

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateBookData.getBookId().toString().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getBookTitle().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getAuthor().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getGenre().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getDate().contains(searchKey)){
                    return true;
                }else return predicateBookData.getPrice().toString().contains(searchKey);
//                return predicateBookData.getBookTitle().toLowerCase().contains(searchKey);
            });
        });

        SortedList<Book> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableBooks_tableView.comparatorProperty());
        availableBooks_tableView.setItems(sortList);
    }
public void availableBooksSearch(KeyEvent event){

//        ObservableList<Book> availableBooksList = availableBooksListData();
//    FilteredList<Book> filter = new FilteredList<>(availableBooksList, e -> true);
//
//    availableBooks_search.textProperty().addListener((Observable, oldValue, newValue) ->{
//
//        filter.setPredicate(predicateBookData -> {
//
//            if(newValue == null || newValue.isEmpty()){
//                return true;
//            }
//
//            String searchKey = newValue.toLowerCase();
//
//            if(predicateBookData.getBookId().toString().contains(searchKey)){
//                return true;
//            }else if(predicateBookData.getBookTitle().toLowerCase().contains(searchKey)){
//                return true;
//            }else if(predicateBookData.getAuthor().toLowerCase().contains(searchKey)){
//                return true;
//            }else if(predicateBookData.getGenre().toLowerCase().contains(searchKey)){
//                return true;
//            }else if(predicateBookData.getDate().toString().contains(searchKey)){
//                return true;
//            }else if(predicateBookData.getPrice().toString().contains(searchKey)){
//                return true;
//            }else return false;
//        });
//    });
//
//    SortedList<Book> sortList = new SortedList(filter);
//    sortList.comparatorProperty().bind(availableBooks_tableView.comparatorProperty());
//    availableBooks_tableView.setItems(sortList);
//    showBookDateInTable();'

}

    public void populatePurchase_bookID() throws SQLException
    {
        String command = "SELECT Book_Id FROM available_books";
        Connection connection;
        DBconnection dBconnection = new DBconnection();
        connection = dBconnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(command);

        ObservableList<String> observableList1 = FXCollections.observableArrayList();
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            observableList1.add(resultSet.getString("Book_id"));
        }
        purchase_bookID.setItems(observableList1);
    }

    public void showAvailableBooks() throws SQLException
    {
        String command = "SELECT Quantity FROM available_books";
        DBconnection dBconnection = new DBconnection();
        Connection connection = dBconnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(command);

        ResultSet resultSet = statement.executeQuery();
        int total = 0;
        while (resultSet.next())
        {
            int x = resultSet.getInt("Quantity");
            total += x;
        }
        dashboard_AB.setText(String.valueOf(total));
    }

    public void populatePurchase_bookTitle() throws SQLException {
        String command = "SELECT Book_Title FROM available_books";
        Connection connection;
        DBconnection dBconnection = new DBconnection();
        connection = dBconnection.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(command);

        ObservableList<String> observableList1 = FXCollections.observableArrayList();
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            observableList1.add(resultSet.getString("Book_Title"));
        }
        purchase_bookTitle.setItems(observableList1);
    }

    public void showTotalCustomer() throws SQLException {
        statement = connection.prepareStatement("SELECT * FROM username_password");
        ResultSet resultSet = statement.executeQuery();
        int customer = 0;
        while(resultSet.next())
            customer++;

        dashboard_TC.setText(String.valueOf(customer));
    }
    int total_income = 0;

    void UpdateChart() throws SQLException {

        XYChart.Series chart = new XYChart.Series();

        String command = "SELECT * FROM profit";

        DBconnection dBconnection = new DBconnection();
        Connection connection;
        connection =dBconnection.getDBConnection();

        PreparedStatement statement = connection.prepareStatement(command);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            int income = resultSet.getInt(2);

            chart.getData().add(new XYChart.Data(resultSet.getString(1) , income));
            total_income += income;
        }
        dashboard_incomeChart.getData().add(chart);
        resultSet.close();
        dashboard_TI.setText(String.valueOf(total_income));

        XYChart.Series series = new XYChart.Series();
        command = "SELECT * FROM customer_count";
        PreparedStatement statement1 = connection.prepareStatement(command);
        resultSet = statement1.executeQuery();
        Map<String , Integer> map = new HashMap<String , Integer>();


        while(resultSet.next())
        {
            Integer customer = resultSet.getInt("count");
            String date = resultSet.getString("date");
//            series.getData().add(new XYChart.Data(resultSet.getString(1) , customer));
            if (map.containsKey(date)) {
                map.put(date, map.get(date) + customer);
            } else {
                map.put(date, customer);
            }
        }
        for (Map.Entry<String, Integer> me : map.entrySet())
        {
            series.getData().add(new XYChart.Data<>(me.getKey() , me.getValue()));
        }
        dashboard_customerChart.getData().add(series);

    }

    @FXML
    Button jsonButton;
    public void jsonButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Showjson.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.hide();
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }
    @Override
    public void initialize(URL url , ResourceBundle resource) {

        availableBooks_date.setValue(LocalDate.now());

        DBconnection dBconnection = new DBconnection();
        connection = dBconnection.getDBConnection();

        usernameLabel.setText(name);
        System.out.println(name);
        showBookDateInTable();
        try {
            UpdateChart();
            populatePurchase_bookID();
            populatePurchase_bookTitle();
            showAvailableBooks();
            showTotalCustomer();
            availableBooks_date.setValue(LocalDate.now());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1);
        purchase_quantity.setValueFactory(valueFactory);
        purchase_quantity.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                purchase_quantity.increment();
                event.consume();
            } else if (event.getCode() == KeyCode.DOWN) {
                purchase_quantity.decrement();
                event.consume();
            }
        });


        ObservableList<Book> availableBooksList = availableBooks_tableView.getItems();
        FilteredList<Book> filter = new FilteredList<>(availableBooksList, e -> true);

        availableBooks_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateBookData -> {

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateBookData.getBookId().toString().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getBookTitle().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getAuthor().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getGenre().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateBookData.getDate().contains(searchKey)){
                    return true;
                }else return predicateBookData.getPrice().toString().contains(searchKey);
//                return predicateBookData.getBookTitle().toLowerCase().contains(searchKey);
            });
        });

        SortedList<Book> sortList = new SortedList<>(filter);
        sortList.comparatorProperty().bind(availableBooks_tableView.comparatorProperty());
        availableBooks_tableView.setItems(sortList);
//        availableBooks_tableView.refresh();
//        showBookDateInTable();

    }

}

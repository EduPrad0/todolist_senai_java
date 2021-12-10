package br.com.dprado.app;
	
import br.com.dprado.io.TarefaIo;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TarefaIo.createFiles();
			
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/dprado/view/Index.fxml"));
			Scene scene = new Scene(root,1137,723);
			scene.getStylesheets().add(getClass().getResource("/br/com/dprado/view/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("To do list app");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/br/com/dprado/assets/todo.jpg")));
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

package br.com.dprado.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.dprado.io.TarefaIo;
import br.com.dprado.model.StatusTarefa;
import br.com.dprado.model.StatusUrgencia;
import br.com.dprado.model.Tarefa;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class IndexController implements Initializable {
	@FXML
	private Label lbCode, lbDataLC, lbStatus;

	@FXML
	private TableColumn<Tarefa, LocalDate> tcDataLimite;

	@FXML
	private TableColumn<Tarefa, String> tcTarefa;

	@FXML
	private TableColumn<Tarefa, StatusUrgencia> tcUrgencia;

	@FXML
	private TableColumn<Tarefa, StatusTarefa> tcStatus;

	@FXML
	private TableView<Tarefa> tvTarefa;

	@FXML
	private DatePicker inputLimite;

	@FXML
	private ChoiceBox<StatusUrgencia> selectUrg;

	@FXML
	private TextArea textComentario;

	@FXML
	private TextField inputTarefa, inputStatus, tfCodigo;

	@FXML
	private Button btnConcluir, btnLimpar, btnAdiar, btnCriar, btnApagar;

	private List<Tarefa> tarefas;
	private Tarefa tarefa;

	  @FXML
	    void OpenSair(ActionEvent event) {
		  System.exit(0);
	    }
	  

	    @FXML
	    void openExportar(ActionEvent event) {
	    	FileFilter filter = new FileNameExtensionFilter("Arquivos html", "htm");
	    	JFileChooser chooser = new JFileChooser();
	    	chooser.setFileFilter(filter);
	    	if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
	    		File arqSelecionado = chooser.getSelectedFile();
	    		if(!arqSelecionado.getAbsolutePath().endsWith(".html") || !arqSelecionado.getAbsolutePath().endsWith(".htm")) {
	    			arqSelecionado = new File(arqSelecionado + ".html");
	    		}
	    		try {
					TarefaIo.exportHtml(tarefas, arqSelecionado);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Erro ao gravar " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
	    	}
	    }

	    @FXML
	    void openSobre(ActionEvent event) {
	    	try {
	    		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/br/com/dprado/view/About.fxml"));
				Scene scene = new Scene(root,191,262);
				scene.getStylesheets().add(getClass().getResource("/br/com/dprado/view/application.css").toExternalForm());
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
	    	}catch(IOException e) {
	    		e.printStackTrace();
	    	}
	    }


	@FXML
	void handleAdiar(ActionEvent event) {
		if (tarefa != null) {
			int dias = Integer.parseInt(JOptionPane.showInputDialog(null, "Digite o número de dias que deseja adiar",
					"Informe quantos dias", JOptionPane.QUESTION_MESSAGE));
			LocalDate novaData = tarefa.getDataLimite().plusDays(dias);
			tarefa.setDataLimite(novaData);
			tarefa.setStatus(StatusTarefa.ADIADA);

			try {
				TarefaIo.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null, "Tarefa adiada com sucesso, adiada para " + novaData, "Adiar",
						JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Erro ao gravar " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	@FXML
	void handleApagar(ActionEvent event) {

		if (tarefa != null) {

			// Confirmar exclusão se retornar 0 //
			if (JOptionPane.showConfirmDialog(null, "Deseja excluir a tarefa " + tarefa.getId() + "?",
					"Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
				tarefas.remove(tarefa);

				try {
					TarefaIo.saveTarefas(tarefas);
					JOptionPane.showMessageDialog(null, "Tarefa " + tarefa.getId() + " removida", "Remover",
							JOptionPane.INFORMATION_MESSAGE);
					carregarTarefas();
					limpar();
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Erro ao remover " + e.getMessage(), "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		}
	}

	@FXML
	void handleClick(ActionEvent event) {
		limpar();
	}

	@FXML
	void handleConcluir(ActionEvent event) {
		if (tarefa != null) {
			LocalDate novaData = LocalDate.now();
			tarefa.setDataConcluida(novaData);
			tarefa.setStatus(StatusTarefa.CONCLUIDA);

			try {
				TarefaIo.saveTarefas(tarefas);
				JOptionPane.showMessageDialog(null,
						"Tarefa concluida, parabéns !, data de conclusão : " + tarefa.getDataConcluida(), "Adiar",
						JOptionPane.INFORMATION_MESSAGE);
				carregarTarefas();
				limpar();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Erro ao gravar " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	@FXML
	void handleCriar(ActionEvent event) {
		// Validar campos

		if (inputLimite.getValue() == null) {
			JOptionPane.showMessageDialog(null, "Data é um campo obrigatório", "Atenção!", JOptionPane.ERROR_MESSAGE);
			inputLimite.requestFocus();

		} else if (inputTarefa.getText().isEmpty()) {

			inputTarefa.setPromptText("* obrigatório!");
			inputTarefa.setStyle("-fx-border-color: red;");

		} else if (textComentario.getText().isEmpty()) {
			textComentario.setPromptText("* obrigatório!");
			textComentario.setStyle("-fx-border-color: red;");
		} else if (inputLimite.getValue().isBefore(LocalDate.now())) {
			inputLimite.hide();
			JOptionPane.showMessageDialog(null, "Data inválida ! ", "Atenção! ", JOptionPane.ERROR_MESSAGE);
			inputLimite.requestFocus();
		} else {
			if (tarefa == null) {
				// instanciar e popular a tarefa
				tarefa = new Tarefa();
				tarefa.setDataCriacao(LocalDate.now());
				tarefa.setStatus(StatusTarefa.ABERTA);
				tarefa.setGrau(selectUrg.getValue());
			}

			tarefa.setDataLimite(inputLimite.getValue());
			tarefa.setDescricao(inputTarefa.getText());
			tarefa.setComentarios(textComentario.getText());

			try {
				if(tarefa.getId() == 0) {
					TarefaIo.insert(tarefa);
				}else {
					TarefaIo.saveTarefas(tarefas);
				}
				limpar();
				carregarTarefas();

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Arquivo não encontrado: " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erro ao gravar " + e.getMessage(), "Erro",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}
	}

	private void limpar() {
		tarefa = null;

		tvTarefa.getSelectionModel().clearSelection();
		
		lbStatus.setText("Próximo IdCode");
		btnConcluir.setDisable(true);
		btnAdiar.setDisable(true);
		btnApagar.setDisable(true);
		btnCriar.setDisable(false);
		selectUrg.setDisable(false);
		inputLimite.setDisable(false);
		tfCodigo.setOpacity(0);
		lbCode.setOpacity(0);

		inputLimite.setValue(null);
		selectUrg.setValue(null);

		lbDataLC.setText("Data de realização");
		inputStatus.setText("");
		inputTarefa.setText("");
		textComentario.setText("");
		textComentario.setEditable(true);
		inputTarefa.setEditable(true);

		inputLimite.requestFocus();
		
		
		try {
			inputStatus.setText(""+TarefaIo.readIdCode());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			inputStatus.setText("error");
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectUrg.setItems(FXCollections.observableArrayList(StatusUrgencia.values()));
		try {
			inputStatus.setText(""+TarefaIo.readIdCode());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			inputStatus.setText("error");
		}
		
		
		tcDataLimite.setCellValueFactory(new PropertyValueFactory<>("dataLimite"));
		tcTarefa.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tcUrgencia.setCellValueFactory(new PropertyValueFactory<>("grau"));
		tcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

		tcDataLimite.setCellFactory(call -> {
			TableCell<Tarefa, LocalDate> celula = new TableCell<Tarefa, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
					if (!empty) {
						setText(item.format(fmt));
					}
				}
			};
			celula.setStyle("-fx-alignment: center");
			tcTarefa.setStyle("-fx-alignment: center");
			tcUrgencia.setStyle("-fx-alignment: center");
			return celula;
		});
		
		tvTarefa.setRowFactory(call -> new TableRow<Tarefa>(){
			protected void updateItem(Tarefa item, boolean empty) {
				super.updateItem(item, empty);
				if(item == null) {
					setStyle("");
				}else if(item.getStatus() == StatusTarefa.CONCLUIDA) {
					setStyle("-fx-background-color: #0f0");
				}else if(item.getDataLimite().isBefore(LocalDate.now())) {
					setStyle("-fx-background-color: tomato");
				}else if(item.getStatus() == StatusTarefa.ADIADA){
					setStyle("-fx-background-color: #FF5");
				}else {
					setStyle("-fx-background-color: cyan");
				}
			};
		});
		
	
		tvTarefa.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			tarefa = newValue;
			if (tarefa != null) {
				
				switch (tarefa.getStatus()) {
				case CONCLUIDA:
					btnApagar.setDisable(false);
					btnCriar.setDisable(true);
					btnConcluir.setDisable(true);
					btnAdiar.setDisable(true);
					lbDataLC.setText("Data de conclusão");
					inputLimite.setValue(tarefa.getDataConcluida());
					textComentario.setEditable(false);
					inputTarefa.setEditable(false);

					break;
				case ADIADA:
					lbDataLC.setText("Data de realização");
					btnConcluir.setDisable(false);
					btnApagar.setDisable(false);
					btnAdiar.setDisable(true);
					textComentario.setEditable(false);
					inputTarefa.setEditable(false);
					inputLimite.setValue(tarefa.getDataLimite());
					break;
				default:
					lbDataLC.setText("Data de realização");
					btnConcluir.setDisable(false);
					btnAdiar.setDisable(false);
					btnApagar.setDisable(false);
					inputLimite.setValue(tarefa.getDataLimite());
					break;
				}
				
				
				lbStatus.setText("Status");
				selectUrg.setValue(tarefa.getGrau());
				inputStatus.setText("" + tarefa.getStatus());
				inputTarefa.setText(tarefa.getDescricao());
				textComentario.setText(tarefa.getComentarios());
				tfCodigo.setOpacity(1);
				lbCode.setOpacity(1);
				tfCodigo.setText(tarefa.getId() + "");

				selectUrg.setDisable(true);
				inputLimite.setOpacity(1);
				inputLimite.setDisable(true);

			}
		});

		carregarTarefas();
	}

	public void carregarTarefas() {

		try {
			tarefas = TarefaIo.read();
			tvTarefa.setItems(FXCollections.observableArrayList(tarefas));

			tvTarefa.refresh();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregas as tarefas" + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}
}

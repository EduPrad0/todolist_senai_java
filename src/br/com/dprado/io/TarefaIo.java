package br.com.dprado.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import br.com.dprado.model.StatusTarefa;
import br.com.dprado.model.StatusUrgencia;
import br.com.dprado.model.Tarefa;

public class TarefaIo {
	private static final String FOLDER = System.getProperty("user.home") + "/t0d0l1st";
	private static final String FILE_ID = FOLDER + "/id.csv";
	private static final String FILE_TAREFA = FOLDER + "/tarefas.csv";

	public static void createFiles() {
		File folder = new File(FOLDER);
		
		try {
			
			if(!folder.exists()) {
				File fileId = new File(FILE_ID);
				File fileTarefa = new File(FILE_TAREFA);
				
				folder.mkdir();
				fileTarefa.createNewFile();
				fileId.createNewFile();
				
				FileWriter write = new FileWriter(fileId);
				write.write("1");
				write.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static long readIdCode() throws FileNotFoundException {
		long idCode;
		File arqId = new File(FILE_ID);
		Scanner in = new Scanner(arqId);
		idCode = in.nextLong();
		in.close();
		
		return idCode;
	}
	
	public static void insert(Tarefa tarefa) throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		File arqId = new File(FILE_ID);
		
		Scanner in = new Scanner(arqId);
		tarefa.setId(in.nextLong());
		in.close();
		
		FileWriter write = new FileWriter(arqTarefa, true);
		write.append(tarefa.formatSave());
		write.close();
		
		// gerar id
		write = new FileWriter(arqId);
		write.write((tarefa.getId() + 1)+"");
		write.close();
		
	}
	
	public static List<Tarefa> read() throws IOException{
		
		File arqTarefa = new File(FILE_TAREFA);
		FileReader reader = new FileReader(arqTarefa);
		BufferedReader buff = new BufferedReader(reader);
		
		List<Tarefa> tarefas = new ArrayList<>();
		
		String linha;
		while((linha = buff.readLine()) != null) {
			DateTimeFormatter formatDataPadrao = DateTimeFormatter.ofPattern("dd/MM/yy");
			String[] vetor = linha.split(";");
			
			Tarefa t = new Tarefa();
			
			
			t.setId(Long.parseLong(vetor[0]));
			t.setDataCriacao(LocalDate.parse(vetor[1], formatDataPadrao));
			t.setDataLimite(LocalDate.parse(vetor[2], formatDataPadrao));
			if(!vetor[3].isEmpty()) {
				t.setDataConcluida(LocalDate.parse(vetor[3], formatDataPadrao));
			}
			
			t.setDescricao(vetor[4]);
			t.setComentarios(vetor[5]);
			t.setGrau(StatusUrgencia.values()[Integer.parseInt(vetor[6])]);
			t.setStatus(StatusTarefa.values()[Integer.parseInt(vetor[7])]);
			
			tarefas.add(t);
		}
		
		reader.close();
		buff.close();
		Collections.sort(tarefas);
		return tarefas;
	}
	
	public static void saveTarefas(List<Tarefa> tarefas) throws IOException {
		File arqTarefa = new File(FILE_TAREFA);
		FileWriter writer = new FileWriter(arqTarefa);
		for(Tarefa t: tarefas) {
			writer.write(t.formatSave());
		}
		writer.close();
		
	}
	
	public static void exportHtml(List<Tarefa> tarefas, File arquivo) throws IOException {
		FileWriter writer = new FileWriter(arquivo);
		writer.write("<html>\n"
				+ "<head>"
				+ "<style>"
				+ "*{margin:0;padding:0;box-sizing:border-box;}"
				+ "body{background:black;color:black;font-size:1.5rem;}"
				+ "div{margin:5%;display:flex;align-items:center;justify-content:center;}"
				+ "ul{list-style:none;display:flex;flex-direction:column;align-items:center;justify-content:center;}"
				+ "li{padding:2rem;font-size:1.2rem;width:100%;background:gray;box-shadow:10px 10px 10px rgba(0,0,0,.7);border-radius:8px;margin-bottom:.5rem;}"
				+ "h1{color:white;font-family:monospace, sans-serif;line-height:50px;letter-spacing:4px;margin:5%;}"
				+ "span{color:cyan;}"
				+ "</style>"
				+ "<body>\n"
				+ "<div>"
				+ "<h1>Lista de Tarefas</h1>\n"
				+ "<ul>\n");
		for (Tarefa t : tarefas) {
			writer.write("<li>"+t.getDataLimite()+" - "+t.getDescricao()+"  está <span>"+t.getStatus()+"</span> do Nivel  "+t.getGrau()+"</li>");
		}
		
		writer.write("</ul>\n"
				+ "</div>\n");
		writer.write("</body>\n");
		
		writer.write("</html>");
			
		writer.close();
	}
	
}

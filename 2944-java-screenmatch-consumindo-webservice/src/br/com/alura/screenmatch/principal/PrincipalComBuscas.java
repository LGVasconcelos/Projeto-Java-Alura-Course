package br.com.alura.screenmatch.principal;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.alura.screenmatch.excecao.ErroDeConversaoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;

public class PrincipalComBuscas {

	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner req = new Scanner(System.in);
		String busca = "";
		List<Titulo> listaTitulos = new ArrayList();
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).setPrettyPrinting().create();

		while (!busca.equalsIgnoreCase("sair")) {
			System.out.println("Digite o filme que gostaria de pesquisar:");
			busca = req.nextLine();
			if(busca.equalsIgnoreCase("sair")) {
				break;
			}
			
			var endereco = "http://www.omdbapi.com/?t=" + busca.replace(" ", "+") + "&apikey=63db7012";

			try {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).build();
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

				String json = response.body();

				
				TituloOmdb meuTituloOmdb = gson.fromJson(json, TituloOmdb.class);
				System.out.println(meuTituloOmdb);
				// try {
				Titulo meuTitulo = new Titulo(meuTituloOmdb);
				System.out.println("Titulo ja convertido");
				System.out.println(meuTitulo);
				
				listaTitulos.add(meuTitulo);

				//FileWriter escrita = new FileWriter("filmes.txt");
				//escrita.write(meuTitulo.toString());
				//escrita.close();
			} catch (NumberFormatException e) {
				System.out.println("Aconteceu um erro: ");
				System.out.println(e.getMessage());
			} catch (IllegalArgumentException e) {
				System.out.println("Ocorreu um erro de argumento: ");
				System.out.println(e.getMessage());
			} catch (ErroDeConversaoException e) {
				System.out.println(e.getMessage());
			}

		}
			System.out.println(listaTitulos);
			
			FileWriter escrita = new FileWriter("filmes.json");
			escrita.write(gson.toJson(listaTitulos));
			escrita.close();
			System.out.println("O programa executou corretamente");
	}
}

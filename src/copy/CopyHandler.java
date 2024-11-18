package copy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Classe que cuida dos métodos de copia de sites para arquivos.
 */
public class CopyHandler {

    /**
     * Método que gera uma URI do site a ser copiado.
     * 
     * @param urlString Endereço do site.
     * @return URI do site.
     */
    public static URI createUri(String urlString) {
        try {
            return new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace(); // Pilha de execução
            return null; // Retorna null
        }
    }

    /**
     * Método que converte a URI para URL.
     * 
     * @param uri URI a ser convertida.
     * @return URI convertida em URL.
     */
    public static URL convertToUrl(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace(); // Pilha de execução
            return null; // Retorna null
        }
    }

    /**
     * Método que lê o conteúdo do site e o retorna em String.
     * 
     * @param siteUrl Endereço do site.
     * @return Conteúdo do site em String.
     */
    public static String read(String siteUrl) {
        StringBuilder content = new StringBuilder(); // Para transformar o conteúdo do site em String
        URL url = convertToUrl(createUri(siteUrl)); // URL do site a ser lido

        if (url == null)
            return null; // NullPointerException

        // Lendo e concatenando o conteúdo do site
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;

            // Enquanto ainda houverem linhas para copiar:
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator()); // Pega o separador de linhas do sistema
            }
        } catch (IOException e) {
            e.printStackTrace(); // Pilha de execução
        }

        return content.toString(); // Retornando o conteúdo do site
    }

    /**
     * Gera um nome para o arquivo com base no dominio.
     * 
     * @param siteUrl Endereço do site.
     * @return Nome do arquivo.
     */
    public static String generateFileName(String siteUrl) {
        URL url = convertToUrl(createUri(siteUrl)); // Endereço do site

        if (url == null)
            return null; // NullPointerException

        // Gerando o nome do arquivo
        String domain = url.getHost(); // Pegando o nome do dominio
        domain = domain.replaceAll("[^a-zA-Z0-9.-]", "-"); // Removendo caracteres inválidos

        return domain + ".txt"; // Retornando o nome e extensão do arquivo
    }

    /**
     * Escreve o conteúdo do site em um arquivo .txt
     * 
     * @param siteUrl Endereço do site.
     */
    public static void write(String siteUrl) {
        String content = read(siteUrl); // Pegando o conteúdo do site a ser copiado

        if (content == null)
            return; // Não foi possível copiar o conteúdo do site

        // Gerar o nome do arquivo baseado na URL do site
        String outputFileName = generateFileName(siteUrl);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("./out/" + outputFileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
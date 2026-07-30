import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BuscadorDePalabrasSimilares {

    private static class PalabraPuntuada {
        private String palabra;
        private int distancia;

        public PalabraPuntuada(String palabra, int distancia) {
            this.palabra = palabra;
            this.distancia = distancia;
        }

        public String getPalabra() {
            return palabra;
        }

        public int getDistancia() {
            return distancia;
        }
    }

    public static void main(String[] args) {

        List<String> palabrasDelDiccionario = cargarPalabrasDelDiccionario();
        String palabraObjetivo = args[0];
        
        List<String> palabrasSimilares = palabrasDelDiccionario.stream()
                .filter(   palabra         -> Math.abs(palabra.length() - palabraObjetivo.length()) <= 3                                )
                .map(      palabra         -> new PalabraPuntuada(palabra, calcularDistanciaLevenshtein(palabraObjetivo, palabra))      )
                .filter(   palabraPuntuada -> palabraPuntuada.getDistancia() < 3                                                        )
                .sorted(   Comparator.comparingInt(PalabraPuntuada::getDistancia)                                                       )
                .map(      palabraPuntuada -> palabraPuntuada.getPalabra()                                                              )
                .limit(10)
                .collect(Collectors.toList());
        
        System.out.println("Palabras similares a " + palabraObjetivo + ":");
        //for(String palabra : palabrasSimilares) {
        //    System.out.println(palabra);
        //}
        palabrasSimilares.forEach(System.out::println);
    }


    public static int calcularDistanciaLevenshtein(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    private static List<String> cargarPalabrasDelDiccionario() {
        // Leemos el fichero diccionario.txt de esta carpeta:
        File diccionarioFile = new File("./diccionario.txt");
        List<String> palabras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(diccionarioFile))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                palabras.add(linea.split("=")[0].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return palabras;
    }
}
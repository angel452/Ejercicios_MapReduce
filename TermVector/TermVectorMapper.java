import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TermVectorMapper extends Mapper<Object, Text, Text, Text> {
    private Text hostname = new Text();
    private Text termVector = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Suponiendo que cada línea tiene un formato: <URL> <content>
        String line = value.toString();
        String[] parts = line.split("\t"); // Cambia el delimitador según tu formato

        if (parts.length > 1) {
            try {
                URL url = new URL(parts[0]);
                hostname.set(url.getHost());

                // Generar un term vector para el contenido
                String content = parts[1].toLowerCase(); // Convertir a minúsculas
                String[] words = content.split("\\W+"); // Separar en palabras

                Map<String, Integer> wordCount = new HashMap<>();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                    }
                }

                // Construir el term vector como una cadena
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                    sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                }

                // Remover la última coma y espacio
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 2);
                }

                termVector.set(sb.toString());
                context.write(hostname, termVector);
            } catch (Exception e) {
                // Manejo de excepciones
            }
        }
    }
}


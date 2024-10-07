import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TermVectorReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Map<String, Integer> finalVector = new HashMap<>();

        for (Text val : values) {
            String[] terms = val.toString().split(", ");
            for (String term : terms) {
                String[] pair = term.split(":");
                String word = pair[0];
                int frequency = Integer.parseInt(pair[1]);
                finalVector.put(word, finalVector.getOrDefault(word, 0) + frequency);
            }
        }

        // Filtrar términos poco frecuentes (ejemplo: menos de 2)
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : finalVector.entrySet()) {
            if (entry.getValue() >= 2) { // Cambia este umbral según sea necesario
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
            }
        }

        // Remover la última coma y espacio
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        result.set(sb.toString());
        context.write(key, result); // Emitir (hostname, term vector)
    }
}


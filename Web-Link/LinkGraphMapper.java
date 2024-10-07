import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LinkGraphMapper extends Mapper<Object, Text, Text, Text> {
    private Text targetUrl = new Text();
    private Text sourceUrl = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Suponiendo que cada línea tiene el formato: <source> <target1> <target2> ...
        String line = value.toString();
        String[] parts = line.split(" "); // Cambia el delimitador según el formato de tu log

        if (parts.length > 1) {
            sourceUrl.set(parts[0]); // URL de origen es la primera
            for (int i = 1; i < parts.length; i++) {
                targetUrl.set(parts[i]); // Las siguientes son URLs de destino
                context.write(targetUrl, sourceUrl); // Emite (target, source)
            }
        }
    }
}


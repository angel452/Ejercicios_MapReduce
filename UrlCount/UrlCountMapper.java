import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UrlCountMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text url = new Text();

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Suponiendo que las líneas del log tienen el formato: <fecha> <URL>
        String line = value.toString();
        String[] parts = line.split(" "); // Cambia el delimitador según el formato de tu log

        if (parts.length > 1) {
            url.set(parts[1]); // Cambia el índice según la posición de la URL en tu log
            context.write(url, one);
        }
    }
}


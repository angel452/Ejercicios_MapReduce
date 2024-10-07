import java.io.IOException;
import java.util.StringJoiner;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LinkGraphReducer extends Reducer<Text, Text, Text, Text> {
    private Text result = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringJoiner joiner = new StringJoiner(", "); // Para concatenar las URLs de origen

        for (Text val : values) {
            joiner.add(val.toString());
        }

        result.set(joiner.toString());
        context.write(key, result); // Emite (target, list(source))
    }
}


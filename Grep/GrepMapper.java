import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class GrepMapper extends Mapper<LongWritable, Text, Text, Text> {
    private String pattern;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        pattern = context.getConfiguration().get("grep.pattern");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (value.toString().contains(pattern)) {
            context.write(value, new Text(""));
        }
    }
}


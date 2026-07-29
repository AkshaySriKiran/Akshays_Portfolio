import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class TotalStreamsByArtist {

    public static class ArtistMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text artist = new Text();
        private IntWritable streams = new IntWritable();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles commas in quotes
            if (fields.length > 8) {
                try {
                    artist.set(fields[3].trim()); // Artist column
                    streams.set(Integer.parseInt(fields[8].trim())); // Streams column
                    context.write(artist, streams);
                } catch (NumberFormatException e) {
                    // Skip rows where streams are not numeric
                }
            }
        }
    }

    public static class StreamsReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int totalStreams = 0;
            for (IntWritable val : values) {
                totalStreams += val.get();
            }
            context.write(key, new IntWritable(totalStreams));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Total Streams by Artist");
        job.setJarByClass(TotalStreamsByArtist.class);
        job.setMapperClass(ArtistMapper.class);
        job.setCombinerClass(StreamsReducer.class);
        job.setReducerClass(StreamsReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

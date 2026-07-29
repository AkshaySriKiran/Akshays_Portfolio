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
import java.util.TreeMap;

public class TopRegionsByStreams {

    public static class RegionMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text region = new Text();
        private IntWritable streams = new IntWritable();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles commas in quotes
            if (fields.length > 8) {
                try {
                    region.set(fields[5].trim()); // Region column
                    streams.set(Integer.parseInt(fields[8].trim())); // Streams column
                    context.write(region, streams);
                } catch (NumberFormatException e) {
                    // Skip rows where streams are not numeric
                }
            }
        }
    }

    public static class TopReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private TreeMap<Integer, String> topRegions = new TreeMap<>();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int totalStreams = 0;
            for (IntWritable val : values) {
                totalStreams += val.get();
            }
            topRegions.put(totalStreams, key.toString());

            if (topRegions.size() > 5) {
                topRegions.remove(topRegions.firstKey());
            }
        }

        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Integer totalStreams : topRegions.descendingKeySet()) {
                context.write(new Text(topRegions.get(totalStreams)), new IntWritable(totalStreams));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Top Regions by Streams");
        job.setJarByClass(TopRegionsByStreams.class);
        job.setMapperClass(RegionMapper.class);
        job.setReducerClass(TopReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

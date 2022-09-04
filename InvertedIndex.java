import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndex {

    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>{

        private Text word = new Text();


        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            //seperate docID and doc content
            String[] doc = value.toString().split("\t", 2);
            //get docID
            Text docID = new Text(doc[0]);
            String docCont = doc[1].toLowerCase();
            docCont = docCont.replaceAll("\t", " ");
            docCont = docCont.replaceAll("[^a-z\\s]", " ");
            docCont = docCont.replaceAll("\\s+", " ");
            StringTokenizer itr = new StringTokenizer(docCont);
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, docID);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text,Text,Text,Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            HashMap<Text,Integer> map = new HashMap<>();

            for (Text val : values) {
                if(map.get(val) != null) {
                    map.put(val, map.get(val) + 1);
                } else {
                    map.put(val,1);
                }
            }

            StringBuilder sb = new StringBuilder();
            String wordName = key.toString();
            sb.append(wordName).append("\t");

            for(Map.Entry<Text,Integer> entry:map.entrySet()) {
                sb.append(entry.getKey().toString()).append(":").append(entry.getValue().toString()).append(" ");
            }

            context.write(new Text(sb.toString()), new Text(""));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(InvertedIndex.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
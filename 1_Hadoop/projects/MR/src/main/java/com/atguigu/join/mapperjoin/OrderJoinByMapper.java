package com.atguigu.join.mapperjoin;

import com.atguigu.join.bean.Order;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * <p>通过 map 端进行 join 操作</p>
 *
 * <p>提前把 小文件 加入到内存中, 再从 map 端读取大文件, 每读取一行并进行 join 操作.
 * 从而不需要 shuffle 和 reducer 过程.
 * 如果是一个大文件一个小文件, 则通过 map join 比较好.</p>
 *
 * @author Zhang Chao
 * @version mr_day11
 * @date 2020/5/26 8:14 下午
 */
public class OrderJoinByMapper {
    public static class JoinByMapperMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
        private HashMap map = new HashMap<String, String>();
        private Order order = new Order();
        private Text k = new Text();
        /**
         * Called once at the beginning of the task.
         *
         * @param context
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            /*
            context.getCacheFiles() --> URI --> Path(URI)
            FileSystem.get(Configuration).open(Path) --> FSDataInputStream
            --> InputStreamReader --> BufferedReader
             */
            URI[] files = context.getCacheFiles();
            Path path = new Path(files[0]);
            FileSystem fileSystem = FileSystem.get(context.getConfiguration());
            FSDataInputStream inputStream = fileSystem.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));

            String line;

            /*
            善用工具类 StringUtils
             */
            while (StringUtils.isNotEmpty(line = bufferedReader.readLine())){
                String[] split = line.split("\t");
                map.put(split[0], split[1]);
            }
            // 关闭流
            IOUtils.closeStreams(bufferedReader);
        }

        /**
         * Called once for each key/value pair in the input split. Most applications
         * should override this, but the default is the identity function.
         *
         * @param key
         * @param value
         * @param context
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split("\t");
            String line = split[0] + "\t" + map.get(split[1]) + "\t" + split[2];
            k.set(line);
            context.write(k, NullWritable.get());
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(OrderJoinByMapper.class);
        job.setMapperClass(JoinByMapperMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.addCacheFile(new URI("file:///Users/amos/Desktop/tmp/data/cache/pname.txt"));
        job.setNumReduceTasks(0);
        FileInputFormat.setInputPaths(job, new Path("/Users/amos/Desktop/tmp/data/join"));
        FileOutputFormat.setOutputPath(job, new Path("/Users/amos/Desktop/tmp/output/joinbymapper"));
        job.waitForCompletion(true);
    }
}

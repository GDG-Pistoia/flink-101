package it.gdgpistoia;

import it.gdgpistoia.config.KafkaPropertiesBuilder;
import it.gdgpistoia.model.Statistic;
import it.gdgpistoia.operator.Aggregator;
import it.gdgpistoia.operator.SubsriptionsParser;
import it.gdgpistoia.operator.TweetsParser;
import it.gdgpistoia.operator.TweetsPerSearchCriteria;
import lombok.val;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;

import java.util.*;

import org.apache.flink.util.Collector;
import twitter4j.Status;

import static it.gdgpistoia.utils.PropertiesLoader.*;


public class StreamingJob {

	public static void main(String[] args) throws Exception {

		val env = StreamExecutionEnvironment.getExecutionEnvironment();

		val kafkaProperties = KafkaPropertiesBuilder.builder()
				.bootstrapServers("localhost:9092")
				.groupId("test")
				.build();

		Properties properties = load("twitter.properties");

		TwitterSource twitterSource = new TwitterSource(properties);

		DataStream<String> tweetsAsStrings = env.addSource(twitterSource);

		DataStream<Status> tweets = tweetsAsStrings.flatMap(new TweetsParser());

		DataStream<Tuple2<String, Status>> classifiedTweets = tweets.flatMap(new TweetsPerSearchCriteria());

		//Subs example: Lorenzo,+,#HR
		DataStream<String> configurationsAsStrings = env.addSource(
				new FlinkKafkaConsumer<>("subscriptions", new SimpleStringSchema(), kafkaProperties));
		//Crea un DataStream dalla collection
		DataStream<Tuple3<String, String, String>> configurations = configurationsAsStrings.map(new SubsriptionsParser());
		configurations.addSink(new SinkFunction<Tuple3<String, String, String>>() {
			private static final long serialVersionUID = -3912958066159318348L;

			@Override
			public void invoke(Tuple3<String, String, String> value, Context context) throws Exception {
				System.out.println("New subscription recived " + value);
			}
		});

		ConnectedStreams<Tuple2<String, Status>, Tuple3<String, String, String>> tweetsAndConfigurations = classifiedTweets.connect(configurations);

		//partiziona lo  stream (connected) usando un key extractor.
		ConnectedStreams<Tuple2<String, Status>, Tuple3<String, String, String>> keyedTweetsAndConfigurations =
				tweetsAndConfigurations.keyBy(
						new KeySelector<Tuple2<String, Status>, String>() {
							private static final long serialVersionUID = -4323014925589200149L;

							@Override
							public String getKey(Tuple2<String, Status> value) throws Exception {
								return value.f0;
							}
						},
						new KeySelector<Tuple3<String, String, String>, String>() {
							private static final long serialVersionUID = -6719611931078311877L;

							@Override
							public String getKey(Tuple3<String, String, String> value) throws Exception {
								return value.f0;
							}
						}
				);

		//processa i due stream accoppiati
		//ritorna la statistica, la key  e tutte le subscriptions per successive elaborazioni
		SingleOutputStreamOperator<Tuple3<String, Set<String>, Statistic>> stats = keyedTweetsAndConfigurations.flatMap(new Aggregator());
		stats.print();

		stats.flatMap(new FlatMapFunction<Tuple3<String, Set<String>, Statistic>, String>() {
			private static final long serialVersionUID = -1940426969655789355L;

			@Override
			public void flatMap(Tuple3<String, Set<String>, Statistic> stringSetStatisticTuple3, Collector<String> collector) throws Exception {
				ObjectMapper objectMapper = new ObjectMapper();
				collector.collect(objectMapper.writeValueAsString(stringSetStatisticTuple3));
			}
		}).addSink(new FlinkKafkaProducer<>("stats", new SimpleStringSchema(), kafkaProperties));

		// execute program
		env.execute("Twitter Aggregator Job");
	}
}

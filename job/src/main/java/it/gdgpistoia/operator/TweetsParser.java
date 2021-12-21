package it.gdgpistoia.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class TweetsParser implements FlatMapFunction<String, Status> {
    private static final long serialVersionUID = 6413282101220172296L;

    @Override
    public void flatMap(String s, Collector<Status> collector) throws Exception {

        try {
            Status status = TwitterObjectFactory.createStatus(s);
            collector.collect(status);
        } catch (TwitterException e) {
            //we assume that the only case here is a
            //message we do not want to process.
        }
    }
}

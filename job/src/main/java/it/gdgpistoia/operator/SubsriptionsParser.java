package it.gdgpistoia.operator;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple3;

public class SubsriptionsParser implements MapFunction<String, Tuple3<String, String, String>> {
    private static final long serialVersionUID = 7380498559808233004L;

    @Override
    public Tuple3<String, String, String> map(String s) throws Exception {
        String[] parts = s.split(",");
        return new Tuple3<>(parts[2], parts[1], parts[0]);
        //key, add/remove, user
    }
}

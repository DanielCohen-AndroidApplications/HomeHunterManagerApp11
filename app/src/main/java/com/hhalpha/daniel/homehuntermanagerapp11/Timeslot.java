package com.hhalpha.daniel.homehuntermanagerapp11;

/**
 * Created by Daniel on 7/15/2016.
 */
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "Timeslot")
public class Timeslot{

    private String time;
    private String host;


    @DynamoDBHashKey(attributeName = "Time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @DynamoDBRangeKey(attributeName = "Host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


}

package com.hhalpha.daniel.homehuntermanagerapp11;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by Daniel on 7/15/2016.
 */
@DynamoDBTable(tableName = "Appointments")
public class Appointment {

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
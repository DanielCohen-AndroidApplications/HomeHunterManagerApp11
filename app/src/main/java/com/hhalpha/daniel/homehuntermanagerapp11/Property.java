package com.hhalpha.daniel.homehuntermanagerapp11;

/**
 * Created by Daniel on 6/16/2016.
 */
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = "Properties")
public class Property{

    private String Address;
    private String dataString;


    @DynamoDBHashKey(attributeName = "Address")
    public String getAdress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address=Address;
    }

    @DynamoDBRangeKey(attributeName = "dataString")
    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }


}

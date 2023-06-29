package com.hidglobal.reports.persistence.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@DynamoDBTable(tableName = "user_round_trips")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripEntity {

    @DynamoDBHashKey
    private String credentialHolderId;

    @DynamoDBRangeKey
    private long timestamp; //in or out time

    @DynamoDBAttribute
    private String credHolderName;

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    private EventType swipeType;

    public RoundTripEntity(String credentialHolderId) {
        this.credentialHolderId = credentialHolderId;
    }
}

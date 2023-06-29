package com.hidglobal.reports.persistence.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.hidglobal.reports.common.DateTimeUtils.*;

@DynamoDBTable(tableName = "user_in_time_tracker")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTrackerEntity {

    @DynamoDBHashKey
    private String credentialHolderId; //credentialHolderId

    @DynamoDBRangeKey
    private String inTime; //in time

    @DynamoDBAttribute
    private String outTime; //out time

    @DynamoDBAttribute
    private String credHolderName;

    @DynamoDBAttribute
    private long duration;

    @DynamoDBAttribute
    private boolean isLive = true;

    private TimeTrackerEntity(String credentialHolderId) {
        this.credentialHolderId = credentialHolderId;
    }

    private TimeTrackerEntity(String credentialHolderId, String inTime, String credHolderName) {
        this.credentialHolderId = credentialHolderId;
        this.inTime = inTime;
        this.credHolderName = credHolderName;
    }

    private TimeTrackerEntity(String credentialHolderId, String inTime,
                              String swipeOutTime, String credHolderName) {
        this.credentialHolderId = credentialHolderId;
        this.inTime = inTime;
        updateOutTimeAndLivenessAndDuration(swipeOutTime);
        this.credHolderName = credHolderName;
    }

    public void updateOutTimeAndLivenessAndDuration(String swipeOutTime) {
        if (isSameDay(inTime, swipeOutTime)) {
            this.isLive = false;
            this.outTime = swipeOutTime;
        } else {
            this.outTime = timeStampWithEndOfDate(inTime);
        }
        this.duration = calculateDuration(inTime, outTime);
    }

    public static TimeTrackerEntity greenfieldEntity(String credHolderId, String inTime,
                                                     String credHolderName) {
        return new TimeTrackerEntity(credHolderId, inTime, credHolderName);
    }

    public static TimeTrackerEntity entityWithPartitionKey(String credentialHolderId) {
        return new TimeTrackerEntity(credentialHolderId);
    }

    public static TimeTrackerEntity entityWithInAndSwipeOutTime(String credentialHolderId, String inTime, String swipeOutTime,
                                                                String credHolderName) {
        return new TimeTrackerEntity(credentialHolderId, inTime, swipeOutTime, credHolderName);
    }
}
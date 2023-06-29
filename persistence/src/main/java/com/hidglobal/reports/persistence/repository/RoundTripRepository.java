package com.hidglobal.reports.persistence.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.amazonaws.services.dynamodbv2.model.ComparisonOperator.BETWEEN;

@Repository
@RequiredArgsConstructor
public class RoundTripRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public void save(RoundTripEntity roundTripEntity) {
        dynamoDBMapper.save(roundTripEntity);
    }

    public List<RoundTripEntity> findByCredHolderIdBetween(String credHolderId, String startTime,
                                                           String endTime) {
        DynamoDBQueryExpression<RoundTripEntity> queryExpression = expressionWithHashKey(credHolderId);
        queryExpression.withRangeKeyCondition(
                "timestamp",
                new Condition()
                        .withComparisonOperator(BETWEEN)
                        .withAttributeValueList(List.of(createAttribute(startTime),
                                createAttribute(endTime))));
        return dynamoDBMapper.query(RoundTripEntity.class, queryExpression);
    }

    private AttributeValue createAttribute(String time) {
        AttributeValue attribute = new AttributeValue();
        attribute.setN(time);
        return attribute;
    }

    private DynamoDBQueryExpression<RoundTripEntity> expressionWithHashKey(String credHolderId) {
        DynamoDBQueryExpression<RoundTripEntity> queryExpression = new DynamoDBQueryExpression<>();
        RoundTripEntity entity = new RoundTripEntity(credHolderId);
        queryExpression.setHashKeyValues(entity);
        return queryExpression;
    }
}

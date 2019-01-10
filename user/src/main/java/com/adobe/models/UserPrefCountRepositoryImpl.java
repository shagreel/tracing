/*
Copyright 2019 Adobe. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.adobe.models;

import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Random;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class UserPrefCountRepositoryImpl implements UserPrefCountRepository {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final MongoTemplate mongoTemplate;

    private final Random rand = new Random();

    @Autowired
    public UserPrefCountRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserPrefCount findTopUserPref(String user) {
        insertRandomFailure();
        List<UserPrefCount> results = mongoTemplate.aggregate(Aggregation.newAggregation(
                getMatchOperation(user),
                getGroupOperation(),
                getProjectionOperation(),
                getSortOperation(),
                getLimitOperation()
        ), UserPref.class, UserPrefCount.class).getMappedResults();

        if (!results.isEmpty()) {
            return results.get(0);
        } else {
            return new UserPrefCount().setId("default").setCount(1);
        }
    }

    private void insertRandomFailure() {
        if (rand.nextInt(1000) < 10) {
            MongoCursorNotFoundException e = new MongoCursorNotFoundException(666, new ServerAddress());
            logger.error("Message=\"Mongo Query Artificial Failure\"", e);
            throw e;
        }
    }

    private MatchOperation getMatchOperation(String user) {
        Criteria userCriteria = where("user").is(user);
        return match(userCriteria);
    }

    private GroupOperation getGroupOperation() {
        return group("pref")
                .count().as("count");
    }

    private ProjectionOperation getProjectionOperation() {
        return project("pref", "count");
    }

    private SortOperation getSortOperation() {
        return sort(Sort.Direction.DESC, "count");
    }

    private LimitOperation getLimitOperation() {
        return limit(1);
    }
}

package com.application.backend.dao.mongdb;

import com.application.backend.document.DiscountCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface DiscountCodeDao extends MongoRepository<DiscountCode, String> {

    @Query(value="{ $and: [ " +
            "{ 'name': { $regex: :#{[2] != null ? '.*' + [2] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ $or: [ { $expr: { $eq: [ ?3, null ] } }, {'dayStart': { $gte: ?3 } } ] }, " +
            "{ $or: [ { $expr: { $eq: [ ?4, null ] } }, {'dayEnd': { $lte: ?4 } } ]} ]}, " +
            "{ '$skip' : ?0 , '$limit' : ?1 }")
    List<DiscountCode> getEvent(int page, Integer limit, String searching, Date start, Date end);


    @Query(value = "{ $and: [ " +
            "{ 'name': { $regex: :#{[0] != null ? '.*' + [0] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ $or: [ { $expr: { $eq: [ ?1, null ] } }, {'dayStart': { $gte: ?1 } } ] }, " +
            "{ $or: [ { $expr: { $eq: [ ?2, null ] } }, {'dayEnd': { $lte: ?2 } } ]} ]} ", count = true)
    long total(String searching, Date start, Date end);

    @Query(value="{'isActive' : true, 'dayStart': { $lte: ?0 }, 'dayEnd' : { $gte: ?0 } }")
    List<DiscountCode> findActiveEvent(Date today);
}

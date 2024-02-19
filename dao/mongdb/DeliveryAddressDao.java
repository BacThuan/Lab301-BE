package com.application.backend.dao.mongdb;

import com.application.backend.document.DeliveryAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAddressDao extends MongoRepository<DeliveryAddress, String> {

    @Query(value="{ $and: [ " +
            "{ 'city': { $regex: :#{[2] != null ? '.*' + [2] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'district': { $regex: :#{[3] != null ? '.*' + [3] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'ward': { $regex: :#{[4] != null ? '.*' + [4] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'address': { $regex: :#{[5] != null ? '.*' + [5] + '.*' : '.*'}, $options: 'i' } } ]},"+
            "{ '$skip' : ?0 , '$limit' : ?1 }")
    List<DeliveryAddress> getDeliveries(int page, Integer limit, String city, String district, String ward, String searching);


    @Query(value = "{ $and: [ " +
            "{ 'city': { $regex: :#{[0] != null ? '.*' + [0] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'district': { $regex: :#{[1] != null ? '.*' + [1] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'ward': { $regex: :#{[2] != null ? '.*' + [2] + '.*' : '.*'}, $options: 'i' } }, " +
            "{ 'address': { $regex: :#{[3] != null ? '.*' + [3] + '.*' : '.*'}, $options: 'i' } }" +
            "]}", count = true)
    long total(String city, String district, String ward, String searching);
}

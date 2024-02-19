package com.application.backend.service;

import java.util.List;

public interface FavouriteListService {
    void addLike(String email, Long idProduct);

    void removeLike(String email, Long idProduct);

    List<Object> getListLike(String email);

    List<Long> getListLikeId(String email);
}

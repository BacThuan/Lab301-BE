package com.application.backend.service.impl;

import com.application.backend.dao.StateDao;
import com.application.backend.model.State;
import com.application.backend.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StateImpl implements StateService {
    @Autowired
    StateDao stateDao;

    public Map<String, Object> getListData(List<State> states, long total){
        Map<String, Object> results = new HashMap<>();

        List<Map<String, Object>> statesData = new ArrayList<>();

        for(State state : states){
            Map<String, Object> data = new HashMap<>();

            data.put("_id",String.valueOf(state.getId()) );
            data.put("stateName", state.getStateName());

            statesData.add(data);
        }
        results.put("datas", statesData);
        results.put("total", total);

        return results;
    }

    @Override
    public Map<String, Object> getStates(Integer page, Integer limit) {
        List<State> states = stateDao.findPageAndLimit(page * limit, limit);
        long total = stateDao.totalPageAndLimit();
        return getListData(states,total);
    }

    @Override
    public Map<String, Object> getSearching(Integer page, Integer limit, String searching) {
        List<State> states = stateDao.findSearching(page * limit, limit,searching);
        long total = stateDao.totalSearching(searching);
        return getListData(states,total);
    }

    @Override
    public void deleteState(Long stateId) {
        stateDao.deleteById(stateId);
    }

    @Override
    public List<String> getALl() {
        List<State> states = stateDao.findAll();

        List<String> result = new ArrayList<>();
        for(State state : states){
            result.add(state.getStateName());
        }
        return result;
    }

    @Override
    public List<String> getStateUser() {
        System.out.println(stateDao.getState("USER"));
        return stateDao.getState("USER");
    }

    @Override
    public List<String> getStateProduct() {
        return stateDao.getState("PRODUCT");
    }

    @Override
    public List<String> getStatePayment() {
        return stateDao.getState("PAYMENT");
    }

    @Override
    public List<String> getStateOrder() {
        return stateDao.getState("ORDER");
    }
}

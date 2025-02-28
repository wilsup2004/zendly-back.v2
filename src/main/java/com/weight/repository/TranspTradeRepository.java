package com.weight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weight.model.TranspTrade;
import com.weight.model.TranspTradeId;

public interface TranspTradeRepository extends JpaRepository<TranspTrade,TranspTradeId>  {

}

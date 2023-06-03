package com.pfe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfe.model.T24Cheque;



@Repository
public interface T24ChequeRepo extends JpaRepository<T24Cheque, String> {
	
	 T24Cheque findByRibTireur(String ribTireur);
	 
	
	    @Query("SELECT t.ribTireur FROM T24Cheque t WHERE t.id = :id")
	    String findRibTireurById(@Param("id") String id);

}

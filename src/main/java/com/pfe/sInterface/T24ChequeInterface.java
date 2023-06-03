package com.pfe.sInterface;

import java.util.List;
import java.util.Map;
import com.pfe.model.T24Cheque;

public interface T24ChequeInterface {


	Map<String, Map<String, String>> getDecisionData();

	List<T24Cheque> getListChequeFromT24(String t24today) throws Exception;
	T24Cheque getOneChequeFromT24(String t24today, String idCheque) throws Exception;
	public void updateSelectedStatus(String id, boolean isSelected);
	void save(T24Cheque cheque);
	public String getRibTireurById(String id);
	T24Cheque findById(String id);
	public List<String> getImgSignature(String compte);
	public List<String> getSignaturePaths(String id);
	
	List<T24Cheque> getAllCheques();
	
//	T24Cheque save(T24Cheque t24Cheque);
//
//	List<T24Cheque> getAllCheques();
//	void watchFileAndSaveJsonData() throws IOException, InterruptedException;
//	void readAndSaveJsonData() throws IOException;
//	Map<String, Map<String, String>> getDecisionData() throws IOException;
//		T24Cheque getOneChequeFromT24(String t24today, String idCheque) throws NoDataException;	
//		T24Cheque update(String id, Integer[] visDeForme, Integer[] inexploitable);
//		List<T24Cheque> getListChequeFromT24(String t24today) throws Exception;
}

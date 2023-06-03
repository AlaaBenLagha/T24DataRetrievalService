package com.pfe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.pfe.exceptions.NoDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.tools.T24Connector;
import com.pfe.tools.T24FileStructureReader;
import com.pfe.dictionary.DictionaryProvider;
import com.pfe.dto.ImageData;
import com.pfe.dto.ImageRef;
import com.pfe.dto.ImageResponse;
import com.pfe.dto.SelectItemDTO;
import com.pfe.dto.T24ChqResponse;
import com.pfe.model.T24Cheque;
import com.pfe.repository.T24ChequeRepo;
import com.pfe.sInterface.T24ChequeInterface;

@Service
public class T24ChequeService implements T24ChequeInterface {
	
	 private static final Logger logger = LoggerFactory.getLogger(T24ChequeService.class);
	
	
	@Autowired
    private T24ChequeRepo t24ChequeRepository;
	
	
	//optimse
//	 @Autowired
//	 private T24Connector t24Connector;
	
	
	
	private int numberChequeLoaded;

	
	
	public List<String> getSignaturePaths(String id) {
	    // Fetch T24Cheque object from repository by id
	    Optional<T24Cheque> chequeOpt = t24ChequeRepository.findById(id);

	    if (chequeOpt.isPresent()) {
	        T24Cheque cheque = chequeOpt.get();

	        T24FileStructureReader fileStructureReader = new T24FileStructureReader();
	        Properties props = fileStructureReader.getFileProperties("capture.properties");
	        String appName = props.getProperty("applicationNameSimulation");

	        List<String> listPath = new ArrayList<>();
	        
	        if (cheque.getRefSignature().size() == 0) {
	            String pathSignature = appName + "/warning/warning.jpg";
	            listPath.add(pathSignature);
	        } else {
	            for (String ref : cheque.getRefSignature()) {
	                String pathSignature = appName + "/signatures/" + ref;
	                listPath.add(pathSignature);
	            }
	        }
	        return listPath;
	    } else {
	        throw new NoSuchElementException("No T24Cheque found with id: " + id);
	    }
	}

  
	public void updateSelectedStatus(String id, boolean isSelected) {
	    Optional<T24Cheque> chequeOpt = t24ChequeRepository.findById(id);
	    if (!chequeOpt.isPresent()) {
	        throw new EntityNotFoundException("No cheque found with id: " + id);
	    }
	    T24Cheque cheque = chequeOpt.get();
	    String compte = getRibTireurById(id);
	    getImgSignature(compte);
	    cheque.setIsSelected(isSelected);
	    t24ChequeRepository.save(cheque);
	}
	 

	public List<String> getImgSignature(String compte) {
	    logger.info("**** getImgSignature ****");
	    List<String> listSignatures = new ArrayList<String>();
	    String signatureRef = compte.substring(8, 18);
	    String requestStr = ",BZ.GET.IMG.SIGN,IMAGE.REFERENCE:EQ=" + signatureRef;
	    String t24resp;
	    try {
	        T24Connector t24connector = new T24Connector();
	        t24resp = t24connector.getSignatureHttpPost(requestStr); // Use the simulated data retrieval method

	        // Parse the response and extract the image signatures
	        ObjectMapper mapper = new ObjectMapper();
	        ImageResponse map = mapper.readValue(t24resp, ImageResponse.class);
	        if (map != null && map.getStatus() != null) {
	            if (!map.getStatus().equals("KO")) {
	                ImageData data = map.getData();
	                if (data != null) {
	                    List<ImageRef> imageRef = data.getRecord();
	                    if (imageRef != null) {
	                        for (ImageRef imageRef2 : imageRef) {
	                            listSignatures.add(imageRef2.getImage());
	                        }
	                    }
	                    
	                    //Save references f Database  by RibTireur
	                    T24Cheque cheque = t24ChequeRepository.findByRibTireur(compte);

                        if (cheque != null) {
                            cheque.setRefSignature(listSignatures);
                            t24ChequeRepository.save(cheque);
                            logger.info("Signature compte " + compte + " : " + listSignatures);
                        } else {
                            logger.info("No Cheque found for compte: " + compte);
                        }
	                         
	                    
	                }
	            } else if (map.getStatus().equals("ERROR")) {
	                logger.error("Error: " + map.getErrorMessage() + ". Check number: " + signatureRef);
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Error retrieving image signatures", e);
	    }

	    if (listSignatures.isEmpty()) {
	        logger.info("Signature reference " + signatureRef + " does not exist.");
	        logger.info("Signature compte " + compte + " does not exist.");
	    } else {
	        logger.info("Signature compte " + compte + " : " + listSignatures);
	    }
	    
	    

	    return listSignatures;
	}




	 
	 

	 // getListChequeFromT24  ( check deprecation for visedeforme later )
	 
	 @SuppressWarnings("deprecation")
	public List<T24Cheque> getListChequeFromT24(String t24today) throws Exception {
			logger.info("************getListChequeFromT24*****************");
			// T24FileStructureReader fileStructureReader = new T24FileStructureReader();
			// Properties props =
			// fileStructureReader.getFileProperties("I_F_BZ_MDP_CHQ_RECU.properties");
			List<T24Cheque> listT24Chq = new ArrayList<T24Cheque>();

			String requestStr = ",NOFILE.GET.CHEQUE,DATE:EQ=" + t24today;

			String t24resp;
			try {
				
				//DB connection Class  T24Connector
				T24Connector t24connector = new T24Connector();
				t24resp = t24connector.getHttpPost(requestStr);
				ObjectMapper mapper = new ObjectMapper();
				
				//T24ChqResponse class
				T24ChqResponse map = mapper.readValue(t24resp, T24ChqResponse.class);
				if (map.getStatus().equals("KO")) {

					throw new NoDataException("No DATA");
				}

				if (map.getData() != null) {
					listT24Chq = map.getData().getRecord();
					int count = 0;
					for (T24Cheque t24Cheque : listT24Chq) {
						if (t24Cheque.getCodeRemettant().trim().equals("25")) {
							String dateImage = t24Cheque.getDateOperation().trim();

							t24Cheque.setDateImg(dateImage.substring(6, 8) + dateImage.substring(4, 6) + dateImage.substring(0, 4));
						}
						
						
						//processing data
						t24Cheque.setNumChq(t24Cheque.getNumChq());
						t24Cheque.setCMC7(t24Cheque.getCMC7());
						t24Cheque.setDateImgNew(t24Cheque.getDateImgNew());
						t24Cheque.setCheckSignatureReference(t24Cheque.getCheckSignatureReference());

						String CONSULTEE = t24Cheque.getValConsultee();
						if (CONSULTEE.equals("YES")) {
							t24Cheque.setViewed(true);
						}
						String[] intInex;
						Integer[] inexploitableTab = { 99, 99, 99, 99 };
						boolean[] inexploitableTabVerrou = { false, false, false, false };

						intInex = t24Cheque.getInexploitableString().trim().split(" ");

						for (int i = 0; i < intInex.length; i++) {

							if (i < 4 && !intInex[i].trim().isEmpty()) {
								logger.info(t24Cheque.getId() + ": -- intInex[i]: " + intInex[i]);

								try {
									inexploitableTab[i] = new Integer(intInex[i].trim());
									inexploitableTabVerrou[i] = true;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						String[] intVice;
						Integer[] viceDeformeTab = { 99, 99, 99, 99 };
						boolean[] viceDeformeTabVerrou = { false, false, false, false };
						if (t24Cheque.getViseDeformeString() != null && !t24Cheque.getViseDeformeString().isEmpty()) {
							intVice = t24Cheque.getViseDeformeString().trim().split(" ");

							/* Changement suite lenteur chargement cheque */
							// cheque.setRefSignature(getRefImgSignature(cx, cheque.getNumCpt()));

							for (int i = 0; i < intVice.length; i++) {
								if (intVice[i] != "" && !intVice[i].trim().isEmpty()) {
									logger.info(t24Cheque.getId() + ": -- intVice[i]: " + intVice[i]);
									try {
										viceDeformeTab[i] = new Integer(intVice[i]);
										viceDeformeTabVerrou[i] = true;
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}

						t24Cheque.setInexpoitable(inexploitableTab);
						t24Cheque.setInexpoitableVerrou(inexploitableTabVerrou);
						t24Cheque.setVisDeForme(viceDeformeTab);
						t24Cheque.setVisDeFormeVerrou(viceDeformeTabVerrou);
						
						
						t24Cheque.setViceDeFormeSelectedItems(DictionaryProvider.getViseDeFormeDictionary(t24Cheque.getCodeVal().trim()));
						t24Cheque.setInexploitabeleSelectedItems(DictionaryProvider.getInexploitableDictionary(t24Cheque.getCodeVal().trim()));
						
						
						
																		//getViceDeFormdictionary()
					
						count++;

						this.numberChequeLoaded = count;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			t24ChequeRepository.saveAll(listT24Chq);
			return listT24Chq;
		}




	 
	 

	// getOneChequeFromT24		( check deprecation for visedeformeTab et inexploitableTab later )
	@SuppressWarnings("deprecation")
	public T24Cheque getOneChequeFromT24(String t24today, String idCheque) throws Exception {
	    logger.info("************getOneChequeFromT24*****************");

	    T24Cheque t24Cheque ;
	 
	        T24Connector t24connector = new T24Connector();
	        t24Cheque = t24connector.getOneHttpPost(t24today, idCheque);

	        if (t24Cheque.getCodeRemettant().trim().equals("25")) {
	            String dateImage = t24Cheque.getDateOperation().trim();
	            t24Cheque.setDateImg(dateImage.substring(6, 8) + dateImage.substring(4, 6) + dateImage.substring(0, 4));
	        }
	        
	        
	        
	        String CONSULTEE = t24Cheque.getValConsultee();
	        if (CONSULTEE.equals("YES")) {
	            t24Cheque.setViewed(true);
	        }
	        //for testing
	     

	        String[] intInex;
	        Integer[] inexploitableTab = { 99, 99, 99, 99 };
	        boolean[] inexploitableTabVerrou = { false, false, false, false };
	        intInex = t24Cheque.getInexploitableString().trim().split(" ");

	        for (int i = 0; i < intInex.length; i++) {
	            if (i < 4 && !intInex[i].trim().isEmpty()) {
	                logger.info(t24Cheque.getId() + ": -- intInex[i]: " + intInex[i]);
	                try {
	                    inexploitableTab[i] = new Integer(intInex[i].trim());
	                    inexploitableTabVerrou[i] = true;
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	        String[] intVice;
	        Integer[] viceDeformeTab = { 99, 99, 99, 99 };
	        boolean[] viceDeformeTabVerrou = { false, false, false, false };
	        if (t24Cheque.getViseDeformeString() != null && !t24Cheque.getViseDeformeString().isEmpty()) {
	            intVice = t24Cheque.getViseDeformeString().trim().split(" ");
	            for (int i = 0; i < intVice.length; i++) {
	                if (intVice[i] != "" && !intVice[i].trim().isEmpty()) {
	                    logger.info(t24Cheque.getId() + ": -- intVice[i]: " + intVice[i]);
	                    try {
	                        viceDeformeTab[i] = new Integer(intVice[i]);
	                        viceDeformeTabVerrou[i] = true;
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }

	        t24Cheque.setInexpoitable(inexploitableTab);
	        t24Cheque.setInexpoitableVerrou(inexploitableTabVerrou);
	        t24Cheque.setVisDeForme(viceDeformeTab);
	        t24Cheque.setVisDeFormeVerrou(viceDeformeTabVerrou);
	        t24Cheque.setViceDeFormeSelectedItems(DictionaryProvider.getViseDeFormeDictionary(t24Cheque.getCodeVal().trim()));
	        t24Cheque.setInexploitabeleSelectedItems(DictionaryProvider.getInexploitableDictionary(t24Cheque.getCodeVal().trim()));

	        this.numberChequeLoaded = 1;
	        
	    
	   

	    return t24Cheque;
	}


	
	
	 // vise de forme + inexploitable lists
	 public Map<String, Map<String, String>> getDecisionData() {
		    Map<String, Map<String, String>> decisions = new HashMap<>();
		    
		    List<SelectItemDTO> visDeForme = DictionaryProvider.getViseDeFormeDictionary();
		    List<SelectItemDTO> inexploitable = DictionaryProvider.getInexploitableDictionary();
		    
		    Map<String, String> visDeFormeMap = visDeForme.stream()
		            .collect(Collectors.toMap(SelectItemDTO::getLabel, SelectItemDTO::getValue));
		    Map<String, String> inexploitableMap = inexploitable.stream()
		            .collect(Collectors.toMap(SelectItemDTO::getLabel, SelectItemDTO::getValue));
		    
		    decisions.put("visDeForme", visDeFormeMap);
		    decisions.put("inexploitable", inexploitableMap);
		    
		    return decisions;
		}

	 
	 
/**	 		//getListChequeFromT24 Refactored
	 
//	 public List<T24Cheque> getListChequeFromT24(String t24today) throws Exception {
//	    logger.info("************getListChequeFromT24*****************");
//
//	    List<T24Cheque> listT24Chq = new ArrayList<T24Cheque>();
//
//	    String requestStr = ",NOFILE.GET.CHEQUE,DATE:EQ=" + t24today;
//	    T24Connector t24connector = new T24Connector();
//	    String t24resp = t24connector.getHttpPost(requestStr);
//
//	    ObjectMapper mapper = new ObjectMapper();
//	    T24ChqResponse map = mapper.readValue(t24resp, T24ChqResponse.class);
//
//	    if (map.getStatus().equals("KO")) {
//	        throw new NoDataException("No DATA");
//	    }
//
//	    if (map.getData() != null) {
//	        listT24Chq = map.getData().getRecord();
//	        int count = 0;
//
//	        for (T24Cheque t24Cheque : listT24Chq) {
//	            processChequeData(t24Cheque);
//	            count++;
//	        }
//	        this.numberChequeLoaded = count;
//	    }
//	    return listT24Chq;
//	}	 
//	 
//	 private void processChequeData(T24Cheque cheque) {
//		    // Based on your business logic
//		    if (cheque.getCodeRemettant() != null && cheque.getCodeRemettant().trim().equals("25")) {
//		        String dateImage = cheque.getDateOperation().trim();
//		        if (dateImage != null) {
//		            cheque.setDateImg(dateImage.substring(6, 8) + dateImage.substring(4, 6) + dateImage.substring(0, 4));
//		        }
//		    }
//
//		    String CONSULTEE = cheque.getValConsultee();
//		    if (CONSULTEE != null && CONSULTEE.equals("YES")) {
//		        cheque.setViewed(true);
//		    }
//
//		    if (cheque.getInexploitableString() != null && cheque.getViseDeformeString() != null) {
//		        processTabVerrou(cheque, cheque.getInexploitableString(), cheque::setInexpoitable, cheque::setInexpoitableVerrou);
//		        processTabVerrou(cheque, cheque.getViseDeformeString(), cheque::setVisDeForme, cheque::setVisDeFormeVerrou);
//		    }
//
//		    if (cheque.getCodeVal() != null) {
//		        cheque.setViceDeFormeSelectedItems(DictionaryProvider.getViseDeFormeDictionary(cheque.getCodeVal().trim()));
//		        cheque.setInexploitabeleSelectedItems(DictionaryProvider.getInexploitableDictionary(cheque.getCodeVal().trim()));
//		    }
//		}	 
//	 private void processTabVerrou(T24Cheque cheque, String str, Consumer<Integer[]> setTab, Consumer<boolean[]> setTabVerrou) {
//		    // Assuming that Consumer<Integer[]> and Consumer<boolean[]> are functional interfaces to setters of T24Cheque
//		    String[] strArray = str.split(" ");
//		    Integer[] tab = { 99, 99, 99, 99 };
//		    boolean[] tabVerrou = { false, false, false, false };
//
//		    for (int i = 0; i < strArray.length; i++) {
//		        if (i < 4 && !strArray[i].trim().isEmpty()) {
//		            logger.info(cheque.getId() + ": -- strArray[i]: " + strArray[i]);
//		            try {
//		                tab[i] = new Integer(strArray[i].trim());
//		                tabVerrou[i] = true;
//		            } catch (Exception e) {
//		                e.printStackTrace();
//		            }
//		        }
//		    }
//		    setTab.accept(tab);
//		    setTabVerrou.accept(tabVerrou);
//		}
	 
	 
	 
	 
	 **/
	 
	 

    
    
//	public T24Cheque update(String id, Integer[] visDeForme, Integer[] inexploitable) {
//		  // Find the existing cheque
//	    T24Cheque existingT24Cheque = t24ChequeRepository.findById(id)
//	            .orElseThrow(() -> new ResourceNotFoundException("T24Cheque not found with id: " + id));
//
//	    // Update visDeForme and inexploitable fields
//	    existingT24Cheque.setVisDeForme(visDeForme);
//	    existingT24Cheque.setInexploitable(inexploitable);
//
//	    // Save and return the updated cheque
//	    return t24ChequeRepository.save(existingT24Cheque);
//	}
	
	
	
	



	 public String getRibTireurById(String id) {
	        String ribTireur = t24ChequeRepository.findRibTireurById(id);

	        // You could handle the case where ribTireur is null here, if needed
	        if(ribTireur == null) {
	            throw new EntityNotFoundException("T24Cheque with ID " + id + " not found.");
	        }

	        return ribTireur;
	    }



			
			@Override
			public void save(T24Cheque cheque) {
				t24ChequeRepository.save(cheque);
				
			}



			 @Override
			    public List<T24Cheque> getAllCheques() {
			        return t24ChequeRepository.findAll();
			    }



			@Override
			public T24Cheque findById(String id) {

				return t24ChequeRepository.findById(id).orElse(null);
			}



		
	

}

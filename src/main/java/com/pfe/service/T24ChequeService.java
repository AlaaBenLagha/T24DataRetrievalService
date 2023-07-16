package com.pfe.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.tools.T24Connector;
import com.pfe.tools.T24FileStructureReader;
import com.pfe.dictionary.DictionaryProvider;
import com.pfe.dto.ImageData;
import com.pfe.dto.ImageRef;
import com.pfe.dto.ImageResponse;
import com.pfe.dto.T24ChqResponse;
import com.pfe.model.SelectItem;
import com.pfe.model.T24Cheque;
import com.pfe.repository.T24ChequeRepo;
import com.pfe.sInterface.T24ChequeInterface;

@Service
public class T24ChequeService implements T24ChequeInterface {
	
	 private static final Logger logger = LoggerFactory.getLogger(T24ChequeService.class);
	
	
	@Autowired
    private T24ChequeRepo t24ChequeRepository;
	
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private RestTemplate restTemplate;


	public Integer numberChequeLoaded = 0;
	

	@Autowired
	private DictionaryProvider dictionaryProvider;
	
	
	
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
	                // testing : 
	                //  on local   C:/Users/alaw-/Desktop/stages/Stage PFE 2K23 (Zitouna Bank)/Files/signatures/ref
	                //   on kube   /data/Files/signatures/ref
	                listPath.add(pathSignature);
	            }
	        }
	        return listPath;
	    } else {
	        throw new NoSuchElementException("No T24Cheque found with id: " + id);
	    }
	}

  
	public T24Cheque updateSelectedStatus(String id, boolean isSelected) {
	    Optional<T24Cheque> chequeOpt = t24ChequeRepository.findById(id);
	    if (!chequeOpt.isPresent()) {
	        throw new EntityNotFoundException("No cheque found with id: " + id);
	    }
	    T24Cheque cheque = chequeOpt.get();
	    String compte = getRibTireurById(id);
	    getImgSignature(compte);
	    cheque.setIsSelected(isSelected);
	    return t24ChequeRepository.save(cheque);
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
	    List<T24Cheque> listT24Chq = new ArrayList<T24Cheque>();

	    String requestStr = ",NOFILE.GET.CHEQUE,DATE:EQ=" + t24today;
	    String t24resp;
	    try {
	        
	        T24Connector t24connector = new T24Connector();
	        t24resp = t24connector.getHttpPost(requestStr);
	        ObjectMapper mapper = new ObjectMapper();
	        
	        T24ChqResponse map = mapper.readValue(t24resp, T24ChqResponse.class);
	        if (map.getStatus().equals("KO")) {

	            throw new NoDataException("No DATA");
	        }

	        if (map.getData() != null) {
	            List<T24Cheque> allCheques = map.getData().getRecord();
	            int count = 0;
	            for (T24Cheque t24Cheque : allCheques) {
	                // Check if a cheque with the same ID already exists in the database
	                if (findById(t24Cheque.getId()) == null) {
	                    // If a cheque with the same ID doesn't exist, add it to the list
	                    listT24Chq.add(t24Cheque);

	                    if (t24Cheque.getCodeRemettant().trim().equals("25")) {
	                    	
	                        String dateImage = t24Cheque.getDateOperation();
	                        
	                        //old version  t24Cheque.setDateImg(dateImage.substring(6, 8) + dateImage.substring(4, 6) + dateImage.substring(0, 4)); return 20-2352 ??
	                        String[] splitDate = dateImage.split("-");
	                        if(splitDate.length == 3){
	            	            if(splitDate[0].length() == 4 && splitDate[1].length() == 2 && splitDate[2].length() == 2){
	            	                DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            	                DateTimeFormatter newFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
	            	                LocalDate date = LocalDate.parse(dateImage, oldFormat);
	            	                t24Cheque.setDateImg(date.format(newFormat));
	            	            }else{
	            	                throw new IllegalArgumentException("dateOperation does not match the expected format yyyy-MM-dd. It is: " + dateImage);
	            	            }
	                        }

	                     
	
	                        
	                        
	                    }
	                    
	                    if (t24Cheque.getId().trim().length()== 35) {
	                    	
	                    		String id = t24Cheque.getId();
	                    		String ribtireur = id.substring(15, 35);
	                    		
	                    		
	                         	t24Cheque.setCMC7(id.substring(8, 35));
			        	        t24Cheque.setNumChq(id.substring(8, 15));
			        	        t24Cheque.setRibTireur(ribtireur);
			        	        t24Cheque.setNumCpt(id.substring(27, 33));
			        	        
			        	        t24Cheque.setCheckSignatureReference(ribtireur.substring(8, 18));
	           			
	                    }
	                    t24Cheque.setDateImgNew(t24Cheque.getDateImgNewTC());
	                    
	                   

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
	        	        
	        	        t24Cheque.setInexploitable(Arrays.asList(inexploitableTab));
	        	        t24Cheque.setVisDeForme(Arrays.asList(viceDeformeTab));
	        	        t24Cheque.setInexpoitableVerrou(inexploitableTabVerrou);
	        	        t24Cheque.setVisDeFormeVerrou(viceDeformeTabVerrou);
	        	        
	        	        List<SelectItem> viceDeFormeItems = dictionaryProvider.getViseDeFormeDictionary(t24Cheque.getCodeVal().trim());
	        	        viceDeFormeItems.forEach(selectItem -> selectItem.setT24Cheque(t24Cheque));
	        	        t24Cheque.setViceDeFormeSelectedItems(viceDeFormeItems);

	        	        List<SelectItem> inexploitableItems = dictionaryProvider.getInexploitableDictionary(t24Cheque.getCodeVal().trim());
	        	        inexploitableItems.forEach(selectItem -> selectItem.setT24Cheque(t24Cheque));
	        	        t24Cheque.setInexploitabeleSelectedItems(inexploitableItems);


	        	        


	        	    	this.numberChequeLoaded = count;
	        	    	t24ChequeRepository.saveAll(listT24Chq);
	        	   

	                  }
					}
	            }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    
	    return listT24Chq;
	}
	 
	//need for checking data
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
	        
	        t24Cheque.setInexploitable(Arrays.asList(inexploitableTab));
	        t24Cheque.setVisDeForme(Arrays.asList(viceDeformeTab));
	        t24Cheque.setInexpoitableVerrou(inexploitableTabVerrou);
	        t24Cheque.setVisDeFormeVerrou(viceDeformeTabVerrou);
	        
	        List<SelectItem> viceDeFormeItems = dictionaryProvider.getViseDeFormeDictionary(t24Cheque.getCodeVal().trim());
	        viceDeFormeItems.forEach(selectItem -> selectItem.setT24Cheque(t24Cheque));
	        t24Cheque.setViceDeFormeSelectedItems(viceDeFormeItems);

	        List<SelectItem> inexploitableItems = dictionaryProvider.getInexploitableDictionary(t24Cheque.getCodeVal().trim());
	        inexploitableItems.forEach(selectItem -> selectItem.setT24Cheque(t24Cheque));
	        t24Cheque.setInexploitabeleSelectedItems(inexploitableItems);


	        
	    
	   

	    return t24Cheque;
	}

	
	 // vise de forme + inexploitable lists
	 public Map<String, Map<String, String>> getDecisionData() {
		    Map<String, Map<String, String>> decisions = new HashMap<>();
		    
		    List<SelectItem> visDeForme = dictionaryProvider.getViseDeFormeDictionary();
		    List<SelectItem> inexploitable = dictionaryProvider.getInexploitableDictionary();
		    
		    Map<String, String> visDeFormeMap = visDeForme.stream()
		            .collect(Collectors.toMap(SelectItem::getLabel, SelectItem::getValue));
		    Map<String, String> inexploitableMap = inexploitable.stream()
		            .collect(Collectors.toMap(SelectItem::getLabel, SelectItem::getValue));
		    
		    decisions.put("visDeForme", visDeFormeMap);
		    decisions.put("inexploitable", inexploitableMap);
		    
		    return decisions;
		}


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

			@Override
			public T24Cheque setSelected(String id, boolean selected) {
			    T24Cheque cheque = updateSelectedStatus(id, selected);
			    template.convertAndSend("/topic/cheque-selected", cheque);
			    return cheque;
			}

			@Override
			public T24Cheque saveone(T24Cheque cheque) {
				return t24ChequeRepository.save(cheque);
			}

			public T24Cheque getOne(String id) {
				return t24ChequeRepository.findById(id).orElse(null);
			}

			public T24Cheque sveViseinex(T24Cheque cheque) {
				 return t24ChequeRepository.save(cheque);
			}
			
			
		

			
			
			
		



}

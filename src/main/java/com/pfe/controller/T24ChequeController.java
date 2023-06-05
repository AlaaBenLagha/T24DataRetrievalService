package com.pfe.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import com.pfe.exceptions.NoDataException;
import com.pfe.model.T24Cheque;
import com.pfe.sInterface.T24ChequeInterface;



@SpringBootApplication
@RestController
@RequestMapping("/api/cheques")
@CrossOrigin(origins = "*")
public class T24ChequeController {
	private static final Logger logger = LogManager.getLogger(T24ChequeController.class);

	
	
	@Autowired
    private T24ChequeInterface t24ChequeServiceI;
	


	
	
	
	
	
	
	//To delete
	//Extract img references + save fi database (getImgSignature(compte = ribTireur))
	@GetMapping("/signatures/{compte}")
    public ResponseEntity<List<String>> getImgSignature(@PathVariable String compte) {
        List<String> signatures = t24ChequeServiceI.getImgSignature(compte);

        // Return the signatures in the response
        return ResponseEntity.ok(signatures);
    }
	
	
	
	@PutMapping("/select/{id}")
	public T24Cheque updateIsSelectedStatus(@PathVariable String id, @RequestBody Map<String, Boolean> payload) {
	    boolean isSelected = payload.get("isSelected");
	    T24Cheque updatedCheque = t24ChequeServiceI.setSelected(id, isSelected);
	    
	   
        
        return updatedCheque;
	}
	
	
	
	
	 
		@GetMapping("/{id}/signatures")
		public ResponseEntity<List<String>> getSignaturePaths(@PathVariable String id) {
		    try {
		        List<String> paths = t24ChequeServiceI.getSignaturePaths(id);
		        return new ResponseEntity<>(paths, HttpStatus.OK);
		    } catch (NoSuchElementException e) {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
	
	



	//Save Extracted Data to DB then Return Data From DB
    @GetMapping("/all")
    public ResponseEntity<List<T24Cheque>> getListChequeFromT24(String t24today) {
        try {
        	//Save Extracted Data to DB
            t24ChequeServiceI.getListChequeFromT24(t24today);
            //Return Data From DB
            List<T24Cheque> cheques = t24ChequeServiceI.getAllCheques();
            return new ResponseEntity<>(cheques, HttpStatus.OK);
        } catch (NoDataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    

    @GetMapping("/decisionReasons")
    public ResponseEntity<Map<String, Map<String, String>>> getDecisionReasons() {
        try {
          
            Map<String, Map<String, String>> decisions = t24ChequeServiceI.getDecisionData();
            
            return new ResponseEntity<>(decisions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Unable to load decision reasons", e);
            return new ResponseEntity<>(new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    
    
    
    @GetMapping("/{t24today}/{idCheque}")
    public ResponseEntity<T24Cheque> getOneChequeFromT24(@PathVariable("t24today") String t24today, 
            @PathVariable("idCheque") String idCheque) {
        try {
            T24Cheque t24Cheque = t24ChequeServiceI.getOneChequeFromT24(t24today, idCheque);
            return ResponseEntity.ok().body(t24Cheque);
        } catch (NoDataException ex) {
            // Log the exception message as a warning or info level log
        	logger.warn("No Cheque found with given ID: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Log the exception stack trace as an error log
        	logger.error("An error occurred while retrieving the cheque", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    


    
    



    
    
    
    
    
    
    
    
//  @GetMapping("/decision-data")
//  public Map<String, Map<String, String>> getDecisionData() throws IOException {
//      return t24ChequeServiceI.getDecisionData();
//  }
    
    
    
//    @GetMapping("/{t24today}/{idCheque}")
//    public ResponseEntity<T24Cheque> getOneChequeFromT24(
//            @PathVariable("t24today") String t24today,
//            @PathVariable("idCheque") String idCheque) {
//        try {
//            T24Cheque t24Cheque = t24ChequeServiceI.getOneChequeFromT24(t24today, idCheque);
//            return new ResponseEntity<>(t24Cheque, HttpStatus.OK);
//        } catch (NoDataExceptifon e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
    
 



    
    


}

package com.pfe.tools;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.dto.T24ChqResponse;
import com.pfe.exceptions.NoDataException;
import com.pfe.model.T24Cheque;

//@Component
public class T24Connector {
	

	public String getHttpPost(String requestStr) {
        // Simulate the successful response from T24 system
        String simulatedResponse = "{"
            + "\"status\": \"OK\","
            + "\"data\": {"
            + "\"record\": " + getChecksData()
            + "}"
            + "}";
        return simulatedResponse;
    }
	
	
	
	public T24Cheque getOneHttpPost(String t24today, String checkID) throws Exception {
	    String requestStr = ",NOFILE.GET.CHEQUE,DATE:EQ=" + t24today + ",ID:EQ=" + checkID;
	    String t24resp = this.getHttpPost(requestStr);

	    ObjectMapper mapper = new ObjectMapper();
	    T24ChqResponse map = mapper.readValue(t24resp, T24ChqResponse.class);

	    if (map.getStatus().equals("KO")) {
	        throw new NoDataException("No DATA");
	    }

	    // Here we are assuming that each record in the response has a unique ID
	    if (map.getData() != null) {
	        
	        for (T24Cheque t24Cheque : map.getData().getRecord()) {
	        	
	            if (t24Cheque.getId().equals(checkID) && t24Cheque.getDateImgNew().equals(t24today)) {
	                return t24Cheque;
	            }
	        }
	    }

	    throw new NoDataException("No Cheque found with given ID, Please Check t24today OR ID");
	}

	
	private String getChecksData() {
	    return "["
	        + "{"
	        + "\"id\": \"20230526400037525043000000089099070\","
	        + "\"codeVal\": \"30\","
	        + "\"codeRemettant\": \"25\","
	        + "\"dateOperation\": \"2023-05-26\","
	        + "\"mntCheque\": 12500.200,"
	        + "\"mntReclame\": \"\","
	        + "\"mntRegulInt\": \"\","
	        + "\"agence\": \"70\","
	        + "\"ribBenef\": \"21285000000056899077\","
	        + "\"nomBenef\": \"Alaa eddine\","
	        + "\"dateEmission\": \"2023-05-26\","
	        + "\"situationBenef\": \"0\","
	        + "\"natureCmpt\": \"1\","
	        + "\"certifie\": \"YES\","
	        + "\"statut\": 2,"
	        + "\"inexploitableString\": \"\","
	        + "\"opposition\": \"YES\","
	        + "\"cloture\": \"YES\","
	        + "\"visDeForme\": [],"
	        + "\"ftDesionPai\": \"\","
	        + "\"valConsultee\": \"YES\","
	        + "\"dateImg\": \"2023-05-26\","
	        + "\"numCpt\": \"890990 \","
	        + "\"ribTireur\": \"25043000000089099070\""
	        + "},"
	        + "{"
	        //new
	        + "\"id\": \"20230526400036525043100000089100071\","
	        + "\"codeVal\": \"30\","
	        + "\"codeRemettant\": \"25\","
	        + "\"dateOperation\": \"2023-05-26\","
	        + "\"mntCheque\": 3500.000,"
	        + "\"mntReclame\": \"\","
	        + "\"mntRegulInt\": \"\","
	        + "\"agence\": \"71\","
	        + "\"ribBenef\": \"21285000000056900088\","
	        + "\"nomBenef\": \"John Doe\","
	        + "\"dateEmission\": \"2023-04-28\","
	        + "\"situationBenef\": \"1\","
	        + "\"natureCmpt\": \"1\","
	        + "\"certifie\": \"NO\","
	        + "\"statut\": 1,"
	        + "\"inexploitableString\": \"\","
	        + "\"opposition\": \"NO\","
	        + "\"cloture\": \"NO\","
	        + "\"visDeForme\": [],"
	        + "\"ftDesionPai\": \"\","
	        + "\"valConsultee\": \"\","
	        + "\"dateImg\": \"2023-04-28\","
	        + "\"numCpt\": \"891000 \","
	        + "\"ribTireur\": \"25043100000089100071\""
	        + "},"
	        + "{"
	        + "\"id\": \"20230526400035525043200000089101072\","
	        + "\"codeVal\": \"30\","
	        + "\"codeRemettant\": \"25\","
	        + "\"dateOperation\": \"2023-05-26\","
	        + "\"mntCheque\": 14500.000,"
	        + "\"mntReclame\": \"\","
	        + "\"mntRegulInt\": \"\","
	        + "\"agence\": \"72\","
	        + "\"ribBenef\": \"21285000000056901099\","
	        + "\"nomBenef\": \"Jane Doe\","
	        + "\"dateEmission\": \"2023-04-29\","
	        + "\"situationBenef\": \"0\","
	        + "\"natureCmpt\": \"\","
	        + "\"certifie\": \"YES\","
	        + "\"statut\": 1,"
	        + "\"inexploitableString\": \"\","
	        + "\"opposition\": \"NO\","
	        + "\"cloture\": \"YES\","
	        + "\"visDeForme\": [],"
	        + "\"ftDesionPai\": \"\","
	        + "\"valConsultee\": \"YES\","
	        + "\"dateImg\": \"2023-04-29\","
	        + "\"numCpt\": \"891010 \","
	        + "\"ribTireur\": \"25043200000089101072\""
	        + "},"
	        + "{"
	        + "\"id\": \"20230526400033425043400000089103074\","
	        + "\"codeVal\": \"20\","
	        + "\"codeRemettant\": \"25\","
	        + "\"dateOperation\": \"2023-05-26\","
	        + "\"mntCheque\": 65500.000,"
	        + "\"mntReclame\": \"\","
	        + "\"mntRegulInt\": \"\","
	        + "\"agence\": \"74\","
	        + "\"ribBenef\": \"21285000000056903011\","
	        + "\"nomBenef\": \"Bob Jones\","
	        + "\"dateEmission\": \"2023-04-31\","
	        + "\"situationBenef\": \"1\","
	        + "\"natureCmpt\": \"4\","
	        + "\"certifie\": \"YES\","
	        + "\"statut\": 2,"
	        + "\"inexploitableString\": \"\","
	        + "\"opposition\": \"NO\","
	        + "\"cloture\": \"YES\","
	        + "\"visDeForme\": [],"
	        + "\"ftDesionPai\": \"\","
	        + "\"valConsultee\": \"\","
	        + "\"dateImg\": \"2023-04-31\","
	        + "\"numCpt\": \"891030 \","
	        + "\"ribTireur\": \"25043400000089103074\""
	        + "},"
	        + "{"
	        + "\"id\": \"20230526400032425043500000089104075\","
	        + "\"codeVal\": \"30\","
	        + "\"codeRemettant\": \"25\","
	        + "\"dateOperation\": \"2023-05-26\","
	        + "\"mntCheque\": 6500.000,"
	        + "\"mntReclame\": \"\","
	        + "\"mntRegulInt\": \"\","
	        + "\"agence\": \"75\","
	        + "\"ribBenef\": \"21285000000056904012\","
	        + "\"nomBenef\": \"Charlie Brown\","
	        + "\"dateEmission\": \"2023-05-01\","
	        + "\"situationBenef\": \"0\","
	        + "\"natureCmpt\": \"5\","
	        + "\"certifie\": \"NO\","
	        + "\"statut\": 1,"
	        + "\"inexploitableString\": \"\","
	        + "\"opposition\": \"YES\","
	        + "\"cloture\": \"NO\","
	        + "\"visDeForme\": [],"
	        + "\"ftDesionPai\": \"\","
	        + "\"valConsultee\": \"\","
	        + "\"dateImg\": \"2023-05-01\","
	        + "\"numCpt\": \"891040 \","
	        + "\"ribTireur\": \"25043500000089104075\""
	        + "}"
	        + "]";
	}

	
	public String getSignatureHttpPost(String requestStr) {
       
		
		    String motCle = "IMAGE.REFERENCE:EQ=";
		    
		    int index = requestStr.indexOf(motCle);
		    String compteRef = "";

		    // Check if keyword is found and there are enough characters for compteRef
		    if(index != -1 && requestStr.length() >= index + motCle.length() + 10) {
		    	//compteSignatureReferece extraction
		        compteRef = requestStr.substring(index + motCle.length(), index + motCle.length() + 10);
		    } else {
		        // Handle error case where keyword not found or not enough characters for compteRef
		        return "{"
		            + "\"status\": \"ERROR\","
		            + "\"errorMessage\": \"Invalid requestStr\""
		            + "}";
		    }
		
			
	        String simulatedResponse = null;
	
	        switch (compteRef) {
	            case "0000890990":
		               simulatedResponse = "{"
		                    + "\"status\": \"OK\","
		                    + "\"data\": {"
		                    + "\"record\": ["
		                    + "{ \"id\": \"1\", \"description\": \"specimen\", \"image\": \"2611209901.jpg\"},"
		                    + "{ \"id\": \"2\", \"description\": \"mandataires\", \"image\": \"2611209902.jpg\"}"
		                    + "]"
		                    + "}"
		                    + "}";
		               break;
	            case "0000891000":
		               simulatedResponse = "{"
		                    + "\"status\": \"OK\","
		                    + "\"data\": {"
		                    + "\"record\": ["
		                    + "{ \"id\": \"3\", \"description\": \"specimen\", \"image\": \"2001290001.jpg\"},"
		                    + "{ \"id\": \"4\", \"description\": \"mandataires\", \"image\": \"2001290002.jpg\"}"
		                    + "]"
		                    + "}"
		                    + "}";
		               break;
	            case "0000891010":
		               simulatedResponse = "{"
		                    + "\"status\": \"OK\","
		                    + "\"data\": {"
		                    + "\"record\": ["
		                    + "{ \"id\": \"5\", \"description\": \"specimen\", \"image\": \"23041101001.jpg\"},"
		                    + "{ \"id\": \"6\", \"description\": \"mandataires\", \"image\": \"23041101002.jpg\"}"
		                    + "]"
		                    + "}"
		                    + "}";
		               break;
	            case "0000891030":
		        	   simulatedResponse = "{"
		  	                + "\"status\": \"OK\","
		  	                + "\"data\": {"
		  	                + "\"record\": ["	
		  	                + "{ \"id\": \"7\", \"description\": \"specimen\", \"image\": \"22090703001.jpg\"},"
		  	                + "{ \"id\": \"8\", \"description\": \"mandataires\", \"image\": \"22090703002.jpg\"}"
		  	                + "]"
		  	                + "}"
		  	                + "}";
		               break;
		        case "0000891040":
		        	   simulatedResponse = "{"
		  	                + "\"status\": \"OK\","
		  	                + "\"data\": {"
		  	                + "\"record\": ["
		  	                + "{ \"id\": \"9\", \"description\": \"specimen\", \"image\": \"15061504001.jpg\"},"
		  	                + "{ \"id\": \"10\", \"description\": \"mandataires\", \"image\": \"15061504002.jpg\"}"
		  	                + "]"
		  	                + "}"
		  	                + "}";
		               break;
	            default:
	                // handle case when compteRef doesn't match any predefined ones
	                simulatedResponse = "{"
	                    + "\"status\": \"ERROR\","
	                    + "\"errorMessage\": \"Invalid compteRef\""
	                    + "}";
	        }
	
	        return simulatedResponse;
	    }

	
	
	
	




	
}


package com.pfe.modifier;




import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/modifier")
public class JsonModifierController {
	
	
	
	@PostMapping("/modify-json")
	public List<Entry> modifyJson(@RequestBody Map<String, Object> jsonObject) {
	    int multiplier = 30;  // Your preference for multiplication
	    List<Entry> entries = new ArrayList<>();

	    for (int i = 1; i <= multiplier; i++) {
	        Entry entry = new Entry();
	        for (String key : jsonObject.keySet()) {
	            String newKey = key.replaceAll("\\d", "");  // Remove the digit at the end of the key
	            Object value = jsonObject.get(key);

	            // Increment the value if it's a Number, append i if it's a String
	            Object newValue;
	            if (value instanceof Number) {
	                newValue = ((Number) value).doubleValue() + i;
	            } else if (value instanceof String) {
	                newValue = value.toString() + i;
	            } else {
	                newValue = value;  // For other types, leave the value as is
	            }

	            // Assign the value to the entry's corresponding field based on the key
	            try {
	                if (newKey.equals("mntCheque")) {
	                    entry.setMntCheque((float) ((Number) newValue).doubleValue());
	                } else if (newKey.equals("statut")) {
	                    entry.setStatut(((Number) newValue).intValue());
	                } else {
	                    // Use Jackson to convert the value to JSON and set it in the entry
	                    ObjectMapper objectMapper = new ObjectMapper();
	                    String jsonValue = objectMapper.writeValueAsString(newValue);
	                    objectMapper.readerForUpdating(entry).readValue("{\""+newKey+"\": "+jsonValue+"}");
	                }
	            } catch (Exception e) {
	                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while modifying JSON", e);
	            }
	        }
	        entries.add(entry);
	    }

	    return entries;
	}


	
	

}

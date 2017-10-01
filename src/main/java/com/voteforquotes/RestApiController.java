package com.voteforquotes;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.voteforquotes.repository.FormRepository;


@Controller
public class RestApiController {
	
	@Autowired
	private FormRepository form;
	
	@RequestMapping(path = "/addQuote", method = RequestMethod.GET)
	public String upload(Model model)
	{
		return "addQuote";
	}
	
	@RequestMapping(path = "/importFromFile", method = RequestMethod.POST)
	public String importFromFile(Model model,
								@RequestParam("file") MultipartFile file) throws IOException {
						
		InputStream input = new BufferedInputStream(file.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
		
		String quote;
		while((quote = bufferedReader.readLine()) != null) {
			form.createForm(quote, 0);
		}
		
		return "redirect:/form";
	}
	
	
    @RequestMapping(value="/importFromTextField", method = RequestMethod.POST)
    public String importFromTextField(Model model,
    							@RequestParam(value="quote", required=true) String quote) throws IOException{
    	if (!(quote.length()==0)){
    		form.createForm(quote, 0);
		}
    	    	
    	return "redirect:/form";
    }
	
	@RequestMapping(path = "/form", method = RequestMethod.GET)
	public String displayForm(Model model){
		model.addAttribute("data", form.obtainData());
		return "form";
	}
	
	@RequestMapping(path = "/countVotes", method = RequestMethod.POST)
	public String countVotes(Model model,
							@RequestParam Map<String,String> allRequestParams) {
		for (Map.Entry<String, String> entry : allRequestParams.entrySet()){
			String quote = entry.getKey();
			int votes = form.obtainVotes(quote);
			int votesFromForm = Integer.parseInt(entry.getValue());
			votes += votesFromForm;
			form.updateForm(quote, votes);	
		}
		return "redirect:/result";
	}
	
	@RequestMapping(path = "/result", method = RequestMethod.GET)
	public String displayResult(Model model) {
		model.addAttribute("data", form.obtainData());
		return "result";
	}
}

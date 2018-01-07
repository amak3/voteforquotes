package com.voteforquotes;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.voteforquotes.SortEnum;
import com.voteforquotes.repository.FormRepository;




@Controller
public class RestApiController {
	
	@Autowired
	private FormRepository form;
	
	@Autowired 
	private UserService userservice;
		
	@RequestMapping(path = "/addQuote", method = RequestMethod.GET)
	public String upload(Model model)
	{
		return "addQuote";
	}
	
	public static boolean isAscii(List<String> input){
	    boolean isAscii = true;
	    for (int i = 0; i < input.size(); i++) {
	        String line  = input.get(i);
	        int c =  line.charAt(i);
	        if (c < 32 ||  c >= 127) {
	        	isAscii = false;
	            break;
	        }
	    }
	    return isAscii;
	}
	
	@RequestMapping(path = "/importFromFile", method = RequestMethod.POST)
	public String importFromFile(Model model,
								@RequestParam("file") MultipartFile file) throws IOException {	
		try {
			InputStream input = new BufferedInputStream(file.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
			List<String> lines = bufferedReader.lines().collect(Collectors.toList());
			for (int i = 0; i < lines.size(); i++)
			{
				if (isAscii(lines)) {
					form.createForm(lines.get(i));
				}
				else {
					return"redirect:/errorFileFormat";
				}
			}
			return "redirect:/form";
		} catch (DatabaseException e) {
			return"redirect:/error";
		}
	}
	
	@RequestMapping(path = "/error", method = RequestMethod.GET)
	public String displayErrorPage(){
		return "error";
	}
	
	
    @RequestMapping(value="/importFromTextField", method = RequestMethod.POST)
    public String importFromTextField(Model model,
    							@RequestParam(value="quote", required=true) String quote) throws IOException{
    	if (!(quote.length()==0)){
    		try {
				form.createForm(quote);
			} catch (DatabaseException e) {
				return"redirect:/error";
			}
		}
    	return "redirect:/form";
    }
	
	@RequestMapping(path = "/form", method = RequestMethod.GET)
	public String displayForm(Model model,
							@RequestParam(name = "page", required = false, defaultValue = "1") int page){
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username = user.getUsername(); 
	    int userID = form.obtainUserID(username);
	    
		model.addAttribute("page", page);
		model.addAttribute("data", form.obtainDataForForm(page, userID));
		model.addAttribute("numberOfPages", form.obtainNumberOfPagesForForm(userID));
		return "form";
	}
	
	@RequestMapping(path = "/countVotes", method = RequestMethod.POST)
	public String countVotes(Model model,
							@RequestParam Map<String,String> allRequestParams) {
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username = user.getUsername(); 
	    
	    int userID = form.obtainUserID(username);
		
		for (Map.Entry<String, String> entry : allRequestParams.entrySet()){
			String quote = entry.getKey();
			int quoteID = form.obtainQuoteID(quote);
			int score = Integer.parseInt(entry.getValue());
			if (score != -1) {
				form.insertScore(quoteID, score, userID);
			}
		}
		return "redirect:/result";
	}
	

	@RequestMapping(path = "/result", method = RequestMethod.GET)
	public String displayResult(Model model,
								@RequestParam(name = "page", required = false, defaultValue = "1") int page,
								@RequestParam(name = "orderBy", required = false, defaultValue = "DATE") SortOrderBy orderBy,
								@RequestParam(name = "sort", required = false, defaultValue = "DESC") SortEnum sort){

		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("data", form.obtainDataForResult(sort, orderBy.toSqlColumn(), page));
	
		model.addAttribute("numberOfPages", form.obtainNumberOfPagesForResult());		
	 
		return "result";
	}
	
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String displayLogin(){
		return "login";
	}
	
	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String displayRegister(){
		return "register";
	}
	
	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public String register(@RequestParam(name = "username", required = true) String username,
								@RequestParam(name = "password", required = true) String password){
		userservice.createUser(username, password, "ROLE_USER");
		return "login";
	}
}
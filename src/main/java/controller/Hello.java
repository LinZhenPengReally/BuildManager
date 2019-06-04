package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class Hello {
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return "index";
	}
	
	@RequestMapping("/to_single")
	public String toSingle(HttpServletRequest request, HttpServletResponse response) {
		return "single";
	}
	
	@RequestMapping("/to_multipart")
	public String toMultipart(HttpServletRequest request, HttpServletResponse response) {
		return "multipart";
	}
	
	@RequestMapping("/message")
	@ResponseBody
	public String message(HttpSession session) {
		Map<String,String> map=new HashMap<String, String>();
        map.put("name", "lzp");
        map.put("age", "26");
        ObjectMapper mapper=new ObjectMapper();
        String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(jsonString);
        return jsonString;
	}
	
	@RequestMapping(path="/single", method=RequestMethod.POST)
	public String single(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		File dir=new File(request.getServletContext().getRealPath(File.separator)+"files");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File targetFile=new File(dir.getPath()+File.separator+file.getOriginalFilename());
		if (targetFile.exists()) {
			targetFile.delete();
		}
		try {
			targetFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			file.transferTo(targetFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return "redirect:to_single"; 
	}
	
	@RequestMapping(path="/multipart", method=RequestMethod.POST)
	public String multiple(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest mr=(MultipartHttpServletRequest)request;
		Map<String, MultipartFile> map=mr.getFileMap();
		File dir=new File(request.getServletContext().getRealPath(File.separator)+"files");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for(MultipartFile file : map.values()) {
			File targetFile=new File(dir.getPath()+File.separator+file.getOriginalFilename());
			if (targetFile.exists()) {
				targetFile.delete();
			}
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				file.transferTo(targetFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:to_multipart"; 
	}
}

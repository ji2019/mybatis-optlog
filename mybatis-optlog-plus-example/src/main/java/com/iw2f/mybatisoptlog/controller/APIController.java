package com.iw2f.mybatisoptlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {
	@PostMapping("/test")
	@ResponseBody
	public String getString() {
		return "";
	}
}
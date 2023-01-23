package com.example.loveCalculator.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.loveCalculator.model.Person;
import com.example.loveCalculator.service.LoveCalculatorService;



@Controller
public class FormController {

    @Autowired
    private LoveCalculatorService loveSvc;

    @GetMapping("/")
    public String showForm() {
        return "loveCalculator";
    }

    @GetMapping(path = "calculate")
    public String getPerson(@RequestParam(required = true) String fname,
            @RequestParam(required = true) String sname,
            Model model) throws Exception {

        Optional<Person> p = loveSvc.getPerson(fname, sname);
        model.addAttribute("person", p.get());
        return "compatibility";
    }
    
    @GetMapping(path="list")
    public String getList(Model model){
        List<Person> resultMap = loveSvc.findAll();
        if (resultMap!=null){
            model.addAttribute("arr", resultMap);
          
        }
        return "list";
    }
}

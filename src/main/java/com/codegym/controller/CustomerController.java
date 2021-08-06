package com.codegym.controller;

import com.codegym.exception.CustomerNotFoundException;
import com.codegym.exception.DuplicateLastNameException;
import com.codegym.model.Customer;
import com.codegym.model.Items;
import com.codegym.model.Province;
import com.codegym.service.ICustomerService;
import com.codegym.service.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProvinceService provinceService;

    @ModelAttribute("_provinces")
    public List<Province> provinces(){

        List<Province> _provinces = provinceService.findAll();
        return _provinces;
    }

    @GetMapping("")
    public ModelAndView findAll() {

        List<Customer> customers = customerService.findAll();
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable("id") Long id) throws CustomerNotFoundException {

        Customer customer = customerService.findCustomerById(id);
        ModelAndView modelAndView = new ModelAndView("/customer/detail");
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "html/css;charset=UTF-8")
    public ModelAndView save(@ModelAttribute("customer") Customer customer, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("/error/404");
            return modelAndView;
        }

        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("message", "Thêm mới khách hàng thành công!");
        return modelAndView;

    }

    @ExceptionHandler(DuplicateLastNameException.class)
    public ModelAndView showInputNotAcceptable() {

        return new ModelAndView("/customer/inputs-not-acceptable");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ModelAndView findCustomerNotFound() {

        return new ModelAndView("/error/404");
    }

}

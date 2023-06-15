package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;

    private final RoleRepository roleRepository;

    private final UserValidator validator;

    @Autowired
    public AdminController(UserService service, RoleRepository roleRepository, UserValidator validator) {
        this.service = service;
        this.roleRepository = roleRepository;
        this.validator = validator;
    }

    @GetMapping
    public String getUsersList(Model model) {
        model.addAttribute("users", service.getUsersList());
        return "admin";
    }

    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("new");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping()
    public String createNewUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "new";
        }
        service.createNewUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable("id") Long id) {
        User user = service.getUser(id);
        ModelAndView mav = new ModelAndView("edit");
        mav.addObject("user", user);
        List<Role> roles = roleRepository.findAll();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        service.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        service.deleteUser(id);
        return "redirect:/admin";
    }
}
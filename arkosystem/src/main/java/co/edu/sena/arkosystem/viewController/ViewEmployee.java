package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Employee;
import co.edu.sena.arkosystem.repository.RepositoryEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewEmployee {
    @Autowired
    RepositoryEmployee employeeRepository;

    @GetMapping("/view/employee")
    public String list(Model model) {
        model.addAttribute("activePage", "employee");
        model.addAttribute("employees", employeeRepository.findAll());
        return "ViewsEmployees/Employee";
    }

    @GetMapping("viewE/form")
    public String form(Model model) {
        model.addAttribute("activePage", "employee");
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("availableRoles", new String[]{"Administrador", "Empleado", "Cliente"});
        return "ViewsEmployees/employee_form";
    }

    @PostMapping("/viewE/save")
    public String save(@ModelAttribute Employee employee, RedirectAttributes ra) {
        String roleStr = employee.getRole();
        if (roleStr != null) {
            switch (roleStr.toUpperCase()) {
                case "ADMIN":
                    employee.setRole("4");
                    break;
                case "EMPLOYEE":
                    employee.setRole("7");
                    break;
                case "CLIENT":
                    employee.setRole("6");
                    break;
                default:
                    employee.setRole("7");
                    break;
            }
        }
        
        employeeRepository.save(employee);
        ra.addFlashAttribute("success", "Empleado Guardado");
        return "redirect:/view/employee";
    }

    @GetMapping("/viewE/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            String roleNum = employee.getRole();
            if (roleNum != null) {
                switch (roleNum) {
                    case "4":
                        employee.setRole("Administrador");
                        break;
                    case "7":
                        employee.setRole("Empleado");
                        break;
                    case "6":
                        employee.setRole("Cliente");
                        break;
                }
            }
        }
        model.addAttribute("activePage", "employee");
        model.addAttribute("employee", employee);
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("availableRoles", new String[]{"Administrador", "Empleado", "Cliente"});
        return "ViewsEmployees/employee_form";
    }

    @PostMapping("viewE/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        employeeRepository.deleteById(id);
        ra.addFlashAttribute("success", "Empleado eliminado");
        return "redirect:/view/employee";
    }
}
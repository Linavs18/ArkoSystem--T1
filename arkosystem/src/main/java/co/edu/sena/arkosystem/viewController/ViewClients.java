package co.edu.sena.arkosystem.viewController;

import co.edu.sena.arkosystem.model.Clients;
import co.edu.sena.arkosystem.repository.RepositoryClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewClients
{
    @Autowired
    RepositoryClients clientsRepository;

    @GetMapping("/view/clients")
    public String list(Model model)
    {
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", clientsRepository.findAll());
        return "ViewsClients/Clients";
    }

    @GetMapping("viewC/form")
    public String form(Model model)
    {
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", new Clients());
        return "ViewsClients/clients_form";
    }

    @PostMapping("/viewC/save")
    public String save(@ModelAttribute Clients clients, RedirectAttributes ra)
    {
        clientsRepository.save(clients);
        ra.addFlashAttribute("success","Cliente Guardado");
        return "redirect:/view/clients";
    }

    @GetMapping("/viewC/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Clients clients = clientsRepository.findById(id).orElse(null);
        model.addAttribute("activePage", "clients");
        model.addAttribute("clients", clients);
        return "ViewsClients/clients_form";
    }

    @PostMapping("viewC/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        clientsRepository.deleteById(id);
        ra.addFlashAttribute("success", "Cliente eliminado");
        return "redirect:/view/clients";
    }
}

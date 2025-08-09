package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.Sale;
import co.edu.sena.arkosystem.repository.RepositorySale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class ControllerSale {
    @Autowired
    private RepositorySale saleRepository;

    @GetMapping
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Sale getSaleById(@PathVariable Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    @GetMapping("/client/{clientId}")
    public List<Sale> getSalesByClient(@PathVariable Long clientId) {
        return saleRepository.findByClientId(clientId);
    }

    @PostMapping
    public Sale createSale(@RequestBody Sale sale) {
        return saleRepository.save(sale);
    }

    @PutMapping("/{id}")
    public Sale updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        sale.setId(id);
        return saleRepository.save(sale);
    }

    @PutMapping("/{id}/status/{status}")
    public Sale updateSaleStatus(@PathVariable Long id, @PathVariable String status) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if (sale != null) {
            sale.setStatus(status);
            return saleRepository.save(sale);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteSale(@PathVariable Long id) {
        saleRepository.deleteById(id);
    }
}

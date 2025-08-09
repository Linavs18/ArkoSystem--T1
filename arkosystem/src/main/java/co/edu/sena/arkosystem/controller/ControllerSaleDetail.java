package co.edu.sena.arkosystem.controller;
import co.edu.sena.arkosystem.model.SaleDetails;
import co.edu.sena.arkosystem.repository.RepositorySaleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-details")
public class ControllerSaleDetail {
    @Autowired
    private RepositorySaleDetails saleDetailRepository;

    @GetMapping
    public List<SaleDetails> getAllSaleDetails() {
        return saleDetailRepository.findAll();
    }

    @GetMapping("/{id}")
    public SaleDetails getSaleDetailById(@PathVariable Long id) {
        return saleDetailRepository.findById(id).orElse(null);
    }

    @GetMapping("/sale/{saleId}")
    public List<SaleDetails> getDetailsBySale(@PathVariable Long saleId) {
        return saleDetailRepository.findBySaleId(saleId);
    }

    @PostMapping
    public SaleDetails createSaleDetail(@RequestBody SaleDetails saleDetail) {
        return saleDetailRepository.save(saleDetail);
    }

    @PutMapping("/{id}")
    public SaleDetails updateSaleDetail(@PathVariable Long id, @RequestBody SaleDetails saleDetail) {
        saleDetail.setId(id);
        return saleDetailRepository.save(saleDetail);
    }

    @DeleteMapping("/{id}")
    public void deleteSaleDetail(@PathVariable Long id) {
        saleDetailRepository.deleteById(id);
    }
}

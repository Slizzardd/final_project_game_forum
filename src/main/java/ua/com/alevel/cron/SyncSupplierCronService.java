package ua.com.alevel.cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.alevel.service.SupplierService;

@Service
public class SyncSupplierCronService {

    private final SupplierService supplierService;

    public SyncSupplierCronService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void syncToSupplier() {
        supplierService.removingUnverifiedImages();
        supplierService.removingUnverifiedUsers();
    }
}

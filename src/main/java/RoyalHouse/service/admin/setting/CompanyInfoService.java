package RoyalHouse.service.admin.setting;

import RoyalHouse.model.company.CompanyInfo;
import RoyalHouse.service.PhotoService;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {
    private final PhotoService photoService;

    public CompanyInfoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    public void createCompanyInfo(CompanyInfo companyInfo) {

    }
}

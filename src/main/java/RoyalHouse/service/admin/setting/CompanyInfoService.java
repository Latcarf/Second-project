package RoyalHouse.service.admin.setting;

import RoyalHouse.model.company.CompanyInfo;
import RoyalHouse.repository.CompanyInfoRepository;
import RoyalHouse.service.PhotoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;
    private final PhotoService photoService;

    public CompanyInfoService(CompanyInfoRepository companyInfoRepository, PhotoService photoService) {
        this.companyInfoRepository = companyInfoRepository;
        this.photoService = photoService;
    }

    @Transactional
    public void createCompanyInfo(CompanyInfo companyInfo) {
        companyInfoRepository.save(companyInfo);
    }
}

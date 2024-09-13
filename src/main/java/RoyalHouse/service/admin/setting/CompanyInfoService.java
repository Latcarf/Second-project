package RoyalHouse.service.admin.setting;

import RoyalHouse.model.company.CompanyInfo;
import RoyalHouse.repository.CompanyInfoRepository;
import RoyalHouse.service.PhotoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;

    public CompanyInfoService(CompanyInfoRepository companyInfoRepository) {
        this.companyInfoRepository = companyInfoRepository;
    }

    @Transactional
    public void createCompanyInfo(CompanyInfo companyInfo) {
        companyInfoRepository.save(companyInfo);
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfoRepository.findAll().get(0);
    }
}

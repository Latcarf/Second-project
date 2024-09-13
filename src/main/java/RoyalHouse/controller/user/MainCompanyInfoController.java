package RoyalHouse.controller.user;

import RoyalHouse.model.company.CompanyInfo;
import RoyalHouse.service.admin.setting.CompanyInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainCompanyInfoController {
    private final CompanyInfoService companyInfoService;

    public MainCompanyInfoController(CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;
    }

    @GetMapping("/company-info")
    public String getCompanyInfo(Model model) {
        CompanyInfo companyInfo = companyInfoService.getCompanyInfo();
        model.addAttribute("companyInfo", companyInfo);
        return "user/company-info";
    }
}

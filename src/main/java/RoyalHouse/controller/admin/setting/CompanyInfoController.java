package RoyalHouse.controller.admin.setting;

import RoyalHouse.model.company.CompanyInfo;
import RoyalHouse.model.company.Service;
import RoyalHouse.service.PhotoService;
import RoyalHouse.service.admin.setting.CompanyInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin/setting/company-info")
public class CompanyInfoController {
    private final CompanyInfoService companyInfoService;
    private final PhotoService photoService;

    public CompanyInfoController(CompanyInfoService companyInfoService, PhotoService photoService) {
        this.companyInfoService = companyInfoService;
        this.photoService = photoService;
    }

    @GetMapping("/create")
    public String companyInfo() {
        return "admin/setting/company-info";
    }

    @PostMapping("/create")
    public String createCompanyInfo(@ModelAttribute CompanyInfo companyInfo,
                                    @RequestParam("banner") MultipartFile banner,
                                    @RequestParam("photos") List<MultipartFile> photos) {
        companyInfoService.createCompanyInfo(companyInfo);

        String bannerUrl = photoService.saveCompanyInfoBanner(banner, companyInfo.getHeading(), companyInfo.getId());
        List<String> photoUrls = photoService.saveCompanyInfoPhotos(photos, companyInfo.getHeading(), companyInfo.getId());

        companyInfo.setBannerUrl(bannerUrl);
        companyInfo.setPhotoUrls(photoUrls);

        return "redirect:/admin/setting/company-info";
    }
}

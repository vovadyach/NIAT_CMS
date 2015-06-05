package com.niat.cms.web;

import com.niat.cms.domain.Material;
import com.niat.cms.domain.Tag;
import com.niat.cms.domain.User;
import com.niat.cms.exceptions.MaterialNotFoundException;
import com.niat.cms.exceptions.TagNotFoundException;
import com.niat.cms.service.MaterialService;
import com.niat.cms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author gtament
 */

@Controller
public class UserPagesController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/")
    public String mainPage(Model model) {
        model.addAttribute("materials", materialService.findMaterialsOnMain());
        return "main";
    }

    @RequestMapping(value = "/archive")
    public String archivePage(Model model) {
        model.addAttribute("materials", materialService.findMaterialsInArchive());
        return "archive";
    }

    @RequestMapping(value = "/material/{matId}")
    public String materialPage(Model model, Long matId, @AuthenticationPrincipal User currentUser) {
        Material material = materialService.findById(matId);
        if (material == null || (material.getStatus() == Material.Status.DRAFT && !material.getAuthor().equals(currentUser))
                || (material.getStatus() == Material.Status.UNDER_MODERATION && !material.getModerator().equals(currentUser))
                || (material.getStatus() == Material.Status.MODERATION_TASK
                    && (currentUser.getRole() != User.Role.CORRECTOR || currentUser.getRole() != User.Role.ADMIN
                        || currentUser.getRole() != User.Role.EDITOR)))
            throw new MaterialNotFoundException();
        model.addAttribute("material", material);
        return "material_page";
    }

    @RequestMapping(value = "/tag/{tagText}")
    public String materialsWithTagPage(Model model, String tagText) {
        Tag tag = tagService.findByText(tagText);
        if (tag == null)
            throw new TagNotFoundException();
        List<Material> materials = materialService.findMaterialsWithTag(tag);
        model.addAttribute("materialswithtag", materials);
        return "tag_page";
    }
}

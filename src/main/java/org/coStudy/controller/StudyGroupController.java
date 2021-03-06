package org.coStudy.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.coStudy.domain.ApplyGroupMemberVO;
import org.coStudy.domain.Criteria;
import org.coStudy.domain.PageDTO;
import org.coStudy.domain.StudyGroupVO;
import org.coStudy.service.StudyGroupService;
import org.coStudy.utils.UploadFileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/studyGroup/*")
@AllArgsConstructor
public class StudyGroupController {

	private StudyGroupService service;

//	@Resource(name = "uploadPath")
//	private String uploadPath;

	@GetMapping(value = "/list")
	public void list(Criteria cri, Model model) {
		log.info(cri);
		model.addAttribute("list", service.list(cri));
		log.info(service.list(cri));
		int total = service.getTotal(cri);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
		model.addAttribute("total", total);
		model.addAttribute("total1", service.listCategory(1).size());
		model.addAttribute("total2", service.listCategory(2).size());
		model.addAttribute("total3", service.listCategory(3).size());
		model.addAttribute("total4", service.listCategory(4).size());
		model.addAttribute("total5", service.listCategory(5).size());
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = { "category_no" })
	public void list(@RequestParam("category_no") int category_no, Model model) {

		model.addAttribute("total", service.listCategory(category_no).size());
		model.addAttribute("total1", service.listCategory(1).size());
		model.addAttribute("total2", service.listCategory(2).size());
		model.addAttribute("total3", service.listCategory(3).size());
		model.addAttribute("total4", service.listCategory(4).size());
		model.addAttribute("total5", service.listCategory(5).size());
		model.addAttribute("list", service.listCategory(category_no));

	}

	@PostMapping("/insert")
	public String insert(HttpSession session,StudyGroupVO studygroup, RedirectAttributes rttr, MultipartFile file) throws Exception {
		String root_path = session.getServletContext().getRealPath("/");  
	      String uploadPath = "resources/";
	      String filename = file.getOriginalFilename();
	      log.info("root path:"+root_path);
	      log.info("upload path:"+uploadPath);
	      log.info("file name:"+filename);
	      String imgUploadPath = root_path+uploadPath + File.separator + "imgUpload";
	      String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
	      String fileName = null;
	      UUID uuid = UUID.randomUUID();
		if (file != null) {
			fileName = UploadFileUtils.fileUpload(uuid, imgUploadPath, file.getOriginalFilename(), file.getBytes(),
					ymdPath);
		} else {
			fileName = root_path+uploadPath + File.separator + "images" + File.separator + "none.png";
		}

		studygroup.setThumbimg(
				File.separator + "imgUpload" + ymdPath + File.separator + "s" + File.separator + "s_" + fileName);
		studygroup.setImg(File.separator + "imgUpload" + ymdPath + File.separator + fileName);

		service.insert(studygroup);

		Map<Object, Object> map = new HashMap<>();
		map.put("success_no", studygroup.getStudygroup_no());
		map.put("page", "insert");

		rttr.addFlashAttribute("success", map);

		return "redirect:/studyGroup/list";
	}

	@GetMapping("/insert")
	public void insert() {

	}

	@GetMapping("/detail")
	public void datail(@RequestParam("studygroup_no") int studygroup_no, @ModelAttribute("cri") Criteria cri,
			Model model) {
		model.addAttribute("studygroup", service.detail(studygroup_no));

	}

	@PostMapping("/apply")
	public String apply(@RequestParam("user_no") int user_no, @RequestParam("studygroup_no") int studygroup_no,
			Model model) {
		log.info("===============================================");
		log.info(user_no);
		log.info(studygroup_no);

		ApplyGroupMemberVO vo = new ApplyGroupMemberVO();
		vo.setUser_no(user_no);
		vo.setStudygroup_no(studygroup_no);

		service.apply(vo);

		return "redirect:/studyGroup/list";
	}

	@GetMapping(value = "/manager_list")
	public void manager_list(Model model) {
		model.addAttribute("slist", service.managerList());

	}

}
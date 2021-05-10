package org.coStudy.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.coStudy.domain.LoginVO;
import org.coStudy.domain.StudyGroupVO;
import org.coStudy.domain.StudyNoteVO;
import org.coStudy.domain.UserVO;
import org.coStudy.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/myPage")
@Log4j
public class MyPageController {
	@Autowired
	MyPageService service;
	
	@GetMapping("/userProfile")
	public void userProfile(Model model){
		log.info("*********************");
		log.info("userProfile 페이지로 이동!!");
	}
	
	@GetMapping("/userUpdate")
	public void userUpdate(Model model){
		log.info("*********************");
		log.info("userUpdate 페이지로 이동!!");
		
	}
	
	//Update 된 후에 어떤 페이지로 갈 지 정하기.
	@PostMapping("/userUpdate")
	public void userUpdate(HttpSession session,UserVO user){
		log.info("*********************");
		log.info("userUdpate Post");
		log.info("update할 user 정보:"+user);
		int re=service.updateUser(user);
		if(re>0) {
			session.setAttribute("user", user);
		}
	}
	
	@GetMapping("/categoryUpdate")
	public void categoryUpdate(){
		log.info("*********************");
		log.info("categoryUpdate 페이지로 이동!");
	}
	
	@PostMapping("/categoryUpdate")
	public void categoryUpdate(@RequestParam("category_no") String[] category_no,@RequestParam("user_no") int user_no){
		log.info("*********************");
		log.info("categoryUpdate Post");
		log.info("update할 user_no:"+user_no);
		log.info("변경할 카테고리 번호:"+category_no);
		//기존의 카테고리 정보 삭제하고 카테고리를 변경하는 로직?
	}
	
	@GetMapping("/joinGroupList")
	public void joinGroupList(HttpSession session,Model model){
		log.info("*********************");
		log.info("joinGroupList");
		UserVO user=(UserVO) session.getAttribute("user");
		int user_no=user.getUser_no();
		List<StudyGroupVO> joinGroupList=service.joinGroupList(user_no);
		model.addAttribute("joinGroupList",joinGroupList);
	}
	
	@GetMapping("/studyDiaryList")
	public void studyDiaryList(HttpSession session,Model model){
		log.info("*********************");
		log.info("studyDiaryList 보기");
		UserVO user=(UserVO) session.getAttribute("user");
		int user_no=user.getUser_no();
		List<StudyNoteVO> studyNoteList=service.studyDiaryList(user_no);
		model.addAttribute("studyNoteList",studyNoteList);
	}
	
	@GetMapping("/studyDiaryUpdate")
	public void studyDiaryUpdate(Model model){
		log.info("*********************");
		log.info("studyDiaryUpdate 페이지로 이동!!");
		
	}
	
	//study list 로 return
	@PostMapping("/studyDiaryUpdate")
	public String studyDiaryUpdate(StudyNoteVO studyNote){
		log.info("*********************");
		log.info("studyDiaryUpdate post");
		log.info("수정할 studyNote:"+studyNote);
		
		return "myPage/studyDiaryList";
	}
	
	@DeleteMapping("/studyDiaryUpdate")
	public String studyDiaryUpdate(@RequestParam("studyNote_no")int studyNote_no){
		log.info("*********************");
		log.info("studyDiary Delete");
		return "myPage/studyDiaryList";
	}
	
	@GetMapping("/studyDiary")
	public void studyDiary(){
		log.info("*********************");
		log.info("studyDiary 페이지로 이동!!");
	}
	
	@PostMapping("/studyDiary")
	public void studyDiary(StudyNoteVO studyNote){
		log.info("*********************");
		log.info("studyDiary Post");
		log.info("작성할 studyNote 내용:"+studyNote);
		int re=service.writeStudyDiary(studyNote);
	}
	
	@GetMapping("/applyQuitUser")
	public void applyQuitUser(){
		log.info("*********************");
		log.info("applyQuitUser페이지로 이동!!");
	}
	
	@PostMapping("/applyQuitUser")
	public void applyQuitUser(@RequestParam("user_no")int user_no, @RequestParam("quit_reason")String quit_reason){
		log.info("*********************");
		log.info("applyQuitUser Post");
	}
	
	@GetMapping("/applyQuitUserCheck")
	public void applyQuitUserCheck(Model model){
		log.info("*********************");
		log.info("applyQuitUserCheck 페이지로 이동");
	}
	
	@PostMapping("applyQuitUserCheck")
	public String applyQuitUser(LoginVO login){
		log.info("*********************");
		log.info("applyQuitUserCheck Post");
		log.info("회원정보 확인 user 정보:"+login);
		return "myPage/applyQuitUser";
		
	}
	
	
	
}

package org.coStudy.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.coStudy.domain.AttachFileDTO;
import org.coStudy.domain.Criteria;
import org.coStudy.domain.LoginVO;
import org.coStudy.domain.PageDTO;
import org.coStudy.domain.ScheduleVO;
import org.coStudy.domain.StudyGroupVO;
import org.coStudy.domain.StudyNoteVO;
import org.coStudy.domain.UserVO;
import org.coStudy.domain.toDoVO;
import org.coStudy.service.MyPageService;
import org.coStudy.service.UserService;
import org.coStudy.utils.UploadFileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/myPage/*")
@AllArgsConstructor
@Log4j
public class MyPageController {

	MyPageService service;
	UserService user_service;
	
	
	@GetMapping("/toDo")
	public String toDo(HttpSession session) {
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/toDo";
		}
	}
	/*
	 * @PostMapping("toDo") public void toDo2(Model model){
	 * 
	 * }
	 */

	//@RequestMapping(value = "/toDo",method=RequestMethod.POST)
	@ResponseBody
	@PostMapping("/toDo")
	public ResponseEntity<String> toDoInsert(@RequestParam("user_no") int user_no, @RequestParam("todo_content") String todo_content)throws Exception{
		
		
		toDoVO todo = new toDoVO();
		log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		log.info("??????" + todo_content);
		log.info("????????????" + user_no);

		try {
			todo.setUser_no(user_no);
			todo.setTodo_content(todo_content);

			service.toDoInsert(todo);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("ss" ,HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping("/toDoUpdate")
	public ResponseEntity<String> toDoUpdate(@RequestParam("todo_no") int todo_no)throws Exception{
		

		log.info("-------------------------");
		
		
		try {
			service.toDoUpdate(todo_no);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("ss" ,HttpStatus.OK);
	}
	@ResponseBody
	@PostMapping("/toDoDelete")
	public ResponseEntity<String> toDoDelete(@RequestParam("todo_no") int todo_no)throws Exception{
		

		log.info("-------------------------");
		
		
		try {
			service.toDoDelete(todo_no);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>("ss" ,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/commentList", produces = "application/json; charset=utf8")
	@ResponseBody
	public ArrayList<HashMap> ajax_commentList(@ModelAttribute("toDoVO") toDoVO toDoVO) throws Exception {

		ArrayList<HashMap> hmlist = new ArrayList<HashMap>();

		int user_no = toDoVO.getUser_no();
		log.info("========================================");
		log.info(user_no);
		// ?????? ????????? ??????
		List<toDoVO> commentVO = service.toDoList(user_no);

		if (commentVO.size() > 0) {
			for (int i = 0; i < commentVO.size(); i++) {

				HashMap hm = new HashMap();
				hm.put("user_no", commentVO.get(i).getUser_no());
				hm.put("todo_content", commentVO.get(i).getTodo_content());
				hm.put("todo_date", commentVO.get(i).getTodo_date());
				hm.put("complete", commentVO.get(i).getComplete());
				hm.put("todo_no", commentVO.get(i).getTodo_no());

				hmlist.add(hm);

			}

		}

		return hmlist;

	}

	@GetMapping("/userProfile")
	public String userProfile(HttpSession session) {
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/userProfile";
		}
	}

	@GetMapping("/userUpdate")
	public String userUpdate(HttpSession session) {
		log.info("*********************");
		log.info("userUpdate ???????????? ??????!!");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/userUpdate";
		}
	}

	// Update ??? ?????? ?????? ???????????? ??? ??? ?????????.
	@PostMapping("/userUpdate")
	public String userUpdate(HttpSession session, UserVO user, MultipartFile file,RedirectAttributes rttr) throws IOException, Exception {
		log.info("*********************");
		log.info("userUdpate Post");
		log.info("update??? user ??????:" + user);
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
	      if(file != null) {
	       fileName =  UploadFileUtils.fileUpload(uuid,imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath); 
	      } else {
	       fileName = root_path+File.separator+uploadPath + File.separator + "images" + File.separator + "none.png";
	      }
	      
	      user.setUser_thumbImg(File.separator + "imgUpload" + ymdPath + File.separator + "s" + File.separator + "s_" + fileName);
	      user.setUser_photo(File.separator + "imgUpload" + ymdPath + File.separator + fileName);
		int re = service.updateUser(user);
		if (re > 0) {
			log.info("*********************");
			log.info("userProfile ???????????? ??????!!");
			log.info("????????? ????????????:" + user);
			session.setAttribute("user", user);
			rttr.addFlashAttribute("mesg", "??????????????? ?????? ???????????????.");
		} else {
			rttr.addFlashAttribute("mesg", "??????????????? ????????? ?????????????????????.");
		}
		
		return "redirect:/myPage/userProfile";
	}

	@GetMapping("/categoryUpdate")
	public String categoryUpdate(HttpSession session) {
		log.info("*********************");
		log.info("categoryUpdate ???????????? ??????!");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/categoryUpdate";
		}
	}

	@PostMapping("/categoryUpdate")
	public void categoryUpdate(@RequestParam("category_no") String[] category_no,
			@RequestParam("user_no") int user_no) {
		log.info("*********************");
		log.info("categoryUpdate Post");
		log.info("update??? user_no:" + user_no);
		log.info("????????? ???????????? ??????:" + category_no);
		// ????????? ???????????? ?????? ???????????? ??????????????? ???????????? ???????
		
	}

	@GetMapping("/joinGroupList")
	public String joinGroupList(HttpSession session, Model model) {
		log.info("*********************");
		log.info("joinGroupList");
		UserVO user = (UserVO) session.getAttribute("user");
		int user_no = user.getUser_no();
		List<StudyGroupVO> joinGroupList = service.joinGroupList(user_no);
		model.addAttribute("joinGroupList", joinGroupList);
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/joinGroupList";
		}
	}

	// @GetMapping("/studyDiaryList")
	// public void studyDiaryList(HttpSession session,Model model){
	// log.info("*********************");
	// log.info("studyDiaryList ??????");
	// UserVO user=(UserVO) session.getAttribute("user");
	// int user_no=user.getUser_no();
	// List<StudyNoteVO> studyNoteList=service.studyDiaryList(user_no);
	// model.addAttribute("studyNoteList",studyNoteList);
	// }
	@GetMapping("/studyDiaryList")
	public String studyDiaryListWithPaging(HttpSession session, Model model, Criteria cri) {
		log.info("*********************");
		log.info("studyDiaryListWithPaging ??????");
		UserVO user = (UserVO) session.getAttribute("user");
		cri.setUser_no(user.getUser_no());
		int total = service.totalStudyDiaryList(user.getUser_no());
		PageDTO pageDTO = new PageDTO(cri, total);
		log.info(pageDTO);
		List<StudyNoteVO> studyNoteList = service.studyDiaryListWithPaging(cri);
		log.info("????????? ???????????? ??????=======================================");
		log.info(studyNoteList);
		model.addAttribute("studyNoteList", studyNoteList);
		model.addAttribute("pageMaker", pageDTO);
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/studyDiaryList";
		}
	}

	@GetMapping("/studyDiaryUpdate")
	public String studyDiaryUpdate(@RequestParam("studyNote_no") int studyNote_no, Model model,HttpSession session) {
		log.info("*********************");
		log.info("studyDiaryUpdate ???????????? ??????!!");
		log.info("????????? studyNote_no:" + studyNote_no);
		StudyNoteVO diary = service.studyDiaryDetail(studyNote_no);
		model.addAttribute("diary", diary);
		// update query??? ????????????
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/studyDiaryUpdate";
		}

	}

	// ??? ?????? ?????? ??????
	@GetMapping("/studyDiaryDetail")
	public String studyDiaryDetail(@RequestParam("studyNote_no") int studyNote_no, Model model,HttpSession session) {
		StudyNoteVO diary = service.studyDiaryDetail(studyNote_no);
		model.addAttribute("diary", diary);
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/studyDiaryDetail";
		}

	}

	// study list ??? return
	@PostMapping("/studyDiaryUpdate")
	public String studyDiaryUpdate(StudyNoteVO studyNote) {
		log.info("*********************");
		log.info("studyDiaryUpdate post");
		log.info("????????? studyNote:" + studyNote);
		int re = service.updateStudyDiary(studyNote);
		log.info("????????????:" + re);
		return "redirect:studyDiaryList";
	}

	@GetMapping("/studyDiaryDelete")
	public String studyDiaryDelete(@RequestParam("studyNote_no") int studyNote_no,HttpSession session) {
		log.info("*********************");
		log.info("studyDiary Delete");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}
		log.info("????????? studyNote_no:" + studyNote_no);
		int re = service.deleteStudyDiary(studyNote_no);
		log.info("?????? ??????:" + re);
		return "redirect:studyDiaryList";
	}

	@GetMapping("/studyDiary")
	public String studyDiary(HttpSession session) {
		log.info("*********************");
		log.info("studyDiary ???????????? ??????!!");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/studyDiary";
		}
	}

	

	@PostMapping("/studyDiary")
	public String studyDiary(HttpSession session,StudyNoteVO studyNote,MultipartFile file,RedirectAttributes rttr)throws IOException, Exception {
		log.info("*********************");
		log.info("studyDiary Post");
		log.info("????????? studyNote ??????:" + studyNote);
		log.info("???????????? ??????:"+file);
		   	  String root_path = session.getServletContext().getRealPath("/");  
		      String uploadPath = "resources/";
		      String filename = file.getOriginalFilename();
		      log.info("root path:"+root_path);
		      log.info("upload path:"+uploadPath);
		      log.info("file name:"+filename);
		      AttachFileDTO attachDTO=new AttachFileDTO();
		      String imgUploadPath = root_path+uploadPath + File.separator + "imgUpload";
		      String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
		      String fileName = null;
		      UUID uuid = UUID.randomUUID();
			String uploadFileName=file.getOriginalFilename();
			uploadFileName=uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("only file name:"+uploadFileName);
			attachDTO.setFileName(uploadFileName);
			attachDTO.setUuid(uuid.toString());
			attachDTO.setUploadPath(ymdPath);
		      if(file != null) {
		    	log.info("?????? ??????**********************************");
		       fileName =  UploadFileUtils.fileUpload(uuid,imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath); 
		       studyNote.setStudyNote_file(File.separator + "imgUpload" + ymdPath + File.separator + fileName);
		       studyNote.setStudyNote_fileName(uploadFileName);
		       studyNote.setStudyNote_uuid(uuid.toString());
		       studyNote.setStudyNote_uploadPath("imgUpload"+File.separator+ymdPath);
		      }
			  int re = service.writeStudyDiary(studyNote);
			log.info("?????? ????????? AttachDTO ?????????=========================");
			log.info(attachDTO);
		return "redirect:/myPage/studyDiaryList";
	}

	@GetMapping("/applyQuitUser")
	public String applyQuitUser(HttpSession session) {
		log.info("*********************");
		log.info("applyQuitUser???????????? ??????!!");
		log.info("studyDiary ???????????? ??????!!");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/applyQuitUser";
		}
	}

	
	@GetMapping("/applyQuitUserCheck")
	public String applyQuitUserCheck(HttpSession session) {
		log.info("*********************");
		log.info("applyQuitUserCheck ???????????? ??????");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/applyQuitUserCheck";
		}
	}

	@PostMapping("applyQuitUserCheck")
	public String applyQuitUser(LoginVO login) {
		log.info("*********************");
		log.info("applyQuitUserCheck Post");
		log.info("???????????? ?????? user ??????:" + login);
		return "redirect:/myPage/applyQuitUser";

	}

	@GetMapping("/scheduleList")
	public String scheduleList(HttpSession session) {
		log.info("*********************");
		log.info("scheduleList ???????????? ??????");
		if(session.getAttribute("user")==null){
			return "redirect:/user/login";
		}else{
			return "myPage/scheduleList";
		}
	}

	@RequestMapping(value="/scheduleSave" ,method=RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String scheduleSave(@RequestParam("jsondata") String jsondata,HttpSession session){
		log.info("*********************");
		log.info("scheduleSave ???");
		log.info(jsondata);
		JSONParser parser=new JSONParser();
		
		JSONArray scheduleObj=null;
		try {
			scheduleObj = (JSONArray)parser.parse(jsondata);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserVO user=(UserVO)session.getAttribute("user");
		int user_no=user.getUser_no();
		
		List<ScheduleVO> list=new ArrayList<ScheduleVO>();
		service.scheduleDeleteAll(user_no);
		for (Object s : scheduleObj) {
			log.info(s);
			JSONObject tmp=(JSONObject)s;
//			int schedule_no=Integer.parseInt((String)tmp.get("schedule_no"));
			String title=(String)tmp.get("title");
			Boolean allday=(Boolean)tmp.get("allday");
			String start=(String)tmp.get("start");
			String end=(String)tmp.get("end");
			log.info("title==>"+title);
			log.info("allday==>"+allday);
			log.info("start==>"+start);
			log.info("end==>"+end);
			ScheduleVO schedule=new ScheduleVO();
//			schedule.setSchedule_no(schedule_no);
			schedule.setUser_no(user_no);
			schedule.setTitle(title);
			schedule.setSchedule_start(start);
			schedule.setSchedule_end(end);
			schedule.setAllday(allday);
			list.add(schedule);
			

			service.scheduleRegister(schedule);
		}
		return "????????? ?????????????????????.";
	}
	
	@PostMapping("/scheduleAdd")
	@ResponseBody
	public void scheduleAdd(@RequestParam("jsondata") String jsondata,HttpSession session){
		log.info("*********************");
		log.info("scheduleAdd ???");
		log.info(jsondata);
		JSONParser parser=new JSONParser();
		
		JSONObject scheduleObj=null;
		try {
			scheduleObj = (JSONObject)parser.parse(jsondata);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserVO user=(UserVO)session.getAttribute("user");
		int user_no=user.getUser_no();
//			int schedule_no=Integer.parseInt((String)scheduleObj.get("schedule_no"));
			String title=(String)scheduleObj.get("title");
			Boolean allday=(Boolean)scheduleObj.get("allday");
			String start=(String)scheduleObj.get("start");
			String end=(String)scheduleObj.get("end");
			log.info("title==>"+title);
			log.info("allday==>"+allday);
			log.info("start==>"+start);
			log.info("end==>"+end);
			ScheduleVO schedule=new ScheduleVO();
			schedule.setUser_no(user_no);
			schedule.setTitle(title);
			schedule.setSchedule_start(start);
			schedule.setSchedule_end(end);
			schedule.setAllday(allday);
		
			service.scheduleRegister(schedule);
		
		
	}
	
	@RequestMapping(value="/scheduleDelete" ,method=RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String scheduleDelete(@RequestParam("schedule_no") int schedule_no,HttpSession session){
		log.info("*********************");
		log.info("scheduleDelete ???");
		log.info("????????? ?????? schedule_no:"+schedule_no);
		int re=service.scheduleDelete(schedule_no);
		String mesg="?????? ??????";
		if(re!=0){
			mesg="??????  ??????";
		}
		return mesg;
		
	}
	
		@PostMapping("/scheduleLoad")
		@ResponseBody
		public List<ScheduleVO> scheduleLoad(HttpSession session){
			log.info("????????? list");
			UserVO user=(UserVO)session.getAttribute("user");
			int user_no=user.getUser_no();
			
			List<ScheduleVO> list=service.scheduleList(user_no);
			log.info(list);
			return list;
		}
		
		@PostMapping(value="/quitUser")
		public String applyQuitUser(RedirectAttributes rttr,@RequestParam(value="user_no",required=false)int user_no,@RequestParam(value="withdraw_reason", required=false) String withdraw_reason){
			log.info("applyQuitUser ?????? ??? ????????????");
			log.info(user_no);
			Map<String, Object> map=new HashMap<>();
			map.put("user_no", user_no);
			map.put("withdraw_reason", withdraw_reason);
			int re=service.quitUser(map);
			log.info("re ???:"+re);
			String mesg="???????????? ?????? ???????????????.";
			if(re==0){
				mesg="???????????? ?????? ??????";
			}
			rttr.addAttribute("mesg", mesg);
			return "redirect:/user/logout";
		}
}

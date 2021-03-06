package org.coStudy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.coStudy.domain.Criteria;
import org.coStudy.domain.ScheduleVO;
import org.coStudy.domain.StudyGroupVO;
import org.coStudy.domain.StudyNoteVO;
import org.coStudy.domain.UserVO;
import org.coStudy.domain.toDoVO;
import org.coStudy.mapper.MyPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImple implements MyPageService{
	@Autowired
	MyPageMapper mapper;
	
	@Override
	public int updateUser(UserVO user) {
		return mapper.updateUser(user);
	}
	@Override
	public int writeStudyDiary(StudyNoteVO studyNote) {
		return mapper.writeStudyDiary(studyNote);
	}
	@Override
	public List<StudyNoteVO> studyDiaryList(int user_no){
		return mapper.studyDiaryList(user_no);		
	}
	
	//참여중인 스터디 그룹 조회
	@Override
	public List<StudyGroupVO> joinGroupList(int user_no){
		return mapper.joinGroupList(user_no);
	}
	@Override
	public StudyNoteVO studyDiaryDetail(int studyNote_no) {
		// TODO Auto-generated method stub
		return mapper.studyDiaryDetail(studyNote_no);
	}
	@Override
	public int updateStudyDiary(StudyNoteVO diary) {
		// TODO Auto-generated method stub
		return mapper.updateStudyDiary(diary);
	}
	@Override
	public int deleteStudyDiary(int studyNote_no) {
		// TODO Auto-generated method stub
		return mapper.deleteStudyDiary(studyNote_no);
	}
	@Override
	public List<StudyNoteVO> studyDiaryListWithPaging(Criteria cri) {
		// TODO Auto-generated method stub
		return mapper.studyDiaryListWithPaging(cri);
	}
	@Override
	public int totalStudyDiaryList(int user_no) {
		// TODO Auto-generated method stub
		return mapper.totalStudyDiaryList(user_no);
	}
	@Override
	public int toDoInsert(toDoVO todo) {
		// TODO Auto-generated method stub
		return mapper.toDoInsert(todo);
	}
	@Override
	public List<toDoVO> toDoList(int user_no) {
		// TODO Auto-generated method stub
		return mapper.toDoList(user_no);
	}	
	@Override	
	public int scheduleRegister(ScheduleVO scheduel) {
		// TODO Auto-generated method stub
		return mapper.scheduleRegister(scheduel);
	}
	@Override
	public List<ScheduleVO> scheduleList(int user_no) {
		// TODO Auto-generated method stub
		return mapper.scheduleList(user_no);
	}
	@Override
	public int toDoUpdate(int todo_no) {
		// TODO Auto-generated method stub
		return mapper.toDoUpdate(todo_no);
	}
	@Override
	public int toDoDelete(int todo_no) {
		// TODO Auto-generated method stub
		return mapper.toDoDelete(todo_no);
	}
	@Override
	public int scheduleDelete(int schedule_no) {
		// TODO Auto-generated method stub
		return mapper.scheduleDelete(schedule_no);
	}
	@Override
	public int scheduleDeleteAll(int user_no) {
		// TODO Auto-generated method stub
		return mapper.scheduleDeleteAll(user_no);
	}
	@Override
	public int quitUser(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.quitUser(map);
	}

}

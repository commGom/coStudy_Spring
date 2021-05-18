package org.coStudy.service;

import java.util.List;

import org.coStudy.domain.StudyGroupVO;
import org.coStudy.domain.StudyNoteVO;
import org.coStudy.domain.UserVO;
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
}

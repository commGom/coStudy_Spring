package org.coStudy.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.coStudy.domain.ApplyGroupMemberVO;
import org.coStudy.domain.Search;
import org.coStudy.domain.StudyGroupVO;

public interface StudyGroupService {
/*	   int insertStudyGroup(StudyGroupVO studyGroup);
	   StudyGroupVO studyGroupDetail(int studygroup_no);
	   int countstudyGroupList();
	   int insertApplyGroupStudy(ApplyGroupMemberVO applyGroupMember);
	   List<StudyGroupVO> studyGroupListInfo();
	   int getGroupNo(String studygroup_name);
*/	   
	   public List<StudyGroupVO> list(Search search) throws Exception;
	   public void insert(StudyGroupVO studygroup) throws Exception;
	   public StudyGroupVO detail(int studygroup_no);
}

package com.bpic.system.service;

import com.bpic.system.domain.Attachment;

import java.util.List;


public interface AttachmentService {

	public int saveMuti(List<Attachment> uploadList);

	int save(List<Attachment> uploadList);

	Boolean insert(Attachment attachment);

	Boolean updateById(Attachment attachment);

	Boolean updateByattachmentId(Attachment attachment);
	
	Boolean updateIsDeleteById(Long id);

	Attachment selectById(Long id);

	List<Attachment> findFileByTableAndHostId(String category, Long hostId);

	List<Attachment> selectByhostId(String hostId);

	List<Attachment> findImgListByTableAndHostId(List<Integer> list, Long hostId);

	Attachment selectContractNum(Long id);

	int dele(String id);
	
	Attachment selectByNoteId(String id);
}

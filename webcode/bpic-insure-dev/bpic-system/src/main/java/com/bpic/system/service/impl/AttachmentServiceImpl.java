package com.bpic.system.service.impl;


import com.bpic.system.domain.Attachment;
import com.bpic.system.mapper.AttachmentMapper;
import com.bpic.system.service.AttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	AttachmentMapper attachmentMapper;
	
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);
	@Override
	public int save(List<Attachment> uploadList) {
		// TODO Auto-generated method stub
		int i=0;
		if(uploadList!=null && uploadList.size()>0){
			for(Attachment upload :uploadList){
				if(upload.getHostId()!=null && upload.getCategory() != null){
					attachmentMapper.updateIsDeleteByHostIdAndCategory(upload);
		    	}
				attachmentMapper.insert(upload);
				i++;
			}
		}
		return i;
	}

	@Override
	public int saveMuti(List<Attachment> uploadList) {
		// TODO Auto-generated method stub
		int i=0;
		if(uploadList!=null && uploadList.size()>0){
			for(Attachment upload :uploadList){
				attachmentMapper.insert(upload);
				i++;
			}
		}
		return i;
	}

	@Override
	public Boolean insert(Attachment attachment) {
		// TODO Auto-generated method stub
		Boolean flag=false;
		if(attachment.getHostId()!=null && attachment.getCategory() != null){
			attachmentMapper.updateIsDeleteByHostIdAndCategory(attachment);
    	}
		int ins=attachmentMapper.insert(attachment);
		if(ins>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Boolean updateById(Attachment attachment) {
		// TODO Auto-generated method stub
		Boolean flag=false;
		if(attachment.getHostId()!=null && attachment.getCategory() != null){
			attachmentMapper.updateIsDeleteByHostIdAndCategory(attachment);
    	}
		int ins=attachmentMapper.updateById(attachment);
		if(ins>0){
			flag=true;
		}
		return flag;
	}
	
	
	
	@Override
	public Boolean updateByattachmentId(Attachment attachment) {
		
		Boolean flag=false;
		
		int ins=attachmentMapper.updateById(attachment);
		if(ins>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Boolean updateIsDeleteById(Long id) {
		// TODO Auto-generated method stub
		Boolean flag=false;
		//int ins=attachmentMapper.deleteById(id);
		int ins=attachmentMapper.updateIsDeleteById(id);
		if(ins>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Attachment selectById(Long id) {
		
		return attachmentMapper.selectByIdAndIsDelete(id);
	}

	@Override
	public List<Attachment> findFileByTableAndHostId(String category, Long hostId) {
		
		return attachmentMapper.findByTableAndHostId(category,hostId);
	}
	
	

	@Override
	public List<Attachment> findImgListByTableAndHostId(List<Integer> list, Long hostId) {
		Map<String,Object> map=new HashMap<>();
		map.put("list", list);
		map.put("hostId", hostId);
		List<Attachment> findImgListByTableAndHostId = attachmentMapper.findImgListByTableAndHostId(map);
		return findImgListByTableAndHostId;
	}

	@Override
	public List<Attachment> selectByhostId(String hostId) {
		return attachmentMapper.selectByhostId(hostId);
	}

	@Override
	public Attachment selectContractNum(Long id) {
		return attachmentMapper.selectContractNum(id);
	}

	@Override
	public int dele(String id) {
		attachmentMapper.dele(id);
		return 0;
	}
	
	@Override
	public Attachment selectByNoteId(String id) {
		return attachmentMapper.selectByNoteId(id);
	}



}

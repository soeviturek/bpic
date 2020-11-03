package com.bpic.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.bpic.system.domain.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
    Integer insert(Attachment record);
    
	Attachment selectByIdAndIsDelete(Long id);
    
    List<Attachment> findByTableAndHostId(@Param("category") String category, @Param("hostId") Long hostId);

    @Update("update attachment set is_delete=0 where id=#{id}")
	int updateIsDeleteById(Long id);

    @Update("update attachment set is_delete=0 where host_id=#{hostId} and category =#{category} and is_delete=1")
   	int updateIsDeleteByHostIdAndCategory(Attachment record);

    List<Attachment> selectByhostId(@Param("hostId") String hostId);

	List<Attachment> findImgListByTableAndHostId(Map<String, Object> map);

	Attachment selectContractNum(Long id);

    int dele(@Param("id") String id);
    
    Attachment selectByNoteId(String id);

}
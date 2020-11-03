package com.bpic.web.controller.system;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.file.FileUtils;
import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.reponse.Response;
import com.bpic.system.domain.Attachment;
import com.bpic.system.service.AttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "AttachmentApi", description = "附件接口")
@RestController
@RequestMapping(value = "/attachment")
public class AttachmentController {
	private static final Logger LOG = LoggerFactory.getLogger(AttachmentController.class);
	// public static String dictionary.getValue() = "/home/upload";

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	IdGenerator idG;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Value("${filePath:/images}")
	private String filePath;

	@Value("${serverPort:http://mosales.bpic.com.cn}")
	private String serverPort;

	@Value("${systemFileCommenPath:/home/bpic/uploadPath/upload/}")
	private String systemFileCommenPath;

	@Value("${erCoreFilePath:/home/bpic/uploadPath/upload/}")
	private String erCoreFilePath;
	/**
	 * 根据hostid查询附件
	 * 
	 * @param
	 * @return
	 */
	@GetMapping(value = "selectByHostId")
	public @ResponseBody List<Attachment> selectByHostId(@RequestParam String id) {

		return attachmentService.selectByhostId(id);

	}

	/**
	 * 附件上传
	 * 
	 * @param request
	 * @throws IOException
	 */
	@ApiOperation(value = "附件上传", notes = "上传附件", produces = "application/json;charset=UTF-8")
	@PostMapping(value = "/uploadFiles")
	@ResponseBody
	public Response<List<Attachment>> upload(HttpServletRequest request, @RequestParam(value = "fileName",required = false) String fileName)
			throws IOException {
		FileChannel fileChannel = null;
		try {


			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			String category = request.getParameter("category");
			String HostId = request.getParameter("hostId");
			Map<String, MultipartFile> fileMap = mRequest.getFileMap();

//			SFTPUtil sftp = new SFTPUtil(ftpuploadusername,ftpuploadpassword,ftpuploadhost, ftpuploadport);
//		    sftp.login();
			File file = new File(systemFileCommenPath);// 查字典表找出文件存放路径
			if (!file.exists()) {
				file.mkdirs();
			}
			List<Attachment> uploadList = new ArrayList<Attachment>(); // 上传文件之后保存到附件表
			Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
			String newName = "";
			String fileDatePath = "";
			List<Attachment> attachmentlist = null;
			if (!StringUtils.isEmpty(HostId) && !StringUtils.isEmpty(category)) {
				attachmentlist = attachmentService.findFileByTableAndHostId(category, Long.parseLong(HostId));
			}
			if (attachmentlist != null) {
				for (Attachment attachment : attachmentlist) {
					attachment.setIsDelete(0);
					attachmentService.updateById(attachment);
				}
			}

			while (it.hasNext()) {
				Attachment upload = new Attachment();
				Map.Entry<String, MultipartFile> entry = it.next();
				MultipartFile mFile = entry.getValue();
				if (mFile.getSize() != 0 && !"".equals(mFile.getName())) {
					newName = idG.generate() + "." + FileUtils.getFileExtName(mFile.getOriginalFilename()); // 上传文件存放公共路径
					fileDatePath = "/" + FileUtils.getYearWeekFolder() + "/"; // 上传文件在公共路径中详细路径
					File fileLocal = new File(systemFileCommenPath + fileDatePath);
					if (!fileLocal.exists()) {
						fileLocal.mkdirs();
					}
					// 创建附件
					upload.setId(idG.generate());
					upload.setTableName("");// 宿主表名
					if (HostId != null && !"".equals(HostId)) {
						upload.setHostId(HostId);// 宿主id
					}

					upload.setName(newName);// 附件名称
					upload.setSize(mFile.getSize());// 附件大小
					upload.setPath(serverPort+ filePath+fileDatePath + newName);// 附件路径
					upload.setDescription("");
					upload.setFileExt(FileUtils.getFileExtName(mFile.getOriginalFilename()));
					upload.setOriginalName(fileName);
					upload.setSeq(1);

					if (category != null && !"".equals(category)) {
						upload.setCategory(category);// 宿主id
					}

					upload.setUrl(filePath + newName);
					upload.setIsDelete(1);
					upload.setGmtCreate(new Date());
					// 保存附件信息
					uploadList.add(upload);

					LOG.info(systemFileCommenPath + systemFileCommenPath + newName + "本地上传开始...");
					File file1 = new File(systemFileCommenPath + systemFileCommenPath);
					if (!file1.exists()) {
						LOG.info(systemFileCommenPath + systemFileCommenPath + "开始创建文件夹...");
						file1.mkdir();
						LOG.info(systemFileCommenPath + systemFileCommenPath + "创建文件夹完成!");
					}
					fileChannel = new FileOutputStream(systemFileCommenPath + fileDatePath + newName).getChannel();
					ByteBuffer byteBuffer = ByteBuffer.wrap(mFile.getBytes());
					fileChannel.write(byteBuffer);
					LOG.info(systemFileCommenPath + systemFileCommenPath + newName + "本地上传完成!");
				    //sftp.upload(systemFileCommenPath+ filePath, newName, mFile.getInputStream());
					 write(mFile.getInputStream(), new FileOutputStream( systemFileCommenPath+fileDatePath+newName));
				}
			}
		// sftp.logout();
			attachmentService.save(uploadList);

			Response<List<Attachment>> resp = new Response<List<Attachment>>(Response.SUCCESS, "上传成功");
			resp.setCode(Response.SUCCESS);
			resp.setData(uploadList);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return new Response<List<Attachment>>(Response.ERROR, "上传失败");
		} finally {
			if (null != fileChannel) {
				LOG.info("关闭fileChannel...");
				fileChannel.close();
				fileChannel = null;
			}
		}
		// return uploadList;
	}


	/**
	 * 图片上传
	 *
	 * @param request
	 * @throws IOException
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传", response = Response.class)
	@PostMapping(value = "/uploadImage")
	@ResponseBody
	public Response<List<Attachment>> uploadImage(
			@ApiParam(name = "params", value = "{category（必传）:类型,hostId(必传):代理人编号,base64StrImg(必传):图片base64编码},ismuti: 1|0") @RequestBody(required = false) Map<String, Object> params,
			HttpServletRequest request) throws IOException {
		FileChannel fileChannel = null;
//    	SFTPUtil sftp = new SFTPUtil(ftpuploadusername,ftpuploadpassword,ftpuploadhost, ftpuploadport);
//		sftp.login();
		// 前端传过来base64 图片数据
		String base64StrImg = String.valueOf(params.get("base64StrImg"));

		String ismuti = String.valueOf(params.get("ismuti"));
		String imgType = "png";
		try{
			 imgType = org.apache.commons.lang3.StringUtils.substringBefore(base64StrImg,";").substring(11);
		}catch (Exception e){
			LOG.info("base64StrImg获取imgType失败，失败原因{}",e.getMessage());
		}
	//	String imgType ="jpg";
		String hostId = String.valueOf(params.get("hostId"));

		String category = String.valueOf(params.get("category"));
		String fileName = idG.generate() + "." + ("jpeg".equals(imgType.toLowerCase())?"jpg":imgType);// 文件名称
		String fileDatepath = "/" + FileUtils.getYearWeekFolder() + "/"; // 上传文件在公共路径中详细路径
		// String imgpathstr="http://"+ftpuploadhost+":"+CommenConstant.nginxechoport
		// +"/"+CommenConstant.mediaPlay+ filepath+ fileName;//附件路劲
		String imgpathstr = serverPort +filePath+ fileDatepath + fileName;// 附件路劲
		File file = new File(systemFileCommenPath + fileDatepath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			if (!file.exists()) {
				file.mkdirs();
			}

			InputStream input = null;
			@SuppressWarnings("deprecation")
			String base64Str = URLDecoder.decode(base64StrImg.toString());
			base64Str = base64Str.replaceAll("data:image/" + imgType + ";base64,", "");
			base64Str = base64Str.replaceAll(" ", "+");
			@SuppressWarnings("restriction")
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			@SuppressWarnings("restriction")
			byte[] data = decoder.decodeBuffer(base64Str);
			input = new ByteArrayInputStream(data);
			// 上传文件之后保存到附件表
			List<Attachment> Attachmentlist = new ArrayList<>();
			Attachment attachment = new Attachment();
			// 人员头像默认为24
			attachment.setCategory(category);
			attachment.setId(idG.generate());
			attachment.setIsDelete(1);
			attachment.setGmtCreate(new Date());
			attachment.setName(fileName);
			attachment.setPath(imgpathstr);
			attachment.setUrl(fileDatepath + fileName);
			attachment.setTableName("");
			attachment.setHostId(hostId);
			Attachmentlist.add(attachment);
//         fos.flush();
//         fos.close();
//         InputStream input=new FileInputStream(file);

			LOG.info(systemFileCommenPath + fileDatepath + fileName + "本地上传开始...");
			File file1 = new File(systemFileCommenPath + fileDatepath);
			if (!file1.exists()) {
				LOG.info(systemFileCommenPath + fileDatepath + "开始创建文件夹...");
				file1.mkdir();
				LOG.info(systemFileCommenPath + fileDatepath + "创建文件夹完成!");
			}
			fileChannel = new FileOutputStream(systemFileCommenPath + fileDatepath + fileName).getChannel();
			ByteBuffer byteBuffer = ByteBuffer.wrap(data);
			fileChannel.write(byteBuffer);
			LOG.info(systemFileCommenPath + fileDatepath + fileName + "本地上传完成!");

//		    sftp.upload(dictionary.getValue()+ filepath, fileName,input);
//            sftp.logout();
			if(ismuti!=null&&"1".equals(ismuti)){
				attachmentService.saveMuti(Attachmentlist);
			}else{
				attachmentService.save(Attachmentlist);

			}
			Response<List<Attachment>> resp = new Response<List<Attachment>>(Response.SUCCESS, "上传成功");
			resp.setCode(Response.SUCCESS);
			resp.setData(Attachmentlist);
			return resp;
		} catch (IOException e) {
			e.printStackTrace();
			return new Response<List<Attachment>>(Response.ERROR, "上传失败");
		} finally {
			if (null != fileChannel) {
				LOG.info("关闭fileChannel...");
				fileChannel.close();
				fileChannel = null;
			}
		}
	}

	/**
	 * 写入数据
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void write(InputStream in, OutputStream out) throws IOException {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 新增附件信息
	 *
	 * @param attachment
	 * @return
	 */
	@ApiOperation(value = "新增附件信息", notes = "新增一个附件信息", response = Response.class)
	@PostMapping("/insert")
	public Boolean insert(
			@ApiParam(name = "attachment", value = "不能为空字段：</br>path</br>") @RequestBody Attachment attachment) {
		attachment.setId(idG.generate());
		attachment.setIsDelete(1);
		try {
			return attachmentService.insert(attachment);
		} catch (Exception e) {
			LOG.error("", e);
			throw e;
		}

	}

	/**
	 * 更新附件信息 唯一附件
	 * 
	 * @return
	 */
	@ApiOperation(value = "更新附件信息", notes = "根据id更新附件信息", response = Response.class)
	@PostMapping(value = "/update")
	public Boolean update(@ApiParam("id不可不传") @RequestBody Attachment attachment) {
		try {
			return attachmentService.updateById(attachment);
		} catch (Exception e) {
			LOG.error("", e);
			throw e;
		}
	}

	/**
	 * updatebyattachmentId
	 * 
	 * @warn(注意事项 – 可选)
	 * @param attachment
	 * @return
	 */
	@ApiOperation(value = "更新附件信息", notes = "根据id更新附件信息", response = Response.class)
	@PostMapping(value = "/updatebyattachmentId")
	public Boolean updatebyattachmentId(@ApiParam("id不可不传") @RequestBody Attachment attachment) {
		try {
			return attachmentService.updateByattachmentId(attachment);
		} catch (Exception e) {
			LOG.error("", e);
			throw e;
		}
	}

	/**
	 * 删除单个附件信息------逻辑删除 将is_delete修改成0；
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除单个附件信息", notes = "删除单个附件信息", response = Response.class)
	@PostMapping("/delete")
	public Response<Boolean> delete(@RequestParam("id") Long id) {
		Response<Boolean> resp  = new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
		try {
			Attachment attach = findById(id);
			if (attach != null) {
				deleteFile(attach.getUrl());
			}
			resp.setData(attachmentService.updateIsDeleteById(id));
			return resp;
		} catch (Exception e) {
			LOG.error("", e);
			resp.setCode(Response.ERROR);
			resp.setMsg(Response.ERROR_MESSAGE);
			return resp;
		}
	}

	/**
	 * 根据id查找附件信息
	 *
	 * @param id 附件Id
	 * @return
	 */
	@ApiOperation(value = "根据id查找附件信息", notes = "根据id查找单个附件", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "附件id", dataType = "long", paramType = "query", example = "1") })
	@GetMapping(value = "/find")
	public Attachment findById(@RequestParam("id") Long id) {
		try {
			return attachmentService.selectById(id);
		} catch (Exception e) {
			LOG.error("", e);
			throw e;
		}
	}



	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	@ApiOperation(value = "根据category和hostId查找单个附件", notes = "根据category和hostId查找单个附件", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
			@ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
	@GetMapping(value = "/filesingle/find")
	public String findFileBytypeAndHostId(@RequestParam("category") String category,
			@RequestParam("hostId") Long hostId) {
		try {
			List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
			String str = "";
			if (attachmentlist.size() == 0) {
				str = "无此图片";
			} else {
				// 存在图片时
				if (attachmentlist.size() == 1) {
					str = attachmentlist.get(0).getPath();
				}
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * 根据category附件类型和hostId关联编号显示附件
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "根据category和hostId查找附件", notes = "根据category和hostId查找附件", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
			@ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
	@GetMapping(value = "/file/find")
	public Response<List<String>> findFileByTableAndHostId(@RequestParam("category") String category,
			@RequestParam("hostId") Long hostId) {
		try {
			List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
			List<String> list = new ArrayList<String>();
			if (attachmentlist.size() == 0) {
				list.add("http://mosales.bpic.com.cn/images/a3be0b095429b562ed47eec58b65aad.jpg");
				Response<List<String>> resp = new Response<List<String>>(Response.SUCCESS, "无此图片");
				resp.setCode(Response.SUCCESS);
				resp.setData(list);
				return resp;
			} else {
				// 存在图片情况
				if (attachmentlist.size() == 1) {
					String str = attachmentlist.get(0).getPath();
					list.add(str);
				} else {
					for (Attachment attachment : attachmentlist) {
						String str = attachment.getPath();
						list.add(str);
					}
				}
				Response<List<String>> resp = new Response<List<String>>(Response.SUCCESS, "回显成功");
				resp.setCode(Response.SUCCESS);
				resp.setData(list);
				return resp;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return new Response<List<String>>(Response.ERROR, "回显失败");
		}
	}

	/**
	 * 根据category附件类型和hostId关联编号显示附件
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "根据category和hostId查找附件", notes = "根据category和hostId查找附件", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
			@ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
	@GetMapping(value = "/file/findAttachment")
	public Response<List<Attachment>> findAttachment(@RequestParam("category") String category,
			@RequestParam("hostId") Long hostId) {
		try {
			List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
			List<Attachment> list = new ArrayList<Attachment>();
			if (attachmentlist.size() == 0) {
				list.add(null);
				Response<List<Attachment>> resp = new Response<List<Attachment>>(Response.SUCCESS, "无此图片");
				resp.setCode(Response.SUCCESS);
				resp.setData(list);
				return resp;
			} else {
				// 存在图片情况
				if (attachmentlist.size() == 1) {
					list.add(attachmentlist.get(0));
				} else {
					for (Attachment attachment : attachmentlist) {
						list.add(attachment);
					}
				}
				Response<List<Attachment>> resp = new Response<List<Attachment>>(Response.SUCCESS, "回显成功");
				resp.setCode(Response.SUCCESS);
				resp.setData(list);
				return resp;
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return new Response<List<Attachment>>(Response.ERROR, "回显失败");
		}
	}


	/**
	 * 根据category附件类型和hostId关联编号显示附件
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "根据category和hostId查找附件", notes = "根据category和hostId查找附件", response = Response.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
			@ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
	@GetMapping(value = "/file/findbyparms")
	public List<Attachment> findbyparms(@RequestParam("category") String category,
			@RequestParam("hostId") Long hostId) {
		try {
			List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
			return attachmentlist;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			throw e;
		}
	}


	@ApiOperation(value = "更新关联上传文件信息", notes = "更新文件信息", response = Response.class)
	@RequestMapping(value = "/updateFileInfo", produces = {
			"application/json;charset=utf-8" }, method = RequestMethod.POST)
	public Response<String> updateFileInfo(@RequestBody List<Attachment> attachmentList) {
		try {
			for (Attachment attachment : attachmentList) {
				attachmentService.updateById(attachment);
			}
			return new Response<String>(Response.SUCCESS, "文件信息更新成功");
		} catch (Exception e) {
			return new Response<String>(Response.ERROR, "文件信息更新失败");
		}
	}

	/**
	 * 根据category附件类型和hostId关联编号显示附件(为大后台使用)
	 *
	 * @param
	 * @return
	 */
//	@ApiOperation(value = "根据category和hostId查找附件", notes = "根据category和hostId查找附件", response = Response.class)
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "category", value = "附件类型", dataType = "int", paramType = "query", example = "1"),
//			@ApiImplicitParam(name = "hostId", value = "关联编号", dataType = "long", paramType = "query", example = "1") })
//	@GetMapping(value = "/file/findbyparmsForWebManeger")
//	public List<Attachment> findbyparmsForWebManeger(@RequestParam("category") String category,
//			@RequestParam("hostId") Long hostId) {
//		try {
//			List<Attachment> attachmentlist = attachmentService.findFileByTableAndHostId(category, hostId);
//			Dictionary uploadPath = dictionaryService.findByCode("backPath_webManage");
//			if (attachmentlist != null && attachmentlist.size() > 0) {
//				for (Attachment attachment : attachmentlist) {
//					attachment.setPath(uploadPath.getValue() + "/" + CommenConstant.mediaPlay + attachment.getUrl());
//				}
//			}
//			return attachmentlist;
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error(e.getMessage());
//			throw e;
//		}
//	}



}

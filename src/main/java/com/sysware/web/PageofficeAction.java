package com.sysware.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.sysware.util.FileUpload;
import com.sysware.util.PropertiesUtil;
import com.sysware.vo.FileProperty;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;

@Controller
@RequestMapping("/pageoffice")
public class PageofficeAction {

	// 需要配置的服务器地址
	private static String keServer = "http://test.product.sysware.com.cn/ke";
	private static String testKeServer = "http://127.0.0.1:8080/maven-springmvc/";
	private static String fileServerUpload = "http://dev.product.sysware.com.cn/fileserver/upload/";
	private static String fileServerDownload = "http://dev.product.sysware.com.cn/fileserver/download/?ID=";
	private static String fileInfoUrl = "http://dev.product.sysware.com.cn/fileserver/fileService/file/";
	private static String suffix = ".action";
	private static String saveMethod = "/pageoffice/saveParam";
	 
	
	@RequestMapping("/PropertiesUtil")
	public String PropertiesUtil( String key,HttpServletRequest request, Map<String, Object> map) {
		String property = PropertiesUtil.getProperty(key);
		System.out.println(property);
		request.setAttribute("property",property);
		return "test";
	}
	@RequestMapping("/editOffice")
	public String editOffice(FileProperty fileProperty, HttpServletRequest request, Map<String, Object> map) {
		
		if(fileProperty.getFileId()==null){
			request.setAttribute("message", "fileId为空");
			return "error/401";
		}
		if(fileProperty.getLoginName()==null){
			request.setAttribute("message", "未指定操作人");
			return "error/401";
		}
		// 打开word
		OpenModeType modelType = OpenModeType.docAdmin;
		
		String fileName = fileProperty.getFileName();
		if(fileName == null){
			RestTemplate template = new RestTemplate();
			String url  = fileInfoUrl +fileProperty.getFileId()+suffix;
			String forObject = template.getForObject(url, String.class);
			JSONObject json = new JSONObject(forObject);
			fileName = json.getString("filename");
			
			if(fileName.toLowerCase().contains(".doc")){
				modelType = OpenModeType.docNormalEdit;
			}else if(fileName.toLowerCase().contains(".xls")){
				modelType = OpenModeType.xlsNormalEdit;
			}else if(fileName.toLowerCase().contains(".ppt")){
				modelType = OpenModeType.pptNormalEdit;
			}else{
//				PDF为另一种枚举格式
//				PDFOpenModeType pdfviewonly = PDFOpenModeType.pdfViewOnly;
			}
			
		}
		if (fileProperty.getOpenModeType()!=null&&"docReadOnly".equals(fileProperty.getOpenModeType())) {
			modelType = OpenModeType.docReadOnly;
		}else if (fileProperty.getOpenModeType()!=null&&"xlsNormalEdit".equals(fileProperty.getOpenModeType())){
//			modelType= OpenModeType.xlsNormalEdit;
		}
		
		
		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");// 设置服务页面
		poCtrl.addCustomToolButton("保存", "Save()", 1);// 添加自定义保存按钮
		poCtrl.addCustomToolButton("保存并关闭", "SaveAndClose()", 1);// 保存关闭按钮
//		poCtrl.addCustomToolButton("关闭", "Close()", 16);// 关闭按钮
		// poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
		try {
			poCtrl.setSaveFilePage("save?fileId=" + fileProperty.getFileId() 
				+ "&loginName=" + fileProperty.getLoginName()
				+"&fileName="+new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 设置处理文件保存的请求方法
		
			

		poCtrl.webOpen("wordStream?fileId=" + fileProperty.getFileId(), modelType, fileProperty.getLoginName());
		poCtrl.setTagId("PageOfficeCtrl1");

		request.setAttribute("fileName", fileName);
		return "editOffice";
	}
	
	@RequestMapping("/word")
	public String openWord(FileProperty fileProperty, HttpServletRequest request, Map<String, Object> map) {
		
		if(fileProperty.getFileId()==null){
			request.setAttribute("message", "fileId为空");
			return "error/401";
		}
		if(fileProperty.getLoginName()==null){
			request.setAttribute("message", "未指定操作人");
			return "error/401";
		}
	 
		String fileName = fileProperty.getFileName();
		if(fileName == null){
			RestTemplate template = new RestTemplate();
			String url  = fileInfoUrl +fileProperty.getFileId()+suffix;
			String forObject = template.getForObject(url, String.class);
			JSONObject json = new JSONObject(forObject);
			fileName = json.getString("filename");
		}
		
		
		
		PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
		poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");// 设置服务页面
		poCtrl.addCustomToolButton("保存", "Save()", 1);// 添加自定义保存按钮
		// poCtrl.addCustomToolButton("盖章","AddSeal",2);//添加自定义盖章按钮
		try {
			poCtrl.setSaveFilePage("save?fileId=" + fileProperty.getFileId() 
				+ "&loginName=" + fileProperty.getLoginName()
				+"&fileName="+new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 设置处理文件保存的请求方法
		// 打开word
		OpenModeType modelType = OpenModeType.docAdmin;
		if (fileProperty.getOpenModeType()!=null&&"docReadOnly".equals(fileProperty.getOpenModeType())) {
			modelType = OpenModeType.docReadOnly;
		}else if (fileProperty.getOpenModeType()!=null&&"xlsNormalEdit".equals(fileProperty.getOpenModeType())){
//			modelType= OpenModeType.xlsNormalEdit;
		}
			

		poCtrl.webOpen("wordStream?fileId=" + fileProperty.getFileId(), modelType, fileProperty.getLoginName());
		poCtrl.setTagId("PageOfficeCtrl1");
		request.setAttribute("fileName", fileName);
		return "word";
	}

	/**
	 * @param request
	 * @param response
	 */
	/**
	 * @param request
	 * @param response
	 */
	@RequestMapping("/save")
	public void save(String fileId, String loginName,String fileName, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(request.getCharacterEncoding()); 
		String path = request.getServletContext().getRealPath("");
		//获取文件名 
		RestTemplate template = new RestTemplate();
		String url  = fileInfoUrl +fileId+suffix;
		String forObject = template.getForObject(url, String.class);
		JSONObject json = new JSONObject(forObject);
		fileName = json.getString("filename");
		
		FileSaver fs = new FileSaver(request, response);
		
//		 fs.saveToFile(path + fileName);
		FileInputStream fileStream = fs.getFileStream();
		 
		JSONObject upload = FileUpload.upload(fileServerUpload, fileName, fileStream);
//		JSONObject upload = FileUpload.uploadFileImpl(fileServerUpload, path + fileName, fileName,null);
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("result", upload.toString());
		params.add("oldFileId", fileId);
		params.add("loginName", loginName);
		HttpEntity httpEntity = new HttpEntity(params, headers);

		// restTemplate.postForEntity(keServer+saveMethod, httpEntity,
		// String.class);
		restTemplate.postForEntity(testKeServer + saveMethod, httpEntity, String.class);

	
		fs.close();
		File file=new File(path+fileName);
        if(file.exists()&&file.isFile())
            file.delete();
	}

	@RequestMapping(value = "/saveParam")
	public void testParam(String oldFileId, String result, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("A:" + oldFileId);
		System.out.println("B:" + result);
	}

	@RequestMapping("/wordStream")
	public void wordStream(String fileId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		URL url = new URL(fileServerDownload+fileId);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		Map<String, List<String>> headerFields = conn.getHeaderFields();

		// 获取url中的文件名
		
		
		
		List<String> list = headerFields.get("Content-Disposition");
		String disposition = "";
		String[] split;
		String filename = "default.docx";
		if (list != null) {
			disposition = list.get(0);
		}
		//中文乱码
		
		if (disposition.length() > 1 && disposition.contains("filename=")) {
			disposition = new String(disposition.getBytes("ISO-8859-1"),"GBK");
			split = disposition.split("filename=");
			String qutoName = split[1];
			if (qutoName.length() > 2) {
				filename = qutoName.substring(1, qutoName.length() - 1);
			}
		}

		InputStream inputStream = conn.getInputStream();

		byte[] fileData = readInputStream(inputStream);
		int fileSize = fileData.length;

		response.reset();
		response.setContentType("application/octet-stream;charset=UTF-8"); // application/x-excel,
																// application/ms-powerpoint,
																// application/pdf
		response.setHeader("Content-Disposition", "attachment; filename=" + filename); // fileN应该是编码后的(utf-8)
		response.setContentLength(fileSize);
	
		OutputStream outputStream = response.getOutputStream();

		outputStream.write(fileData);

		outputStream.flush();
		outputStream.close();
		outputStream = null;

	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}

<%@ page language="java"
	import="java.util.*,com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordwriter.*"
	pageEncoding="gb2312"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
//******************************׿��PageOffice�����ʹ��*******************************
	//����PageOffice���������
// 	PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
// 	poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz"); //���б���
	
// // 	poCtrl1.setTitlebar(false); //���ر�����
// 	poCtrl1.setMenubar(false); //���ز˵���
// // 	poCtrl1.setOfficeToolbars(false);//����Office������
// // 	poCtrl1.setCustomToolbar(false);//�����Զ��幤����

// 	//���ļ�
// 	poCtrl1.webOpen("doc/template.doc", OpenModeType.docNormalEdit, "����");

// 	//��ӱ���
// 	poCtrl1.setCaption("sysware���߱༭");
// 	//����Զ��尴ť
// 	poCtrl1.addCustomToolButton("����","Save()",1);
// 	poCtrl1.setSaveFilePage("SaveFile.jsp");
// // 	poCtrl1.webOpen("doc/template.doc", OpenModeType.docReadOnly, "����");
// 	poCtrl1.setTagId("PageOfficeCtrl1"); //���б���	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>   
    <title>�޸ı�����</title>
    

	
	
  <script type="text/javascript">
        function Save() {
            document.getElementById("PageOfficeCtrl1").WebSave();
            //document.getElementById("PageOfficeCtrl1").CustomSaveResult��ȡ���Ǳ���ҳ��ķ���ֵ
            document.getElementById("PageOfficeCtrl1").Alert("����ɹ�������ֵΪ��" + document.getElementById("PageOfficeCtrl1").CustomSaveResult);
        }
 
    </script>

  </head>
  
  <body>
  	�����˱��������˵������Զ���������Office��������Ч����ÿ�������ǿ��Ե����Ŀ����Ƿ����ء�
    <div style=" width:auto; height:700px;">
    <po:PageOfficeCtrl id="PageOfficeCtrl1"></po:PageOfficeCtrl>
    </div>
  </body>
</html>

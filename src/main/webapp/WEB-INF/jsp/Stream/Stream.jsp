<%@ page language="java"
	import="java.util.*,com.zhuozhengsoft.pageoffice.*,com.zhuozhengsoft.pageoffice.wordwriter.*"
	pageEncoding="gb2312"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%
//******************************׿��PageOffice�����ʹ��*******************************
	PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
	poCtrl1.setServerPage(request.getContextPath()+"/poserver.zz"); //���б���
	//���ز˵���
	poCtrl1.setMenubar(false);
	poCtrl1.addCustomToolButton("����","Save()",1);
	poCtrl1.setSaveFilePage("SaveFile.jsp?id=1");
	//��Word�ļ�
// 	poCtrl1.webOpen("http://127.0.0.1:8081/office/getWord?filename=testEdit.docx", OpenModeType.docReadOnly, "����");
// 	poCtrl1.webOpen("http://localhost:8080/getWord2.action?filename=testEdit.docx", OpenModeType.docReadOnly, "����");
	poCtrl1.webOpen("wordStream.action?fileId=201902120953140009538792e8de85a34318a691", OpenModeType.docNormalEdit, "����");
	poCtrl1.setTagId("PageOfficeCtrl1"); //���б���	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>

</head>
<body>
    <form id="form1">
    <div style="width: auto; height: 700px;">
    <!-- *********************PageOffice����ͻ���JS����*************************** -->
    <script type="text/javascript">
        function Save() {
            document.getElementById("PageOfficeCtrl1").WebSave();
            if (document.getElementById("PageOfficeCtrl1").CustomSaveResult == "ok") {
                document.getElementById("PageOfficeCtrl1").Alert('����ɹ���');
            }
        }
    </script>
    <!-- *********************PageOffice���������*************************** -->
        <po:PageOfficeCtrl id="PageOfficeCtrl1" />
    </div>
    </form>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: chengxu
  Date: 2019/1/18
  Time: 2:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p>单文件上传</p>
<form action="upload" method="POST" enctype="multipart/form-data">
    文件：<input type="file" name="file"/>
    <input type="submit"/>
</form>
<hr/>
<p>文件下载</p>
<a href="download">下载文件</a>
<hr/>
<p>多文件上传</p>
<form method="POST" enctype="multipart/form-data" action="batch">
    <p>文件1：<input type="file" name="file"/></p>
    <p>文件2：<input type="file" name="file"/></p>
    <p><input type="submit" value="上传"/></p>
</form>
</body>
</html>

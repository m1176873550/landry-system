<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/12
  Time: 10:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>干洗店在线管理系统</title>
    <%@ include file="../../common/common.jsp" %>
    <link rel="stylesheet" href="/static/css/sys/login/login.css?v=1.0">
    <style>
        .el-form-item__error {
            left: 308px;
            top: 10px;
        }

        .is-error {
            width: 400px;
        }

        .el-input--suffix .el-input__inner {
            padding-right: 50px;
        }

        .el-button--text {
            color: #a0c4e7;
            background: 0 0;
            padding-left: 0;
            padding-right: 0;
        }

        .el-input__suffix {
            right: 0px;
            top: -16px;
            transition: all .3s;
        }

        .el-dialog .el-form-item__error {
            color: #F56C6C;
            font-size: 12px;
            line-height: 1;
            padding-top: 4px;
            position: absolute;
            top: 100%;
            left: 0px;
        }

        .el-input--suffix .el-input__inner {
            padding-right: 130px;
        }

        .el-checkbox__label {
            display: inline-block;
            padding-left: 10px;
            line-height: 19px;
            font-size: 12px;
        }

        .el-checkbox__input.is-checked + .el-checkbox__label {
            color: #808080;
        }
    </style>
</head>
<body style="min-height: 600px; max-height:1080px;min-width: 1366px;max-width: 1920px;padding: 0;margin: 0; overflow: auto;">
<div id="app" style="width: 100%;height:100%;position: relative;" v-cloak>
    <el-dialog title="忘记密码" :close-on-click-modal="false" :visible.sync="dialogFormVisible" :before-close="handleClose"
               width="500px">
        <el-form class="demo-ruleForm" :model="rememberVO" :rules='rules' label-width="100px" ref="rememberVO">
            <el-form-item label="登陆名称" prop="name">
                <el-input v-model="rememberVO.name"></el-input>
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="rememberVO.idCard"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="rememberVO.newPassword"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="rememberPassword">确 定</el-button>
        </div>
    </el-dialog>


    <img style="position: absolute;left: 0;top: 0; width: 83px; height: 152px;" src="/static/images/login/Corner1.png"/>
    <img style="position: absolute;right: 0;top: 0; width: 139px; height: 146px;" src="/static/images/login/Corner2.png"/>
    <img style="position: absolute;left: 0;bottom:0;width: 94px; height: 170px;" src="/static/images/login/Corner3.png"/>
    <img style="position: absolute;right: 0;bottom: 0; width: 75px; height: 191px;" src="/static/images/login/Corner4.png"/>


    <div class="content">

        <img class="myImg" src="/static/images/login/main.png"/>

        <div class="myForm">
            <div class="myLogo">
                <img src="/static/images/login/logo.png" class="fill">
            </div>
            <div class="myFont">
                干洗店管理系统
            </div>
            <div class="MyFormDiv">
                <el-form :model="form" :rules="rules" ref="form" class="demo-ruleForm">
                    <el-form-item prop="name">
                        <div class="maindiv" id="userNameMaindiv">
                            <div class="userName" id="userName"></div>
                            <div class="centerdiv"></div>
                            <div class="rightdiv">
                                <el-input style="padding-left: 10px" type="userName" v-model="form.name" clearable
                                          @keyup.enter.native="handleFilter"
                                          autocomplete="off" placeholder="请输入账号" @focus="change()" @blur="replace()">

                                </el-input>
                            </div>
                        </div>
                    </el-form-item>

                    <el-form-item prop="password" style="position:absolute;top: 64px">
                        <div class="maindiv" id="passwordMaindiv">
                            <div class="password" id="password"></div>
                            <div class="centerdiv"></div>
                            <div class="rightdiv">
                                <el-input @keyup.enter.native="handleFilter" style="padding-left: 10px" clearable
                                          type="password" v-model="form.password"
                                          autocomplete="off" placeholder="请输入密码" @focus="change1()"
                                          @blur="replace1()">

                                </el-input>
                            </div>
                            <div style="position:absolute;top:44px;left: 245px;font-size: 14px;" id="myRemember">
                                <el-button type="text" @click="rememberClick" size="small" stype="font-size: 14px;">忘记密码</el-button>
                            </div>
                            <div style="position:absolute;top:44px;left: 25px">
<%--                                <el-button type="text" @click="rememberClick" size="small" stype="font-size: 14px;">注册</el-button>--%>
                            </div>
                        </div>
                    </el-form-item>

                    <el-form-item style="position:absolute;bottom: -20px ">
                        <el-button round type="primary" @click="submitForm('form')"
                                   class="MyLoginButton">
                            登 录
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/login/login.js?v=1.0"></script>
<script>
    var context = "${pageContext.request.contextPath}";
</script>
</html>

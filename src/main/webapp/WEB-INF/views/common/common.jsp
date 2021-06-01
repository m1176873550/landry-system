<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<script src="${pageContext.request.contextPath}/static/js/common/http_cdn.jsdelivr.net_npm_vue@2.6.11_dist_vue.js"></script>
<!-- 引入样式 -->
<%--<link rel="stylesheet" href="https://unpkg.com/element-ui@2.13.0/lib/theme-chalk/index.css">--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/element-ui/index.css">
<!-- 引入组件库 -->
<%--<script src="https://unpkg.com/element-ui@2.13.0/lib/index.js"></script>--%>
<script src="${pageContext.request.contextPath}/static/js/common/http_unpkg.com_element-ui@2.13.0_lib_index.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/common/axios.min.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/sys/common.css">
<script>
    var context = "${pageContext.request.contextPath}";
    var IS_ADMIN = "isAdmin";                   // 是否为管理员
    var CURRENT_USER_ID = 'CURRENT_USER_ID';    // 当前登录用户
    var ASC = "ascending";                      // 顺序
    var DESC = "descending";                    // 逆序
    var PRINTTIMES = "printTimes";                    // 逆序
    var BUTTON_CODES="BUTTON_CODES";            //按钮编码集合
    var ON_MSG ='onMsg';

    loadBtnCodes();
    // loginEmail();
    // checkLogin();
    // setDateFormatFun();

    // function changWeight() {
    //    /* if (document.getElementById("myMain").clientWidth > 1366 && document.getElementById("myMain").clientWidth < 1920) {
    //         document.getElementById("elMain").style.maxWidth = document.getElementById("myMain").clientWidth - 232;
    //     }
    //     if (document.getElementById("myMain").clientWidth > 1366 && document.getElementById("myMain").clientWidth < 1920) {
    //         document.getElementById("elMain").style.maxWidth = document.getElementById("myMain").clientWidth - 232;
    //     }
    //     console.log(document.getElementById("myMain").clientWidth);*/
    // }
    //
    // function changHeight() {
    //     if (document.body.clientHeight > 768 && document.body.clientHeight < 1080) {
    //
    //         document.getElementById("myMenu").style.maxHeight = document.body.clientHeight - 80;
    //         document.getElementById("myMain").style.maxHeight = document.body.clientHeight - 80;
    //         document.getElementById("iframe-page").style.maxHeight = document.body.clientHeight - 80;
    //     }
    //
    //     if (document.body.clientHeight < 768&&document.body.clientHeight>607) {
    //         document.getElementById("myMenu").style.minHeight = document.body.clientHeight - 80;
    //         document.getElementById("myMain").style.minHeight = document.body.clientHeight - 80;
    //         document.getElementById("iframe-page").style.maxHeight = document.body.clientHeight - 80;
    //     }
    //
    //     if (document.body.clientHeight<607){
    //         document.getElementById("myMenu").style.minHeight = 527;
    //         document.getElementById("myMain").style.minHeight = 527;
    //         document.getElementById("iframe-page").style.maxHeight = 527;
    //     }
    // }
    //
    // function checkBtn(code) {              // 检查按钮编码
    //     var btnCodesStr = localStorage.getItem(BTN_CODES);
    //     var btnCodes = btnCodesStr === null ? [] : JSON.parse(btnCodesStr);
    //     for (var t of btnCodes) {
    //         if (t === code) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }
    // function checkEnCode(code) {          // 检查菜单编码
    //     var enCodesStr = localStorage.getItem(EN_CODES);
    //     var enCodes = enCodesStr === null ? [] : JSON.parse(enCodesStr);
    //     for (var t of enCodes) {
    //         if (t === code) {
    //             return;
    //         }
    //     }
    //     window.location.href="/view/capital/list.htm";
    // }
    function loadBtnCodes() {
        var _this = this;
        var data = {
            method: 'get',
            url: context + '/roleAndPermission/getPermissionButton',
        };
        axios(data).then(function (rs) {
            // if (rs.data===403){
            //     window.parent.location.href="/view/login/login.htm";
            // }
            var data = rs.data;
            if (data.status === 1) {
                localStorage.setItem(BUTTON_CODES, JSON.stringify(data.data));
            } else {
                _this.$message.error(data.msg);
            }
        }).catch(function (error) {
            console.log("loadBtnCodes error");
        })
    }

    // function loadEnCodes() {
    //     if (!getQueryVariable("userId")) {
    //         var data = {
    //             method: 'get',
    //             url: context + '/permissions/enCodes'
    //         };
    //         axios(data).then(function (result) {
    //             var data = result.data;
    //             if (data.status === 1) {
    //                 localStorage.setItem(EN_CODES, JSON.stringify(data.data));
    //             } else {
    //                 localStorage.setItem(EN_CODES, JSON.stringify([]));
    //             }
    //         });
    //     }
    // }
    // function checkLogin() {
    //     if (!getQueryVariable("userId")){
    //         var data = {
    //             method: 'get',
    //             url: context + '/user/check/login'
    //         };
    //         axios(data).then(function (result) {
    //             var data = result.data;
    //             if (data.status === 1) {
    //             } else {
    //                 window.parent.location.href=context+'/view/login/login.htm';
    //             }
    //         });
    //     }
    // }
    // function getQueryVariable(variable)         // 获取url参数
    // {
    //     var query = window.location.search.substring(1);
    //     var vars = query.split("&");
    //     for (var i = 0;i < vars.length; i++) {
    //         var pair = vars[i].split("=");
    //         if(pair[0] === variable){return pair[1];}
    //     }
    //     return false;
    // }
    // function loginEmail() {
    //     var userId=getQueryVariable("userId");
    //     if (userId){
    //         var data = {
    //             method: 'get',
    //             url: context + '/user/login?userId='+userId,
    //         };
    //         axios(data).then(function (result) {
    //             var data = result.data;
    //             if (data.status === 1) {
    //
    //             } else {
    //                 window.location.href=context+'/view/login/login.htm';
    //             }
    //         });
    //     }
    // }
    // function valivalue(val) {
    //     return val === '' ? null : val;
    // }
    // function setCheckedItem(val) {    // 设置左侧菜单栏选中
    //     if (window.checkedItem) {
    //         window.checkedItem(val);
    //     }
    // }


    // function setDateFormatFun() {
    //     Date.prototype.format = function(fmt) {
    //         var o = {
    //             "M+" : this.getMonth()+1,                 //月份
    //             "d+" : this.getDate(),                    //日
    //             "h+" : this.getHours(),                   //小时
    //             "m+" : this.getMinutes(),                 //分
    //             "s+" : this.getSeconds(),                 //秒
    //             "q+" : Math.floor((this.getMonth()+3)/3), //季度
    //             "S"  : this.getMilliseconds()             //毫秒
    //         };
    //         if(/(y+)/.test(fmt)) {
    //             fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    //         }
    //         for(var k in o) {
    //             if(new RegExp("("+ k +")").test(fmt)){
    //                 fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    //             }
    //         }
    //         return fmt;
    //     };
    // }
    // function navigate() {
    //     var query=getQueryVariable("id");
    //     if (query){
    //         var data = {
    //             method: 'get',
    //             url: '/permissions/parent_name_by_id',
    //             params: {
    //                 id: query,
    //             }
    //         };
    //         axios(data).then(function (result) {
    //             var data = result.data;
    //             if (data.status === 1) {
    //                 window.localStorage.setItem("name",data.data.name);
    //                 window.localStorage.setItem("parentName",data.data.parent);
    //             } else {
    //                 window.location.href=context+'/view/login/login.htm';
    //             }
    //         });
    //     }
    // }
</script>

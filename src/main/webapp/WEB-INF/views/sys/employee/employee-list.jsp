<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%;overflow: hidden!important;">
<head>
    <title>职员列表</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/employee/employee_list.css?v=1.0">
</head>
<body>

<div id="employeeList" style="height: 97%">
    <%--    导航--%>
    <%--    <div>--%>
    <%--        <el-breadcrumb separator-class="el-icon-arrow-right" v-cloak>--%>
    <%--            <el-breadcrumb-item><a href="/static/html/iframe-default.jsp">首页</a></el-breadcrumb-item>--%>
    <%--            <el-breadcrumb-item>{{permissionParentName}}</el-breadcrumb-item>--%>
    <%--            <el-breadcrumb-item>{{permissionName}}</el-breadcrumb-item>--%>
    <%--        </el-breadcrumb>--%>
    <%--    </div>--%>
    <%--    <hr class="myHr">--%>
    <div class="query" style="margin-top:10px;padding-bottom: 10px;height: 30px">
        <el-row :gutter="5">
            <span style="position:absolute;left: 30px;width: 80px;height: 30px">
                <el-button type="primary"
                           size="small"
                           v-if="showBtn('employee_add_button')"
                           @click="showAdd"
                           class="button-model">新增
                </el-button>
           </span>
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="职员姓名"
                              v-model="query.key"
                              clearable
                              @keyup.enter.native="list"
                              @clear="list">
                        <el-button slot="append" icon="el-icon-search"
                                   @click="list"></el-button>
                    </el-input>
                </div>
            </span>
        </el-row>
    </div>

    <%--    列表--%>
    <div id="myTableDiv" class="myTableDiv">
        <el-table
                :data="employeeList"
                border
                ref="employeeListRef"
                size="small"
        <%--                :height="myTableHeight"--%>
                @sort-change="orderChange"
                style="width: 100%;border-top-width: 0;border-radius: 10px">
            <el-table-column
                    prop="laundryName"
                    label="店铺名"
            <%--                    sortable="custom"--%>
            <%--                    show-overflow-tooltip--%>
                    min-width="250">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="fullName"
            <%--                    show-overflow-tooltip--%>
                    label="职员名称"
                    width=150">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="loginName"
            <%--                    show-overflow-tooltip--%>
                    label="登陆名称"
                    width=150">
            </el-table-column>
            <%--            <el-table-column--%>
            <%--                    prop="isAdmin"--%>
            <%--                    label="管理员"--%>
            <%--                    width="130"--%>
            <%--&lt;%&ndash;                    show-overflow-tooltip&ndash;%&gt;--%>
            <%--            >--%>
            </el-table-column>
            <el-table-column
                    prop="phone"
            <%--                    show-overflow-tooltip--%>
                    label="手机号"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="idCard"
                    label="身份证号"
            <%--                    show-overflow-tooltip--%>
                    width="190"
            <%--                    :formatter="typeFormatter"--%>
            >
            </el-table-column>
            <el-table-column
                    prop="address"
                    label="地址"
            <%--                    show-overflow-tooltip--%>
                    min-width="250"
            >
            </el-table-column>
            <el-table-column
                    prop="updatedAt"
                    sortable="custom"
                    label="更新时间"
            <%--                    show-overflow-tooltip--%>
                    width="220">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="250"
                    fixed="right">
                <template slot-scope="scope">
                    <el-button type="text"
                               @click="info(scope.row)"
                               v-show="showBtn('employee_info_button')"
                    <%--                               title=""--%>
                    <%--                               style="background: url('/static/image/doIcon/manager.png') no-repeat;width: 26px;height: 26px;border: none;">--%>
                    >查看
                    </el-button>
                    <el-button type="text"
                               @click="showEdit(scope.row)"
                               v-show="showBtn('employee_edit_button')"
                    >
                        编辑
                    </el-button>
                    <el-button type="text"
                               @click="showDel(scope.row)"
                               v-show="showBtn('employee_del_button')"
                    <%--                               title="删除"--%>
                    <%--                               style="background: url('/static/image/doIcon/delete.png') no-repeat;width: 26px;height: 26px;border: none;">--%>
                    >删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="myPaginationDiv">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="query.currentPage"
                    :page-sizes="[10,20,30,40]"
                    :page-size="query.size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="query.total"
                    class="myPagination">
            </el-pagination>
        </div>
        <hr style="height: 50px;width: 100%;border: 0;margin: 0"/>
    </div>
    <%--窗口--%>
    <el-dialog :title="addOrEditTitle"
               :visible.sync="dialogTableVisible"
    <%--               class="addAndEdit"--%>
               :close-on-click-modal="false"
               :before-close="handleClose"
               :height="myTableHeight"
               v-cloak>
        <hr class="form-hr">
        <el-form :model="employee" :rules='rules' label-width="100px" ref="employeeRef" class="demo-ruleForm">
            <el-form-item label="所属店铺" prop="laundryId" v-if="isAdmin">
                <el-select v-model="employee.laundryId"
                           placeholder="请选择所属店铺"
                           :disabled="addOrEdit==='info'"
                           filterable
                           clearable>
                    <el-option
                            v-for="laundryIdOption in laundryIdOptions"
                            :key="laundryIdOption.id"
                            :label="laundryIdOption.name"
                            :value="laundryIdOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="职员姓名" prop="fullName">
                <el-input v-model="employee.fullName"
                          :disabled="addOrEdit==='info'"
                ></el-input>
            </el-form-item>
            <el-form-item label="登陆名称" prop="loginName">
                <el-input v-model="employee.loginName"
                          :disabled="addOrEdit==='info'"
                ></el-input>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone">
                <el-input v-model="employee.phone"
                          :disabled="addOrEdit==='info'"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="职员角色" prop="roleId" v-if="addOrEdit==='edit' || addOrEdit==='info' ">
                <el-select
                        v-model="employee.roleId"
                        :disabled="addOrEdit==='info'"
<%--                        @focus="getRoleIdAndName"--%>
                        filterable
                        clearable
                        multiple
                        default-first-option>
                    <el-option
                            v-for="roleIdOption in roleIdOptions"
                            :key="roleIdOption.id"
                            :label="roleIdOption.name"
                            :value="roleIdOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="employee.idCard"
                          :disabled="addOrEdit==='info'"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="地址" prop="address">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          placeholder="职员地址"
                          :disabled="addOrEdit==='info'"
                          v-model="employee.address"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="职员描述" prop="des">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          :disabled="addOrEdit==='info'"
                          placeholder="职员描述"
                          v-model="employee.des"
                          clearable>
                </el-input>
            </el-form-item>
            <%--            <el-form-item label="附件">--%>
            <%--                <el-image :src="processInstanceImage">--%>
            <%--                    style="width: 70%;height: 30%;margin-left: 10%;margin-bottom: 5%;"--%>
            <%--                    @error="handleImgError">--%>
            <%--                </el-image>--%>
            <%--            </el-form-item>--%>

        </el-form>
        <div slot="footer" class="dialog-footer" v-show="addOrEdit==='edit' || addOrEdit==='add'">
            <el-button @click="chanceClose">取 消</el-button>
            <el-button type="primary" @click="saveEdit">确 定</el-button>
        </div>
    </el-dialog>


</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/employee/employee_list.js?v=1.0"></script>
</html>

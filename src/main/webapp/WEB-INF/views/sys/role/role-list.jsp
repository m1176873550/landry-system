<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>角色列表</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/role/role_list.css?v=1.0">
</head>
<body >

<div id="role" style="height: 97%">
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
                           @click="showAdd"
                           v-if="showBtn('role_add_button')"
                           class="button-model">新增
                </el-button>
           </span>
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="店铺名"
                              v-model="query.laundryId"
                              clearable
                              @keyup.enter.native="list"
                              @clear="list">
                    </el-input>
                </div>
            </span>
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="角色名称"
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
                :data="roleList"
                border
                ref="roleListRef"
                size="small"
                @sort-change="orderChange"
                style="width: 100%;border-top-width: 0;border-radius: 10px">
            <%--            <el-table-column--%>
            <%--                    prop="laundryId"--%>
            <%--                    label="店铺ID"--%>
            <%--                    fixed--%>
            <%--                    show-overflow-tooltip--%>
            <%--                    min-width="250">--%>
            <%--            </el-table-column>--%>
            <el-table-column
                    prop="name"
                    label="角色名称"
                    show-overflow-tooltip
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="status"
                    label="状态"
                    :formatter="statusFormatter"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="des"
                    label="描述"
                    show-overflow-tooltip
                    min-width="250">
            </el-table-column>
            <el-table-column
                    prop="editor"
                    label="编辑人"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="updatedAt"
                    sortable="custom"
                    label="更新时间"
                    width="200">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="200"
                    fixed="right">
                <template slot-scope="scope">
                    <el-button type="text"
                               @click="showEdit(scope.row)"
<%--                               v-if="showBtn('role_edit_button')"--%>
                    >
                        编辑
                    </el-button>
                    <el-button type="text"
                               @click="showDel(scope.row)"
                               v-if="showBtn('role_del_button')"
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
        <el-form :model="role" :rules='rules' label-width="100px" ref="roleRef" class="demo-ruleForm">
            <el-form-item label="角色名称" prop="name">
                <el-input v-model="role.name"
                ></el-input>
            </el-form-item>
            <el-form-item label="拥有权限" prop="permissionIds" v-if="addOrEdit==='edit'">
                <el-tree
                        :data="permissions"
                        :default-expand-all="true"
                        :check-strictly="true"
<%--                        :default-expanded-keys="[2, 3]"--%>
                        :default-checked-keys="role.permissionIds"
                        :props="defaultProps"
                        highlight-current
                        show-checkbox
                        ref="tree"
                        node-key="id"
                        style="margin-top: 9px">
                </el-tree>
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-radio-group v-model="role.status">
                    <el-radio :label="1" border>启用</el-radio>
                    <el-radio :label="0" border>禁用</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="描述" prop="des">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          placeholder="描述"
                          v-model="role.des"
                          clearable>
                </el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" v-show="addOrEdit==='edit' || addOrEdit==='add'">
            <el-button @click="chanceClose">取 消</el-button>
            <el-button type="primary" @click="saveEdit">保 存</el-button>
        </div>
    </el-dialog>


</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/role/role_list.js?v=1.0"></script>
</html>

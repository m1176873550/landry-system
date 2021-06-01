<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html style="height: 97%;width: 99%;">
<head>
    <title>权限列表</title>
    <%@include file="/WEB-INF/views/common/common.jsp" %>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/sys/permission/permission_list.css">
</head>
<body >
<div id="permission" style="height: 97%;overflow: hidden ">
    <div class="query" style="margin-top:10px;padding-bottom: 10px;height: 30px">
        <el-row :gutter="5" class="row-css">
            <span style="position:absolute;left: 30px;width: 80px;height: 30px">
                <el-button type="primary" size="small"
                           @click="showAdd"
                           v-if="showBtn('permission_add_button')"
                           class="button-model">新增
                </el-button>
            </span>
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="权限名称"
                              v-model="query.key"
                              clearable
                              @keyup.enter.native="list"
                              @clear="resetList">
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
                style="width: 100%;border-top-width: 0;border-radius: 10px"
                :data="permissionList"
                ref="permissionListRef"
                row-key="id"
                border
                :default-expand-all="false"
        <%--                :height="myTableHeight"--%>
                @cell-click="clickTable"
                @sort-change="orderChange"
                :tree-props="{children: 'children'}">
            <el-table-column
                    show-overflow-tooltip
                    prop="name"
                    label="权限名称"
                    width="150">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="level"
                    label="级别"
                    sortable="custom"
                    width="150">
            </el-table-column>
            <el-table-column
                    label="状态"
                    sortable="custom"
                    prop="status"
                    show-overflow-tooltip
                    min-width="150"
                    :formatter="statusFormatter">
            </el-table-column>
            <el-table-column
                    prop="url"
                    label="页面路径"
                    min-width="300"
                    show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                    prop="des"
                    label="权限描述"
                    show-overflow-tooltip
                    min-width="250">
            </el-table-column>
            <el-table-column
                    prop="updatedAt"
                    sortable="custom"
                    show-overflow-tooltip
                    width="200"
                    sortable="custom"
                    label="更新时间">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="200"
                    fixed="right">
                <template slot-scope="scope">
                    <el-button
                            v-if="showBtn('permission_edit_button')"
                            type="text"
                            size="medium"
                            @click="showEdit(scope.$index, scope.row)">编辑
                    </el-button>
                    <el-button
                            v-if="scope.row.status === 1"
                            type="text"
                            size="medium"
                            @click="changeStatus(scope.$index, scope.row)">禁用
                    </el-button>
                    <el-button
                            v-if="scope.row.status === 0"
                            type="text"
                            size="medium"
                            @click="changeStatus(scope.$index, scope.row)">
                        启用
                    </el-button>
                    <el-button
                            v-if="showBtn('permission_del_button')"
                            type="text"
                            size="medium"
                            type="danger"
                            @click="del(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="myPaginationDiv">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="query.pageNum"
                    :page-sizes="[10,20,30,40]"
                    :page-size="query.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="query.total"
                    class="myPagination">

            </el-pagination>
        </div>
        <hr style="height: 50px;width: 100%;border: 0;margin: 0"/>
    </div>

    <!-- Form -->
        <el-dialog :title="title"
                   :visible.sync="dialogTableVisible"
                   :before-close="handleClose"
                   :close-on-click-modal="false"
                   v-cloak>
            <hr class="form-hr">
            <el-form :model="permission" :rules="rules" ref="permissionRef">
                <el-form-item label="权限名称" :label-width="formLabelWidth" prop="name">
                    <el-input v-model="permission.name" clearable autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="描述" :label-width="formLabelWidth" prop="des">
                    <el-input style="width:90%;"
                              type="textarea"
                              :rows="2"
                              placeholder="权限描述"
                              v-model="permission.des"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="排序" :label-width="formLabelWidth" prop="level" v-if="permission.type==='0'">
                    <el-input-number v-model="permission.level" :precision="0"  label="权限排序"></el-input-number>
                </el-form-item>
                <el-form-item label="功能类型" :label-width="formLabelWidth" prop="type">
                    <el-radio-group v-model="permission.type">
                        <el-radio :label="'0'" border>菜单</el-radio>
                        <el-radio :label="'1'" border>按钮</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="状态" :label-width="formLabelWidth" prop="status">
                    <el-radio-group v-model="permission.status">
                        <el-radio :label="1" border>启用</el-radio>
                        <el-radio :label="0" border>禁用</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="编码"
                              v-if="permission.type==='1'"
                              :label-width="formLabelWidth"
                              prop="code">
                    <el-input style="width:90%;"
                              placeholder="权限编码"
                              v-model="permission.code"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="路径"
                              :label-width="formLabelWidth"
                              v-if="permission.type==='0'"
                              prop="url">
                    <el-input style="width:90%;"
                              placeholder="页面路径"
                              v-model="permission.url"
                              clearable></el-input>
                </el-form-item>
                <el-form-item label="上级" :label-width="formLabelWidth" v-if="permission.type==='1'" prop="parentId">
                    <template>
                        <el-select v-model="permission.parentId"
                                   clearable
                                   placeholder="请选择上级权限">
                            <el-option
                                    v-for="item in parentIdOptions"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </template>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="chanceClose">取 消</el-button>
                <el-button type="primary" @click="addOrEdit">确 定</el-button>
            </div>
        </el-dialog>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/sys/permission/permission_list.js?v=1.0"></script>
</html>

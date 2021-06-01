<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%;">
<head>
    <title>顾客列表</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/customer/customer_list.css?v=1.0">
</head>
<body>

<div id="customer" style="height: 97%">
    <div class="query" style="margin-top:10px;padding-bottom: 10px;height: 30px;">
        <el-row :gutter="5">
            <span style="position:absolute;left: 30px;width: 80px;height: 30px;margin-top: 2px;">
                <el-button type="primary"
                           size="small"
                           @click="showAdd"
                           v-if="showBtn('customer-add-button')"
                           class="button-model">新增
                </el-button>
           </span>
            <span style="position:absolute;left: 140px;width: 80px;">
                <el-dropdown  @command="handleDropDown" v-cloak >
                  <el-button type="primary" class="select-button">
                    {{customerType}}<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="1">全部</el-dropdown-item>
                    <el-dropdown-item command="2">会员</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
            </span>
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="姓名或手机号"
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
                :data="customerList"
                border
                ref="customerListRef"
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
                    prop="fullName"
                    label="姓名"
                    show-overflow-tooltip
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="手机号"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="discount"
                    label="折扣"
                    sortable="custom"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="balance"
                    label="余额"
                    sortable="custom"
                    width="100"
                    :formatter="twoDecimalCustomerInfoFormat">
            </el-table-column>
            <el-table-column
                    prop="nic"
                    label="会员号"
                    show-overflow-tooltip
                    min-width="250">
            </el-table-column>
            <el-table-column
                    prop="address"
                    label="地址"
                    width=250">
            </el-table-column>
            <el-table-column
                    prop="des"
                    label="描述"
                    show-overflow-tooltip
                    width="250">
            </el-table-column>
            <el-table-column
                    prop="birthday"
                    sortable="custom"
                    label="生日"
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
                    width="150"
                    fixed="right">
                <template slot-scope="scope">
                    <el-button type="text"
                               @click="showEdit(scope.row)"
                               v-if="showBtn('customer-edit-button')">
                        编辑
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
        <el-form :model="customer" :rules='rules' label-width="100px" ref="customerRef" class="demo-ruleForm">
<%--            <br style="1px solid #EBEEF5;">--%>
            <%--            <el-form-item label="所属店铺" prop="laundryId" >--%>
            <%--                <el-select v-model="employee.laundryId"--%>
            <%--                           placeholder="请选择所属店铺"--%>
            <%--                           filterable--%>
            <%--                           clearable--%>
            <%--                &lt;%&ndash;                           @change="changeHandle(employee.laundryId)"&ndash;%&gt;--%>
            <%--                >--%>
            <%--                    <el-option--%>
            <%--                            v-for="laundryIdOption in laundryIdOptions"--%>
            <%--                            :key="laundryIdOption.id"--%>
            <%--                            :label="laundryIdOption.name"--%>
            <%--                            :value="laundryIdOption.id">--%>
            <%--                    </el-option>--%>
            <%--                </el-select>--%>
            <%--            </el-form-item>--%>
            <el-form-item label="姓名" prop="fullName">
                <el-input v-model="customer.fullName"
                ></el-input>
            </el-form-item>
            <div class="input-and-button">
                <el-form-item label="会员号" prop="nic">
                    <el-input v-model="customer.nic"
                              disabled
                              clearable></el-input>
                    <el-button type="primary" @click="generateNic" v-if="addOrEdit==='add'" icon="el-icon-refresh-left"></el-button>
                </el-form-item>
            </div>
            <el-form-item label="折扣" prop="discount" v-if="customer.nic!==''">
                <el-input v-model="customer.discount"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="余额" prop="balance" v-if="customer.nic!==''">
                <el-input v-model="customer.balance"
                          :disabled="addOrEdit==='edit'"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="customer.phone"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="生日" prop="birthday" :label-width="'100px'">
                <el-date-picker
                        v-model="customer.birthday"
                        align="left"
                        type="date"
                        clearable
                        placeholder="选择日期"
                        :picker-options="pickerOptions"
                        value-format="yyyy-MM-dd"
                >
                </el-date-picker>
            </el-form-item>
            <el-form-item label="地址" prop="address">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          placeholder="地址"
                          v-model="customer.address"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="des">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          placeholder="描述"
                          v-model="customer.des"
                          clearable>
                </el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" v-show="addOrEdit==='edit' || addOrEdit==='add'">
            <el-button @click="chanceClose">取 消</el-button>
            <el-button type="primary" @click="saveEdit">确 认</el-button>
        </div>
    </el-dialog>


</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/customer/customer_list.js?v=1.0"></script>
</html>

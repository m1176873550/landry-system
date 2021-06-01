<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>挂点列表</title>
    <%@ include file="../../common/common.jsp" %>
    <%--    <%@ include file="../../index.jsp" %>--%>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/hangingPoint/hanging_point_list.css?v=1.0">
</head>
<body>

<div id="hangingPoint" style="height: 97%">
    <div class="query" style="margin-top:25px;padding-bottom: 10px;height: 40px">
        <el-row :gutter="5">
            <span style="position:absolute;left: 30px;width: 80px;height: 30px;margin-top: 2px;">
                <el-button type="primary"
                           v-cloak
                           size="small"
                           @click="showAdd"
                           v-if="showBtn('hangingPoint_add_button')"
                           class="button-model">新增
                </el-button>
           </span>
            <span style="position:absolute;left: 140px;width: 80px;">
                <el-dropdown @command="handleDropDown" v-cloak>
                  <el-button type="primary" class="select-button">
                    {{hangingPointType}}<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="0">全部</el-dropdown-item>
                    <el-dropdown-item command="1">已取</el-dropdown-item>
                    <el-dropdown-item command="2">未取</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
            </span>
            <span style="position:absolute;right: 548px;width: auto;height: 40px">
                <el-input
                <%--                            style="width: auto;font-size: 12px;"--%>
                        placeholder="票单号"
                        v-model="query.orderId"
                        clearable
                        @change="list"
                        @keyup.enter.native="list">
                </el-input>
            </span>
            <span style="position:absolute;right: 322px;width: auto;height: 40px">
                <el-input
                <%--                            style="width: auto;font-size: 12px;"--%>
                        placeholder="手机号"
                        v-model="query.phone"
                        clearable
                        @change="list"
                        @keyup.enter.native="list">
                </el-input>
            </span>
            <span style="position:absolute;right: 96px;width: auto;height: 40px">
                <el-input
                <%--                            style="width: auto;font-size: 12px;"--%>
                        placeholder="挂点号"
                        v-model="query.number"
                        clearable
                        @change="list"
                        @keyup.enter.native="list">
                </el-input>
            </span>
            <span style="position:absolute;right: 20px;width: auto;height: 40px">
                <el-button
                        class="normal-button"
                        type="primary"
                        icon="el-icon-search"
                        @click="list">
                </el-button>
            </span>
            <%--                <div style="margin-top: 0;float: right" class="input-button">--%>
            <%--                    <el-input style="width: auto;font-size: 12px;"--%>
            <%--                              v-cloak--%>
            <%--                              placeholder="挂点查询"--%>
            <%--                              v-model="query.key"--%>
            <%--                              clearable--%>
            <%--                              @keyup.enter.native="list"--%>
            <%--                              @clear="list">--%>
            <%--                        <el-button slot="append" icon="el-icon-search"--%>
            <%--                                   @click="list"></el-button>--%>
            <%--                    </el-input>--%>
            <%--                </div>--%>
            </span>
        </el-row>
    </div>
    <div id="myTableDiv" class="myTableDiv">
        <el-table
                :data="hangingPointList"
                border
                ref="hangingPointListRef"
                size="small"
        <%--                        :height="myTableHeight"--%>
                @sort-change="orderChange"
                style="width: 100%;border-top-width: 0;border-radius: 10px"
                v-cloak>
            <el-table-column
                    fixed
                    prop="orderId"
                    show-overflow-tooltip
                    label="订单ID"
                    width=150">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="number"
                    label="挂点号"
                    sortable="custom"
                    show-overflow-tooltip
                    min-width="150">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="isPay"
                    show-overflow-tooltip
                    :formatter="isPayFormatter"
                    label="是否付款"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="clothType"
                    show-overflow-tooltip
                    label="衣物类型"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="price"
                    sortable="custom"
                    label="价格"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="payer"
                    show-overflow-tooltip
                    label="付款人"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    show-overflow-tooltip
                    label="手机号"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="isFinished"
                    show-overflow-tooltip
                    sortable="custom"
                    label="是否可取"
                    :formatter="isFinishedFormatter"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="beanTook"
                    label="是否被取"
                    :formatter="isBeanTookFormatter"
                    sortable="custom"
                    show-overflow-tooltip
                    min-width="150">
            </el-table-column>
            <el-table-column
                    prop="discount"
                    show-overflow-tooltip
                    sortable="custom"
                    label="折扣"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="isBack"
                    show-overflow-tooltip
                    sortable="custom"
                    label="返厂"
                    :formatter="isBackFormatter"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="refund"
                    show-overflow-tooltip
                    sortable="custom"
                    label="退款"
                    :formatter="refundFormatter"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="refundAmount"
                    show-overflow-tooltip
                    sortable="custom"
                    label="退款金额"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="editor"
                    show-overflow-tooltip
                    label="编辑人"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="des"
                    show-overflow-tooltip
                    label="描述"
                    width=150">
            </el-table-column>
            <el-table-column
                    prop="pickingTime2"
                    sortable="custom"
                    label="预计取衣时间"
                    show-overflow-tooltip
                    width="220">
            </el-table-column>
            <el-table-column
                    prop="updatedAt"
                    sortable="custom"
                    label="更新时间"
                    show-overflow-tooltip
                    width="220">
            </el-table-column>
            <el-table-column
                    label="操作"
                    width="200"
                    fixed="right">
                <template slot-scope="scope">
                    <%--                    <el-button type="text"--%>
                    <%--                               @click="showInfo(scope.row)"--%>
                    <%--                               size="small"--%>
                    <%--                    &lt;%&ndash;                               v-show="showBtn('organization_choose_manager')"&ndash;%&gt;--%>
                    <%--                    &lt;%&ndash;                               title=""&ndash;%&gt;--%>
                    <%--                    &lt;%&ndash;                               style="background: url('/static/image/doIcon/manager.png') no-repeat;width: 26px;height: 26px;border: none;">&ndash;%&gt;--%>
                    <%--                    >附件--%>
                    <%--                    </el-button>--%>
                    <el-button type="text"
                               @click="showTakeCloth(scope.row)"
                               v-if="scope.row.isFinished===1 && scope.row.beanTook===0 && showBtn('hangingPoint_takeCloth_button')"
                    <%--                               title="删除"--%>
                    <%--                               style="background: url('/static/image/doIcon/delete.png') no-repeat;width: 26px;height: 26px;border: none;">--%>
                    >取衣
                    </el-button>
                    <el-button type="text"
                               @click="showEdit(scope.row)"
                               v-if="showBtn('hangingPoint_edit_button')"
                    >编辑
                    </el-button>
                    <el-button type="text"
                               @click="showDel(scope.row)"
                               v-if="showBtn('hangingPoint_del_button')"
                    <%--                               v-show="showBtn('organization_del')"--%>
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

    <%--新增编辑--%>
    <el-dialog :title="addOrEditTitle"
               :visible.sync="saveDialogVisible"
    <%--               class="addAndEdit"--%>
               :close-on-click-modal="false"
               :before-close="addOrEditClose"
               :height="myTableHeight"
               v-cloak>
        <hr class="form-hr">
        <el-form :model="hangingPoint" :rules='rules' label-width="100px" ref="hangingPointRef" class="demo-ruleForm">
            <el-row :gutter="5" class="row-css" v-if="isCheckVip">
                <div class="main-bottom-info">
                    <div class="main-bottom-info-context-nic">
                        <el-form-item stype="width: 185px;" label="会员编号:">{{customerInfo.nic}}</el-form-item>
                    </div>
                </div>
            </el-row>
            <el-row :gutter="5" class="row-css" v-if="isCheckVip">
                <div class="main-bottom-info">
                    <div class="main-bottom-info-context">
                        <el-form-item label="姓名:">{{customerInfo.fullName}}</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="余额:">{{customerInfo.balance}}元</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="折扣:">{{customerInfo.discount}}</el-form-item>
                    </div>
                    <div class="main-bottom-info-context">
                        <el-form-item label="实收金额:">{{customerInfo.actualPayment}}元</el-form-item>
                    </div>
                </div>
            </el-row>
            <el-form-item label="是否被取"
                          prop="beanTook"
                          v-show="addOrEdit==='edit'">
                <el-select v-model="hangingPoint.beanTook"
                           placeholder="请选择是否被取"
                           disabled
                           clearable>
                    <el-option
                            v-for="isOption in isOptions"
                            :key="isOption.id"
                            :label="isOption.name"
                            :value="isOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否可取"
                          prop="isFinished"
                          v-show="addOrEdit==='edit'">
                <el-select v-model="hangingPoint.isFinished"
                           placeholder="选择是否可取"
                           clearable>
                    <el-option
                            v-for="isOption in isOptions"
                            :key="isOption.id"
                            :label="isOption.name"
                            :value="isOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <%--            <el-form-item label="挂点号" prop="number">--%>
            <%--                <el-input v-model="hangingPoint.number"--%>
            <%--                          :disabled="true"--%>
            <%--                          placeholder="自动生成">--%>
            <%--                </el-input>--%>
            <%--            </el-form-item>--%>
            <el-form-item label="衣物类型" prop="clothTypeId">
                <el-cascader
                        placeholder="选择衣物类型"
                <%--                        @visible-change="clothTypes"--%>
                        ref="clothTypeRef"
                        v-model="hangingPoint.clothTypeId"
                        :options="clothTypeOptions"
                        clearable
                        filterable
                        @change="typeChange">
                </el-cascader>
                <%--                <el-button>羽绒服</el-button>                <el-button>大衣</el-button>--%>
            </el-form-item>
            <el-form-item label="衣物颜色" prop="colorId">
                <el-select v-model="hangingPoint.colorId"
                           placeholder="选择衣物颜色"
                           filterable
                           clearable>
                    <el-option
                            v-for="colorOption in colorOptions"
                            :key="colorOption.id"
                            :label="colorOption.name"
                            :value="colorOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务类型" prop="serviceTypeId" v-show="addOrEdit==='add'">
                <el-select v-model="hangingPoint.serviceTypeId"
                           placeholder="选择服务类型"
                           multiple
                           filterable
                           clearable>
                    <el-option
                            v-for="serviceTypeOption in serviceTypeOptions"
                            :key="serviceTypeOption.id"
                            :label="serviceTypeOption.name"
                            :value="serviceTypeOption.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="衣物品牌" prop="brandId">
                <el-select v-model="hangingPoint.brandId"
                           placeholder="选择衣物品牌"
                           filterable
                           clearable>
                    <el-option
                            v-for="brandOption in brandOptions"
                            :key="brandOption.id"
                            :label="brandOption.name"
                            :value="brandOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <div class="price-input">
                <el-form-item label="金额" prop="price" v-show="addOrEdit==='add'">
                    <el-input v-model="hangingPoint.price"
                              :disabled="!showBtn('edit-price')"
                              placeholder="输入付款金额"
                    ></el-input>
                </el-form-item>
            </div>
            <el-form-item label="付款人" prop="payer" v-show="addOrEdit==='add'">
                <el-autocomplete
                        class="inline-input"
                        v-model="hangingPoint.payer"
                        :fetch-suggestions="queryNameSearch"
                        placeholder="付款人姓名"
                        clearable
                        @focus="selectAll"
                        :highlight-first-item="true"
                        select-when-unmatched
                        placement="top"
                        :trigger-on-focus="false"
                        @select="querySearchSelect"
                ></el-autocomplete>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone" v-show="addOrEdit==='add'">
                <el-autocomplete
                        class="inline-input"
                        v-model="hangingPoint.phone"
                        clearable
                        :highlight-first-item="true"
                        placement="top"
                        @focus="selectAll"
                        select-when-unmatched
                        :fetch-suggestions="queryPhoneSearch"
                        :trigger-on-focus="false"
                        placeholder="电话"
                        @select="setOrderDataName">
                </el-autocomplete>
                <el-button type="primary" @click="checkVip" style="margin-top: 2px">会员检索</el-button>
            </el-form-item>
            <el-form-item label="描述" prop="des">
                <el-input style="width:300px"
                          type="textarea"
                          :rows="2"
                          placeholder="描述"
                          v-model="hangingPoint.des"
                          clearable>
                </el-input>
            </el-form-item>
            <el-form-item label="是否返厂" prop="isBack">
                <el-select v-model="hangingPoint.isBack"
                           placeholder="选择是否返厂"
                           :disabled="!showBtn('edit-price')"
                           clearable>
                    <el-option
                            v-for="isOption in isOptions"
                            :key="isOption.id"
                            :label="isOption.name"
                            :value="isOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否退款" prop="refund">
                <el-select v-model="hangingPoint.refund"
                           placeholder="选择是否退款"
                           :disabled="!showBtn('edit-price')"
                           clearable
                           @change="isBackChange">
                    <el-option
                            v-for="isOption in isOptions"
                            :key="isOption.id"
                            :label="isOption.name"
                            :value="isOption.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="退款金额" prop="refundAmount">
                <el-input v-model="hangingPoint.refundAmount"
                          :disabled="(!showBtn('edit-price')) && isRefund"></el-input>
            </el-form-item>
            <el-form-item label="预计取衣时间"
            <%--                          :label-width="formLabelWidth"--%>
                          prop="pickingTime2"
                          v-show="addOrEdit==='edit'">
                <div id="formTime">
                    <el-date-picker
                            v-model="hangingPoint.pickingTime2"
                            align="left"
                            type="date"
                            placeholder="选择日期"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd hh:mm:ss"
                            clearable
                    <%--                        :picker-options="dateOptions"--%>
                    >
                    </el-date-picker>
                </div>
            </el-form-item>

            <%--            <el-form-item>--%>
            <%--                <el-image :src="annexUrl"--%>
            <%--                          style="width: 70%;height: 30%;margin-left: 10%;margin-bottom: 5%;"--%>
            <%--                          @error="handleImgError">--%>
            <%--                </el-image>--%>
            <%--            </el-form-item>--%>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="chanceClose">取 消</el-button>
            <el-button type="primary" @click="saveEdit(hangingPoint)">确 认</el-button>
        </div>
    </el-dialog>
    <el-dialog
            title="提示"
            :visible.sync="isPayedDialogVisible"
            width="30%"
            center
            v-cloak>
        <%--        <span>{{isPayedInformation}}</span>--%>
        <el-form :model="isPayedObj" :rules='isPayedObjRules' label-width="100px" ref="isPayedObjRef"
                 class="demo-ruleForm">
            <el-form-item label="是否付款" prop="isPay">
                <el-input v-model="isPayedObj.isPay" disabled></el-input>
            </el-form-item>
            <el-form-item label="付款金额" prop="price">
                <el-input v-model="isPayedObj.price" :disabled="isPayed"></el-input>
            </el-form-item>
            <el-form-item label="付款人" prop="payer">
                <el-input v-model="isPayedObj.payer" :disabled="isPayed"></el-input>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone">
                <el-input v-model="isPayedObj.phone" :disabled="isPayed"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click=isPayedChance>取 消</el-button>
            <el-button type="primary" @click=changeIsPay v-show="isPayedObj.isPay==='否'">付 款</el-button>
            <el-button type="primary" @click=changeBeanTook v-show="isPayedObj.isPay==='是'">取 衣</el-button>
        </span>
    </el-dialog>
</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/hangingPoint/hanging_point_list.js?v=1.0"></script>
</html>

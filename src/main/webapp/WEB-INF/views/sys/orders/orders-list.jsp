<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html style="height: 97%;width: 99%">
<head>
    <title>订单列表</title>
    <%@include file="../../common/common.jsp" %>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/sys/orders/orders.css?v=1.0">
</head>
<body >

<div id="orders" style="height: 97%">
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
            <span style="position:absolute;right: 14px;width: auto;height: 40px">
                <div style="margin-top: 0;float: right" class="input-button">
                    <el-input style="width: auto;font-size: 12px;"
                              placeholder="订单编号"
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
                :data="ordersList"
                border
                ref="ordersListRef"
                size="small"
                @sort-change="orderChange"
                style="width: 100%;border-top-width: 0;border-radius: 10px">
<%--            <el-table-column--%>
<%--                    prop="laundryName"--%>
<%--                    label="店铺名"--%>
<%--                    show-overflow-tooltip--%>
<%--                    min-width="150">--%>
<%--            </el-table-column>--%>
            <el-table-column
                    prop="ordersId"
                    label="订单号"
                    show-overflow-tooltip
                    min-width="150">
            </el-table-column>
            <el-table-column
                    prop="isFinished"
                    label="是否完成"
                    sortable="custom"
                    :formatter="isFinishedFormatter"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="isPay"
                    label="是否支付"
                    sortable="custom"
                    :formatter="isPayFormatter"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="totalPrice"
                    label="订单收款"
                    sortable="custom"
                    width="130"
                    :formatter="twoDecimalCustomerInfoFormat">
            </el-table-column>
            <el-table-column
                    prop="payer"
                    label="付款人"
                    show-overflow-tooltip
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="手机号"
                    width="150">
            </el-table-column>
            <el-table-column
                    prop="address"
                    label="地址"
                    min-width=250">
            </el-table-column>
            <el-table-column
                    prop="createdAt"
                    sortable="custom"
                    label="更新时间"
                    width="200">
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

</div>
</body>
<script type="text/javascript" charset="utf-8"
        src="${pageContext.request.contextPath}/static/js/sys/orders/orders.js?v=1.0"></script>
</html>
